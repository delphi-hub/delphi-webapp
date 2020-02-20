<template>
	<div style="background-color: rgb(139, 12, 12)">
		<app-header></app-header>
		<keep-alive include="SearchPart">
			<app-search></app-search>
		</keep-alive>
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

</style>