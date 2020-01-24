<template>
	<div>
		<app-header></app-header>
		<app-search></app-search>
		<!-- <app-result></app-result> -->
		<app-footer></app-footer>  
	</div>
</template>

<script>
	import SearchPart from "./SearchPart";
	import Header from "../Layout/Header";
	import Footer from "../Layout/Footer";

	export default {
		data() {
			return {};
		},
		components: {
			"app-search": SearchPart,
			"app-header": Header,
			"app-footer": Footer
		},
		methods: {}
	};
</script>

<style scoped>
	div {
		background-color: rgb(250, 250, 250);
	}
</style>