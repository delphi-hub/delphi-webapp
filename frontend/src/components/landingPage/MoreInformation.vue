<template>
  <div>
    <app-header></app-header>
    <h5></h5>
    <div id="moreInfoDiv">
      <div id="moreInfoTextDiv">
        <p v-if="this.receivedId != this.errorStr">ID : {{ receivedId }}</p>
        <p v-if="this.groupID != this.errorStr">Group ID : {{ groupID }}</p>
        <p v-if="this.artifactID != this.errorStr">Artifact ID : {{ artifactID }}</p>
        <p v-if="this.discovered != this.errorStr">Discovered : {{ discovered }}</p>
        <p v-if="this.source != this.errorStr">Source : {{ source }}</p>
        <p v-if="this.version != this.errorStr">Version : {{ version }}</p>
      </div>
    </div>
    <div id="moreInfoTableDiv">
      <v-data-table :headers="headers" :items="metrics" class="elevation-1"></v-data-table>
    </div>
    <div>

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
      receivedId: String,
      artifactID: String,
      groupID: String,
      discovered: String,
      source: String,
      version: String,
      errorStr: "function String() { [native code] }",
      metricKeys: [],
      metricValues: [],
      metrics: [],
      headers: [
        { text: "Metric Name", align: "center", sortable: "true", value: "id" },
        {
          text: "Metric Value",
          align: "center",
          sortable: "true",
          value: "values"
        }
      ]
    };
  },
  watch : function() {
    setTimeout(() => {
      this.startRetrieve();
    }, 100);
  },
  created: function() {
    eventBus.$on("moreInfoEvent", data => {
      this.receivedId = data;
    }),
    setTimeout(() => {
      this.startRetrieve();
    }, 100);
  },
  components: {
    "app-header": Header,
    "app-footer": Footer
  },
  methods: {
    startRetrieve() {
      var vm = this;
      this.$http
        .get("retrieve/" + vm.receivedId)
        .then(response => {
          return response.json();
        })
        .then(data => {
          (vm.groupID = data.messages[0].metadata.groupId),
            (vm.artifactID = data.messages[0].metadata.artifactId),
            (vm.discovered = data.messages[0].metadata.discovered),
            (vm.source = data.messages[0].metadata.source),
            (vm.version = data.messages[0].metadata.version),
            (vm.metricKeys = Object.keys(data.messages[0].metricResults));
          for (var key = 0; key < vm.metricKeys.length; key++) {
            var metricKey = vm.metricKeys[key];
            vm.metricValues.push(data.messages[0].metricResults[metricKey]);
          }

          for (var i = 0; i < vm.metricKeys.length; i++) {
            vm.metrics.push({
              id: vm.metricKeys[i],
              values: vm.metricValues[i]
            });
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

#moreInfoDiv {
  margin: 30px;
  box-shadow: 1px 1px 5px 3px grey;
  font-variant: small-caps;
  font-size: 1.2em;
}

#moreInfoTextDiv {
  padding-left: 30px;
  padding-right: 30px;
  padding-top: 30px;
  padding-bottom: 30px;
}

</style>