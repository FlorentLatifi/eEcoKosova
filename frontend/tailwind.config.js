/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'eco-green': '#10B981',
        'eco-amber': '#F59E0B',
        'eco-red': '#EF4444',
        'eco-blue': '#3B82F6',
        'eco-gray': '#6B7280',
      }
    },
  },
  plugins: [],
}

