
var popupFinalAssignment = null;
var stageTypeSel = 1, dsTypeSelectorDef = -1;
var defaults 	 = window.jsonSchemaDefaults.noConflict();



function uuidv4() {
  return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
    (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
  )
}

function toggleFieldSet(givenElm, srcIMG){
	givenElm = $(srcIMG).attr("data")+givenElm;
	$("table[id='"+givenElm+"']").toggle();
	$("tr[id='"+givenElm+"TR']").toggle();
	var src = $(srcIMG).attr('src');
    var newsrc = (src=='assets/imgs/gen/down.gif') ? 'assets/imgs/gen/right.gif' : 'assets/imgs/gen/down.gif';
	$(srcIMG).attr('src', newsrc );
}

function doPopupPicker(srcObject){
	var tarTable = $($("table[data='"+$(srcObject).attr("data")+"'][role='"+$(srcObject).attr("target")+"']")[0]);
	doPopupDisplayWidget(pullDataFrom(localHoster+$(tarTable).attr("datatarget")), srcObject);
}

function buildjLinqQueryfromTargetTable(tarTable, thead, selArray, pkGiven, includePKdata){
	var selQuery = "jLinq.from(JSON.parse(content))";
	
	if (selArray && selArray.length>0){
		selQuery += ".";
		$.each($(selArray), function(index, value){
			if (index===0){ selQuery += 'equals("'+pkGiven+'", "'+value+'")';
			} else { selQuery += '.or("'+value+'")'; }
		});
	}
	selQuery +=  ".select(function(rec){return { ";
	var thArray  = $(thead).find("th");
	if (includePKdata){
		selQuery +=  defaultIDCol+":rec."+pkGiven+",";
	}
	$.each(thArray, function(index,value){
		var dkGiven = $(value).attr("data-key");
		selQuery += ( (index==0) ? "":", ")+ " '"+dkGiven+"' :";
		var dkArray = dkGiven.split(",");
			if (dkArray.length >1){
				var output = "";
				for (i=0; i<dkArray.length; i++){
					if ((i+1)==dkArray.length){ output += "rec."+jQuery.trim(dkArray[i]);
					} else { output += " rec."+jQuery.trim(dkArray[i])+"+ \' , \' +"; }
				}
				selQuery += output;
			} else { selQuery += " rec."+dkGiven; }
		}
	);
	selQuery += " }; } );";
	return selQuery;
	
}

function doPopupDisplayWidget(content, srcObject){
	var tarTable = $($("table[data='"+$(srcObject).attr("data")+"'][role='"+$(srcObject).attr("target")+"']")[0]);
	var theads   = $(tarTable).find("thead");
	
	if (tarTable && (tarTable.attr("datapk")) && theads.length >0){
		
		$("#popupDisplayHeader").text($(srcObject).text());
		var outputObj = eval( buildjLinqQueryfromTargetTable(tarTable, theads[0], null, (tarTable.attr("datapk")), true ));
	
		$("#popupDisplayContent").createTable(outputObj, 
			{tblRepId : "dataRequested", tblClass : "table simple medium", 
			idColumnType : mulSelType, idColumnData : "IDColumn", styleAttr : "border:1px solid #ccc;border-top:0px;",
			dataAttr : $(srcObject).attr("data")});
		
		$($("table[id='dataRequested'][data='"+$(srcObject).attr("data")+"']")[0]).DataTable();
	
		popupFinalAssignment = srcObject;
		popupDisplayWidget.show();
	} else {
		alert("NO data PK "+(tarTable.attr("datapk")));
	}
	
}

function fillDataToTarget(){
	if (popupFinalAssignment){
		var dataLookup  = $(popupFinalAssignment).attr("data");
		var targetObj   = $(popupFinalAssignment).attr("target");
		var tarTable    = $($("table[data='"+dataLookup+"'][role='"+targetObj+"']")[0]);
		var theads   	= $(tarTable).find("thead");
		
		var dataTarget  = $(tarTable).attr("datatarget");
		var dataPKey    = $(tarTable).attr("datapk");
		var tarHidden   = $($("input[id='values'][type='hidden'][data='"+dataLookup+"'][role='"+targetObj+"']")[0]);
		var cbsSelArray = $("input:checked[type='checkbox'][data='"+dataLookup+"']").map(function() {return $(this).val()});
		
		$(tarHidden).val(cbsSelArray.get());
		
		var content = pullDataFrom(localHoster+dataTarget);
		var outputObj = eval( buildjLinqQueryfromTargetTable(tarTable, theads[0], $(cbsSelArray).toArray(), dataPKey, false ));
		
		tarTable.find("tbody").empty();
		var trRows = "";
		$.each($(outputObj), function(){
			trRows += "<tr>";
			var jsonOBJROW = this;
			$.each(Object.keys(this), function(index, value){
				trRows += "<td>"+jsonOBJROW[value]+"</td>";
			});
			trRows += "</tr>";
		});
		tarTable.append(trRows);
	}
}

function cleanUpPopupDisplayWidget(){
	popupFinalAssignment = null;
	$("#popupDisplayContent").empty();
	$("#popupDisplayHeader").text("");
	$("#popupDisplayContent").text("");
	popupDisplayWidget.hide();
}

function add2Stages(pURL){
	var content = stageContentGet(localHoster+"/"+pURL);
	$("#happeningTD").css("height", "100%");
	$("#happeningDIV").css("height", "auto");
}

function invokeDataDisplayWidget(message){
	$("#dataDisplayWidgetTitle").text(message);
	dataDisplayWidget.show();
} 

function invokeNotifier(type, message){
	$("#juiNotifier").attr("class", "notify "+type);
	if (type == "danger"){
		$("#juiNotifierTile").text("Error");
	}
	$("#juiNotifierMssg").text(message);
	notification.show();
}

function tweakTimeApproval(switcher){
	$(switcher).toggleClass("on off");
	$($("table[data='"+$($(switcher)).attr("data")+"'][id='timeBoundConfiguration']")[0]).toggle();
}


function stageBuild(srcBttn){
	
	var btndata = $(srcBttn).attr('data');
	var stageName = $($("input[id='"+btndata+"StageName']")).val();
	var selType   = $("select[id='"+btndata+"TypeSelector']").children("option:selected").val();
		selType   = $("a[id='"+btndata+"-StageTypeSelected']").text();
	var fsElement = $($(srcBttn).closest("fieldset")[0]);
	var nameSpan  = $($("span[id='"+btndata+"NameSpan']"));
	
	var stageTypeURL = localHoster+"/stage-";
	
	if (!(stageName === undefined || stageName === null || stageName === "")) {
		nameSpan.text(stageName+" - "+selType);
	} else {
		nameSpan.text("Stage"+" - "+selType); 
	}
	  
	if (selType == "Data Connector"){
		fsElement.attr("class","legendOfBox6");
		stageTypeURL = stageTypeURL+"dc.html";
	} else if (selType == "Activity Stage"){
		fsElement.attr("class","legendOfBox5");
		stageTypeURL = stageTypeURL+"act.html";
	} else if (selType == "Distribution Queue"){
		fsElement.attr("class","legendOfBox7");
		stageTypeURL = stageTypeURL+"qf.html";
	}
	buildStageTypeDetailed(btndata, selType, stageTypeURL);
	furtherOperations(selType);
}

function furtherOperations(selType){
	if (selType == "Data Connector"){
	} else if (selType == "Activity Stage"){
		
	} else if (selType == "Distribution Queue"){
	}
}

function buildStageTypeDetailed(btndata, stageType, stageTypeURL){
	var divToUpdate = $($("div[id='"+btndata+"-StageTypeDetailed']"));
	stageTypeDetailGet(stageTypeURL, btndata, divToUpdate);
}


function pullDataFrom(theUrl){
	var retCtnt = null;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else { 
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			var type = xmlhttp.getResponseHeader('Content-Type');
			var content = xmlhttp.responseText;
			retCtnt = content;
			return content;
        }
    }
    xmlhttp.open("GET", theUrl, false );
    xmlhttp.send(); 
	return retCtnt;	
}

function stageTypeDetailGet(theUrl, uuiddata, divToUpdate) {
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else { 
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			var type = xmlhttp.getResponseHeader('Content-Type');
			var content = xmlhttp.responseText;
			content = content.replace(/UUID4Value/g,uuiddata);
			rand5DN = Math.floor(Math.random()*90000) + 10000;
			content = content.replace(/RAND_5DIGIT_NUMBER/g,rand5DN);
			divToUpdate.empty();
			divToUpdate.append(content);
			
			return content;
        }
    }
    xmlhttp.open("GET", theUrl, false );
    xmlhttp.send();    
}

function stageContentGet(theUrl) {
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else { 
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			var type = xmlhttp.getResponseHeader('Content-Type');
			var content = xmlhttp.responseText;
			content = content.replace(/UUID4Value/g,uuidv4());
			
			$("#stageHOLDER").append(content);
            return content;
        }
    }
    xmlhttp.open("GET", theUrl, false );
    xmlhttp.send();    
}
