<template>
	<div id="searchPartDiv">
		<v-row justify="center">
			<v-col cols="12" class="py-1">
				<v-card elevation=24 style="border-radius: 10px 10px 10px 10px; background-color: rgb(255, 255, 255);">
					<v-card-title style="background-color: #db2909; color:white" class="justify-center mb-1 pa-1"> 
						<div  class="headline text-center">Welcome to Delphi</div> 
					</v-card-title>
					<v-card-text class="pb-1">
						Delphi is a software... . Type in your query directly into the 
						'Your Query' textarea or use the query creation menu below, which will guide you through the steps of forming a query.
					</v-card-text>	
				</v-card>
			</v-col>
			<v-col cols="12">
				<v-card class="px-2 pt-0 pb-2" elevation=24 style="border-radius: 10px 10px 10px 10px; background-color: rgb(255, 255, 255);">
						<!--savedQuery updates the prop called partQuery
						finalQueryToReset updates the prop called finalQueryShouldBeReseted. It is false on default. If it becomes true, the finalQuery will be reseted.
						After adding a new partQuery to the query component, it asks for resetting the savedquery.
						After the reset of the finalQuery, the finalQueryToReset variable will be set to false.
						After the click on the search button in the query component it sends the final query here, because the startSearch function lies here.-->	
					<query
						:errMsg="queryError"
						:errCol="errorColumn"
						:partQuery="savedQuery"
						:isLoading="progressBar"
						@emptyQuery="clearItems = $event"
						@finalQueryAndLimitSend="finalQAndLimit = $event"
						@resetSavedQuery="savedQuery = $event"
						@resetErrorColumn="errorColumn = $event"
						@sendQueryToStorage="storageSaveQuery = $event"
					></query>
					<!--The created query is comming from queryMenu component and is saved in the saveQuery variable by the saveQueryMethod.-->
					<queryMenu
						:queryForStorage="storageSaveQuery"
						@resetStorageQuery="storageSaveQuery = $event"
						@addQuerySent="saveQuery"
						@addFromStorage="saveQuery">
					</queryMenu>
				</v-card>
			</v-col>
			<v-col cols="12" class="py-0 pr-5">
				<div class="downloadDiv" v-if="items.totalHits > 0">
					<v-btn @click.stop="dialog = true" outlined color="rgb(63, 53, 53)">
						Export to Excel
						<v-icon>mdi-file-excel</v-icon>
					</v-btn>
				</div>
			</v-col>
			<v-col cols="12" class="pt-2"> 
				<v-card elevation=24 style="border-radius: 10px 10px 10px 10px; background-color: rgb(255, 255, 255);">
					<div
						class="inputQueryInResult"
						v-if="items.totalHits > 0"> 
							<br> You searched for the query : 
							<p id="searchedQueryInResult">{{ readyToSearchQuery }}</p>
					</div>
					<hr />
					<v-data-table
						:headers="headers"
						:items="items.hits"
						:loading="progressBar"
						loading-text="Searching for the results, please wait...."
						class="elevation-1">
						<v-progress-linear
							v-show="progressBar"
							slot="progress"
							loading-text="Loading... Please wait"
							indeterminate
						></v-progress-linear>
						<v-alert slot="no-data" :value="true" class="ma-0">No data available</v-alert>
						<template v-slot:item.moreInfo="{ item }">
							<router-link
								v-on:click.native="moreInfoNavigation(item.metadata.artifactId, item.metadata.groupId, item.metadata.version)"
								:to="{ path: '/MoreInformation' }"
							>More Information</router-link>
						</template>
					</v-data-table>
				</v-card>
			</v-col>
			<v-dialog v-model="dialog" max-width="550">
				<v-card>
					<v-card-title class="headline">Click yes to export all results to excel?</v-card-title>
					<v-card-text>If you click 'yes', an excel file 'results.xlsx' will be downloaded.</v-card-text>
					<v-card-actions>
						<v-spacer></v-spacer>
						<v-btn color="green darken-1" text @click="dialog = false">No</v-btn>
						<v-btn color="green darken-1" text @click="download">Yes</v-btn>
					</v-card-actions>
				</v-card>
			</v-dialog>
			<v-dialog v-model="dialog2" max-width="550">
				<v-card>
					<v-card-title class="headline">OOPS...!!!</v-card-title>
					<v-card-text>{{errorText}}</v-card-text>
					<v-card-actions>
						<v-spacer></v-spacer>
						<v-btn color="green darken-1" text @click="dialog2 = false">OK</v-btn>
					</v-card-actions>
				</v-card>
			</v-dialog>
		</v-row>
	</div>
</template>

<script>
	import Query from "./Query.vue";
	import QueryMenu from "./QueryMenu.vue";
	import { eventBus } from "../../main";
	import XLSX from "xlsx";
	import flatten from "flat";

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
					{
						text: "GroupId",
						align: "center",
						value: "metadata.groupId",
						width: "10%"
					},
					{
						text: "ArtifactId",
						align: "center",
						value: "metadata.artifactId",
						width: "10%"
					},
					{
						text: "Source",
						align: "center",
						value: "metadata.source",
						width: "10%"
					},
					{
						text: "Version",
						align: "center",
						value: "metadata.version",
						width: "10%"
					},
					{
						text: "More Information",
						align: "center",
						value: "moreInfo",
						width: "10%"
					}
				],
				items: [],
				queryError: "",
				errorColumn: 0,
				progressBar: false,
				resultLimit: 100,
				flattenItems: [],
				dialog: false,
				dialog2:false,
				errorText:"",
				finalQAndLimit: {query:"", limit:100},
				reqBody: {
					query: this.readyToSearchQuery,
					limit: this.resultLimit
				},
				clearItems: false,
				storageSaveQuery: "",
			};
		},
		watch: {
			//if this variable is changed, the startSearch function will be triggered
			finalQAndLimit: function(newVal, oldVal) {
				if (newVal.query) {
					this.readyToSearchQuery = newVal.query;
					this.resultLimit = newVal.limit;
					if(!((oldVal.query == newVal.query) && (oldVal.limit == newVal.limit))){ //If both values stay the same, no search will be happen
						this.startSearch();
					}
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
						.post("search/", {query: this.readyToSearchQuery, limit: parseInt(this.resultLimit)})
						.then(response => {
							return response.json();
						})
						.then(
							data => {
								vm.items = data.messages;
								if (data.messages.length != 0) {
									if (data.messages.totalHits != 0) {
										vm.items = data.messages;
										vm.headers.splice(4, vm.headers.length - 5);
										var keys = Object.keys(vm.items.hits[0].metricResults);
										for (var j = 0; j < keys.length; j++) {
											var modifiedKey = keys[j]
											.substring(keys[j].lastIndexOf(".") + 1)
											.trim();
											for (var i = 0; i < vm.items.hits.length; i++) {
												var obj = vm.items.hits[i].metricResults;
												obj[modifiedKey] = obj[keys[j]];
												delete obj[keys[j]];
												vm.items.hits[i].metricResults = obj;
												vm.flattenItems[i] = flatten(vm.items.hits[i]);
											}
											vm.headers.splice(4, 0, {
												text: modifiedKey,
												align: "center",
												value: "metricResults." + modifiedKey,
												width: "10%"
											});
										}
									}
								}
								vm.progressBar = false;
								// vm.readyToSearchQuery = "";
								},
								error => {
									vm.items = [];
									vm.headers.splice(4, vm.headers.length - 4);
									vm.progressBar = false;
									vm.readyToSearchQuery = "";
									//vm.queryError = error.body;
									if (error.status == 500) {
										this.errorText= error.status + "  " + error.statusText+"!!! "+ " Something Went Wrong......Please Try Again";
										this.dialog2=true;
									} else if (error.status == 422) {
										vm.errorColumn = error.body.column;
										vm.queryError = "Syntax Error: " + error.body.problem;
									} else {
										vm.queryError = error.body;
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
					},
					download() {
						this.dialog = false;
						for (let i = 0; i < this.flattenItems.length; i++) {
							delete this.flattenItems[i].id;
							delete this.flattenItems[i]["metadata.discovered"];
						}
						const data = XLSX.utils.json_to_sheet(this.flattenItems);
						const wb = XLSX.utils.book_new();
						XLSX.utils.book_append_sheet(wb, data, "results");
						XLSX.writeFile(wb, "results.xlsx");
					}
				}
		};
</script>

<style>
	#searchPartDiv {
		background-color: rgb(255, 255, 255);
		margin-bottom: 10px;
		padding:0 30px 30px 30px;
	}
	.inputQueryInResult {
		text-align: center;
	}
	#searchedQueryInResult {
		font-weight: bold;
	}
	.downloadDiv {
		text-align: right;
	}
</style>