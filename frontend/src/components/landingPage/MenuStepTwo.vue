<template>
	<div class="col-4" id="steptwoCol">
		<div class="card">					
			<div class="card-body" v-bind:style="{ 'box-shadow': borderColor}">
				<h6 class="card-title">2. Operator and Value</h6>
				<select id="inputState" class="form-control" v-model="selectedOperator" @change="sendOperator">
					<option>&lt;</option>
					<option value="&lt;=">&#8804;</option>
					<option>&gt;</option>
					<option value="&gt;=">&#8805;</option>
					<option>=</option>
				</select>
				<input class="form-control" id="valueInput" type="text" placeholder="Input Value" v-model="selectedValue" @input="sendValue">  
				<h6 id="nanInformation" v-show="notANumber">Value should be numerical</h6>           
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		props: {
			operatorAndValueShouldBeReseted: {
				type: Boolean,
				default: false
			}
		},
		data () {
			return {
				selectedOperator: null,
				selectedValue: null,
				borderColor: null,
				notANumber: false
			}
		},
		watch: {
			operatorAndValueShouldBeReseted: function (newVal) {
				if(newVal){
					this.selectedOperator = null;
					this.selectedValue = null;
					this.operatorAndValueIsReseted(newVal);
				}
			},
			selectedOperator: function (newVal) {
				if(newVal && this.selectedValue && !this.notANumber){
					this.borderColor = '1px 1px 5px 3px green';
				}
				else if(this.notANumber){
					this.borderColor = '1px 1px 5px 3px red';
				}
				else {
					this.borderColor = null;
				}
			},
			selectedValue: function (newVal) {
				if(newVal && this.selectedOperator && !this.notANumber){
					this.borderColor = '1px 1px 5px 3px green';
				}
				else if(this.notANumber){
					this.borderColor = '1px 1px 5px 3px red';
				}
				else {
					this.borderColor = null;
				}
			}
		},
		methods: {
			sendOperator(){
				this.$emit('operatorSent', this.selectedOperator);               
			},
			sendValue(){
				if(this.isNumeric(this.selectedValue) || (this.selectedValue === '')){
					this.$emit('valueSent', this.selectedValue);
					this.notANumber = false;
				}
				else {
					this.notANumber = true;
					this.$emit('valueSent', null);
				}
				
			},
			operatorAndValueIsReseted(){
				this.$emit('confirmOperatorAndValueReset', false);
			},
			isNumeric: function (n) {
				return !isNaN(parseInt(n)) && isFinite(n);  //is .5 or 5. or +5 or -5 allowed? Are real numbers allowed?
			}
		}
	}
</script>


<style>
	#steptwoCol {
		padding:0 5px 0 5px;
	}
	.card-body {
		height: 230px;
		overflow:hidden;
	}
	#valueInput {
		margin: 30px auto;
	}
	#nanInformation {
		color:red;
		text-align: center;
	}
</style>