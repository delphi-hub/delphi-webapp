<template>
    <div class="col-4" id="steponeCol">
        <div class="card">					
            <div class="card-body">
                <h6 class="card-title">Step 1</h6>
                <input type="text" id="filter" name="metric_suggest" onkeyup="filter1()" size="15" v-model="selectedMetric">
		      	<div>	
				    <select id="select" size="10" style="width: 165px;height: 120px;" v-model="selectedMetric" @change="sendMetric"></select>
                    
				</div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  data () {
    return {
        selectedMetric: ''
    }
  },
  methods: {
    sendMetric(event){
        this.$emit('metricSent', this.selectedMetric);
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