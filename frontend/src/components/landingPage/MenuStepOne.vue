<template>
    <div class="col-4" id="steponeCol">
        <div class="card">				
            <div class="card-body" v-bind:style="{ 'box-shadow': borderColor}">
                <h6 class="card-title"><span v-if="selectedMetric">1. Metric: [{{ selectedMetric }}]</span><span v-else>1. Metric</span></h6>
                <input type="text" id="filter" name="metric_suggest" onkeyup="filter1()" size="15" :value="selectedMetric"> 
		      	<div>	
				    <select id="select" size="10" style="width: 165px;height: 120px;" v-model="selectedMetric" @change="sendMetric"></select>      
				</div>
            </div>
        </div>
    </div>
</template>

<script>
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
            borderColor: null
        }
    },
    watch: {
        metricShouldBeReseted: function (newVal, oldVal) {
            if(newVal){
                this.selectedMetric = null;
                this.metricIsReseted(newVal);
            }
        },
        selectedMetric: function (newVal, oldVal) {
            if(newVal){
                this.borderColor = '1px 1px 5px 3px green';
            }
            else {
                this.borderColor = null;
            }
        }
    },
    methods: {
        sendMetric(event){
            this.$emit('metricSent', this.selectedMetric);
        },
        metricIsReseted(event){
            this.$emit('confirmMetricReset', false);
        }
    }
}
</script>

<style>
    #steponeCol {
		padding:0 5px 0 10px;
	}
    .card-body {
		height: 230px;
		overflow:hidden;
	}
</style>