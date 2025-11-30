import React, { useState } from 'react';
import { X, MapPin, Activity, Calendar, Trash2 } from 'lucide-react';
import type { Container } from '../services/api';
import { updateFillLevel } from '../services/api';

interface ContainerDetailsProps {
  container: Container;
  onClose: () => void;
  onUpdate: () => void;
}

const ContainerDetails: React.FC<ContainerDetailsProps> = ({ container, onClose, onUpdate }) => {
  const [newFillLevel, setNewFillLevel] = useState(container.fillLevel);
  const [updating, setUpdating] = useState(false);
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const handleUpdate = async () => {
    setUpdating(true);
    setMessage(null);

    try {
      await updateFillLevel(container.id, newFillLevel);
      setMessage({ type: 'success', text: 'Niveli u përditësua me sukses!' });

      setTimeout(() => {
        onUpdate();
        onClose();
      }, 1500);
    } catch (error) {
      setMessage({ type: 'error', text: 'Gabim gjatë përditësimit!' });
    } finally {
      setUpdating(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="bg-gradient-to-r from-eco-blue to-blue-600 text-white p-6 rounded-t-xl">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-2xl font-bold">{container.id}</h2>
              <p className="text-blue-100">{container.type}</p>
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
          {/* Fill Level Display */}
          <div className="bg-gray-50 rounded-lg p-4">
            <div className="flex items-center justify-between mb-2">
              <span className="text-sm font-medium text-gray-700">Niveli Aktual</span>
              <span className="text-4xl font-bold text-eco-blue">{container.fillLevel}%</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-4 overflow-hidden">
              <div
                className={`h-full transition-all ${
                  container.fillLevel >= 90
                    ? 'bg-red-500'
                    : container.fillLevel >= 70
                    ? 'bg-amber-500'
                    : 'bg-green-500'
                }`}
                style={{ width: `${container.fillLevel}%` }}
              />
            </div>
          </div>

          {/* Details Grid */}
          <div className="grid grid-cols-2 gap-4">
            <div className="flex items-start space-x-3">
              <div className="bg-eco-blue bg-opacity-10 rounded-lg p-2">
                <MapPin className="w-5 h-5 text-eco-blue" />
              </div>
              <div>
                <p className="text-xs text-gray-500 font-medium">Adresa</p>
                <p className="text-sm text-gray-900">{container.address}</p>
              </div>
            </div>

            <div className="flex items-start space-x-3">
              <div className="bg-eco-green bg-opacity-10 rounded-lg p-2">
                <Activity className="w-5 h-5 text-eco-green" />
              </div>
              <div>
                <p className="text-xs text-gray-500 font-medium">Kapaciteti</p>
                <p className="text-sm text-gray-900">{container.capacity} Litra</p>
              </div>
            </div>

            <div className="flex items-start space-x-3">
              <div className="bg-amber-500 bg-opacity-10 rounded-lg p-2">
                <Trash2 className="w-5 h-5 text-amber-500" />
              </div>
              <div>
                <p className="text-xs text-gray-500 font-medium">Statusi</p>
                <p className="text-sm text-gray-900">{container.status}</p>
              </div>
            </div>

            <div className="flex items-start space-x-3">
              <div className="bg-purple-500 bg-opacity-10 rounded-lg p-2">
                <Calendar className="w-5 h-5 text-purple-500" />
              </div>
              <div>
                <p className="text-xs text-gray-500 font-medium">Operativ</p>
                <p className="text-sm text-gray-900">{container.operational ? 'Po ✓' : 'Jo ✗'}</p>
              </div>
            </div>
          </div>

          {/* Update Fill Level */}
          <div className="border-t pt-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Përditëso Nivelin e Mbushjes</h3>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Nivel i Ri: {newFillLevel}%
                </label>
                <input
                  type="range"
                  min="0"
                  max="100"
                  value={newFillLevel}
                  onChange={(e) => setNewFillLevel(parseInt(e.target.value))}
                  className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer"
                />
                <div className="flex justify-between text-xs text-gray-500 mt-1">
                  <span>0%</span>
                  <span>50%</span>
                  <span>100%</span>
                </div>
              </div>

              {message && (
                <div
                  className={`p-3 rounded-lg ${
                    message.type === 'success' ? 'bg-green-50 text-green-800' : 'bg-red-50 text-red-800'
                  }`}
                >
                  {message.text}
                </div>
              )}

              <button
                onClick={handleUpdate}
                disabled={updating || newFillLevel === container.fillLevel}
                className={`w-full py-3 rounded-lg font-medium transition-all ${
                  updating || newFillLevel === container.fillLevel
                    ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                    : 'bg-eco-blue text-white hover:bg-blue-600'
                }`}
              >
                {updating ? 'Duke përditësuar...' : 'Përditëso Nivelin'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContainerDetails;

