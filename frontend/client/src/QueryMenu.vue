<template>
    <div class="col-6" id="menuCol">
        <div class="row">
            <menuStepOne></menuStepOne>
            <menuStepTwo></menuStepTwo>
            <menuStepThree></menuStepThree>       
        </div>
        <button class="btn btn-dark" id="addQueryButton">Add to Query</button>
    </div>
</template>

<script>
	import MenuStepOne from './MenuStepOne.vue';
	import MenuStepTwo from './MenuStepTwo.vue';
    import MenuStepThree from './MenuStepThree.vue';

	export default {
		components: {
			'menuStepOne': MenuStepOne,
			'menuStepTwo': MenuStepTwo,
            'menuStepThree': MenuStepThree
		}
	}
</script>


<style>
    #menuCol {
		background-color:gray; 
		padding-top: 10px; 
		border-radius: 10px;
	}
	#addQueryButton {
		width: 100%; 
		margin: 10px 0 10px 0;
	}
</style>