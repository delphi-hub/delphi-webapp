<template>
    <div class="col-6" id="menuCol">
        <div class="row">
            <menuStepOne @metricSent="metric = $event"></menuStepOne>
            <menuStepTwo @operatorSent="operator = $event" @valueSent="value = $event"></menuStepTwo>
            <menuStepThree @logicalNotSent="logicalNOT = $event" @logicalOperatorSent="logicalOperator = $event"></menuStepThree>       
		</div>
        <button class="btn btn-dark" id="addQueryButton" @click.prevent="onAddQuery()">Add to Query</button>
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
                metric: '',
                operator: '',
				value: '',
				logicalNOT: '',
				logicalOperator: ''
            }
        },
		methods: {
			onAddQuery() {
				let querySent = {
					metric: this.metric,
					operator: this.operator,
					value: this.value,
					logicalNOT: this.logicalNOT,
					logicalOperator: this.logicalOperator
				}
				this.$emit('addQuerySent', querySent);
				this.metric = '';
				this.operator = '';
				this.value = '';
				this.logicalNOT = '';
				this.logicalOperator = '';
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