<template>
    <div class="col-5" id="queryCol">
        <!--This input is equals to the finalQuery variable. 
        After an input, the finalQuery variable will be set to the new input inside this textarea-->
        <textarea class="form-control form-control-lg" id="queryInput" type="text" :value="finalQuery" @input="addToFinalQuery"></textarea>
        <!--This button is grey when the input is not a valid query and otherwise red -->
        <button
            id="startSearchButton"
            class="btn btn-dark"
            @click="onStartSearch"
            :style="[(finalQuery) ? {'background-color': '#c20202'} : {'background-color':null}]">
            <!-- TODO: The condition has to be changed -->
            <h5 id="searchButtonText">Search</h5>
        </button>
	</div>
</template>

<script>
    export default {
        props: {
            partQuery: {        //query from SearchPart component
                type: String
            }
        },
        data () {
            return {
                finalQuery: ''
            }
        },
        watch: {
            //whenever a new query is comming from the queryMenu, it will be added to the finalQuery
            partQuery: function (newVal) {
                if(newVal){
                    this.finalQuery += newVal;
                    this.$emit('resetSavedQuery', "");  //without this line, if the user would choose twice the same query, nothing would happen
                }
            }
        },
        methods: {
            //the value of the current final query becomes the events value.
            //This function i needed if the user types in the textfield.
            addToFinalQuery(event){
                this.finalQuery = event.target.value;
            },
            //if a valid query is given, then this function sends the final query to the searchPart component to initiate a search
            onStartSearch() {
                if(this.finalQuery) {
                    this.$emit('finalQuerySend', this.finalQuery);
                }
            },
            //after a reset this sends a confirmation to searchPart
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
        height: 140px;
        width: 100%;
        background-color: white;
        resize: none;
    }
    #startSearchButton {
        width: 50%;
        margin-top: 10px;
        text-align: center;
        padding: 0 !important;
    }
    #searchButtonText  {
		font-variant: small-caps;
		font-size: 1.5em;
	}
    #startSearchButton:hover {
        box-shadow: 1px 1px 5px 3px grey;
        border-radius: 3px;
    }
</style>