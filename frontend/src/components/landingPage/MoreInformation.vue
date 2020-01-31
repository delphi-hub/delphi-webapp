<template>
  <div>
    <app-header></app-header>
    <h5>{{ receivedId }}</h5>
    <button v-on:click="startRetrieve"><h5>Retrieve</h5></button>
    <div id="moreInfoTableDiv">
    <v-data-table :headers="headers" :items="metrics" class="elevation-1">
    </v-data-table>
    </div>
    <app-footer></app-footer>
  </div>
</template>
<script>
import Header from "../Layout/Header";
import Footer from "../Layout/Footer";
import { eventBus } from "../../main";

export default {
  name: "moreInformation",
  data() {
    return {
      metricKeys : [],
      metricValues : [],
      receivedId: String,
      metrics : [], 
      headers: [
        { text: "Metric Name", align: "center", sortable: "true", value: "id" },
        { text: "Metric Value", align: "center", sortable: "true", value: "values" }
      ]
    };
  },
  watch: {
    readyToRetrieve: function(newValue) {
      if(newValue){
        this.startRetrieve();
      }
    }
  },
  created: function() {
    eventBus.$on("moreInfoEvent", data => {
      this.receivedId = data;
    });
  },
  // mounted() {
  //   this.startRetrieve();
  // },
  components: {
    "app-header": Header,
    "app-footer": Footer
  },
  methods: {
    startRetrieve () {
      var vm = this;
      this.$http
      .get("retrieve/" + vm.receivedId)
      .then(response => {
        return response.json();
      })
      .then(data => {
          vm.metricKeys = Object.keys(data.messages[0].metricResults);
          for(var key=0; key<vm.metricKeys.length; key++){
            var metricKey = vm.metricKeys[key];
            vm.metricValues.push(data.messages[0].metricResults[metricKey]);
          }
          
          for(var i=0;i<vm.metricKeys.length; i++){
            vm.metrics.push({"id" : vm.metricKeys[i],"values" : vm.metricValues[i]});
          }
        }),
      error => {
        alert("Invalid results!", error.messages);
      };
    },

  }
};
</script>
<style>
  #moreInfoTableDiv {
  margin: 30px;
  box-shadow: 1px 1px 5px 3px grey;
}
</style>