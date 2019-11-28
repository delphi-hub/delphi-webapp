<template>
    <div class="col-6" id="menuCol">
        <div class="row">
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
		<hr>
		<button class="btn btn-dark" id="addQueryButton" 
			@click="onAddQuery" 
			:style= "[(operator && metric && value && logicalOperator !== null) ? {'background-color': 'green'} : {'background-color':null}]">
			<h5 id="addQueryButtonText">Add to Query</h5>
		</button>		
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
				errors: [],
				metricToReset: false,
				operatorAndValueToReset: false,
				lNotAndLOperatorToReset: false
            }
        },
		methods: {
			onAddQuery() {
				if(this.metric && this.operator && this.value && (this.logicalOperator !== null)){
					if(this.errors.length){
						this.errors = [];
					}

					var out = '';		
					if(this.logicalNOT) {																																					
						out += 'NOT ' + '(' + '[' + this.metric + '] ' + this.operator + ' ' + this.value.replace(/[\s\/]/g, '') + ')'; // + '\r\n'
					}
					else {
						out += '(' + '[' + this.metric + '] ' + this.operator + ' ' + this.value.replace(/[\s\/]/g, '') + ')';
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
				else {
					if(!this.metric) {
						if(!this.errors.includes("Metric required.")){
							this.errors.push("Metric required.");
						}
					}
					else {
							this.removeElement(this.errors, "Metric required.");
					}
					if(!this.operator) {
						if(!this.errors.includes("Operator required.")){
							this.errors.push("Operator required.");
						}						
					}
					else {
							this.removeElement(this.errors, "Operator required.");
					}
					if(!this.value) {
						if(!this.errors.includes("Value required.")){
							this.errors.push("Value required.");
						}					
					}
					else {
							this.removeElement(this.errors, "Value required.");	
					}
				}			
			},
			removeElement(array, elem) {
   				var index = array.indexOf(elem);
    			if (index > -1) {
        			array.splice(index, 1);
    			}
			}
		}
	}
</script>

<style>
    #menuCol {
		background-color:rgb(235, 235, 235); 
		padding-top: 10px; 
		border-radius: 0 10px 10px 0;
		
	}
	#addQueryButton {
		width: 25%; 
		background-color: rgb(97, 97, 97);
		float:left;
		margin-bottom: 15px;
		height: 40px;
	}

	#addQueryButtonText {
		font-variant: small-caps;
	}

	#addQueryButton:hover{
    	background-color: rgb(97, 97, 97);
    	box-shadow: 1px 1px 5px 3px grey;
    	border-radius: 3px;
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