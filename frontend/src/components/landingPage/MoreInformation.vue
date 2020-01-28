<template>
  <div>
    <app-header></app-header>
    <h1>{{ receivedArtifactId }}</h1>
    <app-footer></app-footer>
  </div>
</template>
<script>
import Header from "../Layout/Header";
import Footer from "../Layout/Footer";
import { eventBus } from "../../main";

export default {
  name: "moreInformation",
  data() {
    return {
      receivedArtifactId: String
    };
  },
  created() {
    eventBus.$on("moreInfoEvent", data => {
      this.receivedArtifactId = data;
    });
  },
  components: {
    "app-header": Header,
    "app-footer": Footer
  },
  methods: { 
  }
};
</script>