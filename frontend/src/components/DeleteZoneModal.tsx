import React from "react";
import { Trash2, X } from "lucide-react";
import { deleteZone, ApiError, type Zone } from "../services/api";
import { useToast } from "../context/ToastContext";

interface DeleteZoneModalProps {
  zone: Zone | null;
  onClose: () => void;
  onDeleted?: () => void;
}

const DeleteZoneModal: React.FC<DeleteZoneModalProps> = ({
  zone,
  onClose,
  onDeleted,
}) => {
  const { showSuccess, showError } = useToast();

  if (!zone) return null;

  const handleDelete = async () => {
    try {
      await deleteZone(zone.id);
      showSuccess("Zona u fshi me sukses!");
      onDeleted?.();
      onClose();
    } catch (error) {
      const message =
        error instanceof ApiError ? error.message : "Dështoi fshirja e zones";
      showError(message);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-2xl max-w-md w-full">
        <div className="flex items-center justify-between p-4 border-b">
          <h2 className="text-lg font-semibold text-gray-900">Konfirmo Fshirjen</h2>
          <button
            onClick={onClose}
            className="p-2 rounded-lg hover:bg-gray-100 transition-colors"
          >
            <X className="w-5 h-5 text-gray-600" />
          </button>
        </div>

        <div className="p-6 space-y-4">
          <div className="flex items-start space-x-3">
            <div className="bg-red-100 rounded-full p-3">
              <Trash2 className="w-6 h-6 text-red-600" />
            </div>
            <div>
              <p className="text-sm text-gray-700">
                A jeni i sigurt që dëshironi të fshini zonën{" "}
                <span className="font-semibold">{zone.name}</span> (
                <span className="font-mono">{zone.id}</span>)? Kjo veprim nuk mund
                të kthehet prapa.
              </p>
            </div>
          </div>

          <div className="flex justify-end gap-3 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-100 transition-colors"
            >
              Anulo
            </button>
            <button
              type="button"
              onClick={handleDelete}
              className="px-4 py-2 rounded-lg bg-red-600 text-white hover:bg-red-700 transition-colors"
            >
              Fshi Zonën
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DeleteZoneModal;


