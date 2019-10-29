<template>
    <div class="col-6" id="menuCol">
        <div class="row">
            <menuStepOne @metricSent="metric = $event" @confirmMetricReset="metricToReset = $event" :metricShouldBeReseted="metricToReset"></menuStepOne>
            <menuStepTwo @operatorSent="operator = $event" @valueSent="value = $event" @confirmOperatorAndValueReset="operatorAndValueToReset = $event" :operatorAndValueShouldBeReseted="operatorAndValueToReset"></menuStepTwo>
            <menuStepThree @logicalNotSent="logicalNOT = $event" @logicalOperatorSent="logicalOperator = $event" @confirmLNotAndLOperatorReset="lNotAndLOperatorToReset = $event" :lNOTAndLOperatorShouldBeReseted="lNotAndLOperatorToReset"></menuStepThree>       
		</div>
		<button class="btn btn-dark" id="addQueryButton" @click="onAddQuery">Add to Query</button>
		<p v-if="errors.length" style="background-color: red; color:antiquewhite" v-for="error in errors">
			{{ error }}
		</p>
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
				queries: [],
				metricToReset: false,
				operatorAndValueToReset: false,
				lNotAndLOperatorToReset: false
			//TODO	isSearchable: false
            }
        },
		methods: {
			onAddQuery() {
				if(this.metric && this.operator && this.value){
					if(this.errors.length){
						this.errors = [];
					}
					let queryCurrent = {
						metric: this.metric,
						operator: this.operator,
						value: this.value,
						logicalNOT: this.logicalNOT,
						logicalOperator: this.logicalOperator
					}
					this.queries.push(queryCurrent);

					var out = '';

					var i;
					for (i = 0; i < this.queries.length; i++) {
						if(this.queries[i].logicalNOT) {																																					
							out += 'NOT ' + '(' + this.queries[i].metric + ' ' + this.queries[i].operator + ' ' + this.queries[i].value + ')'; // + '\r\n'
						}
						else {
							out += '(' + this.queries[i].metric + ' ' + this.queries[i].operator + ' ' + this.queries[i].value + ')';
						}
						if(this.queries[i].logicalOperator){
							out += ' ' + this.queries[i].logicalOperator + ' ';
						}
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
		border-radius: 10px;
	}
	#addQueryButton {
		width: 100%; 
		margin: 10px 0 10px 0;
		background-color: rgb(199, 5, 5);
	}

	#addQueryButton:hover{
    background-color: rgb(199, 5, 5);
    box-shadow: 1px 1px 5px 3px grey;
    border-radius: 3px;
}


</style>