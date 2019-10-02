<template>
    <div class="col-4" id="stepthreeCol">
        <div class="card">					
            <div class="card-body">
                <h6 class="card-title">Step 3</h6>
                <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="addnot">
                    <label class="form-check-label" for="addnot">Add logical NOT</label>
                </div>
                <hr>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="exampleRadios" id="logical1" value="option1" checked>
                    <label class="form-check-label" for="logical1">No logical operator</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="exampleRadios" id="logical2" value="option2">
                    <label class="form-check-label" for="logical2">AND</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="exampleRadios" id="logical3" value="option3">
                    <label class="form-check-label" for="logical3">OR</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="exampleRadios" id="logical4" value="option4">
                    <label class="form-check-label" for="logical4">XOR</label>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

</script>


<style>
   #stepthreeCol {
		padding:0 10px 0 5px;
	}
    .card-body {
		height: 230px;
		overflow:hidden;
	}
</style>