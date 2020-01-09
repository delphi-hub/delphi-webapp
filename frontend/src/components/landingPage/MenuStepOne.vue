<template>
	<div class="col-6" id="steponeCol">
		<div class="card">				
			<div class="card-body" v-bind:style="{ 'box-shadow': borderColor}">
				<h6 class="card-title"><u>1. Metric</u></h6>
				<!--This will be shown if the user has chosen a metric-->
				<div v-show="selectedMetric"> 
					<p>Chosen Metric:</p>
					<div id="chosenMetricDiv"> 
						<p id="chosenMetricP">[{{ selectedMetric }}]</p>
						<button id="removeMetricButton" class="btn btn-dark" @click="removeMetric">Rechoose</button>
					</div>
				</div>    
				<!--This will be shown if the user hasn't yet chosen a metric-->
				<div v-show="!selectedMetric">
					<input type="text" id="filter" name="metric_suggest" v-on:keyup="filter1" size="15" placeholder="Filter Metric"> 
					<div v-if="info">	 
						<select id="select" size="10" v-model="selectedMetric" @change="sendMetric">
							<option data-toggle="tooltip" v-bind:title="data.description" id="optionSelect" v-for="data in info" v-bind:key="data.name">
								{{data.name}}
							</option>
						</select>      
					</div>
				</div>                
			</div>
		</div>
	</div>
</template>

<script>
import axios from 'axios'
//import VueAxios from 'vue-axios'
export default {
	props: {
		metricShouldBeReseted: {
			type: Boolean,
			default: false
		}
	},
	data () {
		return {
			selectedMetric: null,
			borderColor: null,
			info: null,
		//	data: null
		}
	},
	watch: {
		metricShouldBeReseted: function (newVal) {	//if queryMenu asks for a metric reset, then this code here will be triggered and it calls the method metricIsReseted to tell queryMenu
			if(newVal){
				this.selectedMetric = null;
				this.metricIsReseted(newVal);
				document.getElementById("filter").value = ''; //without these, after removing metric the 
				this.filter1();								  //list would still be filtered even though the text field is empty
			}
		},
		selectedMetric: function (newVal) {		//if a metric is chosen, the border of step 1 will become green
			if(newVal){
				this.borderColor = '1px 1px 5px 3px green';
			}
			else {
				this.borderColor = null;
			}
		}
	},
	methods: {
		//Sends the metric to QueryMenu
		sendMetric(){
			this.$emit('metricSent', this.selectedMetric);
		},
		//Sends a confirmation to queryMenu that the metric is resetted
		metricIsReseted(){
			this.$emit('confirmMetricReset', false);
		},
		//If the user is rechoosing his metric, the chosen metric has to be removed and set to null.
		removeMetric(){
			this.selectedMetric = null;
			this.$emit('metricSent', this.selectedMetric);	//This sets the metric to null in the component queryMenu
		},
		filter1() {
			var keyword = document.getElementById("filter").value;
			var select = document.getElementById("select");
			for (var i = 0; i < select.length; i++) {
				var txt = select.options[i].text;
				var txt2 = keyword.toLowerCase();
				if (txt.toLowerCase().includes(txt2)) {
					select.options[i].style.display = 'list-item';           
				}
				else {
					select.options[i].style.display = 'none';
				}
			}
		}
	},
	mounted () {
		axios
		.get('https://delphi.cs.uni-paderborn.de/api/features')
		.then(response => (this.info = response.data.sort((a, b) => (a.name > b.name) ? 1 : -1)))
	}
}
</script>

<style>
	#steponeCol {
		padding:0 5px 0 0px;
	}
	.card-body {
		height: 200px;
		overflow-y:auto;
        padding:5px !important;
	}
	#chosenMetricDiv {
		text-align: center;
		border-radius: 5px;
		padding: 5px;
		background-color:rgba(176, 240, 176, 0.753);
	}    
	#chosenMetricP {
		margin: 0px 0px 5px 0px;
	}
	#removeMetricButton {
		background-color:rgb(192, 197, 192);
		font-variant: small-caps;
		color:black;
		width:100%;
	}
	#removeMetricButton:hover{
		box-shadow: 1px 1px 5px 3px rgb(190, 32, 32);
		color:red;
		border-radius: 3px;
	}
	#filter {
		width:100%;
		border-style: solid;
		border-color: grey;
		border-width: 0.2em 0.2em 0 0.2em;
		border-radius: 8px 8px 0 0;
	}
	#select {
		width: 100%;
		height: 125px;
		overflow: auto;
		border-style: solid;
		border-color: grey;
		border-width: 0.1em 0.2em 0.2em 0.2em;
		border-radius: 0 0 8px 8px;
	}
	#optionSelect:hover {
		background-color:rgba(176, 240, 176, 0.753);
	}
	
</style>