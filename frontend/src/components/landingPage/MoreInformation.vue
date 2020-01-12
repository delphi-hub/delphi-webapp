<template>
  <div>
    <app-header></app-header>
    <app-footer></app-footer>
    <h1>Welcome to the More Information Page</h1>
  </div>
</template>
<script>
import Header from "../Layout/Header";
import Footer from "../Layout/Footer";

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