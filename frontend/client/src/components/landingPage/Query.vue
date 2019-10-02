<template>
    <div class="col-6" id="queryCol">
        <div id="queryInputGrid">
		    <p>Query</p>					
		    <input class="form-control form-control-lg" id="queryInput" type="text" placeholder="Example: Metric 4 Operator Value OR NOT(Metric 2 Operator Value)"></input>
		</div>
		<button id="startSearchButton" class="btn btn-success">Start the Search</button>
	</div>
</template>

<scritp>

</scritp>


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