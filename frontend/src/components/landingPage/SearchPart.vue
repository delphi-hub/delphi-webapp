<template>
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
      <div class="row">
        <query
          :partQuery="savedQuery"
          @finalQuerySend="readyToSearchQuery = $event"
          @confirmFinalQueryReset="finalQueryToReset = $event"
          :finalQueryShouldBeReseted="finalQueryToReset"
        ></query>
        <queryMenu @addQuerySent="saveQuery"></queryMenu>
      </div>
    </div>
    <hr />
    <button
      id="startSearchButton"
      class="btn btn-dark"
      @click="startSearch"
      :style="[(readyToSearchQuery) ? {'background-color': '#c20202'} : {'background-color':null}]"
    >
      <!-- TODO: The condition has to be changed -->
      <h5 id="searchButtonText">Start Search</h5>
    </button>
    <div>
      <v-app>
        <div>
          <v-data-table v-bind:headers="headers" :items="items" class="elevation-1"></v-data-table>
        </div>
      </v-app>
    </div>
  </div>
</template>

<script>
import Query from "./Query.vue";
import QueryMenu from "./QueryMenu.vue";

export default {
  components: {
    query: Query,
    queryMenu: QueryMenu
  },
  data() {
    return {
      savedQuery: "",
      readyToSearchQuery: "",
      finalQueryToReset: false,
      headers: [
        { text: "artifactId", align: "left", value: "metadata.artifactId" },
        { text: "groupId", value: "metadata.groupId" },
        { text: "source", value: "metadata.source" },
        { text: "version", value: "metadata.version" }
      ],
      items: [
        {
          id: "uuu",
          metadata: {
            artifactId: "ererg",
            discovered: "2018-10-29T02:48:24.763Z",
            groupId: "wrwer",
            source: "ewrwer",
            version: "2.7",
            metricResults: {
              "using KeyStore": 25
            }
          }
        },
        {
          id: "uuu1",
          metadata: {
            artifactId: "ererg1",
            discovered: "2018-10-29T02:48:24.763Z",
            groupId: "wrwer",
            source: "ewrwer",
            version: "2.7",
            metricResults: {
              "using KeyStore": 25
            }
          }
        },
        {
          id: "uuu2",
          metadata: {
            artifactId: "ererg2",
            discovered: "2018-10-29T02:48:24.763Z",
            groupId: "wrwer",
            source: "ewrwer",
            version: "2.7",
            metricResults: {
              "using KeyStore": 25
            }
          }
        },
        {
          id: "uuu3",
          metadata: {
            artifactId: "ererg3",
            discovered: "2018-10-29T02:48:24.763Z",
            groupId: "wrwer",
            source: "ewrwer",
            version: "2.7",
            metricResults: {
              "using KeyStore": 25
            }
          }
        },
        {
          id: "uuu4",
          metadata: {
            artifactId: "ererg4",
            discovered: "2018-10-29T02:48:24.763Z",
            groupId: "wrwer",
            source: "ewrwer",
            version: "2.7",
            metricResults: {
              "using KeyStore": 25
            }
          }
        },
        {
          id: "uuu5",
          metadata: {
            artifactId: "ererg5",
            discovered: "2018-10-29T02:48:24.763Z",
            groupId: "wrwer",
            source: "ewrwer",
            version: "2.7",
            metricResults: {
              "using KeyStore": 25
            }
          }
        },
        {
          id: "uuu6",
          metadata: {
            artifactId: "ererg6",
            discovered: "2018-10-29T02:48:24.763Z",
            groupId: "wrwer",
            source: "ewrwer",
            version: "2.7",
            metricResults: {
              "using KeyStore": 25
            }
          }
        }
      ]
    };
  },
  methods: {
    saveQuery(querySent) {
      this.savedQuery = querySent;
    },
    startSearch() {
      if (this.readyToSearchQuery) {
        var vm = this;
        console.log(this.readyToSearchQuery);
        this.$http
          .get("search/" + vm.readyToSearchQuery)
          .then(response => {
            return response.json();
          })
          .then(data => {
            console.log("search/" + vm.readyToSearchQuery, data);
            vm.items = JSON.parse(
              data.messages.substring(8, data.messages.length - 1)
            );
            console.log(vm.items);
            this.readyToSearchQuery = "";
            this.finalQueryToReset = true;
          }),
          error => {
            console.log("fail", error);
          };
      }
    }
  }
};
</script>

<style>
.container-fluid {
  margin-top: 10px;
}

#searchPart {
  margin: 40px;
  padding: 10px;
  height: 480px;
  box-shadow: 1px 1px 5px 3px grey;
  background-color: rgb(250, 250, 250);
}

.queryHelpText {
  font-variant: small-caps;
}

#startSearchButton {
  width: 30%;
  height: 45px;
  margin-left: 15px;
  /*  background-color:#c20202; */
  font-variant: small-caps;
}

#startSearchButton:hover {
  /*	background-color: #c30303; */
  box-shadow: 1px 1px 5px 3px grey;
  border-radius: 3px;
}
</style>