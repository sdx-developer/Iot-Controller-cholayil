
var retrievedData = null;
var dataPreviewed = false;
	
var groupColumn 	= 2;
var displayCount 	= 10000;
var groupByInfo 	= null;
var dataTabler    	= null;
var dataCollected 	= null;
var groupCounter	= 0;

function buildListItemForGroupBy(bttnData, index, value){
	var outText = "";
	outText = outText + '<li data="'+bttnData+'" value="'+bttnData+'_Indexer_'+(index+1)+'">';
	outText = outText + '<img src="assets/imgs/icons/ico-column.png" width="16" height="16">';
	outText = outText + '<span style="padding-left:5px;line-height:28px;">'+value+'</span>';
	outText = outText + '</li>';
	return outText;
}

function updateGroupByData(data){
	
	var comboArray = data.value.split("_Indexer_");
	if (comboArray && comboArray.length == 2){
		groupColumn = parseInt(comboArray[1]);
		groupByInfo = data.text.charAt(0).toUpperCase() + data.text.slice(1);
		buildDataTabler(null, comboArray[0]);
	}
}


function buildDataTabler(titlesArray, bttnData){
	groupCounter = 0;
	if (titlesArray){
		var colsUL = $($("ul[data='"+bttnData+"'][id='grouperUL']")[0]);
		$.each(titlesArray, function( index, value ) {
			colsUL.append(buildListItemForGroupBy(bttnData, index, value));
			if (index == (groupColumn-1) ){
				groupByInfo = value.charAt(0).toUpperCase() + value.slice(1);
			}
		});
	}
	var dtInst = $($("table[data='"+bttnData+"'][id='sampleParsedData']")[0]);
	if ( $.fn.DataTable.isDataTable(dtInst)) {
		dtInst.DataTable().draw(groupColumn);
		alert("Group COUNTER "+groupCounter);
	  return;
	}
	
	dataTabler = dtInst.DataTable({
			responsive: true,
			"columnDefs": [ { "visible": true, "targets": groupColumn } ],
			"order": [[ groupColumn, 'asc' ]],
			"displayLength": displayCount,
			"drawCallback": function ( settings ) {
				groupCounter = 0;
				var api  = this.api();
				var rows = api.rows( {page:'current'} ).nodes();
				var last = null;
				api.column(groupColumn, {page:'current'} ).data().each( function(group,i) {
				
				if ( last !== group ) {
					$(rows).eq( i ).before(
						'<tr rowid="'+(groupCounter)+'" class="parent group"><td colspan="'+(titlesArray.length + 1)+
							'" class="dataTablegroup">'+groupByInfo+'  :  '+group+'</td></tr>'
					);
					groupCounter++;
					last = group;
					
					
					
				}
			});
		}
	});
	
	
}