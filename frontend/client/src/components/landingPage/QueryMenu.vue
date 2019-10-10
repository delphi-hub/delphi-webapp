<template>
    <div class="col-6" id="menuCol">
        <div class="row">
            <menuStepOne @metricSent="metric = $event"></menuStepOne>
            <menuStepTwo @operatorSent="operator = $event" @valueSent="value = $event"></menuStepTwo>
            <menuStepThree @logicalNotSent="logicalNOT = $event" @logicalOperatorSent="logicalOperator = $event"></menuStepThree>       
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
				errors: []
            }
        },
		methods: {
			onAddQuery() {
				if(this.metric && this.operator && this.value){
					if(this.errors.length){
						this.errors = [];
					}
					let querySent = {
						metric: this.metric,
						operator: this.operator,
						value: this.value,
						logicalNOT: this.logicalNOT,
						logicalOperator: this.logicalOperator
					}
					this.$emit('addQuerySent', querySent);
					this.metric = null;
					this.operator = null;
					this.value = null;
					this.logicalNOT = null;
					this.logicalOperator = null;
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
		background-color:gray; 
		padding-top: 10px; 
		border-radius: 10px;
	}
	#addQueryButton {
		width: 100%; 
		margin: 10px 0 10px 0;
	}
</style>