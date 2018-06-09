document.getElementById("runQuery").onclick = runQuery;

var input = document.getElementById("queryInput");

input.addEventListener("keyup", function(event) {
    event.preventDefault();
    if (event.keyCode === 13) {
        document.getElementById("runQuery").click();
    }
});

function runQuery() {
  if(input.value && input.value !== "")
  {
      window.location.href = '/?query='+input.value;
  }
}