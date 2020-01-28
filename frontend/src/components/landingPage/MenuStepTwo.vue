<template>
	<div class="col-3" id="steptwoCol">
		<div class="card">					
			<div class="card-body" v-bind:style="{ 'box-shadow': borderColor}">
				<h6 class="card-title"><u>2. Operator and Value</u></h6>
				<select id="inputState" class="form-control" v-model="selectedOperator" @change="sendOperator">
					<option>&lt;</option>
					<option value="&lt;=">&#8804;</option>
					<option>&gt;</option>
					<option value="&gt;=">&#8805;</option>
					<option>=</option>
				</select>
				<input class="form-control" id="valueInput" type="text" placeholder="Input Value" v-model="selectedValue" @input="sendValue">  
				<h6 id="nanInformation" v-show="notANumber">Number Please</h6>           
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
			//if queryMenu asks for a reset, then this code here will be triggered and it calls the method operatorAndValueIsReseted to tell queryMenu
			operatorAndValueShouldBeReseted: function (newVal) {
				if(newVal){
					this.selectedOperator = null;
					this.selectedValue = null;
					this.operatorAndValueIsReseted(newVal);
				}
			},
			//if a operator and value is given, the border of step 2 will become green
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
			//if a operator and value is given, the border of step 2 will become green
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
			//sends the operator to the queryMenu
			sendOperator(){
				this.$emit('operatorSent', this.selectedOperator);               
			},
			//sends the value to the queryMenu if it is a number
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
			//after a reset this sends a confirmation to queryMenu
			operatorAndValueIsReseted(){
				this.$emit('confirmOperatorAndValueReset', false);
			},
			//needed to check if the value is a number
			isNumeric: function (n) {
				return !isNaN(parseInt(n)) && isFinite(n);
			}
		}
	}
</script>


<style>
	#steptwoCol {
		padding:0 5px 0 5px;
		width:100px !important;
	}
	.card-body {
		height: 200px;
		overflow-y:auto;
        padding:5px !important;
	}
	#valueInput {
		margin: 20px auto 5px auto;
	}
	#nanInformation {
		color:red;
		text-align: center;
	}
</style>