<template id="moreInfoPage">
  <div>
    <app-header></app-header>
    <grid-loader v-if="this.flag" color="#c20202" class="loaderIcon"></grid-loader>
    <v-container fluid>
      <v-row style="width:1365px; height: 20px; position: relative; left: -112px; ">
      <h6 style="font-variant:small-caps">Enter the ID to retrieve corresponding metric values</h6>
      </v-row>
      <v-row style="width:1365px; height: 50px; position: relative; left: -112px; ">
      <v-textarea
          outlined
          append-icon="mdi-magnify" 
          @keydown.enter.prevent
					id="retrieveInput"
          :value ="this.idToRetrieve"
          :v-model="this.receivedId"
					rows="1"
					clearable
          auto-grow
          @input="startRetrieve($event)"
          >
				</v-textarea>
      </v-row>
        </v-container>
        <v-container fluid style="max-width:1400px">
      <v-row style="max-width:1400px;">
      <v-card
        class="mx-auto"
        id="basicInfo"
        width="400"
        height="500"
        outlined
        elevation="20px"
        style="overflow: auto;"
      >
      <v-card-title style="font-variant: small-caps; top: 0; position: sticky; background-color: rgb(190, 33, 33); color: white">Information</v-card-title>
          <div id="moreInfoTextDiv">
            
            <v-card-text style="font-size:1em; font-weight:bold" v-if="this.groupID != this.errorStr">Group ID :</v-card-text>
            <v-card-text style="font-size:1em" v-if="this.groupID != this.errorStr">{{ groupID }}</v-card-text>
            <v-card-text style="font-size:1em; font-weight:bold" v-if="this.artifactID != this.errorStr">Artifact ID :</v-card-text>
            <v-card-text style="font-size:1em" v-if="this.groupID != this.errorStr">{{ artifactID }}</v-card-text>
            <v-card-text style="font-size:1em; font-weight:bold" v-if="this.version != this.errorStr">Version :</v-card-text>
            <v-card-text style="font-size:1em" v-if="this.groupID != this.errorStr">{{ version }}</v-card-text>
            <v-card-text style="font-size:1em; font-weight:bold" v-if="this.discovered != this.errorStr">Discovered :</v-card-text>
            <v-card-text style="font-size:1em" v-if="this.groupID != this.errorStr">{{ discovered[0] }} at {{ discovered[2][0] }}</v-card-text>
            
          </div>
          
      </v-card>
      <v-card
        class="mx-auto"
        id="tree"
        width="400"
        max-height="500"
        outlined
        elevation="5"
      >
      <v-card-title style=" font-variant: small-caps;  top: 0; position: sticky; background-color: rgb(190, 33, 33); color: white">Metric Categories</v-card-title>
        <div style="margin-left:20px; top:10%; height:90%; max-height:87%; overflow: auto;">
        <div>{{ temp4 }}</div>
        <div
          @click="displayTreeParent"
          v-for="iterator1 in this.temp2"
          :key="iterator1"
        ><v-chip large style="margin: 10px; width: 310px"><span style="margin-left:20px; font-weight:bold">{{ iterator1 }}</span></v-chip></div>
        <div v-if="this.parentFlag">
          <div
            v-for="(iterator2) in this.temp3"
            :key="(iterator2)"
            @click="displayChildren(iterator2)"
          >
            <span class="childElement"> &#8627; <v-chip large style="margin: 7px; width: 240px"> <span style="margin-left:20px; font-weight:bold">{{ iterator2 }}</span> </v-chip> <br> </span>
            <v-slot id="child"></v-slot>
          </div>
        </div>
        </div>
      </v-card>
      <v-card
        class="mx-auto"
        id="results"
        width="500"
        height="500"
        outlined
        elevation="5"
        style="overflow: auto;"
      >
      <v-card-title style="font-variant: small-caps; top: 0; position: sticky; background-color: rgb(190, 33, 33); color: white">Result</v-card-title>
        <v-data-table
          :headers="headers"
          :items="temp4"
          :loading="progressBar"
          loading-text="Searching for the results, please wait...."
          class="elevation-1"
        ></v-data-table>
      </v-card>
    </v-row>
    </v-container>
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
      parentFlag: true,
      childFlag: false,
      idToRetrieve: "",
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
      this.startRetrieve(this.receivedId);
    }, 100);
  },

  created: function() {
    eventBus.$on("moreInfoEvent", data => {
      this.receivedId = data;
    }),
      setTimeout(() => {
        this.startRetrieve(this.receivedId);
      }, 100);
  },
  components: {
    "app-header": Header,
    "app-footer": Footer,
    "grid-loader": GridLoader
  },
  methods: {
    startRetrieve(receivedIdParam) {
      var vm = this;
      if(vm.receivedId !="function String() { [native code] }"){
        vm.idToRetrieve = receivedIdParam;
      }
      
      this.$http
        .get("retrieve/" + receivedIdParam)
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
  margin-left: 50px;
}

.cardDeck {
  position: relative;
  left: 5%;
}

.loaderIcon {
  position: absolute;
  top: 50%;
  left: 50%;
}

</style>