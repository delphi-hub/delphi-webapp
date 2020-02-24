<template>
	<v-row>
		<v-btn
			class="ml-6 mb-1 white--text"
			rounded
			color="rgb(190, 33, 33)"
			@click="expanded = !expanded">
			<v-icon left>mdi-creation</v-icon>
				<p style="margin: 0" v-if="expanded">Hide Query Creation Menu</p>
				<p style="margin: 0" v-else>Use Query Creation Menu</p>
		</v-btn>
		<div class="text-center">
			<v-menu
				v-model="menu"
				:close-on-content-click="false"
				min-width="300"
				max-height="300"
				offset-y>
				<template v-slot:activator="{ on }">
					<v-btn
						class="mx-1 mb-1 white--text"
						rounded
						color="#5E35B1"
						v-on="on">
						<v-icon left>mdi-database</v-icon>
						Storage		
					</v-btn>
				</template>	
					<v-list v-if="menuItems.length != 0" color="#5E35B1" class="py-1">
							<v-list-item
							class="mx-1"
							v-for="(item, index) in menuItems"
							:key="index">
								<v-row style="background-color:white; border-radius: 5px 5px 5px 5px;">
									<v-list-item-content>
										<v-tooltip bottom color="#5E35B1">
											<template v-slot:activator="{ on }">
												<v-list-item-title class="ml-1" v-on="on">{{ item.title }}</v-list-item-title>
											</template>
											<span>{{ item.value }}</span>
										</v-tooltip>
										
									</v-list-item-content>
									<v-tooltip top color="green">
										<template v-slot:activator="{ on }">
											<v-btn
												class="mt-1 green--text"
												icon
												v-on="on"
												@click.stop="dialogFromStorage = true"
												>
												<v-icon>mdi-content-copy</v-icon>
											</v-btn>
										</template>
										<span>Copy Query to 'Your Query'</span>
									</v-tooltip>
									<v-dialog
										v-model="dialogFromStorage"
										max-width="400">
										<v-card>
											<v-card-title
												class="headline red lighten-2 py-1"
												primary-title>
												Warning
											</v-card-title>
											<v-card-text class="mt-2 py-0">
												The created query will replace the content of 'Your Query'. 
											</v-card-text>
											<v-card-actions>
												<v-spacer></v-spacer>
												<v-btn
													color="green darken-1"
													text
													@click="copyFromStorage(item.value)">
													Ok
												</v-btn>
												<v-btn
													color="red darken-1"
													text
													@click="dialogFromStorage = false">
													Back
												</v-btn>
											</v-card-actions>
										</v-card>
									</v-dialog>
									<v-tooltip top color="red">
										<template v-slot:activator="{ on }">
											<v-btn
												class="mt-1 red--text"
												icon
												v-on="on"
												@click="menuItems.splice(index, 1)">
												<v-icon>mdi-delete</v-icon>
											</v-btn>
										</template>
										<span>Remove Query from Storage</span>
									</v-tooltip>
								</v-row>
							</v-list-item>
					</v-list>
					<v-list v-else color="#5E35B1" class="py-1">
						<v-list-item>
							<v-row style="background-color:white; border-radius: 5px 5px 5px 5px;">
								<v-list-item-title class="text-center title">Query Storage Empty</v-list-item-title>
							</v-row>
						</v-list-item>
					</v-list>
			</v-menu>
		</div>
		<v-expand-transition>
			<div v-show="expanded" style="border-radius: 15px; padding: 0 10px 10px 10px; margin: 0 20px 5px 20px; background-color: rgb(190, 33, 33)">
				<v-row>
					<v-col cols="12" class="px-4 py-3">
						<v-card>
							<v-card-subtitle class="py-0 pl-1">
								Query Creation Progress:
							</v-card-subtitle>
							<v-textarea
								rows="1"
								hide-details
								@input="addToFinalQuery($event), setQuery($event)"
								auto-grow
								v-model="createdQuery"
								readonly
								class="px-2 py-0 title">
							</v-textarea>
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
						<v-stepper-content class="pa-2" step="1">
							<v-card	min-height="350px" elevation="0">
								<v-card-title class="pa-1">
									Step 1 (Expression {{level+1}}) : Metric
								</v-card-title>
								<v-card-text class="pa-1">
									Morbi mattis ullamcorper velit. Donec orci lectus, aliquam ut, faucibus non, euismod id, nulla. Fusce convallis metus id felis luctus adipiscing. Aenean massa. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus.
											Nulla consequat massa quis enim. Praesent venenatis metus at tortor pulvinar varius. Donec venenatis vulputate lorem. Ph
								</v-card-text>
								<v-row>
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
						<v-stepper-content class="pa-2" step="2">
							<v-card	min-height="350px" :elevation="0">
								<v-card-title class="pa-1">
									Step 2 (Expression {{level+1}}): Operator & Value
								</v-card-title>
								<v-card-text class="pa-1">
									Morbi mattis ullamcorper velit. Donec orci lectus, aliquam ut, faucibus non, euismod id, nulla. Fusce convallis metus id felis luctus adipiscing. Aenean massa. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus.
											Nulla consequat massa quis enim. Praesent venenatis metus at tortor pulvinar varius. Donec venenatis vulputate lorem. Ph
								</v-card-text>
								<v-row>
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
						<v-stepper-content class="pa-2" step="3">
							<v-card	min-height="350px" :elevation="0">
								<v-card-title class="pa-1">
									Step 3 (Expression {{level+1}}): Wrap Up
								</v-card-title>
								<v-card-text class="pa-1">
									Morbi mattis ullamcorper velit. Donec orci lectus, aliquam ut, faucibus non, euismod id, nulla. Fusce convallis metus id felis luctus adipiscing. Aenean massa. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus.
											Nulla consequat massa quis enim. Praesent venenatis metus at tortor pulvinar varius. Donec venenatis vulputate lorem. Ph
								</v-card-text>
								<v-row>
									<v-col cols="12">
										<v-checkbox color="blue" class="pl-2" v-model="logicalNOT" label="Add Logical NOT"></v-checkbox>
									</v-col>
									<v-col cols="12">
										<v-select
											v-model="logicalOperator"
											:items="logicalOperators"
											outlined
											v-show="!(level >= maxLevel)"
											label="Logical Operator">
										</v-select>
										<v-alert
											outlined
											type="warning"
											prominent
											dense
											v-show="level >= maxLevel"
											>
											Maximum of 10 expressions is reached
										</v-alert>
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
									:disabled="!logicalOperator || (level >= maxLevel)"
									@click="nextStepThree"
									>
										<v-icon>mdi-shape-square-plus</v-icon>
									</v-btn>
								</template>
								<span>Add new expression</span>
							</v-tooltip>
							<v-tooltip top color="blue">
								<template v-slot:activator="{ on }">
									<v-btn
										v-on="on"
										style="margin-left: 6px;"
										color="blue"
										:disabled="(logicalOperator != '') && !(level >= maxLevel)"
										@click.stop="dialog = true"
									>
										<v-icon>mdi-file-send-outline</v-icon>
									</v-btn>
								</template>
								<span>Send Query</span>
							</v-tooltip>
							<v-dialog
								v-model="dialog"
								max-width="400">
								<v-card>
									<v-card-title
										class="headline red lighten-2 py-1"
										primary-title>
										Warning
									</v-card-title>
									<v-card-text class="mt-2 py-0">
										The created query will replace the content of 'Your Query'. 
									</v-card-text>
									<v-card-actions>
										<v-spacer></v-spacer>
										<v-btn
											color="green darken-1"
											text
											@click="onAddQuery">
											Ok
										</v-btn>
										<v-btn
											color="red darken-1"
											text
											@click="dialog = false">
											Back
										</v-btn>
										<v-btn
											color="blue darken-1"
											text
											@click="addToMenu">
											Save in Query Storage
										</v-btn>
									</v-card-actions>
								</v-card>
							</v-dialog>
						</v-stepper-content>
					</v-stepper-items>
				</v-stepper>
			</div>
		</v-expand-transition>
		<v-snackbar
			class="mb-3"
			v-model="snackbar"
			:timeout=3000>
			Query saved in Storage
			<v-btn
				color="red"
				text
				@click="snackbar = false">
				Close
			</v-btn>
		</v-snackbar>
		<v-snackbar
			class="mb-3"
			v-model="snackbar2"
			:timeout=3000>
			Query already in Storage
			<v-btn
				color="red"
				text
				@click="snackbar2 = false">
				Close
			</v-btn>
		</v-snackbar>
	</v-row>
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
				menuItems: [],
				snackbar: false,
				snackbar2: false,
				menu: false,
				createdQuery: '',
				info: null,
				dialog: false,
				dialogFromStorage: false,
				expanded: false,
				step: 1,
				level: 0,
				maxLevel: 9,
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
				if(this.createdQuery){
					this.queryInCreation.push(this.logicalOperator);
					this.queryInCreation.push(this.logicalNOT);
					this.updateCreatedQuery();
					this.$emit('addQuerySent', this.createdQuery);
					this.metric = null;
					this.operator = null;
					this.value = null;
					this.logicalNOT = null;
					this.logicalOperator = null;
					document.getElementById("filter").value = ''; 
					this.filter1();	
					this.expanded = !this.expanded;
					this.createdQuery = "";
					this.queryInCreation = [];
					this.step = 1;
					this.level = 0;
					if(this.dialog) {
						this.dialog = false;
					}
				}		
			},
			updateCreatedQuery() {
				if(this.queryInCreation) {
					this.createdQuery = "";
					var i;
					var restQueryAmount = this.queryInCreation.length % 5;
					var wholeQueriesAmount = this.queryInCreation.length - restQueryAmount;	
					//first append all whole subqueries. Afterwards in the switch statement the rest will be appended				
					for (i = 0; i < wholeQueriesAmount; i+=5) {						
						if(this.queryInCreation[i+4]) {																																					
							if(this.isNumeric(this.queryInCreation[i+2])){
								this.createdQuery += '!([' + this.queryInCreation[i] + ']' + this.queryInCreation[i+1] + this.queryInCreation[i+2] + ')';
							}
							else {
								this.createdQuery += '!([' + this.queryInCreation[i] + ']' + this.queryInCreation[i+1] + '"' + this.queryInCreation[i+2] + '"' + ')';
							}
						}
						else {
							if(this.isNumeric(this.queryInCreation[i+2])){
								this.createdQuery += '([' + this.queryInCreation[i] + ']' + this.queryInCreation[i+1] + this.queryInCreation[i+2] + ')';
							}
							else {
								this.createdQuery += '([' + this.queryInCreation[i] + ']' + this.queryInCreation[i+1] + '"' + this.queryInCreation[i+2] + '"' + ')';
							}
						}
						if(this.queryInCreation[i+3]){
							this.createdQuery += ' ' + this.queryInCreation[i+3] + ' ';
						}
					}
					switch(restQueryAmount){
						case 1:
							this.createdQuery += '([' + this.queryInCreation[wholeQueriesAmount] + '])'; 
							break;
						case 3:
							if(this.isNumeric(this.queryInCreation[wholeQueriesAmount+2])){
								this.createdQuery += '([' + this.queryInCreation[wholeQueriesAmount] + ']' + this.queryInCreation[wholeQueriesAmount+1] + this.queryInCreation[wholeQueriesAmount+2] + ')';
							}
							else {
								this.createdQuery += '([' + this.queryInCreation[wholeQueriesAmount] + ']' + this.queryInCreation[wholeQueriesAmount+1] + '"' + this.queryInCreation[wholeQueriesAmount+2] + '"' + ')';
							}
							break;
						case 5:
							
						if(this.queryInCreation[wholeQueriesAmount+4]) {																																					
							if(this.isNumeric(this.queryInCreation[wholeQueriesAmount+2])){
								this.createdQuery += '!([' + this.queryInCreation[wholeQueriesAmount] + ']' + this.queryInCreation[wholeQueriesAmount+1] + this.queryInCreation[wholeQueriesAmount+2] + ')';
							}
							else {
								this.createdQuery += '!([' + this.queryInCreation[wholeQueriesAmount] + ']' + this.queryInCreation[wholeQueriesAmount+1] + '"' + this.queryInCreation[wholeQueriesAmount+2] + '"' + ')';
							}
						}
						else {
							if(this.isNumeric(this.queryInCreation[wholeQueriesAmount+2])){
								this.createdQuery += '([' + this.queryInCreation[wholeQueriesAmount] + ']' + this.queryInCreation[wholeQueriesAmount+1] + this.queryInCreation[wholeQueriesAmount+2] + ')';
							}
							else {
								this.createdQuery += '([' + this.queryInCreation[wholeQueriesAmount] + ']' + this.queryInCreation[wholeQueriesAmount+1] + '"' + this.queryInCreation[wholeQueriesAmount+2] + '"' + ')';
							}
						}
						if(this.queryInCreation[wholeQueriesAmount+3]){
							this.createdQuery += ' ' + this.queryInCreation[wholeQueriesAmount+3] + ' ';
						}
							break;
					}
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
				this.updateCreatedQuery();
			},
			nextStepTwo(){
				this.step = 3;
				this.queryInCreation.push(this.operator);
				this.queryInCreation.push(this.value);	
				this.updateCreatedQuery();					
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
				if(this.level >= this.maxLevel){
					this.logicalOperator = "";
				}
				this.updateCreatedQuery();
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
				this.updateCreatedQuery();
			},
			undoInStepTwo() {
				this.step = 1;
				this.queryInCreation.pop();	
				this.updateCreatedQuery();		
			},
			undoInStepThree() {
				this.step = 2;
				this.queryInCreation.pop();
				this.queryInCreation.pop();
				this.updateCreatedQuery();
			},
			addToMenu(){
				this.queryInCreation.push(this.logicalOperator);
				this.queryInCreation.push(this.logicalNOT);
				this.updateCreatedQuery();
				if(!this.storageCheckDuplicates(this.createdQuery)){
					if(this.createdQuery.length > 35){
						this.menuItems.push({title: this.createdQuery.substring(0,35)+'...', value: this.createdQuery});
					}
					else{
						this.menuItems.push({title: this.createdQuery, value: this.createdQuery});
					}	
					this.snackbar = true;
				}
				else {
					this.snackbar2 = true;
				}
				this.metric = null;
				this.operator = null;
				this.value = null;
				this.logicalNOT = null;
				this.logicalOperator = null;
				document.getElementById("filter").value = ''; 
				this.filter1();	
				this.expanded = !this.expanded;
				this.createdQuery = "";
				this.queryInCreation = [];
				this.step = 1;
				this.level = 0;
				if(this.dialog) {
					this.dialog = false;
				}
				
			},
			copyFromStorage(query){
				this.$emit('addFromStorage', query);
				if(this.dialogFromStorage) {
					this.dialogFromStorage = false;
				}
			},
			storageCheckDuplicates(q){
				for(var i = 0; i < this.menuItems.length; i++) {
					if (this.menuItems[i].value == q) {
						return true;
					}
				}
				return false;
			}
		},
		mounted() {
			this.$http.get('https://delphi.cs.uni-paderborn.de/api/features').then(response => {
			//this.$http.get("features").then(response => {
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