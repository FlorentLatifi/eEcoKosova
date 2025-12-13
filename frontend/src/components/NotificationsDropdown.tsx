import React from 'react';
import { Bell, X, Check, Trash2, AlertTriangle, Info, CheckCircle } from 'lucide-react';
import { useNotifications } from '../context/NotificationContext';
import { formatDistanceToNow } from 'date-fns';

const NotificationsDropdown: React.FC<{
  isOpen: boolean;
  onClose: () => void;
}> = ({ isOpen, onClose }) => {
  const { notifications, unreadCount, markAsRead, markAllAsRead, clearAll } =
    useNotifications();

  if (!isOpen) return null;

  const getIcon = (type: string) => {
    switch (type) {
      case 'critical':
        return <AlertTriangle className="w-5 h-5 text-red-600" />;
      case 'warning':
        return <AlertTriangle className="w-5 h-5 text-amber-600" />;
      case 'success':
        return <CheckCircle className="w-5 h-5 text-green-600" />;
      default:
        return <Info className="w-5 h-5 text-blue-600" />;
    }
  };

  const getBgColor = (type: string) => {
    switch (type) {
      case 'critical':
        return 'bg-red-50 border-red-200';
      case 'warning':
        return 'bg-amber-50 border-amber-200';
      case 'success':
        return 'bg-green-50 border-green-200';
      default:
        return 'bg-blue-50 border-blue-200';
    }
  };

  return (
    <>
      {/* Backdrop */}
      <div
        className="fixed inset-0 z-40"
        onClick={onClose}
      />

      {/* Dropdown */}
      <div className="absolute right-0 top-12 w-96 bg-white rounded-lg shadow-2xl border border-gray-200 z-50 max-h-[600px] flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-gray-200">
          <div className="flex items-center space-x-2">
            <Bell className="w-5 h-5 text-gray-700" />
            <h3 className="font-semibold text-gray-900">Njoftime</h3>
            {unreadCount > 0 && (
              <span className="px-2 py-0.5 bg-red-500 text-white text-xs font-semibold rounded-full">
                {unreadCount}
              </span>
            )}
          </div>
          <div className="flex items-center space-x-2">
            {notifications.length > 0 && (
              <>
                <button
                  onClick={markAllAsRead}
                  className="p-1.5 hover:bg-gray-100 rounded-lg transition-colors"
                  title="Shëno të gjitha si të lexuara"
                >
                  <Check className="w-4 h-4 text-gray-600" />
                </button>
                <button
                  onClick={clearAll}
                  className="p-1.5 hover:bg-gray-100 rounded-lg transition-colors"
                  title="Fshi të gjitha"
                >
                  <Trash2 className="w-4 h-4 text-gray-600" />
                </button>
              </>
            )}
            <button
              onClick={onClose}
              className="p-1.5 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <X className="w-4 h-4 text-gray-600" />
            </button>
          </div>
        </div>

        {/* Notifications List */}
        <div className="overflow-y-auto flex-1">
          {notifications.length === 0 ? (
            <div className="p-8 text-center">
              <Bell className="w-12 h-12 text-gray-300 mx-auto mb-3" />
              <p className="text-gray-500">Nuk ka njoftime</p>
            </div>
          ) : (
            <div className="divide-y divide-gray-100">
              {notifications.map((notification) => (
                <div
                  key={notification.id}
                  className={`p-4 hover:bg-gray-50 transition-colors cursor-pointer border-l-4 ${
                    notification.read ? 'bg-white' : 'bg-blue-50'
                  } ${getBgColor(notification.type)}`}
                  onClick={() => {
                    if (!notification.read) {
                      markAsRead(notification.id);
                    }
                  }}
                >
                  <div className="flex items-start space-x-3">
                    <div className="flex-shrink-0 mt-0.5">{getIcon(notification.type)}</div>
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between">
                        <p
                          className={`text-sm font-medium ${
                            notification.read ? 'text-gray-700' : 'text-gray-900'
                          }`}
                        >
                          {notification.title}
                        </p>
                        {!notification.read && (
                          <span className="ml-2 w-2 h-2 bg-blue-500 rounded-full flex-shrink-0 mt-1.5"></span>
                        )}
                      </div>
                      <p className="text-sm text-gray-600 mt-1">{notification.message}</p>
                      <p className="text-xs text-gray-400 mt-2">
                        {formatDistanceToNow(notification.timestamp, {
                          addSuffix: true,
                        }).replace('ago', 'më parë').replace('in', 'në')}
                      </p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Footer */}
        {notifications.length > 0 && (
          <div className="p-3 border-t border-gray-200 bg-gray-50">
            <button
              onClick={clearAll}
              className="w-full text-sm text-red-600 hover:text-red-700 font-medium py-2"
            >
              Fshi të gjitha njoftimet
            </button>
          </div>
        )}
      </div>
    </>
  );
};

export default NotificationsDropdown;

