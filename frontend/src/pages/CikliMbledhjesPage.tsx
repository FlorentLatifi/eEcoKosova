import React, { useState, useEffect } from 'react';
import { Calendar, Plus, Edit, Trash2, CheckCircle, XCircle, Clock } from 'lucide-react';
import {
  getAllCiklet,
  getActiveCiklet,
  createCikli,
  updateCikli,
  deleteCikli,
  activateCikli,
  completeCikli,
  cancelCikli,
  type CikliMbledhjes,
  ApiError,
} from '../services/api';
import { useToast } from '../context/ToastContext';
import { format } from 'date-fns';

const CikliMbledhjesPage: React.FC = () => {
  const [ciklet, setCiklet] = useState<CikliMbledhjes[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingCikli, setEditingCikli] = useState<CikliMbledhjes | null>(null);
  const [filter, setFilter] = useState<'all' | 'active'>('all');
  const { showSuccess, showError } = useToast();

  const fetchCiklet = async () => {
    try {
      setLoading(true);
      const data = filter === 'active' 
        ? await getActiveCiklet()
        : await getAllCiklet();
      setCiklet(data);
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi marrja e cikleve';
      showError(message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCiklet();
  }, [filter]);

  const handleCreate = async (data: any) => {
    try {
      await createCikli(data);
      showSuccess('Cikli u krijua me sukses!');
      setShowForm(false);
      fetchCiklet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi krijimi';
      showError(message);
    }
  };

  const handleUpdate = async (id: string, data: any) => {
    try {
      await updateCikli(id, data);
      showSuccess('Cikli u përditësua me sukses!');
      setEditingCikli(null);
      fetchCiklet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi përditësimi';
      showError(message);
    }
  };

  const handleDelete = async (id: string) => {
    if (!confirm('A jeni të sigurt që doni të fshini këtë cikël?')) return;
    
    try {
      await deleteCikli(id);
      showSuccess('Cikli u fshi me sukses!');
      fetchCiklet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi fshirja';
      showError(message);
    }
  };

  const handleActivate = async (id: string) => {
    try {
      await activateCikli(id);
      showSuccess('Cikli u aktivizua me sukses!');
      fetchCiklet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi aktivizimi';
      showError(message);
    }
  };

  const handleComplete = async (id: string) => {
    try {
      await completeCikli(id);
      showSuccess('Cikli u kompletuar me sukses!');
      fetchCiklet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi kompletuar';
      showError(message);
    }
  };

  const handleCancel = async (id: string) => {
    try {
      await cancelCikli(id);
      showSuccess('Cikli u anulua me sukses!');
      fetchCiklet();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi anulimi';
      showError(message);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'ACTIVE':
        return 'bg-green-100 text-green-800';
      case 'SCHEDULED':
        return 'bg-blue-100 text-blue-800';
      case 'COMPLETED':
        return 'bg-gray-100 text-gray-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusText = (status: string) => {
    switch (status) {
      case 'ACTIVE':
        return 'Aktiv';
      case 'SCHEDULED':
        return 'I Planifikuar';
      case 'COMPLETED':
        return 'I Kompletuar';
      case 'CANCELLED':
        return 'I Anuluar';
      default:
        return status;
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-eco-blue mx-auto mb-4"></div>
          <p className="text-gray-600">Duke ngarkuar ciklet...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
            <Calendar className="w-8 h-8 text-eco-blue" />
            Ciklet e Mbledhjes
          </h1>
          <p className="text-gray-600 mt-1">Menaxhimi i cikleve të mbledhjes</p>
        </div>
        <button
          onClick={() => setShowForm(true)}
          className="flex items-center gap-2 bg-eco-blue text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
        >
          <Plus className="w-5 h-5" />
          Krijo Cikël
        </button>
      </div>

      <div className="flex gap-2">
        <button
          onClick={() => setFilter('all')}
          className={`px-4 py-2 rounded-lg transition-all ${
            filter === 'all'
              ? 'bg-eco-blue text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Të Gjitha ({ciklet.length})
        </button>
        <button
          onClick={() => setFilter('active')}
          className={`px-4 py-2 rounded-lg transition-all ${
            filter === 'active'
              ? 'bg-green-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Aktive
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {ciklet.map((cikli) => (
          <div
            key={cikli.id}
            className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
          >
            <div className="flex items-start justify-between mb-4">
              <div>
                <h3 className="text-xl font-bold text-gray-900">{cikli.id}</h3>
                <p className="text-sm text-gray-500">Zona: {cikli.zoneId}</p>
              </div>
              <div className={`px-3 py-1 rounded-full text-xs font-semibold ${getStatusColor(cikli.status)}`}>
                {getStatusText(cikli.status)}
              </div>
            </div>

            <div className="space-y-2 mb-4">
              <div className="flex items-center gap-2 text-sm">
                <Clock className="w-4 h-4 text-gray-400" />
                <span className="text-gray-600">
                  {format(new Date(cikli.scheduleTime), 'dd/MM/yyyy HH:mm')}
                </span>
              </div>
              <div className="text-sm">
                <span className="text-gray-600">Kapaciteti:</span>
                <span className="font-semibold ml-2">{cikli.maxCapacity.toLocaleString()} L</span>
              </div>
              <div className="text-sm">
                <span className="text-gray-600">Ditët:</span>
                <span className="font-semibold ml-2">{cikli.collectionDays.join(', ')}</span>
              </div>
              {cikli.kamioniId && (
                <div className="text-sm">
                  <span className="text-gray-600">Kamioni:</span>
                  <span className="font-semibold ml-2">{cikli.kamioniId}</span>
                </div>
              )}
            </div>

            <div className="flex gap-2 flex-wrap">
              {cikli.status === 'SCHEDULED' && (
                <button
                  onClick={() => handleActivate(cikli.id)}
                  className="flex items-center gap-2 bg-green-600 text-white px-3 py-2 rounded-lg hover:bg-green-700 transition-colors text-sm"
                >
                  <CheckCircle className="w-4 h-4" />
                  Aktivizo
                </button>
              )}
              {cikli.status === 'ACTIVE' && (
                <button
                  onClick={() => handleComplete(cikli.id)}
                  className="flex items-center gap-2 bg-blue-600 text-white px-3 py-2 rounded-lg hover:bg-blue-700 transition-colors text-sm"
                >
                  <CheckCircle className="w-4 h-4" />
                  Kompleto
                </button>
              )}
              {(cikli.status === 'SCHEDULED' || cikli.status === 'ACTIVE') && (
                <button
                  onClick={() => handleCancel(cikli.id)}
                  className="flex items-center gap-2 bg-red-600 text-white px-3 py-2 rounded-lg hover:bg-red-700 transition-colors text-sm"
                >
                  <XCircle className="w-4 h-4" />
                  Anulo
                </button>
              )}
              <button
                onClick={() => setEditingCikli(cikli)}
                className="flex items-center gap-2 bg-eco-blue text-white px-3 py-2 rounded-lg hover:bg-blue-600 transition-colors text-sm"
              >
                <Edit className="w-4 h-4" />
                Përditëso
              </button>
              <button
                onClick={() => handleDelete(cikli.id)}
                className="flex items-center gap-2 bg-red-600 text-white px-3 py-2 rounded-lg hover:bg-red-700 transition-colors text-sm"
              >
                <Trash2 className="w-4 h-4" />
                Fshi
              </button>
            </div>
          </div>
        ))}
      </div>

      {ciklet.length === 0 && (
        <div className="bg-white rounded-lg shadow-md p-12 text-center">
          <Calendar className="w-16 h-16 text-gray-400 mx-auto mb-4" />
          <h3 className="text-xl font-semibold text-gray-700 mb-2">Nuk ka cikle</h3>
          <p className="text-gray-500">Kliko "Krijo Cikël" për të krijuar një të ri</p>
        </div>
      )}

      {(showForm || editingCikli) && (
        <CikliForm
          cikli={editingCikli}
          onClose={() => {
            setShowForm(false);
            setEditingCikli(null);
          }}
          onSubmit={editingCikli 
            ? (data) => handleUpdate(editingCikli.id, data)
            : handleCreate
          }
        />
      )}
    </div>
  );
};

interface CikliFormProps {
  cikli?: CikliMbledhjes | null;
  onClose: () => void;
  onSubmit: (data: any) => void;
}

const CikliForm: React.FC<CikliFormProps> = ({ cikli, onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    id: cikli?.id || '',
    scheduleTime: cikli?.scheduleTime 
      ? new Date(cikli.scheduleTime).toISOString().slice(0, 16)
      : new Date().toISOString().slice(0, 16),
    maxCapacity: cikli?.maxCapacity || 10000,
    collectionDays: cikli?.collectionDays || [],
    zoneId: cikli?.zoneId || '',
    kamioniId: cikli?.kamioniId || '',
  });

  const daysOfWeek = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

  const handleDayToggle = (day: string) => {
    setFormData(prev => ({
      ...prev,
      collectionDays: prev.collectionDays.includes(day)
        ? prev.collectionDays.filter(d => d !== day)
        : [...prev.collectionDays, day]
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const submitData = {
      ...formData,
      scheduleTime: new Date(formData.scheduleTime).toISOString(),
    };
    if (cikli && formData.kamioniId) {
      // Update me kamioniId
      onSubmit({ kamioniId: formData.kamioniId });
    } else {
      onSubmit(submitData);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 max-h-[90vh] overflow-y-auto">
        <h2 className="text-2xl font-bold mb-4">
          {cikli ? 'Përditëso Cikël' : 'Krijo Cikël të Ri'}
        </h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">ID</label>
            <input
              type="text"
              value={formData.id}
              onChange={(e) => setFormData({ ...formData, id: e.target.value })}
              required
              disabled={!!cikli}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Koha e Planifikuar</label>
            <input
              type="datetime-local"
              value={formData.scheduleTime}
              onChange={(e) => setFormData({ ...formData, scheduleTime: e.target.value })}
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Kapaciteti Maksimal (L)</label>
            <input
              type="number"
              value={formData.maxCapacity}
              onChange={(e) => setFormData({ ...formData, maxCapacity: parseInt(e.target.value) })}
              required
              min="1"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">ID Zona</label>
            <input
              type="text"
              value={formData.zoneId}
              onChange={(e) => setFormData({ ...formData, zoneId: e.target.value })}
              required
              disabled={!!cikli}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          {cikli && (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">ID Kamioni</label>
              <input
                type="text"
                value={formData.kamioniId}
                onChange={(e) => setFormData({ ...formData, kamioniId: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
            </div>
          )}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Ditët e Mbledhjes</label>
            <div className="grid grid-cols-2 gap-2">
              {daysOfWeek.map(day => (
                <label key={day} className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={formData.collectionDays.includes(day)}
                    onChange={() => handleDayToggle(day)}
                    className="w-4 h-4 text-eco-blue border-gray-300 rounded focus:ring-eco-blue"
                  />
                  <span className="text-sm text-gray-700">
                    {day.charAt(0) + day.slice(1).toLowerCase()}
                  </span>
                </label>
              ))}
            </div>
          </div>
          <div className="flex gap-3">
            <button
              type="submit"
              className="flex-1 bg-eco-blue text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition-colors"
            >
              {cikli ? 'Përditëso' : 'Krijo'}
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

export default CikliMbledhjesPage;

