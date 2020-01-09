var axios = require('axios');
var axiosInstance = axios.create({
  baseURL: 'https://delphi.cs.uni-paderborn.de/api-legacy/features',
});
module.exports = axiosInstance;