<template>
	<div style="background-color: rgb(255, 255, 255); margin-bottom: 10px; padding:0 30px 30px 30px">
		<v-row justify="center">
			<v-col cols="12" class="py-1">
				<v-card elevation=24 style="border-radius: 15px 15px 15px 15px; background-color: rgb(255, 255, 255);">
					<v-card-title style="background-color: rgb(190, 33, 33); color:white" class="layout justify-center mb-1 pa-1"> 
						<div  class="headline text-center">Welcome to Delphi</div> 
					</v-card-title>
					<v-card-text>
						... , formulate your query and start the search. If you need help with the formulation of the 
											query, use the query creation helper below the query text field. 
											formulate your query and start the search. If you need help with the formulation of the 
											query, use the query creation helper below the query text field.
					</v-card-text>	
				</v-card>
			</v-col>
			<v-col cols="12">
				<v-card class="pa-1" elevation=24 style="border-radius: 15px 15px 15px 15px; background-color: rgb(255, 255, 255);">
						<!--savedQuery updates the prop called partQuery
					finalQueryToReset updates the prop called finalQueryShouldBeReseted. It is false on default. If it becomes true, the finalQuery will be reseted.
					After adding a new partQuery to the query component, it asks for resetting the savedquery.
					After the reset of the finalQuery, the finalQueryToReset variable will be set to false.
					After the click on the search button in the query component it sends the final query here, because the startSearch function lies here.-->	
					<query
					:errMsg="queryError"
					:partQuery="savedQuery"
					:isLoading="progressBar"
					@emptyQuery="clearItems = $event"
					@finalQuerySend="readyToSearchQuery = $event"
					@resetSavedQuery="savedQuery = $event"
					></query>
					<!--The created query is comming from queryMenu component and is saved in the saveQuery variable by the saveQueryMethod.-->
					<queryMenu @addQuerySent="saveQuery"></queryMenu>
				</v-card>
			</v-col>
			<v-col cols="12"> 
				<v-card elevation=24 style="border-radius: 15px 15px 15px 15px; background-color: rgb(255, 255, 255);">
					<div
						class="inputQueryInResult"
						v-if="readyToSearchQuery"
					> <br> You searched for the query : <p id="searchedQueryInResult">{{ readyToSearchQuery }}</p></div>
					<hr />
					<v-data-table
						:headers="headers"
						:items="items.hits"
						:loading="progressBar"
						loading-text="Searching for the results, please wait...."
						class="elevation-1"
					>
						<v-progress-linear
							v-show="progressBar"
							slot="progress"
							loading-text="Loading... Please wait"
							indeterminate
						></v-progress-linear>
						<v-alert slot="no-data" :value="true" class="error1">No data available</v-alert>
						<template v-slot:item.moreInfo="{ item }">
							<router-link
								v-on:click.native="moreInfoNavigation(item.metadata.artifactId, item.metadata.groupId, item.metadata.version)"
								:to="{ path: '/MoreInformation' }"
							>More Information</router-link>
						</template>
					</v-data-table>
				</v-card>
			</v-col>
		</v-row>
	</div>
</template>

<script>
	import Query from "./Query.vue";
	import QueryMenu from "./QueryMenu.vue";
	import { eventBus } from "../../main";

	export default {
		components: {
			query: Query,
			queryMenu: QueryMenu
		},
		data() {
			return {
				id: "",
				savedQuery: "", //query from queryMenu will be saved here
				readyToSearchQuery: "", //finalQuery from the query component will be saved here
				headers: [
					{ text: "GroupId",  align: "left", value: "metadata.groupId", width: "10%" },
					{ text: "ArtifactId", value: "metadata.artifactId", width: "10%" },       
					{ text: "Source", value: "metadata.source", width: "10%" },
					{ text: "Version", value: "metadata.version", width: "10%" },
					{ text: "More Information", value: "moreInfo", width: "10%" },
					{ text: "Results", value: "metricResults.result", width: "5%" }
				],
				fields: {
					GroupId: "metadata.groupId",
					ArtifactId: "metadata.artifactId",       
					Source: "metadata.source",
					Version: {
						field: "metadata.version",
						callback: value => {
							return `v.${value}`;
						}
					},
					Results: "metricResults.result"
				},
				meta: [
					[
						{
							key: "charset",
							value: "utf-8"
						}
					]
				],
				items: [],
				queryError: "",
				progressBar: false,
				clearItems: false
			};
		},
		watch: {
			//if this variable is changed, the startSearch function will be triggered
			readyToSearchQuery: function(newVal) {
				if (newVal) {
					this.startSearch();
				}
			},
			clearItems: function(newVal) {
				if (newVal) {
					this.clearItems = newVal;
					this.clearItemsOnReqValidator(newVal);
				}
			}
		},
		methods: {
			//This method saves querySent in the variable savedQuery
			saveQuery(querySent) {
				this.savedQuery = querySent;
			},
			startSearch() {
				var vm = this;
				if (this.readyToSearchQuery) {
					this.progressBar = true;
					this.$http
						.get("search/" + vm.readyToSearchQuery)
						.then(response => {
							return response.json();
						})
						.then(
							data => {
								vm.items = data.messages;
								if (data.messages.length != 0) {
									if (data.messages.totalHits != 0) {
										var key = Object.keys(vm.items.hits[0].metricResults);
										for (var i = 0; i < vm.items.hits.length; i++) {
											var obj = vm.items.hits[i].metricResults;
											obj.result = obj[key[0]];
											delete obj[key[0]];
											vm.items.hits[i].metricResults = obj;
										}
									}
								}
								vm.progressBar = false;
							},
							error => {
								vm.items = [];
								vm.progressBar = false;
								vm.readyToSearchQuery = "";
								if (error.status == 417) {
									vm.queryError = error.body
								} else {
									vm.queryError ="We received " + error.status + " " + error.statusText;
								}
							}
						);
				} else if (this.clearItems) {
					vm.items = [];
				}
			},
			clearItemsOnReqValidator(newVal) {
				if (newVal) {
					this.items = [];
				}
			},
			moreInfoNavigation(artifactIdParam,groupIdParam,versionParam) {
				this.id = groupIdParam+":"+artifactIdParam+":"+versionParam;
				eventBus.$emit("moreInfoEvent", this.id);
			}
		}
	};
</script>

<style>
	.inputQueryInResult {
		text-align: center;
	}

	#searchedQueryInResult {
		font-weight: bold;
	}
</style>