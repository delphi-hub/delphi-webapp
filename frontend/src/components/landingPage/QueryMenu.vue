<template>
	<div>
		<v-btn
			tile
			outlined
			color="red"
			class="mb-1"
			text
			@click="expanded = !expanded">
			<v-icon left dark>mdi-creation</v-icon>
				<p style="margin: 0" v-if="expanded">Hide Query Creation Menu</p>
				<p style="margin: 0" v-else>Use Query Creation Menu</p>
				
		</v-btn>
		<v-expand-transition>
			<div v-show="expanded" style="box-shadow: 1px 1px 5px 3px grey;	border-radius: 3px; padding: 0 5px 5px 5px;">
				<v-row>
					<v-col cols="12">
						<v-card min-height="60px">
							<v-card-subtitle class="px-2 pt-1 pb-0">Query Creation Progress:</v-card-subtitle>
								<p class="px-2 py-1 font-weight-bold">
									{{queryInCreation[0]}} {{queryInCreation[1]}} {{queryInCreation[2]}} {{queryInCreation[3]}} {{queryInCreation[4]}} {{queryInCreation[5]}}
								</p>
						</v-card>
					</v-col>
				</v-row>	
				<v-stepper v-model="step">
					<v-stepper-header>
						<v-stepper-step :complete="step > 1" step="1">Metric</v-stepper-step>
						<v-divider></v-divider>
						<v-stepper-step :complete="step > 2" step="2">Operator & Value</v-stepper-step>
						<v-divider></v-divider>
						<v-stepper-step step="3">Wrap Up</v-stepper-step>						
					</v-stepper-header>
					<v-stepper-items>
						<v-stepper-content step="1">
							<v-card	min-height="350px" :elevation="0">
								<v-row>
									<v-col cols="12">
										<h5>Step 1: Metric</h5>
										<p class="text-justify">
											Morbi mattis ullamcorper velit. Donec orci lectus, aliquam ut, faucibus non, euismod id, nulla. Fusce convallis metus id felis luctus adipiscing. Aenean massa. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus.
											Nulla consequat massa quis enim. Praesent venenatis metus at tortor pulvinar varius. Donec venenatis vulputate lorem. Phasellus accumsan cursus velit. Pellentesque ut neque.
										</p>
									</v-col>
									<v-col cols="12">
										<div v-show="metric">
											<p>Chosen Metric:</p>
											<div id="chosenMetricDiv">
												<p id="chosenMetricP">[{{ metric }}]</p>
												<button id="removeMetricButton" class="btn btn-dark" @click="metric = null">Rechoose</button>
											</div>
										</div>
										<div v-show="!metric">
											<input
												type="text"
												id="filter"
												name="metric_suggest"
												v-on:keyup="filter1"
												size="15"
												placeholder="Filter Metric"/>
											<div v-if="info">
												<select id="select" size="10" v-model="metric">
													<option 
														data-toggle="tooltip"
														v-bind:title="data.description"
														id="optionSelect"
														v-for="data in info"
														v-bind:key="data.name">
															{{data.name}}
													</option>
												</select>
											</div>
										</div>
									</v-col>
								</v-row>	
							</v-card>
							<v-tooltip top color="red">
								<template v-slot:activator="{ on }">
									<v-btn 
										color="red" 
										v-on="on" 
										@click="undoInStepOne"
										:disabled="!queryInCreation[1]">
										<v-icon>mdi-undo</v-icon>
									</v-btn>
								</template>
								<span>Undo Last Step</span>
							</v-tooltip>
							<v-tooltip top color="green">
								<template v-slot:activator="{ on }">
									<v-btn
										v-on="on"
										style="margin-left: 6px;"
										:disabled="!metric"
										color="green"
										@click="nextStepOne">
										<v-icon>mdi-redo</v-icon>
									</v-btn>
								</template>
								<span>Next Step</span>
							</v-tooltip>
						</v-stepper-content>
						<v-stepper-content step="2">
							<v-card	min-height="350px" :elevation="0">
								<v-row>
									<v-col cols="12">
										<h5>Step 2: Operator & Value</h5>
										<p class="text-justify">
											Morbi mattis ullamcorper velit. Donec orci lectus, aliquam ut, faucibus non, euismod id, nulla. Fusce convallis metus id felis luctus adipiscing. Aenean massa. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus.
											Nulla consequat massa quis enim. Praesent venenatis metus at tortor pulvinar varius. Donec venenatis vulputate lorem. Phasellus accumsan cursus velit. Pellentesque ut neque.
										</p>
									</v-col>
									<v-col cols="12" class="py-0">
										<v-select
											v-model="operator"
											:items="operators"
											outlined
											label="Operator"
										></v-select>
									</v-col>
									<v-col cols="12" class="py-0">
										<v-text-field
											label="Value"
											outlined
											v-model="value"
										></v-text-field>
									</v-col>
								</v-row>	
							</v-card>
							<v-tooltip top color="red">
								<template v-slot:activator="{ on }">
									<v-btn color="red" v-on="on" @click="undoInStepTwo">
										<v-icon>mdi-undo</v-icon>
									</v-btn>
								</template>
								<span>Undo Last Step</span>
							</v-tooltip>
							<v-tooltip top color="green">
								<template v-slot:activator="{ on }">
									<v-btn
										style="margin-left: 6px;"
										v-on="on"
										color="green"
										:disabled="!value || !operator"
										@click="nextStepTwo">
										<v-icon>mdi-redo</v-icon>
									</v-btn>
								</template>
								<span>Next Step</span>
							</v-tooltip>						
						</v-stepper-content>
						<v-stepper-content step="3">
							<v-card	min-height="350px" :elevation="0">
								<v-row>
									<v-col cols="12">
										<h5>Step 3: Operator & Value</h5>
										<p class="text-justify">
											Morbi mattis ullamcorper velit. Donec orci lectus, aliquam ut, faucibus non, euismod id, nulla. Fusce convallis metus id felis luctus adipiscing. Aenean massa. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus.
											Nulla consequat massa quis enim. Praesent venenatis metus at tortor pulvinar varius. Donec venenatis vulputate lorem. Phasellus accumsan cursus velit. Pellentesque ut neque.
										</p>
									</v-col>
									<v-col cols="12">
										<v-checkbox color="green" class="pl-2" v-model="logicalNOT" label="Add Logical NOT"></v-checkbox>
									</v-col>
									<v-col cols="12">
										<v-select
											v-model="logicalOperator"
											:items="logicalOperators"
											outlined
											label="Logical Operator">
										</v-select>
									</v-col>
								</v-row>	
							</v-card>
							<v-tooltip top color="red">
								<template v-slot:activator="{ on }">
									<v-btn color="red" v-on="on" @click="undoInStepThree">
										<v-icon>mdi-undo</v-icon>
									</v-btn>
								</template>
								<span>Undo Last Step</span>
							</v-tooltip>
							<v-tooltip top color="green">
								<template v-slot:activator="{ on }">
									<v-btn
									v-on="on"
									style="margin-left: 6px;"
									color="green"
									:disabled="!logicalOperator"
									@click="nextStepThree"
									>
										<v-icon>mdi-redo</v-icon>
									</v-btn>
								</template>
								<span>Add a new subquery?</span>
							</v-tooltip>
							<v-tooltip top color="yellow">
								<template v-slot:activator="{ on }">
									<v-btn
										v-on="on"
										style="margin-left: 6px;"
										color="yellow"
										:disabled="logicalOperator != ''"
										@click="onAddQuery"
									>
										<v-icon>mdi-redo</v-icon>
									</v-btn>
								</template>
								<span>Finish</span>
							</v-tooltip>
						</v-stepper-content>
					</v-stepper-items>
				</v-stepper>
			</div>
		</v-expand-transition>
	</div>
</template>

<script>
	import { eventBus } from '../../main';
	export default {
		data () {
			return {
				operators: [
					">", ">=", "<", "<=", "="
				],
				logicalOperators: [
					{text:"No Logical Operator", value:""},
					{text:"And", value:"&&"},
					{text:"OR", value:"||"},
					{text:"XOR", value:"XOR"}
				],
				queryInCreation: [],
				createdQuery: '',
				info: null,
				expanded: false,
				step: 1,
				level: 0,
				operator: null,
				value: null,
				logicalNOT: null,
				logicalOperator: null,
				metric: null,
			}
		},
		methods: {
			//if all components from the menuStep compunents are given, this function creates a nice looking query. Afterwards the values will be resetted,
			//by setting the reset variables to true. That will trigger the functions in the menuStep components.
			onAddQuery() {
				if(this.metric && this.operator && this.value && (this.logicalOperator !== null)){
					var out = '';
					if(this.isNumeric(this.value)){
						out += '(' + '[' + this.metric + ']' + this.operator + this.value + ')';
					}
					else {
						out += '(' + '[' + this.metric + ']' + this.operator + '"' + this.value + '"' + ')';
					}
					if(this.logicalNOT) {																																					
						out = '!' + out;
					}
					if(this.logicalOperator){
						out += ' ' + this.logicalOperator + ' ';
					}
					this.$emit('addQuerySent', out);
					this.metric = null;
					this.operator = null;
					this.value = null;
					this.logicalNOT = null;
					this.logicalOperator = null;
					document.getElementById("filter").value = ''; //without these, after removing metric the 
					this.filter1();								  //list would still be filtered even though the text field is empty
					this.expanded = !this.expanded;
				}		
			},
			//needed to check if the value is a number
			isNumeric: function (n) {
				return !isNaN(parseInt(n)) && isFinite(n);
			},
			filter1() {
				var keyword = document.getElementById("filter").value;
				var select = document.getElementById("select");
				for (var i = 0; i < select.length; i++) {
					var txt = select.options[i].text;
					var txt2 = keyword.toLowerCase();
					if (txt.toLowerCase().includes(txt2)) {
						select.options[i].style.display = "list-item";
					} else {
						select.options[i].style.display = "none";
					}
				}
			},
			nextStepOne(){
				this.step = 2;
				this.queryInCreation.push(this.metric);
				
				
			},
			nextStepTwo(){
				this.step = 3;
				this.queryInCreation.push(this.operator);
				this.queryInCreation.push(this.value);
						
			},
			nextStepThree(){
				this.level = this.level+1;
				this.step = 1;
				this.queryInCreation.push(this.logicalOperator);
				this.queryInCreation.push(this.logicalNOT);
				document.getElementById("filter").value = '';
				this.filter1();
				this.metric = null;
				this.value = null;
				this.operator = null;	
				this.logicalOperator = null;
				this.logicalNOT = null;
			},
			undoInStepOne() {
				this.level = this.level-1;
				this.step = 3;
				document.getElementById("filter").value = '';
				this.filter1();
				this.metric = this.queryInCreation[(this.level * 5)];
				this.operator = this.queryInCreation[1 + (this.level * 5)];
				this.value = this.queryInCreation[2 + (this.level * 5)];	
				this.logicalOperator = this.queryInCreation[3 + (this.level * 5)];
				this.logicalNOT = this.queryInCreation[4 + (this.level * 5)];
				this.queryInCreation.pop();
				this.queryInCreation.pop();
			},
			undoInStepTwo() {
				this.step = 1;
				this.queryInCreation.pop();			
			},
			undoInStepThree() {
				this.step = 2;
				this.queryInCreation.pop();
				this.queryInCreation.pop();
			},
		},
		mounted() {
			this.$http.get('https://delphi.cs.uni-paderborn.de/api/features').then(response => {
				// this.$http.get("features").then(response => {
				this.info = response.data.sort((a, b) => (a.name > b.name) ? 1 : -1);
				eventBus.$emit('metricList', this.info);
				return response.json();
			});
			error => {
				alert("Invalid results!", error.messages);
			};
		}
	}
</script>

<style>
	#chosenMetricDiv {
		text-align: center;
		border-radius: 5px;
		padding: 5px;
		background-color: rgba(138, 180, 228, 0.753);
	}
	#chosenMetricP {
		margin: 0px 0px 5px 0px;
	}
	#removeMetricButton {
		background-color: rgb(192, 197, 192);
		font-variant: small-caps;
		color: black;
		width: 100%;
	}
	#removeMetricButton:hover {
		box-shadow: 1px 1px 5px 3px rgb(190, 32, 32);
		color: red;
		border-radius: 3px;
	}
	#filter {
		width: 100%;
		border-style: solid;
		border-color: grey;
		border-width: 0.2em 0.2em 0 0.2em;
		border-radius: 8px 8px 0 0;
	}
	#select {
		width: 100%;
		height: 125px;
		overflow: auto;
		border-style: solid;
		border-color: grey;
		border-width: 0.1em 0.2em 0.2em 0.2em;
		border-radius: 0 0 8px 8px;
	}
	#optionSelect:hover {
		background-color: rgba(138, 180, 228, 0.753);
	}
	label {
		margin: 0 !important;
	}
</style>