var path = require("path");
module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],
  configureWebpack: {
    optimization: {
      splitChunks: false
    },
    output: {
      filename: "build.js",
      chunkFilename: "build.js"
    }
  },
  css: {
    extract: false,
  },
  outputDir: path.resolve(__dirname, "../public")
};