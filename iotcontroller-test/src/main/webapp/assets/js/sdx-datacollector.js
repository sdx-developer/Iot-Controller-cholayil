
function tweakDSTabs(srcLink){
	var showTableId = $(srcLink).attr("id").replace("button","table");
	var bttnData = $(srcLink).attr("data");
	var bttnIdfr = $(srcLink).attr("id");
	$.each($("a"), function( index, value ) {
	  if ($(value).attr("data")==bttnData){
		  if ($(value).attr("id") == bttnIdfr){
			  $(value).addClass("active");
		  } else {
			  $(value).removeClass("active");
		  }
	  }
	});
	
	$.each($("table"), function( index, value ) {
		if ($(value).attr("data")==bttnData){
			if ($(value).attr("id")==showTableId){
				$(value).show();
			} else {
				$(value).hide();
			}
		}
	});
}

function dsTypeChanger(selType){
	var gvnUUID = selType.split(dsTypeDelim)[0];
	for (i=0; i<dsTypesArray.length; i++) {
		if (selType == (gvnUUID + dsTypeDelim + dsTypesArray[i])){
			$($("div[id='"+selType+"']")).fadeIn(1000);
		} else {
			$($("div[id='"+gvnUUID + dsTypeDelim + dsTypesArray[i]+"']")).hide();
		}
	}
	dataPreviewed = false;
}

function fillRepoData(srcButton){
	var bttnData = $(srcButton).attr("data");
	var lnURL = localHoster+"/ln-data.json";
	var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			var content = xmlhttp.responseText;
			$("#dataHolder").empty();
			$("#dataHolder").append(content);
			
			retrievedData 	= content;
			
			$($("input[data='"+bttnData+"'][id='ParserString']")[0]).val("production-plan / Bevelling-inputs / lndata");
			invokeDataDisplayWidget("Retrieved Data");
			return "";
        }
    }
    xmlhttp.open("GET", lnURL, false );
    xmlhttp.send(); 
}

function dataParserPreview(srcImg){
	if (retrievedData!=null){
		var bttnData = $(srcImg).attr("data");
		var getter = "production-plan";
		var parsed = JSON.parse(retrievedData);
		
		var prsrStrArray = $($("input[data='"+bttnData+"'][id='ParserString']")[0]).val().split("/");
		for (i=0; i<prsrStrArray.length; i++){
			parsed = parsed[jQuery.trim(prsrStrArray[i])];
		}
		if (parsed){
			dataCollected = parsed;
			//$('#table-2').createTable(data, {});
			var tblHolderDIV = $($("div[data='"+bttnData+"'][id='table-ParsedTableDIV']")[0]);
			displayCount = dataCollected.length;
			
			if (dataCollected!=null && dataCollected.length >0){
				var titlesArray = Object.keys(dataCollected[0]);
				$(tblHolderDIV).createTable(dataCollected, 
					{tblRepId : "sampleParsedData", tblClass : "table simple medium", dataAttr : bttnData,
					idColumnType : mulSelType, idColumnData : null });
				buildDataTabler(titlesArray, bttnData);
			}
		}
		dataPreviewed = true;
	} else {
		invokeNotifier("danger", "Please retrieve sample data first");
	}
}

function verifyPreviewDone(dataSTR){
	if (dataPreviewed && (dataCollected!=null && dataCollected.length >0)){
		var tarTable = $($("table[id='FromJob'][data='"+dataSTR+"']")[0]);
		//data="UUID4Value" id="FromJob"
		var trContent = "";
		var titlesArray = Object.keys(dataCollected[0]);
		$.each($(titlesArray), function(index, value){
			trContent += "<tr><td width='15%'>"+value+"</td><td>Dynamic</td></tr>";
		});
		tarTable.append(trContent);
	}
}




