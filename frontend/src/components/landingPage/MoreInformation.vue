<template id="moreInfoPage">
  <v-app style="background-color: rgb(255, 255, 255);">
    <app-header></app-header>
    <div
      style="background-color: rgb(255, 255, 255); margin-bottom: 10px; padding:0 30px 30px 30px;"
    >
      <v-row>
        <v-col cols="12" class="py-1">
          <v-card
            elevation="24"
            style="border-radius: 10px 10px 10px 10px; background-color: rgb(255, 255, 255);"
          >
            <v-card-title
              class="justify-center mb-1 pa-1"
              style="background-color: #db2909; color:white"
            >
              <div class="headline text-center">Artifact Metadata</div>
            </v-card-title>
            <v-card-text class="pb-1">
              This page gives you more information about a chosen artifact. Use the Category Metric Tree below to decide which metric with its corresponding value will be shown
              in the Metric Table.
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
      <grid-loader
        v-if="this.flag && this.flag2 && this.idToRetrieve.length != 0 && this.queryErrorMoreInfo.length == 0"
        color="#c20202"
        class="loaderIcon"
        style="z-index:1"
      ></grid-loader>
      <v-card elevation="24" class="pa-0 mt-1">
        <v-row no-gutters>
          <v-col cols="12" md="3" lg="3">
            <v-card
              tile
              :elevation="0"
              class="ma-1"
              style="border-style: solid; border-color: #5579b3; border-width: 0.08em;"
            >
              <v-card-title class="title pt-0">Group ID</v-card-title>
              <v-card-subtitle v-if="this.groupID != this.errorStr" class="pb-0">{{ groupID }}</v-card-subtitle>
            </v-card>
          </v-col>
          <v-col cols="12" md="3" lg="3">
            <v-card
              tile
              :elevation="0"
              class="ma-1"
              style="border-style: solid; border-color: #5579b3; border-width: 0.08em;"
            >
              <v-card-title class="title pt-0">Artifact ID</v-card-title>
              <v-card-subtitle v-if="this.groupID != this.errorStr" class="pb-0">{{ artifactID }}</v-card-subtitle>
            </v-card>
          </v-col>
          <v-col cols="12" md="3" lg="3">
            <v-card
              tile
              :elevation="0"
              class="ma-1"
              style="border-style: solid; border-color: #5579b3; border-width: 0.08em;"
            >
              <v-card-title class="title pt-0">Version</v-card-title>
              <v-card-subtitle v-if="this.groupID != this.errorStr" class="pb-0">{{ version }}</v-card-subtitle>
            </v-card>
          </v-col>
          <v-col cols="12" md="3" lg="3">
            <v-card
              tile
              :elevation="0"
              class="ma-1"
              style="border-style: solid; border-color: #5579b3; border-width: 0.08em;"
            >
              <v-card-title class="title pt-0">Discovered</v-card-title>
              <v-card-subtitle
                v-if="this.groupID != this.errorStr"
                class="pb-0"
              >{{ discovered[0] }} at {{ discovered[2][0] }}</v-card-subtitle>
            </v-card>
          </v-col>
          <v-col cols="12" md="7" lg="7" class="py-0">
            <v-card
              :elevation="0"
              class="ma-1"
              style="border-style: solid; border-color: rgb(85, 121, 179); border-width: 0.08em;"
            >
              <v-card-title
                class="justify-center mb-1 pa-1"
                style="background-color: #e9e9e9"
              >Metric Category Tree</v-card-title>
              <v-card-subtitle>
                <v-text-field
                  v-model="search2"
                  append-icon="mdi-magnify"
                  placeholder="Search for Metric in Tree"
                  hide-details
                  clearable
                  clear-icon="mdi-close-circle-outline"
                ></v-text-field>
              </v-card-subtitle>
              <v-card-text style="overflow: auto; max-height: 500px;">
                <v-treeview
                  v-model="tree"
                  :search="search2"
                  :items="items"
                  selected-color="indigo"
                  open-on-click
                  selectable
                  return-object
                  expand-icon="mdi-chevron-down"
                  on-icon="mdi-checkbox-marked-outline"
                  off-icon="mdi-checkbox-blank-outline"
                  indeterminate-icon="mdi-checkbox-intermediate"
                  @update:active="onUpdate"
                ></v-treeview>
              </v-card-text>
            </v-card>
          </v-col>
          <v-col cols="12" md="5" lg="5">
            <v-card
              :elevation="0"
              class="ma-1"
              style="border-style: solid; border-color: #5579b3; border-width: 0.08em;"
            >
              <v-card-title
                class="justify-center mb-0 pa-1"
                style="background-color: #e9e9e9"
              >Metric Table</v-card-title>
              <v-card-subtitle>
                <v-text-field
                  v-model="search"
                  v-show="tree.length !== 0"
                  append-icon="mdi-magnify"
                  placeholder="Search for Metric or Value in Table"
                  hide-details
                  clearable
                  clear-icon="mdi-close-circle-outline"
                ></v-text-field>
              </v-card-subtitle>
              <div
                v-if="tree.length === 0"
                key="title"
                class="title font-weight-light grey--text pa-4 text-center"
              >Select Metrics in the Tree</div>
              <v-card-text style="overflow: auto; max-height: 500px;">
                <v-data-table
                  v-show="tree.length !== 0"
                  :headers="headers"
                  :items="tree"
                  :search="search"
                  disable-pagination
                  :hide-default-footer="true"
                ></v-data-table>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-card>
      <app-footer></app-footer>
    </div>
  </v-app>
</template>

<script>
import Header from "../Layout/Header";
import Footer from "../Layout/Footer";
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
          size={600}
          sizeUnit={"px"}
        />
      </div>
    );
  },
  data() {
    return {
      receivedMetrics: [],
      metricObjects: [],
      metricCategories: [],
      tree: [],
      search: "",
      search2: null,
      loading: true,
      flag: true,
      flag2: false,
      result: String,
      queryErrorMoreInfo: "",
      idToRetrieve: "",
      receivedId: this.$route.query.id,
      artifactID: String,
      groupID: String,
      discovered: String,
      source: String,
      version: String,
      errorStr: "function String() { [native code] }",
      metricKeys: [],
      metricValues: [],
      headers: [
        { text: "Metric Name", 
          align: "left", 
          sortable: "true", 
          value: "id" },
        {
          text: "Metric Value",
          align: "center",
          sortable: "true",
          value: "value"
        }
      ]
    };
  },

  watch: {
    $route(to) {
      this.receivedId = to.query.id;
    }
  },
  created: function() {
    this.receivedId = this.$route.query.id;
    this.flag2 = true;
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
      this.flag2 = true;
      if (receivedIdParam != this.errorStr) {
        if (this.receivedId != this.errorStr) {
          this.idToRetrieve = receivedIdParam;
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
            vm.flag = false;
            vm.flag2 = false;
            this.getAllMetricObjects();
          }),
          error => {
            if (error.status == 417) {
              vm.queryErrorMoreInfo = error.body;
            } else {
              vm.queryErrorMoreInfo =
                "We received " + error.status + " " + error.statusText;
            }
          };
      }
    },
    getChildren(cat) {
      const allMetrics = [];
      for (const metric of this.metricObjects) {
        if (metric.category !== cat) continue;

        allMetrics.push({ ...metric, name: metric.name });
      }
      return allMetrics.sort((a, b) => {
        return a.name > b.name ? 1 : -1;
      });
    },
    getCategory(metric) {
      var t = metric.split(".", 2);
      t = t.pop();
      return t;
    },
    getAllMetricObjects() {
      const temp = Object.keys(this.receivedMetrics);
      for (var element of temp) {
        this.metricObjects.push({
          id: element,
          name: element,
          value: this.receivedMetrics[element],
          category: this.getCategory(element)
        });
      }
      this.getAllCategories();
    },
    getAllCategories() {
      for (const m of this.metricObjects) {
        this.tree.push(m);
        if (!this.metricCategories.includes(m.category)) {
          this.metricCategories.push(m.category);
        }
      }
      this.tree.sort((a, b) => (a.name > b.name) ? 1 : -1);
      this.metricCategories.sort();
    },
    onUpdate(selection) {
      console.log(selection);
    },
    clearPage() {
      this.metricObjects = [];
      this.metricCategories = [];
      this.tree = [];
      this.search = "";
      this.search2 = null;
      this.groupID = this.errorStr;
      this.artifactID = this.errorStr;
      this.version = this.errorStr;
      this.discovered = this.errorStr;
      this.result = this.errorStr;
    }
  },
  computed: {
    items() {
      const children = this.metricCategories.map(category => ({
        id: category,
        name: category,
        children: this.getChildren(category)
      }));

      return [
        {
          id: 1,
          name: "All Metrics",
          children
        }
      ];
    }
  }
};
</script>
<style>
.loaderIcon {
  position: absolute;
  top: 50%;
  left: 50%;
}
</style>