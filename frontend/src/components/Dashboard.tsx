import React, { useState } from "react";
import { RefreshCw, AlertTriangle, Activity, Map, List } from "lucide-react";
import { useContainers } from "../hooks/useContainers";
import StatisticsPanel from "./StatisticsPanel";
import ContainerCard from "./ContainerCard";
import AlertList from "./AlertList";
import ContainerDetails from "./ContainerDetails";
import { ContainerMap } from "./ContainerMap";
import type { Container } from "../services/api";
import { isCritical, isWarning, isNormal } from "../utils/thresholdUtils";

type FilterType = "all" | "critical" | "warning" | "normal";
type ViewType = "list" | "map";

const Dashboard: React.FC = () => {
  const { containers, loading, error, statistics, refresh } =
    useContainers(30000);
  const [selectedContainer, setSelectedContainer] = useState<Container | null>(
    null
  );
  const [filter, setFilter] = useState<FilterType>("all");
  const [view, setView] = useState<ViewType>("list");
  const [isRefreshing, setIsRefreshing] = useState(false);

  const handleRefresh = async () => {
    setIsRefreshing(true);
    await refresh();
    setTimeout(() => setIsRefreshing(false), 1000);
  };

  const filteredContainers = containers.filter((container) => {
    if (filter === "critical") return isCritical(container.fillLevel);
    if (filter === "warning") return isWarning(container.fillLevel);
    if (filter === "normal") return isNormal(container.fillLevel);
    return true;
  });

  const handleUpdateContainer = async () => {
    await refresh();
  };

  if (loading && containers.length === 0) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <RefreshCw className="w-12 h-12 text-eco-blue animate-spin mx-auto mb-4" />
          <p className="text-gray-600">Duke ngarkuar të dhënat...</p>
        </div>
      </div>
    );
  }

  if (error && containers.length === 0) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="bg-red-50 border border-red-200 rounded-lg p-6 max-w-md w-full">
          <AlertTriangle className="w-12 h-12 text-red-600 mx-auto mb-4" />
          <h2 className="text-xl font-bold text-red-900 mb-2">
            Gabim në ngarkim
          </h2>
          <p className="text-red-700 mb-4">{error}</p>
          <button
            onClick={handleRefresh}
            className="w-full bg-red-600 text-white py-2 px-4 rounded-lg hover:bg-red-700 transition-colors"
          >
            Provoni Përsëri
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Statistics Panel */}
      <StatisticsPanel statistics={statistics} />

      {/* Alert List */}
      <AlertList containers={containers} />

      {/* Filter Tabs + View Toggle + Refresh Button */}
      <div className="bg-white rounded-lg shadow-md p-4 flex items-center justify-between flex-wrap gap-4">
        <div className="flex space-x-2">
          <button
            onClick={() => setFilter("all")}
            className={`px-4 py-2 rounded-lg font-medium transition-all ${
              filter === "all"
                ? "bg-eco-blue text-white"
                : "bg-gray-100 text-gray-700 hover:bg-gray-200"
            }`}
          >
            Të Gjitha ({containers.length})
          </button>
          <button
            onClick={() => setFilter("critical")}
            className={`px-4 py-2 rounded-lg font-medium transition-all ${
              filter === "critical"
                ? "bg-red-600 text-white"
                : "bg-gray-100 text-gray-700 hover:bg-gray-200"
            }`}
          >
            Kritikë ({statistics.critical})
          </button>
          <button
            onClick={() => setFilter("warning")}
            className={`px-4 py-2 rounded-lg font-medium transition-all ${
              filter === "warning"
                ? "bg-amber-600 text-white"
                : "bg-gray-100 text-gray-700 hover:bg-gray-200"
            }`}
          >
            Paralajmërim ({statistics.warning})
          </button>
          <button
            onClick={() => setFilter("normal")}
            className={`px-4 py-2 rounded-lg font-medium transition-all ${
              filter === "normal"
                ? "bg-green-600 text-white"
                : "bg-gray-100 text-gray-700 hover:bg-gray-200"
            }`}
          >
            Normal ({statistics.normal})
          </button>
        </div>

        <div className="flex items-center space-x-2">
          {/* View Toggle */}
          <div className="flex bg-gray-100 rounded-lg p-1">
            <button
              onClick={() => setView("list")}
              className={`px-3 py-2 rounded-md transition-all flex items-center space-x-2 ${
                view === "list"
                  ? "bg-white text-eco-blue shadow-sm"
                  : "text-gray-600 hover:text-gray-900"
              }`}
            >
              <List className="w-4 h-4" />
              <span>Listë</span>
            </button>
            <button
              onClick={() => setView("map")}
              className={`px-3 py-2 rounded-md transition-all flex items-center space-x-2 ${
                view === "map"
                  ? "bg-white text-eco-blue shadow-sm"
                  : "text-gray-600 hover:text-gray-900"
              }`}
            >
              <Map className="w-4 h-4" />
              <span>Hartë</span>
            </button>
          </div>

          <button
            onClick={handleRefresh}
            disabled={isRefreshing}
            className="flex items-center space-x-2 bg-eco-blue text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors disabled:opacity-50"
          >
            <RefreshCw
              className={`w-5 h-5 ${isRefreshing ? "animate-spin" : ""}`}
            />
            <span>Rifresko</span>
          </button>
        </div>
      </div>

      {/* Containers View - List or Map */}
      {view === "map" ? (
        <div className="bg-white rounded-lg shadow-md p-4">
          <div className="h-[600px] w-full">
            <ContainerMap
              containers={filteredContainers}
              selectedContainer={selectedContainer}
              onContainerClick={setSelectedContainer}
            />
          </div>
        </div>
      ) : filteredContainers.length === 0 ? (
        <div className="bg-white rounded-lg shadow-md p-12 text-center">
          <Activity className="w-16 h-16 text-gray-400 mx-auto mb-4" />
          <h3 className="text-xl font-semibold text-gray-700 mb-2">
            Nuk ka kontejnerë
          </h3>
          <p className="text-gray-500">
            Nuk u gjetën kontejnerë me këtë filtër.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredContainers.map((container) => (
            <ContainerCard
              key={container.id}
              container={container}
              onClick={() => setSelectedContainer(container)}
            />
          ))}
        </div>
      )}

      {/* Container Details Modal */}
      {selectedContainer && (
        <ContainerDetails
          container={selectedContainer}
          onClose={() => setSelectedContainer(null)}
          onUpdate={handleUpdateContainer}
        />
      )}
    </div>
  );
};

export default Dashboard;
