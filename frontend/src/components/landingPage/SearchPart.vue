<template>
	<div>
		<div class="card" id="searchPart">
			<div class="container-fluid">
				<div class="row">
					<div class="col-6">
						<h5 class="queryHelpText">Type in your query...</h5>
					</div>
					<div class="col-6">
						<h5 class="queryHelpText">...or use these steps to build a query.</h5>
					</div>
				</div>
				<div class="row">
					<query
						:partQuery="savedQuery"
						@finalQuerySend="readyToSearchQuery = $event"
					></query>
					<queryMenu
						@addQuerySent="saveQuery">
					</queryMenu>
				</div>
			</div>
		</div>
		<div>
			<v-app>
				<div id="resultTableDiv" class="card">
					<v-data-table v-bind:headers="headers" :items="items" class="elevation-1"></v-data-table>
				</div>
			</v-app>
			<br />
			<br />
			<br />
		</div>
	</div>
</template>

<script>
	import Query from "./Query.vue";
	import QueryMenu from "./QueryMenu.vue";

	export default {
		components: {
			query: Query,
			queryMenu: QueryMenu
		},
		data() {
			return {
				savedQuery: "",
				readyToSearchQuery: "",
				headers: [
					{ text: "ArtifactId", align: "left", value: "metadata.artifactId" },
					{ text: "GroupId", value: "metadata.groupId" },
					{ text: "Source", value: "metadata.source" },
					{ text: "Version", value: "metadata.version" }
				],
				items: []
			};
		},
		watch: {
			readyToSearchQuery: function (newVal) {
				if(newVal){
					this.startSearch();
				}
			}
		},
		methods: {
			saveQuery(querySent) {
				this.savedQuery = querySent;
			},
			startSearch() {
				if (this.readyToSearchQuery) {
					var vm = this;
					this.$http
						.get("search/" + vm.readyToSearchQuery)
						.then(response => {
							return response.json();
						})
					.then(data => {
						vm.items = data.messages.hits;
					}),
					error => {
						alert("Invalid query!", error.messages)
					};
				}
			}
		}
	};
</script>

<style>
	.container-fluid {
		margin-top: 10px;
	}
	#searchPart {
		margin: 20px;
		padding: 0;
		height: 300px;
		box-shadow: 1px 1px 5px 3px grey;
		background-color: rgb(250, 250, 250);
	}
	.queryHelpText {
		padding: 0;
		font-variant: small-caps;
	}
	.col-6 {
		padding-top: 0 !important;
		padding-bottom: 0 !important;
	}
	#resultTableDiv {
		margin: 30px;
		box-shadow: 1px 1px 5px 3px grey;
	}
</style>
