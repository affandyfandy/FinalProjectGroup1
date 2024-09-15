/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      fontFamily: {
        poppins: ["Poppins", "sans-serif"], // Add Poppins font family
      },
      colors: {
        "fpt-colors": "#F37021", // Correctly define hex color as a string
      },
      dropShadow: {
        "button-dropShadow-fpt": "0px 4px 37px 12px rgba(237, 28, 36, 0.19)", // Define shadow in proper format
      },
    },
  },
  plugins: [],
};
