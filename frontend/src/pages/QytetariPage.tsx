import React, { useState, useEffect } from 'react';
import { Users, Plus, Edit, Trash2, MapPin } from 'lucide-react';
import {
  getAllQytetaret,
  createQytetari,
  updateQytetari,
  deleteQytetari,
  type Qytetari,
  ApiError,
} from '../services/api';
import { useToast } from '../context/ToastContext';

const QytetariPage: React.FC = () => {
  const [qytetaret, setQytetaret] = useState<Qytetari[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingQytetari, setEditingQytetari] = useState<Qytetari | null>(null);
  const { showSuccess, showError } = useToast();

  const fetchQytetaret = async () => {
    try {
      setLoading(true);
      const data = await getAllQytetaret();
      setQytetaret(data);
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi marrja e qytetarëve';
      showError(message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchQytetaret();
  }, []);

  const handleCreate = async (data: any) => {
    try {
      await createQytetari(data);
      showSuccess('Qytetari u krijua me sukses!');
      setShowForm(false);
      fetchQytetaret();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi krijimi';
      showError(message);
    }
  };

  const handleUpdate = async (id: string, data: any) => {
    try {
      await updateQytetari(id, data);
      showSuccess('Qytetari u përditësua me sukses!');
      setEditingQytetari(null);
      fetchQytetaret();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi përditësimi';
      showError(message);
    }
  };

  const handleDelete = async (id: string) => {
    if (!confirm('A jeni të sigurt që doni të fshini këtë qytetar?')) return;
    
    try {
      await deleteQytetari(id);
      showSuccess('Qytetari u fshi me sukses!');
      fetchQytetaret();
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Dështoi fshirja';
      showError(message);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-eco-blue mx-auto mb-4"></div>
          <p className="text-gray-600">Duke ngarkuar qytetarët...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
            <Users className="w-8 h-8 text-eco-blue" />
            Qytetarët
          </h1>
          <p className="text-gray-600 mt-1">Menaxhimi i qytetarëve</p>
        </div>
        <button
          onClick={() => setShowForm(true)}
          className="flex items-center gap-2 bg-eco-blue text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
        >
          <Plus className="w-5 h-5" />
          Shto Qytetar
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {qytetaret.map((qytetari) => (
          <div
            key={qytetari.id}
            className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
          >
            <div className="flex items-start justify-between mb-4">
              <div>
                <h3 className="text-xl font-bold text-gray-900">{qytetari.name}</h3>
                <p className="text-sm text-gray-500">{qytetari.id}</p>
              </div>
            </div>

            {qytetari.address && (
              <div className="flex items-start gap-2 text-sm mb-4">
                <MapPin className="w-4 h-4 text-gray-400 mt-0.5" />
                <span className="text-gray-600">{qytetari.address}</span>
              </div>
            )}

            <div className="flex gap-2">
              <button
                onClick={() => setEditingQytetari(qytetari)}
                className="flex-1 flex items-center justify-center gap-2 bg-eco-blue text-white px-3 py-2 rounded-lg hover:bg-blue-600 transition-colors text-sm"
              >
                <Edit className="w-4 h-4" />
                Përditëso
              </button>
              <button
                onClick={() => handleDelete(qytetari.id)}
                className="flex-1 flex items-center justify-center gap-2 bg-red-600 text-white px-3 py-2 rounded-lg hover:bg-red-700 transition-colors text-sm"
              >
                <Trash2 className="w-4 h-4" />
                Fshi
              </button>
            </div>
          </div>
        ))}
      </div>

      {qytetaret.length === 0 && (
        <div className="bg-white rounded-lg shadow-md p-12 text-center">
          <Users className="w-16 h-16 text-gray-400 mx-auto mb-4" />
          <h3 className="text-xl font-semibold text-gray-700 mb-2">Nuk ka qytetarë</h3>
          <p className="text-gray-500">Kliko "Shto Qytetar" për të krijuar një të ri</p>
        </div>
      )}

      {(showForm || editingQytetari) && (
        <QytetariForm
          qytetari={editingQytetari}
          onClose={() => {
            setShowForm(false);
            setEditingQytetari(null);
          }}
          onSubmit={editingQytetari 
            ? (data) => handleUpdate(editingQytetari.id, data)
            : handleCreate
          }
        />
      )}
    </div>
  );
};

interface QytetariFormProps {
  qytetari?: Qytetari | null;
  onClose: () => void;
  onSubmit: (data: any) => void;
}

const QytetariForm: React.FC<QytetariFormProps> = ({ qytetari, onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    id: qytetari?.id || '',
    name: qytetari?.name || '',
    address: qytetari?.address || '',
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-2xl max-w-md w-full p-6">
        <h2 className="text-2xl font-bold mb-4">
          {qytetari ? 'Përditëso Qytetar' : 'Krijo Qytetar të Ri'}
        </h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">ID</label>
            <input
              type="text"
              value={formData.id}
              onChange={(e) => setFormData({ ...formData, id: e.target.value })}
              required
              disabled={!!qytetari}
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
            <label className="block text-sm font-medium text-gray-700 mb-1">Adresa</label>
            <input
              type="text"
              value={formData.address}
              onChange={(e) => setFormData({ ...formData, address: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            />
          </div>
          <div className="flex gap-3">
            <button
              type="submit"
              className="flex-1 bg-eco-blue text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition-colors"
            >
              {qytetari ? 'Përditëso' : 'Krijo'}
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

export default QytetariPage;

