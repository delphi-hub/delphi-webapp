<template>
  <div>
    <app-header></app-header>
    <app-search></app-search>
    <app-result></app-result>   
  </div>
</template>
<script>
import SearchPart from "./SearchPart";
import ResultPart from "./ResultPart";
import Header from "../Layout/Header";

export default {
  data() {
    return {};
  },
  components: {
    "app-search": SearchPart,
    "app-result": ResultPart,
    "app-header": Header
  },
  methods: {}
};
</script>