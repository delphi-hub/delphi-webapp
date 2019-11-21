function filter1() {
    var keyword = document.getElementById("filter").value;
    var select = document.getElementById("select");
    for (var i = 0; i < select.length; i++) {
        var txt = select.options[i].text;
		var txt2 = keyword.toLowerCase();
        if (txt.toLowerCase().includes(txt2)) {
			select.options[i].style.display = 'list-item';           
        } else {
            select.options[i].style.display = 'none';
        }
    }
}
function populateSelect() {
    var array = ['AWT','Augmented Type','Box','Canopy','Outline','Overrider','Pool','Process','Pseudo Class','Record','SWT','Sampler','Self-recursive Data Structure','Sink']
	var ele = document.getElementById("select");
	for (var i = 0; i < array.length; i++) {
		var option = document.createElement("option");
				option.text = array[i];
				ele.add(option);
        }
}