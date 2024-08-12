const colors = require('tailwindcss/colors');

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["../resources/templates/**/*.{html,js}"],
  theme: {
    colors: {
      primary: colors.violet,
      ...colors
    },
    extend: {},
  },
  plugins: [],
}

