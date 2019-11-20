<template>
    <div class="col-4" id="steptwoCol">
        <div class="card">					
            <div class="card-body" v-bind:style="{ 'box-shadow': borderColor}">
                <h6 class="card-title">2. Operator and Value</h6>
                <select id="inputState" class="form-control" v-model="selectedOperator" @change="sendOperator">
                    <option><</option>
                    <option><=</option>
                    <option>></option>
                    <option>>=</option>
                    <option>=</option>
                </select>
                <input class="form-control" id="valueInput" type="text" placeholder="Input Value" v-model="selectedValue" @input="sendValue">             
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
                borderColor: null
            }
        },
        watch: {
            operatorAndValueShouldBeReseted: function (newVal, oldVal) {
                if(newVal){
                    this.selectedOperator = null;
                    this.selectedValue = null;
                    this.operatorAndValueIsReseted(newVal);
                }
            },
            selectedOperator: function (newVal, oldVal) {
                if(newVal && this.selectedValue){
                  this.borderColor = '1px 1px 5px 3px green';
                }
                else {
                    this.borderColor = null;
                }
            },
            selectedValue: function (newVal, oldVal) {
                if(newVal && this.selectedOperator){
                  this.borderColor = '1px 1px 5px 3px green';
                }
                else {
                    this.borderColor = null;
                }
            }
        },
        methods: {
            sendOperator(event){
                this.$emit('operatorSent', this.selectedOperator);               
            },
            sendValue(event){
                this.$emit('valueSent', this.selectedValue);
            },
            operatorAndValueIsReseted(event){
                this.$emit('confirmOperatorAndValueReset', false);
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
</style>