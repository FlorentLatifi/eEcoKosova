import React, { useState, useEffect } from "react";
import {
  FileText,
  Download,
  Calendar,
  Activity,
  AlertTriangle,
  CheckCircle,
  TrendingUp,
  RefreshCw,
  Map,
} from "lucide-react";
import { getReports, generateReport, type Report } from "../services/api";

const ReportsPage: React.FC = () => {
  const [availableReports, setAvailableReports] = useState<Report[]>([]);
  const [selectedReport, setSelectedReport] = useState<Report | null>(null);
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);

  useEffect(() => {
    fetchReports();
  }, []);

  const fetchReports = async () => {
    try {
      setLoading(true);
      const reports = await getReports();
      setAvailableReports(reports);
    } catch (error) {
      console.error("Error fetching reports:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleGenerateReport = async (reportType: string) => {
    try {
      setGenerating(true);
      const report = await generateReport(reportType);
      setSelectedReport(report);
    } catch (error) {
      console.error("Error generating report:", error);
      alert("Gabim në gjenerimin e raportit!");
    } finally {
      setGenerating(false);
    }
  };

  const formatDate = (dateString: string): string => {
    try {
      const date = new Date(dateString);
      return date.toLocaleString("sq-AL", {
        year: "numeric",
        month: "long",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      });
    } catch {
      return dateString;
    }
  };

  const renderReportData = (report: Report) => {
    if (!report.data) return null;

    const data = report.data;

    switch (report.type) {
      case "GENERAL":
        return (
          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div className="bg-blue-50 rounded-lg p-4 border border-blue-200">
                <p className="text-sm text-gray-600">Total Kontejnerë</p>
                <p className="text-3xl font-bold text-blue-900 mt-2">
                  {data.totalContainers || 0}
                </p>
              </div>
              <div className="bg-red-50 rounded-lg p-4 border border-red-200">
                <p className="text-sm text-gray-600">Kritikë</p>
                <p className="text-3xl font-bold text-red-900 mt-2">
                  {data.criticalContainers || 0}
                </p>
              </div>
              <div className="bg-amber-50 rounded-lg p-4 border border-amber-200">
                <p className="text-sm text-gray-600">Paralajmërim</p>
                <p className="text-3xl font-bold text-amber-900 mt-2">
                  {data.warningContainers || 0}
                </p>
              </div>
              <div className="bg-green-50 rounded-lg p-4 border border-green-200">
                <p className="text-sm text-gray-600">Normal</p>
                <p className="text-3xl font-bold text-green-900 mt-2">
                  {data.normalContainers || 0}
                </p>
              </div>
              <div className="bg-purple-50 rounded-lg p-4 border border-purple-200">
                <p className="text-sm text-gray-600">Total Zona</p>
                <p className="text-3xl font-bold text-purple-900 mt-2">
                  {data.totalZones || 0}
                </p>
              </div>
              <div className="bg-gray-50 rounded-lg p-4 border border-gray-200">
                <p className="text-sm text-gray-600">Mbushje Mesatare</p>
                <p className="text-3xl font-bold text-gray-900 mt-2">
                  {data.averageFillLevel
                    ? `${Number(data.averageFillLevel).toFixed(1)}%`
                    : "0%"}
                </p>
              </div>
            </div>
          </div>
        );

      case "CRITICAL":
        const criticalContainers = data.criticalContainers as any[] || [];
        return (
          <div className="space-y-4">
            <div className="bg-red-50 rounded-lg p-4 border border-red-200">
              <p className="text-sm text-gray-600">Total Kontejnerë Kritikë</p>
              <p className="text-3xl font-bold text-red-900 mt-2">
                {data.count || 0}
              </p>
            </div>
            {criticalContainers.length > 0 && (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50 border-b">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        ID
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Zona
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Mbushje
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Statusi
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Adresa
                      </th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200">
                    {criticalContainers.map((container: any) => (
                      <tr key={container.id} className="hover:bg-gray-50">
                        <td className="px-4 py-3 text-sm font-medium text-gray-900">
                          {container.id}
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-600">
                          {container.zoneId}
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-900">
                          {container.fillLevel}%
                        </td>
                        <td className="px-4 py-3">
                          <span className="px-2 py-1 text-xs font-semibold bg-red-100 text-red-800 rounded">
                            {container.status}
                          </span>
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-600">
                          {container.address}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        );

      case "ZONES":
        const zones = data.zones as any[] || [];
        return (
          <div className="space-y-4">
            <div className="bg-blue-50 rounded-lg p-4 border border-blue-200">
              <p className="text-sm text-gray-600">Total Zona</p>
              <p className="text-3xl font-bold text-blue-900 mt-2">
                {data.totalZones || 0}
              </p>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {zones.map((zone: any) => (
                <div
                  key={zone.zoneId}
                  className="bg-white rounded-lg border border-gray-200 p-4"
                >
                  <h4 className="font-bold text-gray-900">{zone.zoneName}</h4>
                  <p className="text-sm text-gray-500 mb-3">{zone.zoneId}</p>
                  <div className="space-y-2">
                    <div className="flex justify-between">
                      <span className="text-sm text-gray-600">Kontejnerë:</span>
                      <span className="text-sm font-semibold">
                        {zone.totalContainers}
                      </span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-sm text-gray-600">Kritikë:</span>
                      <span className="text-sm font-semibold text-red-600">
                        {zone.criticalContainers}
                      </span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-sm text-gray-600">Mbushje Mesatare:</span>
                      <span className="text-sm font-semibold">
                        {Number(zone.averageFillLevel).toFixed(1)}%
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        );

      case "PERFORMANCE":
        return (
          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="bg-green-50 rounded-lg p-4 border border-green-200">
                <p className="text-sm text-gray-600">Kontejnerë Operativë</p>
                <p className="text-3xl font-bold text-green-900 mt-2">
                  {data.operationalContainers || 0}
                </p>
              </div>
              <div className="bg-red-50 rounded-lg p-4 border border-red-200">
                <p className="text-sm text-gray-600">Jo-Operativë</p>
                <p className="text-3xl font-bold text-red-900 mt-2">
                  {data.nonOperationalContainers || 0}
                </p>
              </div>
              <div className="bg-blue-50 rounded-lg p-4 border border-blue-200">
                <p className="text-sm text-gray-600">Norma e Operativitetit</p>
                <p className="text-3xl font-bold text-blue-900 mt-2">
                  {data.operationalRate
                    ? `${Number(data.operationalRate).toFixed(1)}%`
                    : "0%"}
                </p>
              </div>
              <div className="bg-gray-50 rounded-lg p-4 border border-gray-200">
                <p className="text-sm text-gray-600">Kapacitet Total</p>
                <p className="text-3xl font-bold text-gray-900 mt-2">
                  {data.totalCapacity || 0}L
                </p>
              </div>
            </div>
          </div>
        );

      default:
        return <pre>{JSON.stringify(data, null, 2)}</pre>;
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <Activity className="w-12 h-12 text-eco-blue animate-spin mx-auto mb-4" />
          <p className="text-gray-600">Duke ngarkuar raportet...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">Raporte & Analiza</h2>
            <p className="text-gray-600 mt-1">
              Gjeneroni dhe shikoni raporte mbi performancën e sistemit
            </p>
          </div>
          <button
            onClick={fetchReports}
            className="flex items-center space-x-2 bg-eco-blue text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
          >
            <RefreshCw className="w-5 h-5" />
            <span>Rifresko</span>
          </button>
        </div>
      </div>

      {/* Available Reports */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {availableReports.map((report) => (
          <div
            key={report.id}
            className="bg-white rounded-lg shadow-md hover:shadow-lg transition-all p-6 cursor-pointer"
            onClick={() => handleGenerateReport(report.type)}
          >
            <div className="flex items-center space-x-3 mb-4">
              <div className="bg-eco-blue bg-opacity-10 rounded-lg p-3">
                <FileText className="w-6 h-6 text-eco-blue" />
              </div>
              <div className="flex-1">
                <h3 className="font-bold text-gray-900">{report.title}</h3>
              </div>
            </div>
            <p className="text-sm text-gray-600 mb-4">{report.description}</p>
            <button
              onClick={(e) => {
                e.stopPropagation();
                handleGenerateReport(report.type);
              }}
              disabled={generating}
              className="w-full bg-eco-blue text-white py-2 rounded-lg hover:bg-blue-600 transition-colors disabled:opacity-50"
            >
              {generating ? "Duke gjeneruar..." : "Gjenero"}
            </button>
          </div>
        ))}
      </div>

      {/* Generated Report */}
      {selectedReport && (
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="text-2xl font-bold text-gray-900">
                {selectedReport.title}
              </h3>
              <p className="text-gray-600 mt-1">{selectedReport.description}</p>
              <p className="text-sm text-gray-500 mt-2">
                <Calendar className="w-4 h-4 inline mr-1" />
                Gjeneruar më: {formatDate(selectedReport.generatedAt)}
              </p>
            </div>
            <button
              onClick={() => {
                const dataStr = JSON.stringify(selectedReport.data, null, 2);
                const blob = new Blob([dataStr], { type: "application/json" });
                const url = URL.createObjectURL(blob);
                const link = document.createElement("a");
                link.href = url;
                link.download = `${selectedReport.type}_${Date.now()}.json`;
                link.click();
              }}
              className="flex items-center space-x-2 bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors"
            >
              <Download className="w-5 h-5" />
              <span>Shkarko JSON</span>
            </button>
          </div>

          <div className="border-t pt-6">{renderReportData(selectedReport)}</div>
        </div>
      )}
    </div>
  );
};

export default ReportsPage;
