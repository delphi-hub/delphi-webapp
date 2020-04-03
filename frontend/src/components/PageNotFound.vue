<template>
  <v-app id="vuetifyApp">
    <app-header></app-header>
    <br /><br /><br /><br /><br />
    <div class="noPage">
      <h1>404 ERROR</h1>
      <h3>THE PAGE YOU HAVE REQUESTED COULD NOT BE FOUND</h3>
    </div>
    <app-footer></app-footer>
  </v-app>
</template>

<script>
import Header from "./Layout/Header";
import Footer from "./Layout/Footer";

export default {
  data() {
    return {};
  },
  components: {
    "app-header": Header,
    "app-footer": Footer
  },
  methods: {}
};
</script>

<style>
/* #vuetifyApp {
  background-color: rgb(255, 255, 255);
} */

.noPage {
    align-self: center;
    background: lightgray;
    width: 50%;
}
</style>