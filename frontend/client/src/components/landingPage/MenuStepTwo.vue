<template>
    <div class="col-4" id="steptwoCol">
        <div class="card">					
            <div class="card-body">
                <h6 class="card-title">Step 2</h6>
                <input class="form-control" id="valueInput" type="text" placeholder="Input Value">
                <select id="inputState" class="form-control">
                    <option selected>Choose Operator</option>
                    <option>=</option>
                    <option><=</option>
                    <option>>=</option>
                </select>
            </div>
        </div>
    </div>
</template>

<script>

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