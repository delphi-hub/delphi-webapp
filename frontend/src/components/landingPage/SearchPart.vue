<template>
  <v-app>
    <div>
      <div class="card" id="searchPart">
        <div class="container-fluid">
          <div class="row">
            <div class="col-6">
              <h5 class="queryHelpText">Type in your query...</h5>
            </div>
            <div class="col-6">
              <h5 class="queryHelpText">...or use these steps to build a query.</h5>
            </div>
          </div>
          <!--SearchPart has two child components. Query is where the query textfield and the start search button is.
          QueryMenu consists of the three steps to create a query and the add query button.-->
          <div class="row">
            <!--savedQuery updates the prop called partQuery
              finalQueryToReset updates the prop called finalQueryShouldBeReseted. It is false on default. If it becomes true, the finalQuery will be reseted.
              After adding a new partQuery to the query component, it asks for resetting the savedquery.
              After the reset of the finalQuery, the finalQueryToReset variable will be set to false.
            After the click on the search button in the query component it sends the final query here, because the startSearch function lies here.-->
            <query
              :errMsg="queryError"
              :partQuery="savedQuery"
              @emptyQuery="clearItems = $event"
              @finalQueryAndLimitSend="finalQAndLimit = $event"
              @resetSavedQuery="savedQuery = $event"
            ></query>
            <!--The created query is comming from queryMenu component and is saved in the saveQuery variable by the saveQueryMethod.-->
            <queryMenu @addQuerySent="saveQuery"></queryMenu>
          </div>
        </div>
      </div>
      <div class="downloadDiv" v-if="items.totalHits > 0">
        <button class="download" @click.stop="dialog = true">
          Export to Excel
          <v-icon>mdi-file-excel</v-icon>
        </button>
      </div>
      <div>
        <div id="resultTableDiv" class="card">
          <div class="inputQueryInResult" v-if="items.totalHits > 0">
            <br />You searched for the query :
            <p id="searchedQueryInResult">{{ readyToSearchQuery }}</p>
          </div>
          <hr />
          <v-data-table
            :headers="headers"
            :items="items.hits"
            :loading="progressBar"
            loading-text="Searching for the results, please wait...."
            class="elevation-1"
          >
            <v-progress-linear
              v-show="progressBar"
              slot="progress"
              loading-text="Loading... Please wait"
              indeterminate
            ></v-progress-linear>
            <v-alert slot="no-data" :value="true" class="error1">No data available</v-alert>
            <template v-slot:item.moreInfo="{ item }">
              <router-link
                v-on:click.native="moreInfoNavigation(item.metadata.artifactId, item.metadata.groupId, item.metadata.version)"
                :to="{ path: '/MoreInformation' }"
              >More Information</router-link>
            </template>
          </v-data-table>
        </div>
      </div>
      <v-dialog v-model="dialog" max-width="550">
        <v-card>
          <v-card-title class="headline">Click yes to export all results to excel?</v-card-title>
          <v-card-text>If you click 'yes', an excel file 'results.xlsx' will be downloaded.</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green darken-1" text @click="dialog = false">No</v-btn>
            <v-btn color="green darken-1" text @click="download">Yes</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </v-app>
</template>

<script>
import Query from "./Query.vue";
import QueryMenu from "./QueryMenu.vue";
import { eventBus } from "../../main";
import XLSX from "xlsx";
import flatten from "flat";

export default {
  components: {
    query: Query,
    queryMenu: QueryMenu
  },
  data() {
    return {
      id: "",
      savedQuery: "", //query from queryMenu will be saved here
      readyToSearchQuery: "", //finalQuery from the query component will be saved here
      headers: [
        {
          text: "GroupId",
          align: "center",
          value: "metadata.groupId",
          width: "10%"
        },
        {
          text: "ArtifactId",
          align: "center",
          value: "metadata.artifactId",
          width: "10%"
        },
        {
          text: "Source",
          align: "center",
          value: "metadata.source",
          width: "10%"
        },
        {
          text: "Version",
          align: "center",
          value: "metadata.version",
          width: "10%"
        },
        {
          text: "More Information",
          align: "center",
          value: "moreInfo",
          width: "10%"
        }
      ],
      items: [],
      queryError: "",
      progressBar: false,
      resultLimit: 100,
      clearItems: false,
      flattenItems: [],
      dialog: false,
      finalQAndLimit: {query:"", limit:100},
      reqBody: {
        query: this.readyToSearchQuery,
        limit: this.resultLimit
      }
    };
  },
  watch: {
    //if this variable is changed, the startSearch function will be triggered
    finalQAndLimit: function(newVal, oldVal) {
      if (newVal.query) {
        this.readyToSearchQuery = newVal.query;
        this.resultLimit = newVal.limit;
        if(!((oldVal.query == newVal.query) && (oldVal.limit == newVal.limit))){ //If both values stay the same, no search will be happen
          this.startSearch();
        }
      }
    },
    clearItems: function(newVal) {
      if (newVal) {
        this.clearItems = newVal;
        this.clearItemsOnReqValidator(newVal);
      }
    }
  },
  methods: {
    //This method saves querySent in the variable savedQuery
    saveQuery(querySent) {
      this.savedQuery = querySent;
    },
    startSearch() {
      var vm = this;
      if (this.readyToSearchQuery) {
        this.progressBar = true;
        this.$http
          .post("search/", {query: this.readyToSearchQuery, limit: parseInt(this.resultLimit)})
          .then(response => {
            return response.json();
          })
          .then(
            data => {
              vm.items = data.messages;
              if (data.messages.length != 0) {
                if (data.messages.totalHits != 0) {
                  vm.items = data.messages;
                  vm.headers.splice(4, vm.headers.length - 5);
                  var keys = Object.keys(vm.items.hits[0].metricResults);
                  for (var j = 0; j < keys.length; j++) {
                    var modifiedKey = keys[j]
                      .substring(keys[j].lastIndexOf(".") + 1)
                      .trim();
                    for (var i = 0; i < vm.items.hits.length; i++) {
                      var obj = vm.items.hits[i].metricResults;
                      obj[modifiedKey] = obj[keys[j]];
                      delete obj[keys[j]];
                      vm.items.hits[i].metricResults = obj;
                      vm.flattenItems[i] = flatten(vm.items.hits[i]);
                    }
                    vm.headers.splice(4, 0, {
                      text: modifiedKey,
                      align: "center",
                      value: "metricResults." + modifiedKey,
                      width: "10%"
                    });
                  }
                }
              }
              vm.progressBar = false;
              // vm.readyToSearchQuery = "";
            },
            error => {
              vm.items = [];
              vm.headers.splice(4, vm.headers.length - 4);
              vm.progressBar = false;
              vm.readyToSearchQuery = "";
              vm.queryError = error.body;
            }
          );
      } else if (this.clearItems) {
        vm.items = [];
      }
    },
    clearItemsOnReqValidator(newVal) {
      if (newVal) {
        this.items = [];
      }
    },
    moreInfoNavigation(artifactIdParam, groupIdParam, versionParam) {
      this.id = groupIdParam + ":" + artifactIdParam + ":" + versionParam;
      eventBus.$emit("moreInfoEvent", this.id);
    },
    download() {
      this.dialog = false;
      for (let i = 0; i < this.flattenItems.length; i++) {
        delete this.flattenItems[i].id;
        delete this.flattenItems[i]["metadata.discovered"];
      }
      const data = XLSX.utils.json_to_sheet(this.flattenItems);
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, data, "results");
      XLSX.writeFile(wb, "results.xlsx");
    }
  }
};
</script>

<style>
.container-fluid {
  margin-top: 10px;
}
#searchPart {
  margin: 10px;
  padding: 5px;
  box-shadow: 1px 1px 5px 3px grey;
  background-color: rgb(250, 250, 250);
}
.queryHelpText {
  padding: 0;
  font-variant: small-caps;
}
.col-6 {
  padding-top: 0 !important;
  padding-bottom: 0 !important;
}
#resultTableDiv {
  margin: 10px;
  box-shadow: 1px 1px 5px 3px grey;
}

.inputQueryInResult {
  text-align: center;
}

#searchedQueryInResult {
  font-weight: bold;
}

.v-alert__content {
  background-color: gainsboro;
  padding: 10px;
}

.v-alert__content {
  background-color: gainsboro;
  padding: 10px;
}

.download {
  background-color: white;
  color: black;
}

.downloadDiv {
  text-align: right;
  padding-right: 25px;
}

</style>
