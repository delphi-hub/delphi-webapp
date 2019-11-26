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
