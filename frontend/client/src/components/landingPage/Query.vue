<template>
    <div class="col-6" id="queryCol">
        <div id="queryInputGrid">			
            <!-- for now disabled, because 'type in' functionallity is not supported right now -->
            <textarea class="form-control form-control-lg" id="queryInput" type="text" :value="queryToSearch" disabled></textarea>
		</div>
		<button id="startSearchButton" class="btn btn-success"><p id="searchButtonText">Start the Search</p></button>
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
             
            }
        },
        methods: {
          
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
        border-radius: 10px;
    }

    #queryInputGrid {
        margin: 10px 0px 10px 0px;
        
    }

    #queryInput {
        margin-top: 15px;
        margin-bottom: 17px;
        height:220px;
        width: 680px;
        background-color: white;
    }

    #startSearchButton {
        width: 97%; 
        height: 37px;
        background-color:rgb(199, 5, 5);
        margin-left: 10px;
    }

    #startSearchButton:hover{
    background-color: rgb(199, 5, 5);
    box-shadow: 1px 1px 5px 3px grey;
    border-radius: 3px;
    }

</style>