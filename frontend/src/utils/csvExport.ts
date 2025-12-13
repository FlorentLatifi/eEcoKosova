import type { Container } from '../services/api';

export const exportContainersToCSV = (containers: Container[], filename: string = 'kontejneret') => {
  if (containers.length === 0) {
    alert('Nuk ka të dhëna për eksportim');
    return;
  }

  // CSV Header
  const headers = [
    'ID',
    'Zona ID',
    'Lloji',
    'Niveli i Mbushjes (%)',
    'Statusi',
    'Kapaciteti (L)',
    'Operativ',
    'Adresa',
    'Latitude',
    'Longitude',
    'Nevojë për Mbledhje',
  ];

  // CSV Rows
  const rows = containers.map((container) => [
    container.id,
    container.zoneId,
    container.type,
    container.fillLevel.toString(),
    container.status,
    container.capacity.toString(),
    container.operational ? 'Po' : 'Jo',
    container.address,
    container.latitude.toString(),
    container.longitude.toString(),
    container.needsCollection ? 'Po' : 'Jo',
  ]);

  // Combine headers and rows
  const csvContent = [
    headers.join(','),
    ...rows.map((row) => row.map((cell) => `"${cell}"`).join(',')),
  ].join('\n');

  // Create blob and download
  const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement('a');
  const url = URL.createObjectURL(blob);
  link.setAttribute('href', url);
  link.setAttribute('download', `${filename}_${new Date().toISOString().split('T')[0]}.csv`);
  link.style.visibility = 'hidden';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

export const exportZonesToCSV = (zones: any[], filename: string = 'zonat') => {
  if (zones.length === 0) {
    alert('Nuk ka të dhëna për eksportim');
    return;
  }

  const headers = [
    'Zona ID',
    'Emri i Zonës',
    'Total Kontejnerë',
    'Kritikë',
    'Operativë',
    'Mbushje Mesatare (%)',
    'Statusi',
  ];

  const rows = zones.map((zone) => [
    zone.zoneId,
    zone.zoneName,
    zone.totalContainers.toString(),
    zone.criticalContainers.toString(),
    zone.operationalContainers.toString(),
    Math.round(zone.averageFillLevel).toString(),
    zone.status,
  ]);

  const csvContent = [
    headers.join(','),
    ...rows.map((row) => row.map((cell) => `"${cell}"`).join(',')),
  ].join('\n');

  const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement('a');
  const url = URL.createObjectURL(blob);
  link.setAttribute('href', url);
  link.setAttribute('download', `${filename}_${new Date().toISOString().split('T')[0]}.csv`);
  link.style.visibility = 'hidden';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

