import Vue from 'vue';
import App from './App.vue';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import SearchPart from './SearchPart.vue';
import ResultPart from './ResultPart.vue';

Vue.component('search-part', SearchPart);
Vue.component('result-part', ResultPart);

new Vue({
  el: '#app',
  render: h => h(App)
})
