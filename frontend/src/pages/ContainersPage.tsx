import React, { useState } from "react";
import { useContainers } from "../hooks/useContainers";
import { Search, Download, MapPin, Plus, Edit, Trash2 } from "lucide-react";
import type { Container } from "../services/api";
import ContainerDetails from "../components/ContainerDetails";
import { exportContainersToCSV } from "../utils/csvExport";
import {
  isCritical,
  isWarning,
  isNormal,
  getStatusBadge,
} from "../utils/thresholdUtils";
import CreateContainerModal from "../components/CreateContainerModal";
import EditContainerModal from "../components/EditContainerModal";
import DeleteContainerModal from "../components/DeleteContainerModal";

const ContainersPage: React.FC = () => {
  const { containers, loading, statistics, refresh } = useContainers(30000);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("all");
  const [selectedContainer, setSelectedContainer] = useState<Container | null>(
    null
  );
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [editingContainer, setEditingContainer] = useState<Container | null>(
    null
  );
  const [deletingContainerId, setDeletingContainerId] = useState<string | null>(
    null
  );

  const filteredContainers = containers.filter((container) => {
    const matchesSearch =
      container.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
      container.address.toLowerCase().includes(searchTerm.toLowerCase());

    if (statusFilter === "all") return matchesSearch;
    if (statusFilter === "critical")
      return matchesSearch && isCritical(container.fillLevel);
    if (statusFilter === "warning")
      return matchesSearch && isWarning(container.fillLevel);
    if (statusFilter === "normal")
      return matchesSearch && isNormal(container.fillLevel);
    return matchesSearch;
  });

  return (
    <div className="space-y-6">
      {/* Statistics Cards */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4 flex-1">
        <div className="bg-white rounded-lg shadow p-6">
          <p className="text-sm text-gray-600">Total Kontejnerë</p>
          <p className="text-3xl font-bold text-gray-900 mt-2">
            {statistics.total}
          </p>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <p className="text-sm text-gray-600">Kritikë</p>
          <p className="text-3xl font-bold text-red-600 mt-2">
            {statistics.critical}
          </p>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <p className="text-sm text-gray-600">Paralajmërim</p>
          <p className="text-3xl font-bold text-amber-600 mt-2">
            {statistics.warning}
          </p>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <p className="text-sm text-gray-600">Normal</p>
          <p className="text-3xl font-bold text-green-600 mt-2">
            {statistics.normal}
          </p>
        </div>
        </div>
        <div className="flex gap-3 self-end">
          <button
            onClick={() => setShowCreateModal(true)}
            className="flex items-center space-x-2 px-4 py-2 bg-eco-blue text-white rounded-lg hover:bg-blue-600 transition-colors"
          >
            <Plus className="w-5 h-5" />
            <span>Shto Kontejner</span>
          </button>
          <button
            onClick={() => exportContainersToCSV(filteredContainers)}
            className="flex items-center space-x-2 px-4 py-2 bg-eco-green text-white rounded-lg hover:bg-green-600 transition-colors"
          >
            <Download className="w-5 h-5" />
            <span>Eksporto CSV</span>
          </button>
        </div>
      </div>

      {/* Filters and Search */}
      <div className="bg-white rounded-lg shadow p-4">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between space-y-4 md:space-y-0">
          <div className="flex items-center space-x-4 flex-1">
            <div className="relative flex-1 md:w-80">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
              <input
                type="text"
                placeholder="Kërko sipas ID ose adresës..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
            </div>

            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            >
              <option value="all">Të gjitha</option>
              <option value="critical">Kritikë</option>
              <option value="warning">Paralajmërim</option>
              <option value="normal">Normal</option>
            </select>
          </div>
        </div>
      </div>

      {/* Table */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  ID / Lloji
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Adresa
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Zona
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Mbushje
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Kapacitet
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Statusi
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Veprime
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredContainers.map((container) => (
                <tr
                  key={container.id}
                  className="hover:bg-gray-50 transition-colors"
                >
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div>
                      <div className="text-sm font-medium text-gray-900">
                        {container.id}
                      </div>
                      <div className="text-sm text-gray-500">
                        {container.type}
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center text-sm text-gray-900">
                      <MapPin className="w-4 h-4 text-gray-400 mr-2 flex-shrink-0" />
                      <span className="truncate max-w-xs">
                        {container.address}
                      </span>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {container.zoneId}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="flex-1 mr-4">
                        <div className="w-32 bg-gray-200 rounded-full h-2">
                          <div
                            className={`h-2 rounded-full ${
                              isCritical(container.fillLevel)
                                ? "bg-red-500"
                                : isWarning(container.fillLevel)
                                ? "bg-amber-500"
                                : "bg-green-500"
                            }`}
                            style={{ width: `${container.fillLevel}%` }}
                          />
                        </div>
                      </div>
                      <span className="text-sm font-medium text-gray-900">
                        {container.fillLevel}%
                      </span>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {container.capacity}L
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span
                      className={`px-3 py-1 text-xs font-semibold rounded-full ${getStatusBadge(
                        container.fillLevel
                      )}`}
                    >
                      {container.status}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-3">
                    <button
                      onClick={() => setSelectedContainer(container)}
                      className="text-eco-blue hover:text-blue-700 inline-flex items-center gap-1"
                    >
                      Detaje
                    </button>
                    <button
                      onClick={() => setEditingContainer(container)}
                      className="text-amber-600 hover:text-amber-700 inline-flex items-center gap-1"
                      title="Përditëso"
                    >
                      <Edit className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => setDeletingContainerId(container.id)}
                      className="text-red-600 hover:text-red-700 inline-flex items-center gap-1"
                      title="Fshi"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Container Details Modal */}
      {selectedContainer && (
        <ContainerDetails
          container={selectedContainer}
          onClose={() => setSelectedContainer(null)}
          onUpdate={refresh}
        />
      )}

      <CreateContainerModal
        isOpen={showCreateModal}
        onClose={() => setShowCreateModal(false)}
        onCreated={refresh}
      />

      <EditContainerModal
        container={editingContainer}
        onClose={() => setEditingContainer(null)}
        onUpdated={refresh}
      />

      <DeleteContainerModal
        containerId={deletingContainerId}
        onClose={() => setDeletingContainerId(null)}
        onDeleted={refresh}
      />
    </div>
  );
};

export default ContainersPage;
