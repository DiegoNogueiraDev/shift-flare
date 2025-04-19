/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{js,ts,jsx,tsx}',
    './components/**/*.{js,ts,jsx,tsx}',
    './sections/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        'shift-red': {
          DEFAULT: '#FF3D00',
          light: '#FF8A65',
        },
        'shift-purple': {
          DEFAULT: '#7B1FA2',
          light: '#BA68C8',
        },
        'shift-blue': {
          DEFAULT: '#1976D2',
          light: '#64B5F6',
        },
        'shift-orange': {
          DEFAULT: '#FF9800',
          light: '#FFCC80',
        },
        'shift-dark': '#1A1A2E',
      },
      backgroundColor: {
        'navbar-scrolled': 'rgba(255, 255, 255, 0.95)'
      },
      backgroundImage: {
        'gradient-radial': 'radial-gradient(var(--tw-gradient-stops))',
        'gradient-conic': 'conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))',
        'hero-gradient': 'linear-gradient(135deg, #7B1FA2, #1976D2)',
        'card-gradient': 'linear-gradient(135deg, #FF3D00, #FF9800)',
        'button-gradient': 'linear-gradient(to right, #7B1FA2, #1976D2)',
      },
      animation: {
        'fade-in': 'fadeIn 0.5s ease-out',
        'slide-up': 'slideUp 0.5s ease-out',
        'slide-down': 'slideDown 0.3s ease-out',
        'pulse-slow': 'pulse 3s infinite',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { transform: 'translateY(20px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
        slideDown: {
          '0%': { transform: 'translateY(-10px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
      },
      boxShadow: {
        'card': '0 4px 15px rgba(0, 0, 0, 0.1)',
        'nav': '0 2px 10px rgba(0, 0, 0, 0.1)',
      },
    },
    fontFamily: {
      sans: ['Inter', 'sans-serif'],
    },
  },
  plugins: [],
}; 