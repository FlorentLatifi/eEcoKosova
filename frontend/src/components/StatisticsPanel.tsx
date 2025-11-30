import React from 'react';
import { Trash2, AlertTriangle, CheckCircle, XCircle } from 'lucide-react';

interface StatCardProps {
  icon: React.ElementType;
  label: string;
  value: number;
  color: string;
  bgColor: string;
}

const StatCard: React.FC<StatCardProps> = ({ icon: Icon, label, value, color, bgColor }) => (
  <div className="bg-white rounded-lg shadow-md p-6">
    <div className="flex items-center justify-between">
      <div>
        <p className="text-sm font-medium text-gray-600">{label}</p>
        <p className={`text-3xl font-bold mt-2 ${color}`}>{value}</p>
      </div>
      <div className={`${bgColor} rounded-full p-4`}>
        <Icon className={`w-8 h-8 ${color}`} />
      </div>
    </div>
  </div>
);

interface StatisticsPanelProps {
  statistics: {
    total: number;
    critical: number;
    warning: number;
    normal: number;
    offline: number;
  };
}

const StatisticsPanel: React.FC<StatisticsPanelProps> = ({ statistics }) => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4 mb-6">
      <StatCard
        icon={Trash2}
        label="Total Kontejnerë"
        value={statistics.total}
        color="text-gray-800"
        bgColor="bg-gray-100"
      />
      <StatCard
        icon={AlertTriangle}
        label="Kritikë"
        value={statistics.critical}
        color="text-red-600"
        bgColor="bg-red-100"
      />
      <StatCard
        icon={AlertTriangle}
        label="Paralajmërim"
        value={statistics.warning}
        color="text-amber-600"
        bgColor="bg-amber-100"
      />
      <StatCard
        icon={CheckCircle}
        label="Normal"
        value={statistics.normal}
        color="text-green-600"
        bgColor="bg-green-100"
      />
      <StatCard
        icon={XCircle}
        label="Jo-Operativ"
        value={statistics.offline}
        color="text-gray-600"
        bgColor="bg-gray-100"
      />
    </div>
  );
};

export default StatisticsPanel;

