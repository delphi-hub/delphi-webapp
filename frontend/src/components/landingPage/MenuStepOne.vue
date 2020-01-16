<template>
  <div class="col-4" id="steponeCol">
    <div class="card">
      <div class="card-body" v-bind:style="{ 'box-shadow': borderColor}">
        <h6 class="card-title">1. Metric</h6>
        <div v-show="selectedMetric">
          <p>Chosen Metric:</p>
          <div id="chosenMetricDiv">
            <p id="chosenMetricP">[{{ selectedMetric }}]</p>
            <button id="removeMetricButton" class="btn btn-dark" @click="removeMetric">Rechoose</button>
          </div>
        </div>
        <div v-show="!selectedMetric">
          <input
            type="text"
            id="filter"
            name="metric_suggest"
            v-on:keyup="filter1"
            size="15"
            :value="selectedMetric"
          />
          <div>
            <select id="select" size="10" v-model="selectedMetric" @change="sendMetric">
              <template v-if="info">
                <option data-toggle="tooltip" v-bind:title="data.description" id="optionSelect" v-for="data in info" v-bind:key="data.name">
                  {{data.name}}
                </option>
               </template>
            </select>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    metricShouldBeReseted: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      selectedMetric: null,
      borderColor: null,
      info: null,
    };
  },
  watch: {
    metricShouldBeReseted: function(newVal) {
      if (newVal) {
        this.selectedMetric = null;
        this.metricIsReseted(newVal);
      }
    },
    selectedMetric: function(newVal) {
      if (newVal) {
        this.borderColor = "1px 1px 5px 3px green";
      } else {
        this.borderColor = null;
      }
    }
  },
  methods: {
    sendMetric() {
      this.$emit("metricSent", this.selectedMetric);
    },
    metricIsReseted() {
      this.$emit("confirmMetricReset", false);
    },
    removeMetric() {
      this.selectedMetric = null;
      this.$emit("metricSent", this.selectedMetric);
      document.getElementById("filter").value = ""; //without these, after removing metric the list would still be filtered
      this.filter1(); //even though the text field is empty
    },
    filter1() {
      var keyword = document.getElementById("filter").value;
      var select = document.getElementById("select");
      for (var i = 0; i < select.length; i++) {
        var txt = select.options[i].text;
        var txt2 = keyword.toLowerCase();
        if (txt.toLowerCase().includes(txt2)) {
          select.options[i].style.display = "list-item";
        } else {
          select.options[i].style.display = "none";
        }
      }
    }
  },
  mounted() {
    this.$http.get("features").then(response => {
      this.info = response.data.sort((a, b) => (a.name > b.name) ? 1 : -1);
      return response.json();
    });
    error => {
      alert("Invalid results!", error.messages);
    };
  }
};
</script>

<style>
#steponeCol {
  padding: 0 5px 0 10px;
}
.card-body {
  height: 230px;
  overflow: hidden;
}
#chosenMetricDiv {
  text-align: center;
  border-radius: 5px;
  padding: 5px;
  background-color: rgba(176, 240, 176, 0.753);
}
#chosenMetricP {
  margin: 0px 0px 5px 0px;
}
#removeMetricButton {
  background-color: rgb(192, 197, 192);
  font-variant: small-caps;
  color: black;
  width: 100%;
}
#removeMetricButton:hover {
  box-shadow: 1px 1px 5px 3px rgb(190, 32, 32);
  color: red;
  border-radius: 3px;
}
#filter {
  width: 100%;
  border-style: solid;
  border-width: 0.01em 0.01em 0 0.01em;
}
#select {
  width: 100%;
  height: 120px;
  overflow: auto;
  border-style: solid;
}
#optionSelect:hover {
  background-color: rgba(176, 240, 176, 0.753);
}
</style>