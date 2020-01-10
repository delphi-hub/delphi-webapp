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
    <div class="error" v-if="!$v.finalQuery.required && submitted">Field is required</div>
    <div class="error" v-if="!$v.finalQuery.singleQValidator && finalQuery">Incorrect query format</div>
    <!-- <div
      class="error"
      v-if="metricValidator && finalQuery && submitted"
    >Incorrect metric: "{{this.metric}}" entered</div> -->
    <!--This button is grey when the input is not a valid query and otherwise red-->
    <button
      id="startSearchButton"
      class="btn btn-dark"
      @click="onStartSearch"
      :style="[($v.finalQuery.singleQValidator && finalQuery) ? {'background-color': '#c20202'} : {'background-color':null}]"
    >
      <h5 id="searchButtonText">Search</h5>
    </button>
  </div>
</template>

<script>
import axios from "axios";
import { required } from "vuelidate/lib/validators";
const singleQValidator = value => {
  if (
    /^(?:[(]+)(?:[[]+)\w+(?:[^\]]+)(?:.+)[0-9](?:[)]+)$/.test(value) ||
    /^(?:[[]+)\w+(?:[^\]]+)(?:.+)[0-9]/.test(value)
  ) {
    return true;
  } else {
    return false;
  }
};
export default {
  props: {
    partQuery: {
      //query from SearchPart component
      type: String
    },
    finalQueryShouldBeReseted: {
      //boolean from SearchPart component for resetting the final query
      type: Boolean
    }
  },
  data() {
    return {
      finalQuery: "",
      suggestLib: [">", "<", "=", "&&", "!", "||", "≤", "≥"],
      submitted: false,
      metricValidator: true,
      metric: ""
    };
  },
  validations: {
    finalQuery: {
      required,
      singleQValidator
    }
  },
  watch: {
    partQuery: function(newVal) {
      //whenever a new query is comming from the queryMenu, it will be added to the finalQuery
      if (newVal) {
        this.finalQuery += newVal;
        this.$emit("resetSavedQuery", ""); //without this line, if the user would choose twice the same query, nothing would happen
      }
    },
    finalQueryShouldBeReseted: function() {
      //if searchPart asks for a reset, then this code here will be triggered and it calls the method finalMetricIsReseted to tell searchPart
      this.finalQuery = "";
      this.finalMetricIsReseted();
    }
  },
  methods: {
    //the value of the current final query becomes the events value.
    //This function i needed if the user types in the textfield.
    addToFinalQuery(event) {
      this.finalQuery = event.target.value;
    },
    setQuery(value) {
      this.submitted = false;
      // this.queryArea = value;
      this.$v.finalQuery.$touch();
      this.finalQuery = value;
    },
    //if a valid query is given, then this function sends the final query to the searchPart component to initiate a search
    onStartSearch() {
      this.metric = this.finalQuery.substring(
        this.finalQuery.lastIndexOf("[") + 1,
        this.finalQuery.lastIndexOf("]")
      );
      if (this.finalQuery && this.suggestLib.indexOf(this.metric) > -1) {
        this.$emit("finalQuerySend", this.finalQuery);
        this.submitted = true;
      } else {
        this.submitted = true;
        this.metricValidator = true;
      }
    },
    //after a reset this sends a confirmation to searchPart
    finalMetricIsReseted() {
      this.$emit("confirmFinalQueryReset", false);
    },
    buildCorpus() {
      axios
        .get("https://delphi.cs.uni-paderborn.de/api-legacy/features")
        .then(
          response => (this.suggestLib = this.suggestLib.concat(response.data))
        );
    }
  },
  mounted() {
    this.buildCorpus();
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
  text-align: center;
}

#queryInput {
  height: 140px;
  width: 100%;
  background-color: white;
  resize: none;
}
#startSearchButton {
  width: 50%;
  margin-top: 10px;
  text-align: center;
  padding: 0 !important;
}

#searchButtonText {
  font-variant: small-caps;
  font-size: 2em;
}

#startSearchButton:hover {
  box-shadow: 1px 1px 5px 3px grey;
  border-radius: 3px;
}

.error {
  text-align: left;
  padding-left: 5px;
  color: red;
}
</style>