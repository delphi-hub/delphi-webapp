<template>
  <div class="col-5" id="queryCol">
    <textarea
      class="form-control form-control-lg"
      id="queryInput"
      type="text"
      :value="finalQuery"
      @input="addToFinalQuery, setQuery($event.target.value)"
    ></textarea>
    <div class="error" v-if="!$v.finalQuery.required && submitted">Field is required</div>
    <div class="error" v-if="!$v.finalQuery.singleQValidator && finalQuery">Incorrect query format</div>
    <div
      class="error"
      v-if="metricValidator && finalQuery && submitted"
    >Incorrect metric: "{{this.metric}}" entered</div>
    <button
      id="startSearchButton"
      class="btn btn-dark"
      @click="onStartSearch"
      :style="[(finalQuery) ? {'background-color': '#c20202'} : {'background-color':null}]"
    >
      <!-- TODO: The condition has to be changed -->
      <h5 id="searchButtonText">Start Search</h5>
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
      type: String
    },
    finalQueryShouldBeReseted: {
      type: Boolean
    }
  },
  data() {
    return {
      finalQuery: "",
      suggestLib: [">", "<", "=", "&&", "!", "||", "≤", "≥"],
      submitted: false,
      metricValidator: false,
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
      if (newVal) {
        this.finalQuery += newVal;
      }
    },
    finalQueryShouldBeReseted: function() {
      this.finalQuery = "";
      this.finalMetricIsReseted();
    }
  },
  methods: {
    addToFinalQuery(event) {
      this.finalQuery = event.target.value;
    },
    setQuery(value) {
      this.submitted = false;
      // this.queryArea = value;
      this.$v.finalQuery.$touch();
      this.finalQuery = value;
    },
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
    finalMetricIsReseted() {
      this.$emit("confirmFinalQueryReset", false);
    },
    buildCorpus() {
      axios
        .get("https://delphi.cs.uni-paderborn.de/api/features")
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
  height: 160px;
  width: 100%;
  background-color: white;
  resize: none;
}
#startSearchButton {
  width: 90%;
  height: 50px;
  margin-top: 10px;
  font-variant: small-caps;
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