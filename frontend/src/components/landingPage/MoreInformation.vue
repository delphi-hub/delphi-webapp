<template>
  <div id="moreInfoPage">
    <app-header></app-header>
    <grid-loader v-if="this.flag" color="#c20202" class="loaderIcon"></grid-loader>
    <div id="moreInfoDiv">
      <div id="moreInfoTextDiv">
        <p v-if="this.groupID != this.errorStr">Group ID : {{ groupID }}</p>
        <p v-if="this.artifactID != this.errorStr">Artifact ID : {{ artifactID }}</p>
        <p v-if="this.version != this.errorStr">Version : {{ version }}</p>
        <p v-if="this.discovered != this.errorStr">Discovered : {{ discovered }}</p>
      </div>
    </div>
    <div id="moreInfoTableDiv">
      <v-data-table :headers="headers" :items="metrics" class="elevation-1"></v-data-table>
    </div>
    <app-footer></app-footer>
  </div>
</template>
<script>
import Header from "../Layout/Header";
import Footer from "../Layout/Footer";
import { eventBus } from "../../main";
import { GridLoader } from "@saeris/vue-spinners";

export default {
  name: "moreInformation",
  /* Render is used to set the values for page loading spinners */
  render() {
    return (
      <div class='loader'>
        <ClipLoader
          class="custom-class"
          loading={this.loading}
          color={"#c20202"}
          size={400}
          sizeUnit={"px"}
        />
      </div>
    )
  },
  data() {
    return {
      loading: true,
      flag: true,
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
  watch: function() {
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
    "app-footer": Footer,
    "grid-loader": GridLoader
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
          vm.flag = false;
        }),
        error => {
          alert("Invalid results!", error.messages);
        };
    }
  }
};
</script>
<style>
#moreInfoPage {
  background-color: rgb(250, 250, 250);
}

#moreInfoTableDiv {
  margin-left: 300px;
  margin-right: 300px;
  margin-top: 30px;
  margin-bottom: 60px;
  box-shadow: 1px 1px 5px 3px grey;
  border-radius: 5px;
}

#moreInfoDiv {
  margin-left: 300px;
  margin-right: 300px;
  margin-top: 30px;
  margin-bottom: 30px;
  box-shadow: 1px 1px 5px 3px grey;
  font-variant: small-caps;
  font-size: 1em;
  border-radius: 5px;
}

#moreInfoTextDiv {
  padding-left: 60px;
  padding-right: 30px;
  padding-top: 30px;
  padding-bottom: 30px;
}

.loaderIcon {
  position: absolute; 
  top: 50%;
  left: 50%;
  color: red;
}

</style>