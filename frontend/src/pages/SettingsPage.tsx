import React, { useState } from 'react';
import { Settings, Bell, Globe, RefreshCw, Save, Moon, Sun } from 'lucide-react';

const SettingsPage: React.FC = () => {
  const [settings, setSettings] = useState({
    autoRefresh: true,
    refreshInterval: 30,
    notifications: true,
    criticalThreshold: 90,
    warningThreshold: 70,
    language: 'sq',
    theme: 'light',
  });

  const [saved, setSaved] = useState(false);

  // Load settings from localStorage
  React.useEffect(() => {
    const saved = localStorage.getItem('ecokosova_settings');
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        setSettings(prev => ({ ...prev, ...parsed }));
      } catch (e) {
        console.error('Error loading settings:', e);
      }
    }
  }, []);

  const handleSave = () => {
    localStorage.setItem('ecokosova_settings', JSON.stringify(settings));
    setSaved(true);
    setTimeout(() => setSaved(false), 2000);
    // Trigger refresh për të përditësuar threshold-et në të gjitha komponentet
    window.dispatchEvent(new Event('settingsUpdated'));
  };

  return (
    <div className="space-y-6">
      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex items-center space-x-3 mb-6">
          <div className="w-10 h-10 bg-eco-blue bg-opacity-10 rounded-lg flex items-center justify-center">
            <Settings className="w-6 h-6 text-eco-blue" />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-gray-900">Cilësimet</h2>
            <p className="text-sm text-gray-500">Menaxho konfigurimet e sistemit</p>
          </div>
        </div>

        {/* Auto Refresh */}
        <div className="mb-6 pb-6 border-b border-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center space-x-3">
              <RefreshCw className="w-5 h-5 text-gray-600" />
              <div>
                <h3 className="font-semibold text-gray-900">Auto-Refresh</h3>
                <p className="text-sm text-gray-500">
                  Përditëso automatikisht të dhënat në interval të caktuar
                </p>
              </div>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.autoRefresh}
                onChange={(e) =>
                  setSettings({ ...settings, autoRefresh: e.target.checked })
                }
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-eco-blue peer-focus:ring-opacity-20 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-eco-blue"></div>
            </label>
          </div>

          {settings.autoRefresh && (
            <div className="ml-8">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Intervali i Refresh (sekonda)
              </label>
              <input
                type="number"
                min="10"
                max="300"
                value={settings.refreshInterval}
                onChange={(e) =>
                  setSettings({
                    ...settings,
                    refreshInterval: parseInt(e.target.value) || 30,
                  })
                }
                className="w-32 px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
            </div>
          )}
        </div>

        {/* Notifications */}
        <div className="mb-6 pb-6 border-b border-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center space-x-3">
              <Bell className="w-5 h-5 text-gray-600" />
              <div>
                <h3 className="font-semibold text-gray-900">Njoftime</h3>
                <p className="text-sm text-gray-500">
                  Aktivizo ose çaktivizo njoftimet e sistemit
                </p>
              </div>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.notifications}
                onChange={(e) =>
                  setSettings({ ...settings, notifications: e.target.checked })
                }
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-eco-blue peer-focus:ring-opacity-20 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-eco-blue"></div>
            </label>
          </div>
        </div>

        {/* Thresholds */}
        <div className="mb-6 pb-6 border-b border-gray-200">
          <h3 className="font-semibold text-gray-900 mb-4">Kufijtë e Alarmit</h3>
          <div className="space-y-4 ml-8">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Niveli Kritik (%)
              </label>
              <input
                type="number"
                min="70"
                max="100"
                value={settings.criticalThreshold}
                onChange={(e) =>
                  setSettings({
                    ...settings,
                    criticalThreshold: parseInt(e.target.value) || 90,
                  })
                }
                className="w-32 px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
              <p className="text-xs text-gray-500 mt-1">
                Kontejnerët me mbushje mbi këtë nivel konsiderohen kritikë
              </p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Niveli i Paralajmërimit (%)
              </label>
              <input
                type="number"
                min="50"
                max="90"
                value={settings.warningThreshold}
                onChange={(e) =>
                  setSettings({
                    ...settings,
                    warningThreshold: parseInt(e.target.value) || 70,
                  })
                }
                className="w-32 px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
              />
              <p className="text-xs text-gray-500 mt-1">
                Kontejnerët me mbushje mbi këtë nivel marrin paralajmërim
              </p>
            </div>
          </div>
        </div>

        {/* Language */}
        <div className="mb-6 pb-6 border-b border-gray-200">
          <div className="flex items-center space-x-3 mb-4">
            <Globe className="w-5 h-5 text-gray-600" />
            <div>
              <h3 className="font-semibold text-gray-900">Gjuha</h3>
              <p className="text-sm text-gray-500">Zgjidh gjuhën e ndërfaqes</p>
            </div>
          </div>
          <div className="ml-8">
            <select
              value={settings.language}
              onChange={(e) =>
                setSettings({ ...settings, language: e.target.value })
              }
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            >
              <option value="sq">Shqip</option>
              <option value="en">English</option>
            </select>
          </div>
        </div>

        {/* Theme */}
        <div className="mb-6">
          <div className="flex items-center space-x-3 mb-4">
            {settings.theme === 'light' ? (
              <Sun className="w-5 h-5 text-gray-600" />
            ) : (
              <Moon className="w-5 h-5 text-gray-600" />
            )}
            <div>
              <h3 className="font-semibold text-gray-900">Tema</h3>
              <p className="text-sm text-gray-500">Zgjidh temën e ndërfaqes</p>
            </div>
          </div>
          <div className="ml-8">
            <div className="flex space-x-3">
              <button
                onClick={() => setSettings({ ...settings, theme: 'light' })}
                className={`px-4 py-2 rounded-lg border-2 transition-all ${
                  settings.theme === 'light'
                    ? 'border-eco-blue bg-eco-blue bg-opacity-10 text-eco-blue'
                    : 'border-gray-300 text-gray-700 hover:border-gray-400'
                }`}
              >
                <Sun className="w-5 h-5 inline mr-2" />
                E Dritë
              </button>
              <button
                onClick={() => setSettings({ ...settings, theme: 'dark' })}
                className={`px-4 py-2 rounded-lg border-2 transition-all ${
                  settings.theme === 'dark'
                    ? 'border-eco-blue bg-eco-blue bg-opacity-10 text-eco-blue'
                    : 'border-gray-300 text-gray-700 hover:border-gray-400'
                }`}
              >
                <Moon className="w-5 h-5 inline mr-2" />
                E Errët
              </button>
            </div>
          </div>
        </div>

        {/* Save Button */}
        <div className="flex justify-end pt-4 border-t">
          <button
            onClick={handleSave}
            className={`px-6 py-2 rounded-lg font-medium transition-all flex items-center space-x-2 ${
              saved
                ? 'bg-green-500 text-white'
                : 'bg-eco-blue text-white hover:bg-blue-600'
            }`}
          >
            <Save className="w-4 h-4" />
            <span>{saved ? 'U Ruajt!' : 'Ruaj Cilësimet'}</span>
          </button>
        </div>
      </div>
    </div>
  );
};

export default SettingsPage;

