<template>
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
            :partQuery="savedQuery"
            :finalQueryShouldBeReseted="finalQueryToReset"
            @resetSavedQuery="savedQuery = $event"
            @confirmFinalQueryReset="finalQueryToReset = $event"
            @finalQuerySend="readyToSearchQuery = $event"
          ></query>
          <!--The created query is comming from queryMenu component and is saved in the saveQuery variable by the saveQueryMethod.-->
          <queryMenu @addQuerySent="saveQueryMethod"></queryMenu>
        </div>
      </div>
    </div>
    <div>
      <v-app>
        <div id="resultTableDiv" class="card">
          <div
            class="inputQueryInResult"
            v-if="readyToSearchQuery"
          >you searched for the query : {{ readyToSearchQuery }}</div>
          <hr />
          <v-data-table v-bind:headers="headers" :items="items" class="elevation-1">
            <template v-slot:item.moreInfo="{ item }">
              <!-- <a v-on:click="moreInfo" :href="moreInfoPage">More Information</a> -->
              <router-link to="/moreInformation">More Information</router-link>
            </template>
          </v-data-table>
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
      moreInfoPage: "./MoreInformation.vue",
      savedQuery: "", //query from queryMenu will be saved here
      readyToSearchQuery: "", //finalQuery from the query component will be saved here
      finalQueryToReset: false, //if a reset is neccessary, this has to be set to true
      headers: [
        { text: "ArtifactId", align: "center", value: "metadata.artifactId" },
        { text: "GroupId", align: "center", value: "metadata.groupId" },
        { text: "Source", align: "center", value: "metadata.source" },
        { text: "Version", align: "center", value: "metadata.version" },
        { text: "More Information", align: "center", value: "moreInfo" }
      ],
      items: []
    };
  },
  watch: {
    readyToSearchQuery: function(newVal) {
      //if this variable is changed, the startSearch function will be triggered
      if (newVal) {
        this.startSearch();
      }
    }
  },
  methods: {
    saveQueryMethod(querySent) {
      //This method saves querySent in the variable savedQuery
      this.savedQuery = querySent;
    },
    startSearch() {
      var vm = this;
      this.$http
        .get("search/" + vm.readyToSearchQuery)
        .then(response => {
          return response.json();
        })
        .then(data => {
          vm.items = data.messages;
          //	this.readyToSearchQuery = "";
          //	this.finalQueryToReset = true;
        }),
        error => {
          alert("Invalid query!", error.messages);
        };
    },
    moreInfo() {}
  }
};
</script>

<style>
.container-fluid {
  margin-top: 10px;
}

#searchPart {
  margin: 20px;
  padding: 0;
  height: 300px;
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
  margin: 30px;
  box-shadow: 1px 1px 5px 3px grey;
}

.inputQueryInResult {
  text-align: center;
  margin-top: 10px;
  margin-bottom: 10px;
  font-variant: small-caps;
  font-size: 1.2em;
}
</style>