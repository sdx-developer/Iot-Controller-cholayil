

function doSaveWorkflow(){
	alert("TEST");
	var schemaData = JSON.parse(pullDataFrom(localHoster+"/data/looks-like-final.json"));
	var _wsInstance = defaults(schemaData);
	var stageType = null;
	_wsInstance.stages = [];
	var stageObj = null;
	
	$.each($("div[role='staged'][class='stage-bgs']"), function(index, value){
		var stageType   = $(value).attr("stagetype");
		stageObj 		= buildStageObject(schemaData, stageType, $(value));
		stageObj.guid   = $(value).attr("data");
		stageObj.name   = $("input[id='"+($(value).attr("data"))+"StageName']").val();
		stageObj.order = index+1;

		var assignedIDs  = $($("input[type='hidden'][role='user-assigned'][data='"+($(value).attr("data"))+"']")[0]).val();
		stageObj.assignedUsers = assignedIDs;
		_wsInstance.stages.push(stageObj);
	});
	
	$.each($(_wsInstance.stages), function(index, value){
		//alert("STAGE ["+index+"] "+JSON.stringify($(value)[0]));
		console.log("STAGE ["+index+"] \n"+JSON.stringify($(value)[0]));
	});
	_wsInstance.guid = uuidv4();
	console.log(JSON.stringify(_wsInstance));
}


function buildStageObject(schemaData, stageType, divObject){
	var dataKey  = divObject.attr("data");
	var urnKey   = getURNKeyByStageType(stageType);
	var stageObj = defaults(schemaData.definitions[urnKey]);
	
	updateStageTypeEnum(stageObj, stageType);
	
	invokeTimeBounding(schemaData, stageObj, dataKey, "stage");
	invokeDataConnectorTypeConfiguration(schemaData, stageObj, dataKey);
	invokeActivityStageConfiguration(schemaData, stageObj, dataKey);
	invokeDynamicFromFields(stageObj, schemaData, urnKey, dataKey, null);
	
	return stageObj;
}

function invokeActivityStageConfiguration(schemaData, stageObj, dataKey){
	if (stageObj.stageType == "ACTIVITY"){
		
		var slotConf = $($("div[id='"+dataKey+"-SlotConfig']")).attr("value");	
		stageObj.slotConfType = ((slotConf == "fromDataGrouping")? "DYNAMIC" : 
								 (slotConf == "hardCSlotsConfig")? "FIXED" : null); 
		
		if (stageObj.slotConfType == "DYNAMIC"){
			
			var dynaSlotObj  = defaults(schemaData.definitions[URN_slotImpl]);
			var dynaSlotDiv  = $($("div[data='"+dataKey+"'][slotidentifier='nth-Slot']")[0]);
			var combDataKey  = dataKey+"-nth-Slot";
			
			dynaSlotObj.guid = "nth-Slot";
			
			invokeSlotConfigurations(schemaData, dynaSlotObj, combDataKey);
			invokeDynamicFromFields(dynaSlotObj, schemaData, URN_slotImpl, combDataKey, null);
			stageObj.dynamicSlot = dynaSlotObj;
	
		} else if (stageObj.slotConfType == "FIXED"){
			
			var slotHolder   = $( $("div[data='"+dataKey+"'][id='hardCSlotsConfig'][role='slot-allocation-action']")[0] );
			var dynaSlotObj  = null; 
			var combDataKey  = null;	
			$.each($(slotHolder).children(), function(index, value){
				
				dynaSlotObj  = defaults(schemaData.definitions[URN_slotImpl]);
				combDataKey	 = dataKey+"-"+$(value).attr("slotidentifier");
				
				dynaSlotObj.order = (index+1);
				dynaSlotObj.guid  = "Slot-"+(index+1);
				
				invokeSlotConfigurations(schemaData, dynaSlotObj, combDataKey);	
				stageObj.fixedSlots.push(dynaSlotObj);
			});
		}
	}
}

function invokeSlotConfigurations(schemaData, slotObj, combDataKey){
	slotObj.usersAssigned = $($("input[type='hidden'][role='user-assigned'][data='"+combDataKey+"']")[0]).val();
	invokeSlotApprovalProc(schemaData, slotObj, combDataKey);
	invokeSlotTimeBounding(schemaData, slotObj, combDataKey);		
}

function invokeSlotApprovalProc(schemaData, slotObj, combDataKey){
	
}

function invokeSlotTimeBounding(schemaData, slotObj, combDataKey){
	var isTimeBounded = $($("div[id='"+combDataKey+"-TimeBoundConfig']").find("a.active")[0]).attr("value");
	slotObj.isTimeBounded = isTimeBounded;
	slotObj.timeBound = null;
	if(isTimeBounded){
		var timeBounder = fillDynamicFromFields(schemaData, URN_timeBound, combDataKey, null); 
		timeBounder.type = "MINS";
		var timeUnit = $($("div[id='"+combDataKey+"-timeUnitPicker']")[0]).attr("value");
		if (typeof timeUnit != 'undefined' && timeUnit){
			timeBounder.type = timeUnit;
		}
		slotObj.timeBound = timeBounder;
	} 
}

function invokeTimeBounding(schemaData, stageObj, dataKey, roleKey){
	var isTimeBounded = $("div[data='"+dataKey+"'][role='"+roleKey+"'][id='isTimeBounded']");
	if (isTimeBounded && (typeof isTimeBounded != 'undefined') && isTimeBounded.length>0){
		stageObj.timeBounded = $($("div[data='"+dataKey+"'][role='"+roleKey+"'][id='isTimeBounded']")[0]).hasClass("active on");
		if (stageObj.timeBounded){
			var timeBounder = fillDynamicFromFields(schemaData, URN_timeBound, dataKey, null); 
			timeBounder.type = "MINS";
			var timeUnit = $($("div[id='"+dataKey+"-timeUnitPicker']")[0]).attr("value");
			if (typeof timeUnit != 'undefined' && timeUnit){
				timeBounder.type = timeUnit;
			}
			stageObj.timeBound = timeBounder;
		}
	}
}


function fillDynamicFromFields(schemaData, urnKey, dataSelector, roleSelector ){
	var tarObject = defaults(schemaData.definitions[urnKey]); 
	if (tarObject != null){
		return invokeDynamicFromFields(tarObject, schemaData, urnKey, dataSelector, roleSelector);
	}
	return null;
}

function invokeDynamicFromFields(tarObject, schemaData, urnKey, dataSelector, roleSelector){
	var typeField  = null;
	var comboField = null;
	alert("invokeDynamicFromFields --> dataSelector "+dataSelector);
	$.each($(Object.keys(schemaData.definitions[urnKey].properties)), function(index, value){
		if (roleSelector !=null && jQuery.trim(roleSelector).length>0){
			typeField  = $($("input[data='"+dataSelector+"'][role='"+roleSelector+"'][id='"+value+"']")[0]);
			comboField = $($("div[role='"+roleSelector+"'][id='"+dataSelector+"-"+value+"'][class='combo']")[0]);
		} else {
			typeField  = $($("input[data='"+dataSelector+"'][id='"+value+"']")[0]);
			comboField = $($("div[id='"+dataSelector+'-'+value+"'][class='combo']")[0]);
		}
		
		if (typeField && typeof typeField != 'undefined' && !(typeField.val() == undefined) ){
			tarObject[value] = typeField.val();
		} 
		if (comboField && typeof comboField != 'undefined' && comboField.length > 0 ){
			tarObject[value] = $(comboField).attr("value");
		}
	});
	return tarObject; 
}
function invokeDataConnectorTypeConfiguration(schemaData, stageObj, dataKey){
	if(stageObj.stageType == dc_type){
		var selDCType = jQuery.trim($($("div[id='"+dataKey+"-dsTypeSelector']")[0]).attr("value"));
		if(selDCType!=null && selDCType.length > 0){
			var dcTypeKey = null;
			if (selDCType.endsWith("-ds-ws")){ dcTypeKey = URN_wsConnector; 
			} else if (selDCType.endsWith("-ds-db")){ dcTypeKey = URN_dbConnector;
			} else if (selDCType.endsWith("-ds-xls")){ dcTypeKey = URN_xlsConnector; 
			} else if (selDCType.endsWith("-ds-external")){ dcTypeKey = URN_appConnector; }
			
			dcTypeObjectOwn = fillDynamicFromFields(schemaData, dcTypeKey, dataKey, null );
			
			//dcTypeObject = defaults(schemaData.definitions[dcTypeKey]);
			//var typeField = null;
			//$.each($(Object.keys(schemaData.definitions[dcTypeKey].properties)), function(index, value){
			//	typeField = $($("input[data='"+dataKey+"'][id='"+value+"']")[0]);
			//	if (typeField && !(typeField.val() == undefined) ){
			//		dcTypeObject[value] = typeField.val();
			//	} 
			//});
			stageObj.activeConnector = dcTypeObjectOwn;
			
		} else {
			invokeNotifier("danger", "Please configure Data Connectivity");
			stageObj = null;
		}
	}
}



//NOT useful as of now
function retriveUsers(stageDIVObject){
	var idfier = stageDIVObject.attr("data");
	var assignedIDs  = $($("input[type='hidden'][role='user-assigned'][data='"+idfier+"']")[0]).val();
	var tarTable 	 = $($("table[role='user-assigned'][data='"+idfier+"']")[0]);
	var theads   	 = $(tarTable).find("thead");
	var dataPKey     = $(tarTable).attr("datapk");
	var dataTarget   = $(tarTable).attr("datatarget");
	
	var content = pullDataFrom(localHoster+dataTarget);
	var outputObj = eval( buildjLinqQueryfromTargetTable(tarTable, theads[0], assignedIDs.split(","), dataPKey, false ));
	return outputObj;
}


function getURNKeyByStageType(stageType){
	if (stageType == dc_type)
		return URN_dataColl;
	if (stageType == act_type)
		return URN_activity;
	if (stageType == qf_type)
		return URN_qfactory;
}


function updateStageTypeEnum(stageObj, stageType){
	if (stageType == dc_type)
		stageObj.stageType = "DATA_CONNECTOR";
	if (stageType == act_type)
		stageObj.stageType = "ACTIVITY";
	if (stageType == qf_type)
		stageObj.stageType = "QUEUEFACTORY";	
}
