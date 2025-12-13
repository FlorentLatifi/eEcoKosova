import React, { useState } from "react";
import { Outlet, Link, useLocation, useNavigate } from "react-router-dom";
import {
  LayoutDashboard,
  Trash2,
  Map,
  Route,
  FileText,
  Menu,
  X,
  Bell,
  Settings,
  LogOut,
} from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { useNotifications } from "../context/NotificationContext";
import UserProfileModal from "./UserProfileModal";
import NotificationsDropdown from "./NotificationsDropdown";

const Layout: React.FC = () => {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const [userProfileOpen, setUserProfileOpen] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const { unreadCount } = useNotifications();

  const navigation = [
    { name: "Dashboard", href: "/dashboard", icon: LayoutDashboard },
    { name: "Kontejnerët", href: "/containers", icon: Trash2 },
    { name: "Zonat", href: "/zones", icon: Map },
    { name: "Rrugët", href: "/routes", icon: Route },
    { name: "Raporte", href: "/reports", icon: FileText },
  ];

  const isActive = (path: string) => location.pathname === path;

  const handleSettings = () => {
    navigate("/settings");
  };

  const handleLogout = () => {
    if (confirm("A jeni të sigurt që doni të dilni?")) {
      logout();
    }
  };

  const handleNotifications = () => {
    setNotificationsOpen(!notificationsOpen);
  };

  const handleUserProfile = () => {
    setUserProfileOpen(true);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Sidebar */}
      <aside
        className={`fixed top-0 left-0 z-40 h-screen transition-all duration-300 ${
          sidebarOpen ? "w-64" : "w-20"
        } bg-white border-r border-gray-200`}
      >
        {/* Logo */}
        <div className="flex items-center justify-between h-16 px-4 border-b">
          {sidebarOpen ? (
            <>
              <div className="flex items-center space-x-2">
                <div className="w-8 h-8 bg-eco-green rounded-lg flex items-center justify-center">
                  <Trash2 className="w-5 h-5 text-white" />
                </div>
                <span className="text-xl font-bold text-gray-900">
                  EcoKosova
                </span>
              </div>
              <button
                onClick={() => setSidebarOpen(false)}
                className="p-2 hover:bg-gray-100 rounded-lg"
              >
                <X className="w-5 h-5 text-gray-600" />
              </button>
            </>
          ) : (
            <button
              onClick={() => setSidebarOpen(true)}
              className="p-2 hover:bg-gray-100 rounded-lg mx-auto"
            >
              <Menu className="w-5 h-5 text-gray-600" />
            </button>
          )}
        </div>

        {/* Navigation */}
        <nav className="mt-6 px-3 space-y-1">
          {navigation.map((item) => {
            const Icon = item.icon;
            const active = isActive(item.href);

            return (
              <Link
                key={item.name}
                to={item.href}
                className={`flex items-center px-3 py-3 rounded-lg transition-colors ${
                  active
                    ? "bg-eco-blue text-white"
                    : "text-gray-700 hover:bg-gray-100"
                }`}
              >
                <Icon className="w-5 h-5 flex-shrink-0" />
                {sidebarOpen && (
                  <span className="ml-3 font-medium">{item.name}</span>
                )}
              </Link>
            );
          })}
        </nav>

        {/* Bottom Actions */}
        <div className="absolute bottom-0 w-full p-3 border-t space-y-1">
          <button
            onClick={handleSettings}
            className="flex items-center w-full px-3 py-3 text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <Settings className="w-5 h-5" />
            {sidebarOpen && <span className="ml-3">Cilësimet</span>}
          </button>
          <button
            onClick={handleLogout}
            className="flex items-center w-full px-3 py-3 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
          >
            <LogOut className="w-5 h-5" />
            {sidebarOpen && <span className="ml-3">Dilni</span>}
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <div
        className={`transition-all duration-300 ${
          sidebarOpen ? "ml-64" : "ml-20"
        }`}
      >
        {/* Top Header */}
        <header className="sticky top-0 z-30 bg-white border-b border-gray-200">
          <div className="flex items-center justify-between h-16 px-6">
            <h2 className="text-2xl font-bold text-gray-900">
              {navigation.find((n) => isActive(n.href))?.name || "Dashboard"}
            </h2>

            <div className="flex items-center space-x-4">
              <div className="relative">
                <button
                  onClick={handleNotifications}
                  className="relative p-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors"
                  title="Njoftime"
                >
                  <Bell className="w-5 h-5" />
                  {unreadCount > 0 && (
                    <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
                  )}
                </button>
                <NotificationsDropdown
                  isOpen={notificationsOpen}
                  onClose={() => setNotificationsOpen(false)}
                />
              </div>

              <button
                onClick={handleUserProfile}
                className="flex items-center space-x-3 pl-4 border-l hover:opacity-80 transition-opacity cursor-pointer"
                title="Profil i Përdoruesit"
              >
                <div className="w-10 h-10 bg-eco-blue rounded-full flex items-center justify-center">
                  <span className="text-white font-semibold">
                    {user?.name.charAt(0).toUpperCase() || 'A'}
                  </span>
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-900">
                    {user?.name || 'Admin User'}
                  </p>
                  <p className="text-xs text-gray-500">{user?.email || 'admin@ecokosova.com'}</p>
                </div>
              </button>
            </div>
          </div>
        </header>

        {/* Page Content */}
        <main className="p-6">
          <Outlet />
        </main>
      </div>

      {/* User Profile Modal */}
      <UserProfileModal
        isOpen={userProfileOpen}
        onClose={() => setUserProfileOpen(false)}
      />
    </div>
  );
};

export default Layout;
