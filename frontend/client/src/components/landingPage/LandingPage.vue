<template>
  <div>
    <app-search></app-search>
    <app-result></app-result>
  </div>
</template>
<script>
import SearchPart from "./SearchPart";
import ResultPart from "./ResultPart";
export default {
  data() {
    return {};
  },
  components: {
    "app-search": SearchPart,
    "app-result": ResultPart
  },
  methods: {}
};
</script>