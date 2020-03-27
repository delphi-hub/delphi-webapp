<template>
	<v-row>
		<v-col cols="12" class="py-0 pl-8">
			<v-btn
			class="mb-1"
			outlined
			rounded
			color="#0959db"
			@click="expanded = !expanded">
			<v-icon left>mdi-creation</v-icon>
				<p style="margin: 0">Query Creation</p>
				<v-icon v-if="expanded" right>mdi-arrow-up-bold</v-icon>
				<v-icon v-else right>mdi-arrow-down-bold</v-icon>
		</v-btn>
			<v-menu
				v-model="menu"
				:close-on-content-click="false"
				min-width="410"
				max-height="300"
				offset-y>
				<template v-slot:activator="{ on }">
					<v-btn
						class="mx-1 mb-1"
						rounded
						outlined
						color="#299e3c"
						v-on="on">
						<v-icon left>mdi-database</v-icon>
						Query Storage		
					</v-btn>
				</template>	
					<v-list v-if="menuItems.length != 0" color="#299e3c" class="py-1">
							<v-list-item
							class="mx-0"
							v-for="(item, index) in menuItems"
							:key="index">
								<v-row style="background-color:white;">
									<v-list-item-content>
										<v-tooltip bottom color="#299e3c">
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
												@click.stop="showDialogFromStorage(item.value)">
												<v-icon>mdi-content-copy</v-icon>
											</v-btn>
										</template>
										<span>Copy Query to 'Your Query'</span>
									</v-tooltip>
									<v-tooltip top color="red">
										<template v-slot:activator="{ on }">
											<v-btn
												class="mt-1 red--text"
												icon
												v-on="on"
												@click="DeletItemFromStorage(index)">
												<v-icon>mdi-delete</v-icon>
											</v-btn>
										</template>
										<span>Remove Query from Storage</span>
									</v-tooltip>
								</v-row>
							</v-list-item>
					</v-list>
					<v-list v-else color="#299e3c" class="py-1">
						<v-list-item class="mx-0">
							<v-row style="background-color:white;">
								<v-list-item-content>
									<v-list-item-title style="text-align: center;">Query Storage Empty</v-list-item-title>
								</v-list-item-content>
							</v-row>
						</v-list-item>
					</v-list>
			</v-menu>
		</v-col>
		<v-col cols="12" class="py-0"> 
			<v-expand-transition>
				<div v-show="expanded" style="border-radius: 5px; padding: 0 2px 2px 2px; background-color: #0959db">
					<v-row class="pa-0">
						<v-col cols="12" style="padding: 2px 15px 2px 15px;">
							<v-card>
								<v-card-title
									class="px-2 py-0">
									<span style="margin-right:5px">Query Creation Progress:</span><span style="color:rgb(122, 122, 122)">{{createdQuery}}</span>
								</v-card-title>
							</v-card>
						</v-col>
					</v-row>	
					<v-stepper v-model="step">
						<v-stepper-header style="height: 45px;">
							<v-stepper-step :complete="step > 1" step="1" class="pa-2">Metric</v-stepper-step>
							<v-divider class="mx-2"></v-divider>
							<v-stepper-step :complete="step > 2" step="2" class="pa-2">Operator & Value</v-stepper-step>
							<v-divider class="mx-2"></v-divider>
							<v-stepper-step step="3" class="pa-2">Logical Operator</v-stepper-step>						
						</v-stepper-header> 
						<v-stepper-items>
							<v-stepper-content class="pa-2" step="1">
								<v-card elevation="0" style="min-height:220px">
									<v-card-title class="pa-1">
										Step 1 (Expression {{level+1}}): Metric
										<span>
											<v-tooltip bottom color="#ffae00">
												<template v-slot:activator="{ on }">
													<v-icon height="25"
														width="25"
														v-on="on"
														class="ml-1 mb-1"
														color="#ffae00"
														medium 
														>mdi-information	
													</v-icon>
												</template>
												<span style="color:black">A Delphi query consists of one or more expressions which are connected with logical operators. 
													An expression has the syntax: '[metric] operator value'. The following steps guide you through the query creation process.
													First pick a metric, depending on your desired result of artifacts.
												</span>
											</v-tooltip>
										</span>
									</v-card-title>
									<v-row>
										<v-col cols="12">
											<v-card v-show="metric" :elevation="0">
												<v-card-subtitle class="py-0 px-1">Chosen Metric:</v-card-subtitle>
												<div id="chosenMetricDiv">
													<p id="chosenMetricP">[{{ metric }}]</p>
													<v-btn @click="metric = null">Change Metric</v-btn>
												</div>
											</v-card>
											<div v-show="!metric">
												<input
													type="text"
													id="filter"
													name="metric_suggest"
													v-on:keyup="filter1"
													size="15"
													placeholder="Serach Metrics"/>
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
											:disabled="!queryInCreation[1] || step != 1">
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
											:disabled="!metric || step != 1"
											color="green"
											@click="nextStepOne">
											<v-icon>mdi-redo</v-icon>
										</v-btn>
									</template>
									<span>Next Step</span>
								</v-tooltip>
							</v-stepper-content>
							<v-stepper-content class="pa-2" step="2">
								<v-card :elevation="0" style="min-height:220px">
									<v-card-title class="pa-1">
										Step 2 (Expression {{level+1}}): Operator & Value
										<span>
											<v-tooltip bottom color="#ffae00">
												<template v-slot:activator="{ on }">
													<v-icon height="25"
														width="25"
														v-on="on"
														class="ml-1 mb-1"
														color="#ffae00"
														medium 
														>mdi-information	
													</v-icon>
												</template>
												<span style="color:black">Next you have to pick an operator and give a value.
													That will bound the metric and only artifacts within that bound will be part of the result.
												</span>
											</v-tooltip>
										</span>
									</v-card-title>
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
										<v-btn color="red" v-on="on" @click="undoInStepTwo" :disabled="step != 2">
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
											:disabled="!value || !operator || step != 2"
											@click="nextStepTwo">
											<v-icon>mdi-redo</v-icon>
										</v-btn>
									</template>
									<span>Next Step</span>
								</v-tooltip>						
							</v-stepper-content>
							<v-stepper-content class="pa-2" step="3">
								<v-card :elevation="0" style="min-height:220px">
									<v-card-title class="pa-1">
										Step 3 (Expression {{level+1}}): Logical Operator
										<span>
											<v-tooltip bottom color="#ffae00">
												<template v-slot:activator="{ on }">
													<v-icon height="25"
														width="25"
														v-on="on"
														class="ml-1 mb-1"
														color="#ffae00"
														medium 
														>mdi-information	
													</v-icon>
												</template>
												<span style="color:black">
													This step will wrap up your expression and possibly your query. 
													First decide if you would like to negate your expression.
													Afterwards you have two options. Either pick a logical operator, that will initiate the creation 
													of a new expression (Maximum of 10 expression).
													Or pick 'No Logical Operator' to finish this query and either 
													send it to 'Your Query' or save it in the query storage.
												</span>
											</v-tooltip>
										</span>
									</v-card-title>
									<v-row>
										<v-col cols="12" class="py-0">
											<v-checkbox color="blue" class="pl-2" v-model="logicalNOT" label="Add Logical NOT"></v-checkbox>
										</v-col>
										<v-col cols="12" class="py-0">
											<v-select
												v-model="logicalOperator"
												:items="logicalOperators"
												outlined
												v-show="!(level >= maxLevel)">
											</v-select>
											<v-alert
												outlined
												type="warning"
												prominent
												dense
												v-show="level >= maxLevel">
												Maximum of 10 expressions is reached.
											</v-alert>
										</v-col>
									</v-row>	
								</v-card>
								<v-tooltip top color="red">
									<template v-slot:activator="{ on }">
										<v-btn color="red" v-on="on" @click="undoInStepThree" :disabled="step != 3">
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
										:disabled="!logicalOperator || (level >= maxLevel) || step != 3"
										@click="nextStepThree">
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
											@click.stop="dialog = true">
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
										<v-card-actions class="pa-1">
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
												@click="addToMenu" :disabled="step != 3">
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
		</v-col>
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
						@click="copyFromStorage(tempQuery)">
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
	</v-row>
</template>

<script>
	import { eventBus } from '../../main';
	export default {
		props: {
			queryForStorage: {
				type: String
			},
			shrinkMenu: {
				type: Boolean
			}
		},
		watch: {
			queryForStorage: function(newVal) {
				if(newVal){
					if(!this.storageCheckDuplicates(newVal)){
						if(newVal.length > 35){
							this.menuItems.push({title: newVal.substring(0,35)+'...', value: newVal});
						}
						else{
							this.menuItems.push({title: newVal, value: newVal});
						}	
						this.snackbar = true;
						this.updateMenuInBrowserStorage();
					}
					else {
						this.snackbar2 = true;
					}
					this.$emit("resetStorageQuery", "");
				}
			},
			shrinkMenu: function(newVal) {
				if(newVal) {
					this.expanded = false;
					this.$emit("shrinkingMenu", false);
				}
			}
		},
		data () {
			return {
				operators: [
					">", ">=", "<", "<=", "="
				],
				logicalOperators: [
					{text:"No Logical Operator", value:""},
					{text:"And", value:"&&"},
					{text:"OR", value:"||"},
					{text:"Xor", value:"Xor"}
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
				tempQuery: "",
				expanded: false,
				step: 1,
				level: 0,
				maxLevel: 9,
				operator: null,
				value: null,
				logicalNOT: null,
				logicalOperator: "",
				metric: null,
			}
		},
		methods: {
			onAddQuery() {
				if(this.createdQuery){
					this.step = 1;
					this.queryInCreation.push(this.logicalOperator);
					this.queryInCreation.push(this.logicalNOT);
					this.updateCreatedQuery();
					this.$emit('addQuerySent', this.createdQuery);
					this.metric = null;
					this.operator = null;
					this.value = null;
					this.logicalNOT = null;
					this.logicalOperator = "";
					document.getElementById("filter").value = ''; 
					this.filter1();	
					this.expanded = !this.expanded;
					this.createdQuery = "";
					this.queryInCreation = [];
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
				this.step = 1;
				this.level = this.level+1;
				this.queryInCreation.push(this.logicalOperator);
				this.queryInCreation.push(this.logicalNOT);
				document.getElementById("filter").value = '';
				this.filter1();
				this.metric = null;
				this.value = null;
				this.operator = null;	
				this.logicalOperator = "";
				this.logicalNOT = null;
				if(this.level >= this.maxLevel){
					this.logicalOperator = "";
				}
				this.updateCreatedQuery();
			},
			undoInStepOne() {
				this.step = 3;
				this.level = this.level-1;
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
				this.step = 1;
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
					this.updateMenuInBrowserStorage();
				}
				else {
					this.snackbar2 = true;
				}
				this.metric = null;
				this.operator = null;
				this.value = null;
				this.logicalNOT = null;
				this.logicalOperator = "";
				document.getElementById("filter").value = ''; 
				this.filter1();	
				this.expanded = !this.expanded;
				this.createdQuery = "";
				this.queryInCreation = [];
				this.level = 0;
				if(this.dialog) {
					this.dialog = false;
				}
			},
			DeletItemFromStorage(index) {
				this.menuItems.splice(index, 1);
				this.updateMenuInBrowserStorage();
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
			},
			showDialogFromStorage(qu) {
				this.dialogFromStorage = true;
				this.tempQuery = qu;
			},
			updateMenuInBrowserStorage() {
				const parsed = JSON.stringify(this.menuItems);
				localStorage.setItem('menuItems', parsed);
			}
		},
		mounted() {
			this.$http.get("features").then(response => {
				this.info = response.data.sort((a, b) => (a.name > b.name) ? 1 : -1);
				eventBus.$emit('metricList', this.info);
				return response.json();
			});
			error => {
				alert("Invalid results!", error.messages);
			};
			if (localStorage.getItem('menuItems')) {
				try {
					this.menuItems = JSON.parse(localStorage.getItem('menuItems'));
				} catch(e) {
					localStorage.removeItem('menuItems');
				}
			}
		}
	}
</script>

<style>
	#chosenMetricDiv {
		text-align: center;
		border-radius: 5px;
		padding: 5px;
		background-color: #cdd8eb;
	}
	#chosenMetricP {
		margin: 0px 0px 0px 0px;
	}
	#filter {
		width: 100%;
		border-style: solid;
		border-color: grey;
		border-width: 0.2em 0.2em 0 0.2em;
		border-radius: 8px 8px 8px 8px;
	}
	#select {
		width: 100%;
		height: 125px;
		overflow: auto;
		border-style: solid;
		border-color: grey;
		border-width: 0.1em 0.2em 0.2em 0.2em;
		border-radius: 8px 8px 8px 8px;
	}
	#optionSelect:hover {
		background-color: #cdd8eb;
	}
	label {
		margin: 0 !important;
	}
</style>