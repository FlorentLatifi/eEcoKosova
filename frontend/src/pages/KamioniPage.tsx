import React, { useState, useEffect } from 'react';
import { Truck, Plus, Edit, Trash2, MapPin, Calendar, User } from 'lucide-react';
import {
  getAllKamionet,
  getAvailableKamionet,
  createKamioni,
  updateKamioni,
  deleteKamioni,
  assignRouteToKamioni,
  releaseRouteFromKamioni,
  type Kamioni,
  ApiError,
} from '../services/api';
import { useToast } from '../context/ToastContext';

const KamioniPage: React.FC = () => {
  const [kamionet, setKamionet] = useState<Kamioni[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingKamioni, setEditingKamioni] = useState<Kamioni | null>(null);
  const [filter, setFilter] = useState<'all' | 'available'>('all');
  const { showSuccess, showError } = useToast();

  const fetchKamionet = async () => {
    try {
      setLoading(true);
      const data = filter === 'available' 
        ? await getAvailableKamionet()
        : await getAllKamionet();
      setKamionet(data);
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi marrja e kamionëve';
      showError(message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchKamionet();
  }, [filter]);

  const handleCreate = async (data: any) => {
    try {
      await createKamioni(data);
      showSuccess('Kamioni u krijua me sukses!');
      setShowForm(false);
      fetchKamionet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi krijimi i kamionit';
      showError(message);
    }
  };

  const handleUpdate = async (id: string, data: any) => {
    try {
      await updateKamioni(id, data);
      showSuccess('Kamioni u përditësua me sukses!');
      setEditingKamioni(null);
      fetchKamionet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi përditësimi';
      showError(message);
    }
  };

  const handleDelete = async (id: string) => {
    if (!confirm('A jeni të sigurt që doni të fshini këtë kamion?')) return;
    
    try {
      await deleteKamioni(id);
      showSuccess('Kamioni u fshi me sukses!');
      fetchKamionet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi fshirja';
      showError(message);
    }
  };

  const handleAssignRoute = async (id: string, routeId: string, containerIds: string[]) => {
    try {
      await assignRouteToKamioni(id, routeId, containerIds);
      showSuccess('Rruga u caktua me sukses!');
      fetchKamionet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi caktimi i rrugës';
      showError(message);
    }
  };

  const handleReleaseRoute = async (id: string) => {
    try {
      await releaseRouteFromKamioni(id);
      showSuccess('Rruga u lëshua me sukses!');
      fetchKamionet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi lëshimi i rrugës';
      showError(message);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-eco-blue mx-auto mb-4"></div>
          <p className="text-gray-600">Duke ngarkuar kamionët...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
            <Truck className="w-8 h-8 text-eco-blue" />
            Kamionët
          </h1>
          <p className="text-gray-600 mt-1">Menaxhimi i kamionëve të mbledhjes</p>
        </div>
        <button
          onClick={() => setShowForm(true)}
          className="flex items-center gap-2 bg-eco-blue text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
        >
          <Plus className="w-5 h-5" />
          Shto Kamion
        </button>
      </div>

      {/* Filters */}
      <div className="flex gap-2">
        <button
          onClick={() => setFilter('all')}
          className={`px-4 py-2 rounded-lg transition-all ${
            filter === 'all'
              ? 'bg-eco-blue text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Të Gjitha ({kamionet.length})
        </button>
        <button
          onClick={() => setFilter('available')}
          className={`px-4 py-2 rounded-lg transition-all ${
            filter === 'available'
              ? 'bg-green-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Të Disponueshëm
        </button>
      </div>

      {/* Kamionët Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {kamionet.map((kamioni) => (
          <div
            key={kamioni.id}
            className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
          >
            <div className="flex items-start justify-between mb-4">
              <div>
                <h3 className="text-xl font-bold text-gray-900">{kamioni.name}</h3>
                <p className="text-sm text-gray-500">{kamioni.licensePlate}</p>
              </div>
              <div className={`px-3 py-1 rounded-full text-xs font-semibold ${
                kamioni.available
                  ? 'bg-green-100 text-green-800'
                  : 'bg-red-100 text-red-800'
              }`}>
                {kamioni.available ? 'Disponueshëm' : 'I Zënë'}
              </div>
            </div>

            <div className="space-y-2 mb-4">
              <div className="flex items-center gap-2 text-sm">
                <Truck className="w-4 h-4 text-gray-400" />
                <span className="text-gray-600">Kapaciteti:</span>
                <span className="font-semibold">{kamioni.capacity.toLocaleString()} L</span>
              </div>
              <div className="flex items-center gap-2 text-sm">
                <User className="w-4 h-4 text-gray-400" />
                <span className="text-gray-600">Operatori:</span>
                <span className="font-semibold">{kamioni.operatorId}</span>
              </div>
              <div className="flex items-center gap-2 text-sm">
                <MapPin className="w-4 h-4 text-gray-400" />
                <span className="text-gray-600">
                  {kamioni.latitude.toFixed(4)}, {kamioni.longitude.toFixed(4)}
                </span>
              </div>
              {kamioni.currentRouteId && (
                <div className="flex items-center gap-2 text-sm">
                  <Calendar className="w-4 h-4 text-gray-400" />
                  <span className="text-gray-600">Rruga:</span>
                  <span className="font-semibold">{kamioni.currentRouteId}</span>
                </div>
              )}
            </div>

            <div className="flex gap-2">
              <button
                onClick={() => setEditingKamioni(kamioni)}
                className="flex-1 flex items-center justify-center gap-2 bg-eco-blue text-white px-3 py-2 rounded-lg hover:bg-blue-600 transition-colors text-sm"
              >
                <Edit className="w-4 h-4" />
                Përditëso
              </button>
              {kamioni.currentRouteId ? (
                <button
                  onClick={() => handleReleaseRoute(kamioni.id)}
                  className="flex-1 flex items-center justify-center gap-2 bg-amber-600 text-white px-3 py-2 rounded-lg hover:bg-amber-700 transition-colors text-sm"
                >
                  Lësho Rrugë
                </button>
              ) : (
                <button
                  onClick={() => handleDelete(kamioni.id)}
                  className="flex-1 flex items-center justify-center gap-2 bg-red-600 text-white px-3 py-2 rounded-lg hover:bg-red-700 transition-colors text-sm"
                >
                  <Trash2 className="w-4 h-4" />
                  Fshi
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {kamionet.length === 0 && (
        <div className="bg-white rounded-lg shadow-md p-12 text-center">
          <Truck className="w-16 h-16 text-gray-400 mx-auto mb-4" />
          <h3 className="text-xl font-semibold text-gray-700 mb-2">Nuk ka kamionë</h3>
          <p className="text-gray-500">Kliko "Shto Kamion" për të krijuar një të ri</p>
        </div>
      )}

      {/* Create/Edit Form Modal */}
      {(showForm || editingKamioni) && (
        <KamioniForm
          kamioni={editingKamioni}
          onClose={() => {
            setShowForm(false);
            setEditingKamioni(null);
          }}
          onSubmit={editingKamioni 
            ? (data) => handleUpdate(editingKamioni.id, data)
            : handleCreate
          }
        />
      )}
    </div>
  );
};

// Form Component
interface KamioniFormProps {
  kamioni?: Kamioni | null;
  onClose: () => void;
  onSubmit: (data: any) => void;
}

const KamioniForm: React.FC<KamioniFormProps> = ({ kamioni, onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    id: kamioni?.id || '',
    name: kamioni?.name || '',
    licensePlate: kamioni?.licensePlate || '',
    capacity: kamioni?.capacity || 5000,
    operatorId: kamioni?.operatorId || '',
    latitude: kamioni?.latitude || 42.6629,
    longitude: kamioni?.longitude || 21.1655,
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-2xl max-w-md w-full p-6">
        <h2 className="text-2xl font-bold mb-4">
          {kamioni ? 'Përditëso Kamion' : 'Krijo Kamion të Ri'}
        </h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">ID</label>
            <input
              type="text"
              value={formData.id}
              onChange={(e) => setFormData({ ...formData, id: e.target.value })}
              required
              disabled={!!kamioni}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Emri</label>
            <input
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Targat</label>
            <input
              type="text"
              value={formData.licensePlate}
              onChange={(e) => setFormData({ ...formData, licensePlate: e.target.value })}
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Kapaciteti (L)</label>
            <input
              type="number"
              value={formData.capacity}
              onChange={(e) => setFormData({ ...formData, capacity: parseInt(e.target.value) })}
              required
              min="1"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">ID Operatori</label>
            <input
              type="text"
              value={formData.operatorId}
              onChange={(e) => setFormData({ ...formData, operatorId: e.target.value })}
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Latitude</label>
              <input
                type="number"
                step="0.000001"
                value={formData.latitude}
                onChange={(e) => setFormData({ ...formData, latitude: parseFloat(e.target.value) })}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Longitude</label>
              <input
                type="number"
                step="0.000001"
                value={formData.longitude}
                onChange={(e) => setFormData({ ...formData, longitude: parseFloat(e.target.value) })}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
            </div>
          </div>
          <div className="flex gap-3">
            <button
              type="submit"
              className="flex-1 bg-eco-blue text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition-colors"
            >
              {kamioni ? 'Përditëso' : 'Krijo'}
            </button>
            <button
              type="button"
              onClick={onClose}
              className="flex-1 bg-gray-200 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-300 transition-colors"
            >
              Anulo
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default KamioniPage;

