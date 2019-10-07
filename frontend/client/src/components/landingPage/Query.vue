<template>
    <div class="col-6" id="queryCol">
        <div id="queryInputGrid">
		    <p>Query</p>					
            <input class="form-control form-control-lg" id="queryInput" type="text" v-model="queryToSearch.metric 
            + queryToSearch.operator
            + queryToSearch.value"></input>
		</div>
		<button id="startSearchButton" class="btn btn-success">Start the Search</button>
	</div>
</template>

<script>
    export default {
        props: {
            queryToSearch: {
                type: Object
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
    p {
        text-align: center;
        background-color:#d9d9d9;
        margin-bottom: 3px; 
        border-radius: 10px;
    }

    #queryCol {
        background-color:gray;
        border-radius: 10px
    }

    #queryInputGrid {
        margin: 10px 0px 10px 0px;
    }

    #queryInput {
        height:200px;
    }

    #startSearchButton {
        width: 100%; 
        height: 40px;
    }
</style>