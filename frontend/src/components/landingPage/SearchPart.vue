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
						@confirmFinalQueryReset="finalQueryToReset = $event"
						:finalQueryShouldBeReseted="finalQueryToReset">
					</query>
					<queryMenu @addQuerySent="saveQuery"></queryMenu>
				</div>
			</div>
			<hr/>
			<button
				id="startSearchButton"
				class="btn btn-dark"
				@click="startSearch"
				:style="[(readyToSearchQuery) ? {'background-color': '#c20202'} : {'background-color':null}]">
				<!-- TODO: The condition has to be changed -->
				<h5 id="searchButtonText">Start Search</h5>
			</button>
		</div>
		<div>
			<v-app>
				<div id="resultTableDiv" class="card">
					<v-data-table v-bind:headers="headers" :items="items" class="elevation-1"></v-data-table>
				</div>
			</v-app>
			<br/> <br/> <br/>
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
				finalQueryToReset: false,
				headers: [
					{ text: "ArtifactId", align: "left", value: "metadata.artifactId" },
					{ text: "GroupId", value: "metadata.groupId" },
					{ text: "Source", value: "metadata.source" },
					{ text: "Version", value: "metadata.version" }
				],
				items: []
			};
		},
		methods: {
			saveQuery(querySent) {
				this.savedQuery = querySent;
			},
			startSearch() {
/*				if (this.readyToSearchQuery) {
					var vm = this;
					console.log(this.readyToSearchQuery);
					this.$http
						.get("search/" + vm.readyToSearchQuery)
						.then(response => {
							return response.json();
						})
					.then(data => {
						console.log("search/" + vm.readyToSearchQuery, data);
						vm.items = JSON.parse(
							data.messages.substring(8, data.messages.length - 1)
						);
						console.log(vm.items);
						this.readyToSearchQuery = "";
						this.finalQueryToReset = true;
					}),
					error => {
						console.log("fail", error);
					};
				}*/
			}
		}
	}
</script>

<style>
	.container-fluid {
		margin-top: 10px;
	}

	#searchPart {
		margin: 40px;
		padding: 10px;
		height: 530px;
		box-shadow: 1px 1px 5px 3px grey;
		background-color: rgb(250, 250, 250);
	}

	.queryHelpText {
		font-variant: small-caps;
	}

	#startSearchButton {
		width: 30%;
		height: 45px;
		margin-left: 15px;
		/*  background-color:#c20202; */
		font-variant: small-caps;
	}

	#startSearchButton:hover {
		/*	background-color: #c30303; */
		box-shadow: 1px 1px 5px 3px grey;
		border-radius: 3px;
	}
	#resultTableDiv {
		margin: 40px;
		box-shadow: 1px 1px 5px 3px grey;
	}
</style>