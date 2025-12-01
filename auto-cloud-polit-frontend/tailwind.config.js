/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'space-blue': '#0f172a',
        'neon-cyan': '#00f0ff',
        'dark-blue': '#0c1427',
      },
      boxShadow: {
        'neon': '0 0 10px rgba(0, 240, 255, 0.5)',
      }
    },
  },
  plugins: [],
}