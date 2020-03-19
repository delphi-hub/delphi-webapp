<template>
	<div class="col-7" id="menuCol">
		<div class="row">
			<div class="col-2" id="addQueryCol">
				<button id="addQueryButton" 
					class="btn btn-dark"
					@click="onAddQuery" 
					:disabled= "!(operator && metric && value && logicalOperator !== null)">
					<h5 id="addQueryButtonText">Add</h5>
				</button>	
			</div>
			<div class="col-10" id="menuStepsCol">
				<!--Here the queryMenu is getting the values from the menuStep components. And the confirmations of the resets.
					If a reset is needed then it will be sent here too.-->
				<div class="row" id="stepsGrid">
					<menuStepOne 
						@metricSent="metric = $event" 
						@confirmMetricReset="metricToReset = $event" 
						:metricShouldBeReseted="metricToReset">
					</menuStepOne>
					<menuStepTwo 
						@operatorSent="operator = $event" 
						@valueSent="value = $event" 
						@confirmOperatorAndValueReset="operatorAndValueToReset = $event" 
						:operatorAndValueShouldBeReseted="operatorAndValueToReset">
					</menuStepTwo>
					<menuStepThree 
						@logicalNotSent="logicalNOT = $event" 
						@logicalOperatorSent="logicalOperator = $event" 
						@confirmLNotAndLOperatorReset="lNotAndLOperatorToReset = $event" 
						:lNOTAndLOperatorShouldBeReseted="lNotAndLOperatorToReset">
					</menuStepThree>       
				</div>
			</div>			
		</div>	
	</div>
</template>

<script>
	import MenuStepOne from './MenuStepOne.vue';
	import MenuStepTwo from './MenuStepTwo.vue';
	import MenuStepThree from './MenuStepThree.vue';
	export default {
		components: {
			'menuStepOne': MenuStepOne,
			'menuStepTwo': MenuStepTwo,
			'menuStepThree': MenuStepThree
		},
		data () {
			return {
				metric: null,
				operator: null,
				value: null,
				logicalNOT: null,
				logicalOperator: null,
				metricToReset: false,
				operatorAndValueToReset: false,
				lNotAndLOperatorToReset: false
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
					this.metricToReset = true;
					this.operatorAndValueToReset = true;
					this.lNotAndLOperatorToReset = true;
				}		
			},
			//needed to check if the value is a number
			isNumeric: function (n) {
				return !isNaN(parseInt(n)) && isFinite(n);
			}
		}
	}
</script>

<style>
	#menuCol {
		background-color:rgb(235, 235, 235); 
		border-radius: 0 10px 10px 0;
		padding: 12px;
	}
	#stepsGrid {
		margin:0 10px 0 0;
		background-color: rgb(192, 192, 192);
		padding:10px;
		border-radius: 5px;
	}
	#addQueryCol {
		padding:0 5px 0 5px !important;
		text-align: center;
	}
	#menuStepsCol {
		padding:0 !important;
	}

	#addQueryButton {
		width: 90%; 
		text-align: center;
		padding: 0 !important;
		background-color: green;
	}
	#addQueryButton:disabled {
		background-color: grey;
	}
	#addQueryButton:hover:not([disabled]) {
		box-shadow: 1px 1px 5px 3px grey;
	}
	#addQueryButtonText {
		font-variant: small-caps;
		font-size: 1.5em;
	}
	#addQueryButtonText:before {
		content: '\25c4';
		padding-right: 0.3em;
	}
	#resetButton {
		width: 25%; 
		margin-bottom: 15px;
		background-color: rgb(97, 97, 97);
		float:left;
		margin-left: 15px;
		height: 40px;
	}
	#resetButtonText {
		font-variant: small-caps;
	}
	#resetButton:hover{
		background-color: rgb(97, 97, 97);
		box-shadow: 1px 1px 5px 3px grey;
		border-radius: 3px;
	}
</style>