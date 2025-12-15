import React from "react";
import { useForm } from "react-hook-form";
import { X } from "lucide-react";
import { createZone, ApiError } from "../services/api";
import { useToast } from "../context/ToastContext";

export interface CreateZoneFormValues {
  zoneId: string;
  name: string;
  municipality: string;
  description?: string;
  latitude?: number;
  longitude?: number;
}

interface CreateZoneModalProps {
  isOpen: boolean;
  onClose: () => void;
  onCreated?: () => void;
}

const CreateZoneModal: React.FC<CreateZoneModalProps> = ({
  isOpen,
  onClose,
  onCreated,
}) => {
  const { showSuccess, showError } = useToast();
  const {
    register,
    handleSubmit,
    reset,
    formState: { isSubmitting },
  } = useForm<CreateZoneFormValues>();

  if (!isOpen) return null;

  const onSubmit = async (data: CreateZoneFormValues) => {
    try {
      const payload = {
        ...data,
        latitude: data.latitude ? Number(data.latitude) : undefined,
        longitude: data.longitude ? Number(data.longitude) : undefined,
      };

      await createZone(payload);
      showSuccess("Zona u krijua me sukses!");
      onCreated?.();
      reset();
      onClose();
    } catch (error) {
      const message =
        error instanceof ApiError ? error.message : "Dështoi krijimi i zones";
      showError(message);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-2xl max-w-xl w-full max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between p-4 border-b">
          <h2 className="text-xl font-semibold text-gray-900">Krijo Zonë të Re</h2>
          <button
            onClick={onClose}
            className="p-2 rounded-lg hover:bg-gray-100 transition-colors"
          >
            <X className="w-5 h-5 text-gray-600" />
          </button>
        </div>

        <form onSubmit={handleSubmit(onSubmit)} className="p-6 space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                ID e Zonës
              </label>
              <input
                type="text"
                {...register("zoneId", { required: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
                placeholder="ZONE-001"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Emri i Zonës
              </label>
              <input
                type="text"
                {...register("name", { required: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Komuna
            </label>
            <input
              type="text"
              {...register("municipality", { required: true })}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Përshkrimi
            </label>
            <textarea
              {...register("description")}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              rows={3}
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Gjerësia (latitude)
              </label>
              <input
                type="number"
                step="0.000001"
                {...register("latitude")}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Gjatësia (longitude)
              </label>
              <input
                type="number"
                step="0.000001"
                {...register("longitude")}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
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
              type="submit"
              disabled={isSubmitting}
              className="px-4 py-2 rounded-lg bg-eco-blue text-white hover:bg-blue-600 disabled:opacity-60 transition-colors"
            >
              {isSubmitting ? "Duke krijuar..." : "Krijo Zonën"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateZoneModal;


