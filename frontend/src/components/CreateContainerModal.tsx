import React from "react";
import { useForm } from "react-hook-form";
import { X } from "lucide-react";
import { createContainer, type Container, ApiError } from "../services/api";
import { useToast } from "../context/ToastContext";

export interface CreateContainerFormValues {
  id: string;
  zoneId: string;
  type: string;
  capacity: number;
  street: string;
  city: string;
  municipality: string;
  postalCode?: string;
  latitude?: number;
  longitude?: number;
  operational: boolean;
}

interface CreateContainerModalProps {
  isOpen: boolean;
  onClose: () => void;
  onCreated?: (container: Container) => void;
}

const CreateContainerModal: React.FC<CreateContainerModalProps> = ({
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
  } = useForm<CreateContainerFormValues>({
    defaultValues: {
      type: "PLASTIC",
      operational: true,
    },
  });

  if (!isOpen) return null;

  const onSubmit = async (data: CreateContainerFormValues) => {
    try {
      const payload = {
        ...data,
        capacity: data.capacity ? Number(data.capacity) : undefined,
        latitude: data.latitude ? Number(data.latitude) : undefined,
        longitude: data.longitude ? Number(data.longitude) : undefined,
      };

      const created = await createContainer(payload);
      showSuccess("Kontejneri u krijua me sukses!");
      onCreated?.(created);
      reset();
      onClose();
    } catch (error) {
      const message =
        error instanceof ApiError ? error.message : "Dështoi krijimi i kontejnerit";
      showError(message);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between p-4 border-b">
          <h2 className="text-xl font-semibold text-gray-900">
            Krijo Kontejner të Ri
          </h2>
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
                ID
              </label>
              <input
                type="text"
                {...register("id", { required: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
                placeholder="CONT-001"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Zona (ID)
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
                Lloji
              </label>
              <select
                {...register("type", { required: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              >
                <option value="PLASTIC">Plastikë</option>
                <option value="PAPER">Letër</option>
                <option value="GLASS">Qelq</option>
                <option value="MIXED">I përzier</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Kapaciteti (L)
              </label>
              <input
                type="number"
                min={0}
                {...register("capacity")}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
                placeholder="1100"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Rruga
              </label>
              <input
                type="text"
                {...register("street", { required: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Qyteti
              </label>
              <input
                type="text"
                {...register("city", { required: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
                required
              />
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
                Kodi Postar
              </label>
              <input
                type="text"
                {...register("postalCode")}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
            </div>
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

          <div className="flex items-center justify-between pt-2">
            <label className="flex items-center space-x-2 text-sm text-gray-700">
              <input
                type="checkbox"
                {...register("operational")}
                className="rounded border-gray-300 text-eco-blue focus:ring-eco-blue"
              />
              <span>Operativ</span>
            </label>

            <div className="flex gap-3">
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
                {isSubmitting ? "Duke krijuar..." : "Krijo Kontejner"}
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateContainerModal;


