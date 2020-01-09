var axios = require('axios');
var axiosInstance = axios.create({
  baseURL: 'https://delphi.cs.uni-paderborn.de/api/features',
});
module.exports = axiosInstance;