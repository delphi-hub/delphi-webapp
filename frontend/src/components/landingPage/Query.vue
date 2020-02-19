<template>
	<v-row>
		<v-col cols="11" id="queryCol" class="pr-0">
	<!--
		@input="addToFinalQuery, setQuery($event.target.value)"
		@keydown.enter.prevent="onStartSearch"
	-->
			<v-textarea
				outlined
				id="queryInput"
				rows="1"
				@keydown.enter.prevent
				clearable
				v-model="finalQuery"
				label="Your Query:"
				@input="addToFinalQuery($event), setQuery($event)"
				auto-grow>
			</v-textarea>
			<v-alert
				class="my-0"
				dense
				outlined
				type="error"
				v-if="!$v.finalQuery.required && submitted">
						Please enter a valid query or use the query builder to add a query.
			</v-alert>
			<v-alert
				class="my-0"
				dense
				outlined
				type="error"
				v-if="$v.finalQuery.required && !$v.finalQuery.metricValidator && !$v.finalQuery.queryErrorValidator && submitted">
						{{this.queryError}}
			</v-alert>
		</v-col>
		<v-col cols="1" class="pl-0">
			<v-btn
				height="56px"
				id="startSearchButton"
				@click="onStartSearch"
				:loading="isLoading"
				:disabled="isLoading"
				color="red"
				class="ml-2 white--text">
				<v-icon dark large>mdi-magnify</v-icon>
			</v-btn>
		</v-col>
	</v-row>
</template>

<script>
	import { required } from "vuelidate/lib/validators";
	import { eventBus } from "../../main";

	const queryErrorValidator = (value, vm) => {
		if (vm.queryError != "") {
			return false;
		} else {
			return true;
		}
	};
	export default {
		props: {
			partQuery: {
			//query from SearchPart component
				type: String
			},
			errMsg: {
				type: String
			},
			isLoading: {
				type: Boolean
			}
		},
		data() {
			return {
				finalQuery: "",
				submitted: false,
				queryError: "",
				emptyQuery: "",
				metric: "",
				metrics: [],
				loader: null,
			};
		},
		validations: {
			finalQuery: {
				required,
				queryErrorValidator
			}
		},
		watch: {
			//whenever a new query is comming from the queryMenu, it will be added to the finalQuery
			partQuery: function(newVal) {
				if (newVal) {
					if(this.finalQuery == null){
						this.finalQuery = "";
					}
					this.finalQuery = newVal;
					this.$emit("resetSavedQuery", ""); //without this line, if the user would choose twice the same query, nothing would happen
				}
			},
			errMsg: function(newVal) {
				if (newVal) {
					this.queryError = newVal;
				}
			},
			loader () {
				const l = this.loader
				this[l] = !this[l]
				if(!this.isLoading){
					this.loader = null
				}       
			},
		},
		created() {
			eventBus.$on("metricList", data => {
				// this.metrics = data;
				for (let i = 0; i < data.length; i++) {
					this.metrics.push(data[i].name);
				}
			});
		},
		methods: {
			//the value of the current final query becomes the events value.
			//This function i needed if the user types in the textfield.
			addToFinalQuery(event) {
				this.finalQuery = event;
			},
			//if a valid query is given, then this function sends the final query to the searchPart component to initiate a search
			onStartSearch() {
				this.loader = this.isLoading;
				if (this.finalQuery) {
					this.submitted = true;
					this.emptyQuery = false;
					this.$emit("emptyQuery", this.emptyQuery);
					this.$emit("finalQuerySend", this.finalQuery);
				} else {
					this.emptyQuery = true;
					this.$emit("emptyQuery", this.emptyQuery);
					this.submitted = true;
				}
			},
			//after a reset this sends a confirmation to searchPart
			finalMetricIsReseted() {
				this.$emit("confirmFinalQueryReset", false);
			},
			setQuery(value) {
				this.submitted = false;
				this.queryError = "";
				this.$v.finalQuery.$touch();
				this.finalQuery = value;
			}
		}
	};
</script>

<style>
	#errorDiv {
		min-height: 30px;
	}
</style>