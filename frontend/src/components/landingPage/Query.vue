<template>
	<v-row>
		<v-col cols="12" class="pb-1">
			<v-row>
				<v-tooltip top color="#5E35B1">
					<template v-slot:activator="{ on }">
						<v-btn
							height="25"
							width="25"
							fab
							v-on="on"
							@click="sendToStorage"
							color="#5E35B1"
							class="ml-4 white--text">
							<v-icon small>mdi-content-save</v-icon>
						</v-btn>
					</template>
					<span>Save Query in Query Storage</span>
				</v-tooltip>
				<v-textarea
					outlined
					id="queryInput"
					rows="1"
					@keydown.enter.prevent="onStartSearch"
					clearable
					hide-details
					v-model="finalQuery"
					label="Your Query"
					@input="addToFinalQuery($event), setQuery($event)"
					auto-grow>
				</v-textarea>
				<v-btn
					height="50"
					id="startSearchButton"
					@click="onStartSearch"
					:loading="isLoading"
					:disabled="isLoading || !finalQuery"
					color="green"
					class="mr-4 ml-1 mt-1 white--text">
					<v-icon large>mdi-magnify</v-icon>
				</v-btn>
			</v-row>	
		</v-col>
		<v-col cols="12" class="py-1">
			<v-alert
				class="my-0 mx-6 py-1"
				dense
				outlined
				type="error"
				v-if="!$v.finalQuery.required && submitted">
				Please enter a valid query or use the query creation menu to add a query.
			</v-alert>
			<v-alert
				class="my-0 mx-6 py-1"
				dense
				outlined
				type="error"
				v-if="$v.finalQuery.required && !$v.finalQuery.metricValidator && !$v.finalQuery.queryErrorValidator && submitted">
						{{this.queryError}}
			</v-alert>
		</v-col>
		<v-col cols="6" class="py-0 ml-7">
			<v-text-field
				style="max-width:160px"
				dense
				filled
				hint="Amount of Result Entries"
				v-model="currentLimit"
				:rules="[rules.inlimit]"
				persistent-hint>
			</v-text-field>
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
				currentLimit: 100,
				rules: {
					inlimit: value => {
						const pattern = /^([1-9][0-9]{0,3}|10000)$/;
						if(pattern.test(value)){
							this.brokeRule = false;
						}
						else {
							this.brokeRule = true;
						}
						return pattern.test(value) || 'Only Numbers between 1 and 10000'
					}
				},
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
					var qAndL = {query: this.finalQuery, limit: this.currentLimit};
					this.$emit("finalQueryAndLimitSend", qAndL);
				} else {
					this.emptyQuery = true;
					this.$emit("emptyQuery", this.emptyQuery);
					this.submitted = true;
				}
			},
			sendToStorage() {
				this.$emit("sendQueryToStorage", this.finalQuery);
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

</style>