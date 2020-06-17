

function tweakSlotIndividualTabs(srcLink){
	var showTableId = $(srcLink).attr("id").replace("button","table");
	var bttnData = $(srcLink).attr("data");
	var bttnIdfr = $(srcLink).attr("id");
	
	$.each($($("a[data='"+bttnData+"']")), function( index, value ) {
	  if ($(value).attr("data")==bttnData){
		  if ($(value).attr("id") == bttnIdfr){
			  $(value).addClass("active");
		  } else {
			  $(value).removeClass("active");
		  }
	  }
	});
	$.each($($("div[data='"+bttnData+"']")), function( index, value ) {
		if ($(value).attr("data")==bttnData){
			if ($(value).attr("id")==showTableId){
				$(value).show();
			} else {
				$(value).hide();
			}
		}
	});
}

function activitySlotBuilder(selected, dataSTR){
	$.each($("div[data='"+dataSTR+"'][role='slot-allocation-opts']"), function (){
		if ($(this).attr("id")=== selected){ $(this).show(); } else { $(this).hide(); }
	});
	
	$.each($("div[data='"+dataSTR+"'][role='slot-allocation-action']"), function (){
		if ($(this).attr("id")==="fromDataGrouping"){
			verifyDynamicSlotComponent($(this));
		}
		if ($(this).attr("id")=== selected){ $(this).show(); } else { $(this).hide(); }
	});
	
	if (false && groupCounter >0){
		var slotsDiv = $($("div[id='"+dataSTR+"-slots']")[0]);
		slotsDiv.empty();
		for (i=0; i<groupCounter; i++){
			var dataToAppend = tableDataGet(i);
			appendSlotHTML(slotsDiv, "Slot"+(i+1), dataSTR);
			$($("div[data='"+dataSTR+"-Slot"+(i+1)+"']")[0]).append(dataToAppend);
		}
	}
}

function verifyDynamicSlotComponent(dynaHolder){
	if (dynaHolder.children().length <1 ){
		var dataSTR = $(dynaHolder).attr("data");
		appendSlotHTML(dynaHolder, "nth-Slot", dataSTR, "<strong>n<sup>th</sup>-Slot</strong>");
		var juiWindow = ($($("div[data='"+dataSTR+"'][id='Slot-nth-Slot-Window']")[0]).find("div[class='window']")[0]);
		$(juiWindow).css("max-width","unset");
		//Slot-nth-Slot-Window
	}
}

function addMultiSlot(srcButton){
	var dataSTR = $(srcButton).attr("data");
	var tarDiv  = $("div[data='"+dataSTR+"'][role='slot-allocation-action'][id='hardCSlotsConfig']")[0];
	var length  = $(tarDiv).children().length;
	var dataToAppend = tableDataGet(length+1);
	appendSlotHTML($(tarDiv), "Slot-"+(length+1), dataSTR);
	$(tarDiv).append(dataToAppend);
}


function slotGuiCustomBG(selData, data, slotCounter){
	alert("["+selData+"] data "+data+", slotCounter : "+slotCounter);
	var dIdifier = data+"-"+slotCounter;
	var coreDiv  = $($("div[id='"+dIdifier+"-GUI-Core']")[0]);
	var ctntDiv  = $($("div[id='"+dIdifier+"-GUI-Content']")[0]);
	
	$(coreDiv).removeClass("waveAnimation");
	$.each($(coreDiv).children(),function(){
		if ($(this).attr("id")===undefined	){
			$(this).remove();
		}
	});
	
	if (selData=="waterflow"){
		$(coreDiv).addClass("waveAnimation");
		var wavyText = pullDataFrom(localHoster+"/wavy.html");
		$(coreDiv).append(wavyText);
	} else if (selData=="waterflow"){
	}
	
}

function tableDataGet(index){
	if (dataTabler){
		var cloned = "<div class='jui jennifer rowFlexDiv' style='width:100%;'><table class='table classic small' id='slotData"+index+"' style='width:100%;'>";

		cloned = cloned+$("#sampleParsedData tr:first").html();
		$.each($("table[id='sampleParsedData'] tr[rowid='"+index+"']").nextUntil("tr[rowid='"+(index+1)+"']"), function( index, value ) {
			cloned = cloned+"<tr>"+$(value).html()+"</tr>";
		});
		cloned = cloned+"</table><span>&nbsp;&nbsp;&nbsp;&nbsp;</span></div>";
		return cloned;
	}
}

function appendSlotHTML(targetDIV, slotCounter, dataSTR, supText){
	var slotsURL = localHoster+"/slot.html";
	var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			var content = xmlhttp.responseText;
			content = content.replace(/UUID4Value/g,dataSTR);
			if (supText!=null){
				content = content.replace(/SlotCounter_SUPERTEXT/g,supText);
			} else {
				content = content.replace(/SlotCounter_SUPERTEXT/g,slotCounter);
			}
			content = content.replace(/SlotCounter/g,slotCounter);
			//
			targetDIV.append(content);
			return "";
        }
    }
    xmlhttp.open("GET", slotsURL, false );
    xmlhttp.send(); 
}

function tweakApproval(switcher){
	$(switcher).toggleClass("on off");
	$($("div[data='"+$($(switcher)).attr("data")+"'][id='stageApprovers']")[0]).toggle();
}

function tweakApprovalOfSlot(selData, objData, objSlotId){
	var targetDIV = $($("div[data='"+objData+"'][slotidentifier='"+objSlotId+"'][id='slotRejection']")[0]);
	if (selData.value=='true'){
		targetDIV.show();
	} else {
		targetDIV.hide();
	}
}
function tweakTimeBoundOfSlot(selData, objData, objSlotId){
	var targetDIV = $($("div[data='"+objData+"'][slotidentifier='"+objSlotId+"'][id='slotTimeBounder']")[0]);
	if (selData.value=='true'){
		targetDIV.show();
	} else {
		targetDIV.hide();
	}
}

function winFocus(winBox, hovering){
	
	var slIdfr = $(winBox).attr("slotidentifier");
	var dataGvn = $(winBox).attr("data");
	var cornerDIV 	 = $($("div[data='"+dataGvn+"'][slotidentifier='"+slIdfr+"'][id='cornerDIV']"))[0];
	var cornerIMG 	 = $($("img[data='"+dataGvn+"'][slotidentifier='"+slIdfr+"'][id='cornerIMG']"))[0];
	
	if (hovering){
		$($(winBox).find(".head")[0]).css("background-image",
			"linear-gradient(to top, rgba(255, 255, 255, 0), rgba(165, 193, 235, 0.8))");
		$(winBox).find("#titler").css("color","#1c4bd3");
		$(cornerDIV).css("background-color","#C4D5ED");
		$(cornerIMG).attr("src","assets/imgs/corners/C4D5ED-right-top.png");
		
	} else {
		$($(winBox).find(".head")[0]).css("background-image","");
		$(winBox).find("#titler").css("color","#666666");
		$(cornerDIV).css("background-color","#AAAAAA");
		$(cornerIMG).attr("src","assets/imgs/corners/AAAAAA-right-top.png");
	}
}
