import React, { useState } from 'react';
import { X, User, Mail, Shield, Save, Edit2 } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

interface UserProfileModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const UserProfileModal: React.FC<UserProfileModalProps> = ({ isOpen, onClose }) => {
  const { user, updateUser } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    name: user?.name || '',
    email: user?.email || '',
  });

  if (!isOpen || !user) return null;

  const handleSave = () => {
    updateUser(formData);
    setIsEditing(false);
    alert('Profili u përditësua me sukses!');
  };

  const getRoleDisplay = (role: string) => {
    const roles: Record<string, string> = {
      admin: 'Administrator',
      operator: 'Operatori',
      viewer: 'Shikues',
    };
    return roles[role] || role;
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="bg-gradient-to-r from-eco-blue to-blue-600 text-white p-6 rounded-t-xl">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-white bg-opacity-20 rounded-full flex items-center justify-center">
                <User className="w-6 h-6" />
              </div>
              <div>
                <h2 className="text-2xl font-bold">Profili i Përdoruesit</h2>
                <p className="text-blue-100 text-sm">Menaxho informacionin tuaj personal</p>
              </div>
            </div>
            <button
              onClick={onClose}
              className="hover:bg-white hover:bg-opacity-20 rounded-full p-2 transition-all"
            >
              <X className="w-6 h-6" />
            </button>
          </div>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          {/* Avatar Section */}
          <div className="flex items-center justify-center">
            <div className="w-24 h-24 bg-eco-blue rounded-full flex items-center justify-center text-white text-3xl font-bold">
              {user.name.charAt(0).toUpperCase()}
            </div>
          </div>

          {/* Form Fields */}
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                <div className="flex items-center space-x-2">
                  <User className="w-4 h-4" />
                  <span>Emri i Plotë</span>
                </div>
              </label>
              {isEditing ? (
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
                />
              ) : (
                <div className="px-4 py-2 bg-gray-50 rounded-lg text-gray-900">
                  {user.name}
                </div>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                <div className="flex items-center space-x-2">
                  <Mail className="w-4 h-4" />
                  <span>Email</span>
                </div>
              </label>
              {isEditing ? (
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
                />
              ) : (
                <div className="px-4 py-2 bg-gray-50 rounded-lg text-gray-900">
                  {user.email}
                </div>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                <div className="flex items-center space-x-2">
                  <Shield className="w-4 h-4" />
                  <span>Roli</span>
                </div>
              </label>
              <div className="px-4 py-2 bg-gray-50 rounded-lg">
                <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-semibold bg-blue-100 text-blue-800">
                  {getRoleDisplay(user.role)}
                </span>
              </div>
            </div>
          </div>

          {/* Actions */}
          <div className="flex space-x-3 pt-4 border-t">
            {isEditing ? (
              <>
                <button
                  onClick={() => {
                    setIsEditing(false);
                    setFormData({ name: user.name, email: user.email });
                  }}
                  className="flex-1 px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors font-medium"
                >
                  Anulo
                </button>
                <button
                  onClick={handleSave}
                  className="flex-1 px-4 py-2 bg-eco-blue text-white rounded-lg hover:bg-blue-600 transition-colors font-medium flex items-center justify-center space-x-2"
                >
                  <Save className="w-4 h-4" />
                  <span>Ruaj Ndryshimet</span>
                </button>
              </>
            ) : (
              <button
                onClick={() => setIsEditing(true)}
                className="w-full px-4 py-2 bg-eco-blue text-white rounded-lg hover:bg-blue-600 transition-colors font-medium flex items-center justify-center space-x-2"
              >
                <Edit2 className="w-4 h-4" />
                <span>Ndrysho Profilin</span>
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfileModal;

