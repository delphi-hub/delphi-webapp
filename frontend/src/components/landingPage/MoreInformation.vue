<template id="moreInfoPage">
  <div>
    <app-header></app-header>
    <grid-loader v-if="this.flag" color="#c20202" class="loaderIcon"></grid-loader>
    <div style="height:50px"></div>
    <b-card-group deck>
      <b-card class="text-left" style="max-width: 400px; height:500px">
        <div id="moreInfoDiv">
          <div id="moreInfoTextDiv">
            <b v-if="this.groupID != this.errorStr">Group ID :</b>
            <p v-if="this.groupID != this.errorStr">{{ groupID }}</p>
            <p v-if="this.artifactID != this.errorStr">Artifact ID : {{ artifactID }}</p>
            <p v-if="this.version != this.errorStr">Version : {{ version }}</p>
            <p
              v-if="this.discovered != this.errorStr"
            >Discovered on : {{ discovered[0] }} at {{ discovered[2][0] }}</p>
          </div>
        </div>
      </b-card>
      <b-card class="text-left" style="max-width: 400px; height:500px">
        <div>{{ temp4 }}</div>

        <div
          @click="displayTreeParent"
          v-for="iterator1 in this.temp2"
          :key="iterator1"
        >{{ iterator1 }}</div>
        <div v-if="this.parentFlag">
          <div
            v-for="(iterator2) in this.temp3"
            :key="(iterator2)"
            @click="displayChildren(iterator2)"
          >
            <span class="childElement">{{ iterator2 }}</span>
            <v-slot id="child"></v-slot>
          </div>
        </div>
      </b-card>
      <b-card class="text-left" style="max-width: 400px; height:500px">
        <v-data-table
            :headers="headers"
            :items="temp4"
            :loading="progressBar"
            loading-text="Searching for the results, please wait...."
            class="elevation-1"
          >
        </v-data-table>
      </b-card>
    </b-card-group>
    <app-footer></app-footer>
  </div>
</template>
<script>
import Vue from "vue";
import Header from "../Layout/Header";
import Footer from "../Layout/Footer";
import { eventBus } from "../../main";
import { GridLoader } from "@saeris/vue-spinners";
import { BootstrapVue } from 'bootstrap-vue';
Vue.use(BootstrapVue);

export default {
  name: "moreInformation",
  /* Render is used to set the values for page loading spinners */
  render() {
    return (
      <div class="loader">
        <ClipLoader
          class="custom-class"
          loading={this.loading}
          color={"#c20202"}
          size={400}
          sizeUnit={"px"}
        />
      </div>
    );
  },
  data() {
    return {
      loading: true,
      flag: true,
      tree: [],
      temp: [],
      temp2: [],
      temp3: [],
      temp4: [],
      table: [],
      parentFlag: false,
      childFlag: false,
      receivedId: String,
      artifactID: String,
      groupID: String,
      discovered: String,
      source: String,
      version: String,
      errorStr: "function String() { [native code] }",
      receivedMetrics: [],
      metricKeys: [],
      metricValues: [],
      metrics: [],
      headers: [
        { text: "Metric Name", align: "left", sortable: "true", value: "id" },
        {
          text: "Metric Value",
          align: "left",
          sortable: "true",
          value: "values"
        }
      ]
      // treeItem: [{
      //   id: 1,
      //   name: this.temp2,
      //   children: [
      //     {id:2, name: this.temp3}
      //   ]
      // }
      // ]
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
          (vm.receivedMetrics = data.messages[0].metricResults),
            (vm.groupID = data.messages[0].metadata.groupId),
            (vm.artifactID = data.messages[0].metadata.artifactId),
            (vm.discovered = data.messages[0].metadata.discovered),
            (vm.source = data.messages[0].metadata.source),
            (vm.version = data.messages[0].metadata.version),
            (vm.metricKeys = Object.keys(data.messages[0].metricResults)),
            (vm.discovered = vm.discovered.split("T")),
            vm.discovered.push(vm.discovered[1].split(".", 1));
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
          this.displayTree();
        }),
        error => {
          alert("Invalid results!", error.messages);
        };
    },
    displayTree() {
      var vm = this;
      vm.temp = Object.keys(vm.receivedMetrics);
      for (var element of vm.temp) {
        var metricParent = element.split(".", 1);
        if (!vm.temp2.includes(metricParent)) {
          vm.temp2 = metricParent;
        }
        var metricChild = element.split(".", 2);
        if (metricChild.includes(metricParent[0])) {
          metricChild = metricChild.pop();
          if (!vm.temp3.includes(metricChild)) {
            vm.temp3.push(metricChild);
          }
        }
      }
      vm.temp3.sort();
    },
    displayTreeParent() {
      this.parentFlag = !this.parentFlag;
    },
    displayChildren(e) {
      this.childFlag = !this.childFlag;
      // for (var element of this.temp3) {
      //   this.temp4.push(element);
      // }
      this.temp4 = [];
      for (var element of Object.keys(this.receivedMetrics)) {
        if (element.includes(e)) {
          this.temp4.push(this.receivedMetrics[element]);
        }
      }
    }
  }
};
</script>
<style>
#moreInfoPage {
  background-color: rgb(250, 250, 250);
}

.childElement {
  margin-left: 30px;
}


.loaderIcon {
  position: absolute;
  top: 50%;
  left: 50%;
}
</style>