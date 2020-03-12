<template>
	<v-row>
		<v-col cols="12" class="pb-1">
			<v-row dense>
				<v-tooltip top color="#299e3c">
					<template v-slot:activator="{ on }">
						<v-btn
							height="25"
							width="25"
							fab
							outlined
							v-on="on"
							@click="sendToStorage"
							color="#299e3c"
							class="ml-4">
							<v-icon small>mdi-content-save</v-icon>
						</v-btn>
					</template>
					<span>Save Query in Query Storage</span>
				</v-tooltip>
				<v-col>
					<div class="autocomplete">
						<v-textarea 
							:id="id" 
							outlined
							rows="1"
							hide-details
							auto-grow
							ref="textareaQuery"
							class="autocomplete-input" 
							@input="addToFinalQuery($event), setQuery($event)"
							@keydown.enter.prevent
							@focusout="focusout"
							@focus="focus" 
							@keydown.13="chooseMetric"
							@keydown.tab="chooseMetric"
							@keydown.40="cursorDownAction" 
							@keydown.38="cursorUpAction" 
							v-model="finalQuery" >
						</v-textarea> 
						<ul 
							:class="{
								'autocomplete-list': true,
								[id+'-list']: true
							}" v-if="metricSorted.length > 0">
							<li 
								:class="{active: metricPosition === index}"
								v-for="(result, index) in metricSorted" 
								@click="selectMetric(index), chooseMetric()" 
								v-html="applyHighlight(result)" 
								v-bind:key="index">
							</li>
						</ul>
					</div>
				</v-col>
					<v-btn
					height="50"
					id="startSearchButton"
					@click="onStartSearch"
					:loading="isLoading"
					:disabled="isLoading || !finalQuery || brokeRule"
					color="#db2909"
					class="mr-4 ml-1 mt-1 white--text">
					<v-icon large>mdi-magnify</v-icon>
				</v-btn>		
			</v-row>	
		</v-col>
		<v-col cols="12" class="py-1">
			<v-alert
				class="my-0 mx-6 py-1"
				dense
				outlined
				type="error"
				v-if="!$v.finalQuery.required && submitted">
				Please enter a valid query or use the query creation menu to add a query.
			</v-alert>
			<v-alert
				class="my-0 mx-6 py-1"
				dense
				outlined
				type="error"
				v-if="$v.finalQuery.required && !$v.finalQuery.metricValidator && !$v.finalQuery.queryErrorValidator && submitted">
						{{this.queryError}}
			</v-alert>
		</v-col>
		<v-col cols="6" class="py-0 ml-7">
			<v-text-field
				style="max-width:160px"
				dense
				filled
				hint="Amount of Result Entries"
				v-model="currentLimit"
				:rules="[rules.inlimit]"
				persistent-hint>
			</v-text-field>
		</v-col>	
	</v-row>	
</template>

<script>
/* (function(){function e(b,e,f){if(!h)throw Error("textarea-caret-position#getCaretCoordinates should only be called in a browser");if(f==f&&f.debug||!1){var a=document.querySelector("#input-textarea-caret-position-mirror-div");a&&a.parentNode.removeChild(a)}a=document.createElement("div");a.id="input-textarea-caret-position-mirror-div";document.body.appendChild(a);var c=a.style,d=window.getComputedStyle?window.getComputedStyle(b):b.currentStyle,k="INPUT"===b.nodeName;c.whiteSpace="pre-wrap";k||(c.wordWrap=
"break-word");c.position="absolute";f||(c.visibility="hidden");l.forEach(function(a){k&&"lineHeight"===a?c.lineHeight=d.height:c[a]=d[a]});m?b.scrollHeight>parseInt(d.height)&&(c.overflowY="scroll"):c.overflow="hidden";a.textContent=b.value.substring(0,e);k&&(a.textContent=a.textContent.replace(/\s/g,"\u00a0"));var g=document.createElement("span");g.textContent=b.value.substring(e)||".";a.appendChild(g);b={top:g.offsetTop+parseInt(d.borderTopWidth),left:g.offsetLeft+parseInt(d.borderLeftWidth),height:parseInt(d.lineHeight)};
f?g.style.backgroundColor="#aaa":document.body.removeChild(a);return b}var l="direction boxSizing width height overflowX overflowY borderTopWidth borderRightWidth borderBottomWidth borderLeftWidth borderStyle paddingTop paddingRight paddingBottom paddingLeft fontStyle fontVariant fontWeight fontStretch fontSize fontSizeAdjust lineHeight fontFamily textAlign textTransform textIndent textDecoration letterSpacing wordSpacing tabSize MozTabSize".split(" "),h="undefined"!==typeof window,m=h&&null!=window.mozInnerScreenX;
"undefined"!=typeof module&&"undefined"!=typeof module.exports?module.exports=e:h&&(window.getCaretCoordinates=e)})(); */
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
			},
			errCol: {
				type: Number
			},
			isLoading: {
				type: Boolean
			}
		},
		data() {
			return {
				corpusList: [],
                autocompleteModel: null,
                info: null,
                finalQuery: '',
                id: 'input-' + parseInt(Math.random() * 1000),
				metricSorted: [],
				metricPosition: 0,
				selectedSuggestMetric: false,
				inputPosition: 0,
				submitted: false,
				queryError: "",
				queryErrorCol: 0,
				emptyQuery: "",
				metric: "",
				metrics: [],
				brokeRule: false,
				currentLimit: 100,
				rules: {
					inlimit: value => {
						const pattern = /^([1-9][0-9]{0,3}|10000)$/;
						if(pattern.test(value)){
							this.brokeRule = false;
						}
						else {
							this.brokeRule = true;
						}
						return pattern.test(value) || 'Only Numbers between 1 and 10000'
					}
				},
				loader: null,
			};
		},
		validations: {
			finalQuery: {
				required,
				queryErrorValidator
			}
		},
		computed: {
		corpus() {
			if (typeof this.items !== "undefined" && this.items.length > 0) {
				return this.items;
			} else {

                return this.corpusList;
			}
		},
		currentInput() {
			return this.finalQuery.replace(/(\r\n|\n|\r)/gm, ' ').split(' ')[this.inputPosition];
		},
		inputSplitted() {
			return this.finalQuery.replace(/(\r\n|\n|\r)/gm, ' ').split(" ");
        }
	},
		watch: {
			//whenever a new query is comming from the queryMenu, it will be added to the finalQuery
			partQuery: function(newVal) {
				if (newVal) {
					if(this.finalQuery == null){
						this.finalQuery = "";
					}
					this.finalQuery = newVal;
					this.$emit("resetSavedQuery", ""); //without this line, if the user would choose twice the same query, nothing would happen
				}
			},
			errMsg: function(newVal) {
				if (newVal) {
					this.queryError = newVal;
				}
			},
			errCol: function(newVal) {
				if (newVal > 0) {
					this.queryErrorCol = newVal;
					this.selectText();
				}
			},
			loader () {
				const l = this.loader
				this[l] = !this[l]
				if(!this.isLoading){
					this.loader = null
				}       
			},
			finalQuery() {
			this.focus();
			this.metricPosition = 0;
			this.inputPosition = this.inputSplitted.length - 1;
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
				this.finalQuery = event;
			},
			//if a valid query is given, then this function sends the final query to the searchPart component to initiate a search
			onStartSearch() {
				this.loader = this.isLoading;
				if (this.finalQuery) {
					this.submitted = true;
					this.emptyQuery = false;
					this.$emit("emptyQuery", this.emptyQuery);
					var qAndL = {query: this.finalQuery, limit: this.currentLimit};
					this.$emit("finalQueryAndLimitSend", qAndL);
				} else {
					this.emptyQuery = true;
					this.$emit("emptyQuery", this.emptyQuery);
					this.submitted = true;
				}
			},
			sendToStorage() {
				this.$emit("sendQueryToStorage", this.finalQuery);
			},
			setQuery(value) {
				this.submitted = false;
				this.queryError = "";
				this.$v.finalQuery.$touch();
				this.finalQuery = value;
			},
			selectText() {
				let textArea = this.$refs.textareaQuery.$el.querySelector('textarea');
				textArea.focus();
				textArea.setSelectionRange(this.queryErrorCol-1, this.queryErrorCol);
				this.$emit("resetErrorColumn", 0);
			},			
		applyHighlight(word) {
               let currentNewWord=this.currentInput.replace(/[-[\]{}()*+?.,\\^$|#\\s]/g, '\\$&');
            const regex = new RegExp("(" + currentNewWord + ")", "g");

            //word=escapeRegExp(word);
			return word.replace(regex, '<mark>$1</mark>');
		},
		setMetric(word) {
			let currentInputs = this.finalQuery.replace(/(\r\n|\n|\r)/gm, '__br__ ').split(' ');
			currentInputs[this.inputPosition] = currentInputs[this.inputPosition].replace(this.currentInput, word + ' ');
			this.inputPosition += 1;
			this.finalQuery = currentInputs.join(' ').replace(/__br__\s/g, '\n');
		},
		cursorDownAction() {
			if (this.metricPosition < this.metricSorted.length - 1) {
				this.metricPosition++;
			}
		},
		cursorUpAction() {
			if (this.metricPosition !== -1) {
				this.metricPosition--;
			}
		},
		selectMetric(index) {
			this.metricPosition = index;
			this.chooseMetric();
		},
		chooseMetric(e) {
			this.selectedSuggestMetric = true;

			if (this.metricPosition !== -1 && this.metricSorted.length > 0) {
				if (e) {
					e.preventDefault();
				}
				this.setMetric(this.metricSorted[this.metricPosition]);
				this.metricPosition = -1;
			}
		},
		focusout() {
			setTimeout(() => {
				if (!this.selectedSuggestMetric) {
					this.metricSorted = [];
					this.metricPosition = -1;
				}
				this.selectedSuggestMetric = false;
			}, 100);
		},
		focus() {
			this.metricSorted = [];
			if (this.currentInput !== "") {
                this.metricSorted = this.corpus.filter(
					el => el.indexOf(this.currentInput) >= 0
				);
			}
			if (
				this.metricSorted.length === 1 &&
				this.currentInput === this.metricSorted[0]
			) {
				this.metricSorted = [];
			}
		},
		},
		mounted() {
            /* const _self = this;
            document.querySelector('#' + this.id)
            .addEventListener('input', function() {
            	const caret = getCaretCoordinates(this, this.selectionEnd);
            //console.log(caret.top);
            //console.log(caret.left);

            if (_self.metricSorted.length > 0) {
                const element = document.querySelectorAll('.' + _self.id + '-list');

            if (element[0]) {
                element[0].style.top = caret.top + 40 + 'px';
                element[0].style.left = caret.left + 'px';
            }
            }
      }); */
            this.$http.get("features").then(response => {
            this.info = response.data.sort((a, b) => (a.name > b.name) ? 1 : -1);
            for (let index = 0; index < this.info.length; index++) {
                if(!(this.info[index].name.includes(" ")))
                 this.corpusList = this.corpusList.concat("["+this.info[index].name+"]");    
            }
            return response.json();
            });
            error => {
            alert("Invalid results!", error.messages);
        };
        }

	};
</script>

<style>
.autocomplete {
  width: 100%;
}

.autocomplete-list {
  position: absolute;
  z-index: 2;
  overflow: auto;
  min-width: 250px;
  max-height: 150px;
  margin: 0;
  margin-top: 5px;
  padding: 0;
  border: 1px solid #eee;
  list-style: none;
  border-radius: 4px;
  background-color: #fff;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.05);
}
.autocomplete-list li {
  margin: 0;
  padding: 8px 15px;
  border-bottom: 1px solid #f5f5f5;
}
.autocomplete-list li:last-child {
  border-bottom: 0;
}
.autocomplete-list li:hover, .autocomplete-list li.active {
  background-color: #f5f5f5;
}

</style>