import React from "react";
import { FileText, Download, Calendar } from "lucide-react";

const ReportsPage: React.FC = () => {
  return (
    <div className="space-y-6">
      <div className="bg-white rounded-lg shadow-md p-12 text-center">
        <FileText className="w-16 h-16 text-eco-blue mx-auto mb-4" />
        <h2 className="text-2xl font-bold text-gray-900 mb-2">
          Raporte & Analiza
        </h2>
        <p className="text-gray-600 mb-6">
          Gjeneroni raporte mbi performancën dhe statistikat e sistemit.
        </p>
        <div className="inline-flex items-center space-x-2 text-eco-blue">
          <Download className="w-5 h-5" />
          <span className="font-medium">Së shpejti...</span>
        </div>
      </div>
    </div>
  );
};

export default ReportsPage;
