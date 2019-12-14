<template>
    <div class="col-5" id="queryCol">
        <textarea class="form-control form-control-lg" id="queryInput" type="text" :value="finalQuery" @input="addToFinalQuery"></textarea>
        <button
            id="startSearchButton"
            class="btn btn-dark"
            @click="onStartSearch"
            :style="[(finalQuery) ? {'background-color': '#c20202'} : {'background-color':null}]">
            <!-- TODO: The condition has to be changed -->
            <h5 id="searchButtonText">Start Search</h5>
        </button>
	</div>
</template>

<script>
    export default {
        props: {
            partQuery: {
                type: String
            },
            finalQueryShouldBeReseted: {
                type: Boolean
            }
        },
        data () {
            return {
                finalQuery: ''
            }
        },
        watch: {
            partQuery: function (newVal) {
                if(newVal){
                    this.finalQuery += newVal;
                }
            },
            finalQueryShouldBeReseted: function () {
                this.finalQuery = '';
                this.finalMetricIsReseted();
            }
        },
        methods: {
            addToFinalQuery(event){
                this.finalQuery = event.target.value;
            },
            onStartSearch() {
                if(this.finalQuery) {
                    this.$emit('finalQuerySend', this.finalQuery);
                }
            },
            finalMetricIsReseted(){
                this.$emit('confirmFinalQueryReset', false);
            }
        }
    }
</script>


<style>
    .queryHeading {
        text-align: center;
        color: white;
        background-color: grey;
        margin-bottom: 3px; 
        border-radius: 3px;
    }

    #queryCol {
        background-color:rgb(235, 235, 235);
        border-radius: 10px 0 0 10px;
        padding: 12px 0 12px 12px;
        text-align:center;
    }

    #queryInput {
        height:160px;
        width: 100%;
        background-color: white;
        resize: none;
    }
    #startSearchButton {
        width: 90%;
        height: 50px;
        margin-top: 10px;
        font-variant: small-caps;
    }

    #startSearchButton:hover {
        box-shadow: 1px 1px 5px 3px grey;
        border-radius: 3px;
    }
</style>