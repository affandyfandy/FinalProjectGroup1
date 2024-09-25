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
        "dropShadow-fpt": "0px 4px 37px 12px rgba(237, 28, 36, 0.19)", // Define shadow in proper format
      },
      backgroundImage: {
        "gradient-fpt-1":
          "linear-gradient(88.57deg, rgba(243, 112, 33, 0.88) 49.97%, #F8975C 83.11%, rgba(242, 162, 114, 0.55) 106.6%)",
        "gradient-fpt-2":
          "linear-gradient(73.23deg, #F37021 0%, rgba(245, 133, 64, 0.82) 46.87%, rgba(242, 162, 114, 0.58) 74.8%)",
        "gradient-fpt-3":
          "linear-gradient(88.57deg, rgba(243, 112, 33, 0.88) 49.97%, #F8975C 83.11%, rgba(242, 162, 114, 0.55) 106.6%)",
      },
    },
  },
  plugins: [],
};
