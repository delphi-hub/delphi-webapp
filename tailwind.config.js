module.exports = {
  purge: { content: ['./public/**/*.html', './src/**/*.vue'] },
  darkMode: false, // or 'media' or 'class'
  theme: {
    fontSize: { 
      'xs' : ['0.6rem', {
        lineHeight: '.75rem',
      }]
    },
    colors: {
      'delphi-red': '#ED1C24',
      'delphi-black': '#414042',
      'delphi-gray': '#CFCFCE' 
    },
    // fontFamily: {
    //   sans: ['Graphik', 'sans-serif'],
    //   serif: ['Merriweather', 'serif'],
    // },
    extend: {},
  },
  variants: {
    extend: {},
  },
  plugins: [],
}
