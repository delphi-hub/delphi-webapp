<template>
    <div class="col-3" id="stepthreeCol">
        <div class="card">					
            <div class="card-body" v-bind:style="{ 'box-shadow': borderColor}">
                <h6 class="card-title"><u>3. Logical Operator</u></h6>
                <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="addnot" value="Add logical NOT" v-model="checkedNOT" @change="sendLogicalNOT">
                    <label class="form-check-label" for="addnot">Add NOT</label>
                </div>
                <hr>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="exampleRadios" id="logical1" v-model="radioPicked" @change="sendLogicalOperator" value="">
                    <label class="form-check-label" for="logical1">No operator</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="exampleRadios" id="logical2" v-model="radioPicked" @change="sendLogicalOperator" value="&&">
                    <label class="form-check-label" for="logical2">AND</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="exampleRadios" id="logical3" v-model="radioPicked" @change="sendLogicalOperator" value="||">
                    <label class="form-check-label" for="logical3">OR</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="exampleRadios" id="logical4" v-model="radioPicked" @change="sendLogicalOperator" value="XOR">
                    <label class="form-check-label" for="logical4">XOR</label>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            lNOTAndLOperatorShouldBeReseted: {
                type: Boolean,
                default: false
            }
        },
        data () {
            return {
                checkedNOT: null,
                radioPicked: null,
                borderColor: null
            }
        },
        watch: {
            //if queryMenu asks for a reset, then this code here will be triggered and it calls the method lNOTAndLOperatorIsReseted to tell queryMenu
            lNOTAndLOperatorShouldBeReseted: function (newVal) {
                if(newVal){
                    this.checkedNOT = null;
                    this.radioPicked = null;
                    this.lNOTAndLOperatorIsReseted(newVal);
                }
            },
            //if a radio option is chosen, the border of step 3 will become green
            radioPicked: function (newVal) {
                if(newVal !== null){
                  this.borderColor = '1px 1px 5px 3px green';
                }
                else {
                    this.borderColor = null;
                }
            }
        },
        methods: {
            //sends the logicalNot value to queryMenu
            sendLogicalNOT(){
                this.$emit('logicalNotSent', this.checkedNOT);               
            },
            //sends the chosen logical operator to queryMenu
            sendLogicalOperator(){
                this.$emit('logicalOperatorSent', this.radioPicked);
            },
            //sends confirmation to queryMenu that the logical not and the logical operators are resettet
            lNOTAndLOperatorIsReseted(){
                this.$emit('confirmLNotAndLOperatorReset', false);
            }
        }
    }
</script>


<style>
   #stepthreeCol {
		padding:0 0 0 5px;
	}
    .card-body {
		height: 200px;
		overflow-y:auto;
        padding:5px !important;
	}
    hr {
        margin:5px !important;
    }
</style>