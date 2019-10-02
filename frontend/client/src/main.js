import Vue from 'vue';
import App from './App.vue';
import VueRouter from 'vue-router';
import { routes } from './routes';
import LandingPage from './components/landingPage/LandingPage';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

Vue.component('app-landing', LandingPage);

Vue.use(VueRouter);
const router = new VueRouter({
  routes,
  mode: 'history'
})

new Vue({
  el: '#app',
  router: router,
  render: h => h(App)
})

