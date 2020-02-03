<template>
  <div class="col-5" id="queryCol">
    <!--This input is equals to the finalQuery variable. 
    After an input, the finalQuery variable will be set to the new input inside this textarea-->
    <textarea
      class="form-control form-control-lg"
      id="queryInput"
      type="text"
      :value="finalQuery"
      @input="addToFinalQuery, setQuery($event.target.value)"
    ></textarea>
    <div id="errorDiv">
      <div
        class="error"
        v-if="!$v.finalQuery.required && submitted"
      >Please enter a valid query or use the query builder to add a query.</div>
      <div
        class="error"
        v-if="$v.finalQuery.required && !$v.finalQuery.metricValidator && !$v.finalQuery.queryErrorValidator && submitted"
      >{{this.queryError}}</div>
    </div>
    <!--This button is grey when the input is not a valid query and otherwise red -->
    <button
      id="startSearchButton"
      class="btn btn-dark"
      @click="onStartSearch"
      :disabled="!(finalQuery)"
    >
      <!-- TODO: The condition has to be changed -->
      <h5 id="searchButtonText">Search</h5>
    </button>
  </div>
</template>

<script>
import { required } from "vuelidate/lib/validators";
import { eventBus } from "../../main";

const queryErrorValidator = (value, vm) => {
  if (vm.queryError != "") {
    return false;
  } else {
    return true;
  }
};

export default {
  props: {
    partQuery: {
      //query from SearchPart component
      type: String
    },
    errMsg: {
      type: String
    }
  },
  data() {
    return {
      finalQuery: "",
      submitted: false,
      queryError: "",
      emptyQuery: "",
      metric: "",
      metrics: []
    };
  },
  validations: {
    finalQuery: {
      required,
      queryErrorValidator
    }
  },
  watch: {
    //whenever a new query is comming from the queryMenu, it will be added to the finalQuery
    partQuery: function(newVal) {
      if (newVal) {
        this.finalQuery += newVal;
        this.$emit("resetSavedQuery", ""); //without this line, if the user would choose twice the same query, nothing would happen
      }
    },
    errMsg: function(newVal) {
      if (newVal) {
        this.queryError = newVal;
      }
    }
  },
  created() {
    eventBus.$on("metricList", data => {
      // this.metrics = data;
      for (let i = 0; i < data.length; i++) {
        this.metrics.push(data[i].name);
      }
    });
  },
  methods: {
    //the value of the current final query becomes the events value.
    //This function i needed if the user types in the textfield.
    addToFinalQuery(event) {
      this.finalQuery = event.target.value;
    },
    //if a valid query is given, then this function sends the final query to the searchPart component to initiate a search
    onStartSearch() {
      if (this.finalQuery) {
        this.submitted = true;
        this.emptyQuery = false;
        this.$emit("emptyQuery", this.emptyQuery);
        this.$emit("finalQuerySend", this.finalQuery);
      } else {
        this.emptyQuery = true;
        this.$emit("emptyQuery", this.emptyQuery);
        this.submitted = true;
      }
    },
    //after a reset this sends a confirmation to searchPart
    finalMetricIsReseted() {
      this.$emit("confirmFinalQueryReset", false);
    },
    setQuery(value) {
      this.submitted = false;
      this.queryError = "";
      this.$v.finalQuery.$touch();
      this.finalQuery = value;
    }
  }
};
</script>


<style>
.queryHeading {
  text-align: center;
  color: white;
  background-color: grey;
  margin-bottom: 3px;
  border-radius: 3px;
}
#queryCol {
  background-color: rgb(235, 235, 235);
  border-radius: 10px 0 0 10px;
  padding: 12px 0 12px 12px;
}
#queryInput {
  height: 110px;
  width: 100%;
  background-color: white;
  resize: none;
}
#startSearchButton {
  width: 30%;
  margin-top: 10px;
  text-align: center;
  padding: 0 !important;
  background-color: #c20202;
}
#startSearchButton:disabled {
  background-color: grey;
}
#startSearchButton:hover:not([disabled]) {
  box-shadow: 1px 1px 5px 3px grey;
  border-radius: 3px;
}
#searchButtonText {
  font-variant: small-caps;
  font-size: 1.5em;
}
.error {
  text-align: left;
  padding-left: 5px;
  color: red;
}
#errorDiv {
  min-height: 65px;
}
</style>