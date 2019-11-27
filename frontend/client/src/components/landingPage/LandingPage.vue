<template>
	<div>
    	<app-header></app-header>
    	<app-search></app-search>
    	<app-result></app-result>
    	<app-footer></app-footer>  
	</div>
</template>

<script>
	import SearchPart from "./SearchPart";
	import ResultPart from "./ResultPart";
	import Header from "../Layout/Header";
	import Footer from "../Layout/Footer";

	export default {
		data() {
    		return {};
  		},
		components: {
    		"app-search": SearchPart,
    		"app-result": ResultPart,
    		"app-header": Header,
    		"app-footer": Footer
		},
  		methods: {}
	};
</script>

<style scoped>
	div {
		background-color: rgb(235, 235, 235);
	}
</style>