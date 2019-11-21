<template>
    <div class="col-6" id="queryCol">
        <div id="queryInputGrid">			
            <!-- for now disabled, because 'type in' functionallity is not supported right now -->
            <textarea class="form-control form-control-lg" id="queryInput" type="text" :value="message" @input="addToMessage"></textarea>
		</div>
		
	</div>
</template>

<script>
    export default {
        props: {
            queryToSearch: {
                type: String
            }
        },
        data () {
            return {
                message: ''            
            }
        },
        watch: {
            queryToSearch: function (newVal, oldVal) {
                if(newVal){
                    this.message += newVal;
                }
            }
        },
        methods: {
            addToMessage(event){
                this.message = event.target.value;
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
    }

    #queryInputGrid {
        margin: 10px 0px 10px 0px;
        
    }

    #queryInput {
        margin-top: 10px;
        margin-bottom: 17px;
        height:306px;
        width: 680px;
        background-color: white;
        resize: none;
    }

    

</style>