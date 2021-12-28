<template>
  <div class="h-screen mx-5">
    <img src="./assets/logo.svg" alt="DelphiHub Logo" class="w-52" />
    <router-view/>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  components: {},
})
export default class App extends Vue {}
</script>

