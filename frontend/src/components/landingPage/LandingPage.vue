<template>
	<v-app id="vuetifyApp">
		<app-header></app-header>
		<keep-alive include="SearchPart">
			<app-search></app-search>
		</keep-alive>
		<app-footer></app-footer>
	</v-app>
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

<style>
	#vuetifyApp {
		background-color: rgb(255, 255, 255);
	}
</style>