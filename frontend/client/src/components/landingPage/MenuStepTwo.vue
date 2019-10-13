<template>
    <div class="col-4" id="steptwoCol">
        <div class="card">					
            <div class="card-body">
                <h6 class="card-title">Step 2</h6>
                <select id="inputState" class="form-control" v-model="selectedOperator" @change="sendOperator">
                    <option>=</option>
                    <option><=</option>
                    <option>>=</option>
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
                selectedValue: null
            }
        },
        watch: {
            operatorAndValueShouldBeReseted: function (newVal, oldVal) {
                if(newVal){
                    this.selectedOperator = null;
                    this.selectedValue = null;
                    this.operatorAndValueIsReseted(newVal);
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