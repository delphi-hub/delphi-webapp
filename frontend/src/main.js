import Vue from "vue";
import App from "./App.vue";
import VueRouter from "vue-router";
import { routes } from "./routes";
import LandingPage from "./components/landingPage/LandingPage";
import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import vuetify from "./plugins/vuetify";
import VueResource from "vue-resource";
import { ImagePlugin } from "bootstrap-vue";

Vue.use(VueResource);

Vue.http.options.root = "";
Vue.http.interceptors.push((request, next) => {
  next((response) => {
    response.json = () => {
      return {messages: response.body};
    };
  });
});

Vue.component("app-landing", LandingPage);

Vue.use(VueRouter);
Vue.use(ImagePlugin);
const router = new VueRouter({
  routes,
  mode: "history"
});

var vm = new Vue({
  router,
  vuetify,
  render: (h) => h(App) 
});

vm.$mount("#app");
