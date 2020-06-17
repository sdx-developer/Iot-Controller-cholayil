
!function (t) {
	t.fn.createTable = function (e, a) {
		var r, o = this, //O is the DIV element
		n = t.extend({}, t.fn.createTable.defaults, a);
		if (void 0 !== o[0].className) {
			var i = o[0].className.split(" ");
			r = "." + i.join(".") + " "
		} else
			void 0 !== o[0].id && (r = "#" + o[0].id + " ");
		var l = '<table id="'+n.tblRepId+'" data="'+n.dataAttr+'" class="'+n.tblClass+'" style="'+n.styler+'">';
		//+'" class="'+n.tblClass+'" style="'+n.styler+'" >';
		
		
		return l += '<thead><th>S.No</th>',
		l += t.fn.createTable.parseTableData(e, !0, n),
		l += "</thead>",
		l += "<tbody>",
		l += t.fn.createTable.parseTableData(e, !1, n),
		l += "</tbody>",
		l += "</table>",
		o.html(l),
		function () {}
		( )
	},
	t.fn.createTable.parseTableData = function (e, a, n) {	
	
		for (var r = "", o = 0; o < e.length; o++)
				a === !1 && 
			(
				
				//r += '<tr>'+'<td role="RAMK">' + (o + 1) + "</td>"
				r += "<tr>",	
				r += n.idColumnType != null ? t.fn.buildCustomColumns(n, o) : "<td role='ORDD'>"+ (o + 1),
				r += "</td>"
				
			), 
			t.each(e[o], function (n, filldata, index) {
				a === !0 ? o === t.fn.createTable.getHighestColumnCount(e).when && (r += "<th>" + t.fn.humanize(n) + "</th>") : 
				a === !1 && (r += "<td>" + filldata + "</td>")
			}), a === !1 && (r += "</tr>");
		return r
	},
	
	t.fn.buildCustomColumns = function (n, o) { 
		var retTbl = '<td><div class="jui jennifer item">';
		retTbl += '<input type="checkbox" style="display: none;" class="icon-checkbox">';
		retTbl += '<i class="icon-checkbox2"></i>';
		//retTbl += o + 1;
		retTbl += '</div>';

		return retTbl;
	},
	
	t.fn.createTable.getHighestColumnCount = function (e) {
		for (var a = 0, r = 0, o = {
				max: 0,
				when: 0
			}, n = 0; n < e.length; n++)
			a = t.fn.getObjectLength(e[n]), a >= r && (r = a, o.max = a, o.when = n);
		return o
	},
	t.fn.getObjectLength = function (t) {
		var e = 0;
		for (var a in t)
			t.hasOwnProperty(a) && ++e;
		return e
	},
	t.fn.humanize = function (t) {
		var e = t.split("_");
		for (i = 0; i < e.length; i++)
			e[i] = e[i].charAt(0).toUpperCase() + e[i].slice(1);
		return e.join(" ")
	},
	t.fn.createTable.defaults = {
		tblRepId : "j2lId",
		dataAttr : "tempData",
		styler	 : "width:100%",
		tblClass : "table classic",
		idColumnNumber : -1,
		idColumnType : null,
		idColumnData : null
	}
}
(jQuery);