<template>

<Div>
	<b-card></b-card>
<div class="card" id="searchPart">		
		<div class="container-fluid">
            <div class="row">
	            <div class="col-6">
	            	<h5>Type in your query...</h5>
				</div>
		        <div class="col-6">
		        	<h5>...or use these steps to build a query.</h5>
	        	</div>
        	</div>
            <div class="row">					
				<query :queryToSearch="savedQuery"></query>
				<queryMenu @addQuerySent="saveQuery"></queryMenu>				
			</div>	
			<div>
			</div>		
        </div>
    </div>
</Div>
    
</template>

<script>
	import Query from './Query.vue';
	import QueryMenu from './QueryMenu.vue';

	export default {
		components: {
			'query': Query,
			'queryMenu': QueryMenu
		},
		methods: {
			saveQuery(querySent) {
				this.savedQuery = querySent;
			}
		},
		data () {
            return {
                savedQuery: ''
            }
        }
	}
</script>

<style>
	.container-fluid {
		margin-top: 10px;
	}

	#searchPart {
		margin: 40px; 
		padding: 10px;
		height: 380px;
		box-shadow: 1px 1px 5px 3px grey;
	}
</style>