<template>
  <Div>
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
          <query :queryToSearch="savedQuery"></query>
          <queryMenu @addQuerySent="saveQuery"></queryMenu>
        </div>
      </div>
      <hr />
      <button id="startSearchButton" class="btn btn-dark" @click="test">
        <h5 id="searchButtonText">Start Search</h5>
      </button>
	  <button id="startSearchButton" class="btn btn-dark" @click="test1">
        <h5 id="searchButtonText">Start Search Dummy</h5>
      </button>
    </div>
  </Div>
</template>

<script>
import Query from "./Query.vue";
import QueryMenu from "./QueryMenu.vue";

export default {
  components: {
    query: Query,
    queryMenu: QueryMenu
  },
  methods: {
    saveQuery(querySent) {
      this.savedQuery = querySent;
    },
    test() {
      this.$http
        .get("search/[using KeyStore]=25")
        .then(response => {
          return response.json();
        })
        .then(data => {
          console.log(data);
        }),
        error => {
          console.log(error);
        };
	},
	test1() {
      this.$http
        .get("dummy/'subbu'")
        .then(response => {
          return response.json();
        })
        .then(data => {
          console.log(data);
        }),
        error => {
          console.log(error);
        };
    }
  },
  data() {
    return {
      savedQuery: ""
    };
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
  background-color: #c20202;
  font-variant: small-caps;
}

#startSearchButton:hover {
  background-color: #c30303;
  box-shadow: 1px 1px 5px 3px grey;
  border-radius: 3px;
}
</style>