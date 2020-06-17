
package com.sdx.platform.groovy.extensions

import com.sdx.platform.groovy.JUIFieldUtils
import com.sdx.platform.config.Constants
import com.sdx.platform.config.Memory

import groovy.json.JsonOutput


import groovy.util.logging.Slf4j

@Slf4j
class PyModule4 {

	private String modNameMachine = "Machine";
	
	private String modActive3 ;
	private String modPath3 ;
	private String modCron3 ;
	private String modDesc3 ;
	//private int modulesRefreshInterval;
	
	def returnIoTModuleName(){
		return modNameMachine;
		
	}


	def refreshValues() {
		
		modNameMachine = getFromAppStorage("modNameMachine", String.class) ?: "";
		modActive3 		= getFromAppStorage("modActive3", String.class) ?: "";
		modPath3 			= getFromAppStorage("modPath3", String.class) ?: "";
		modCron3 =  getFromAppStorage("modCron3", String.class) ?: "";
		modDesc3 	= getFromAppStorage("modDesc3", String.class) ?: "";
		
		
		//modulesRefreshInterval =  getFromAppStorage("IoTModRefreshTime ", Integer.class)?: "3";
	}

	private Object getFromAppStorage(String pKey, Class<?> type) {
		
		if (Memory.getIotProperties().containsKey(pKey)) {
			log.info("SEARCH MODE in stamping IOT: "+pKey+ " actual ["+Memory.getIotProperties().getProperty(pKey).getClass()+"] {"+Memory.getIotProperties().getProperty(pKey)+"} ");
			if (type != Memory.getIotProperties().getProperty(pKey).getClass()) {
				if (type.equals(Integer.class)) {
					log.info("CONVERSION HAPPENING >>>>>>>>>>>>>> while ACTUAL is "+Memory.getIotProperties().getProperty(pKey).getClass())
					return Integer.parseInt(Memory.getIotProperties().getProperty(pKey));
				}
			}
			return Memory.getIotProperties().getProperty(pKey);
		}
		return null;
	}



	def buildJUI() {
		refreshValues();
		Map modNameGrp		= JUIFieldUtils.buildTF("Module Display Name", "modNameMachine", modNameMachine, false, "Iot Module name")
		Map moduleActive    = JUIFieldUtils.buildSwitch("Active", "modActive3", true, "Module activeness")
		Map modulePath		= JUIFieldUtils.buildTF("Module path", "modPath3", modPath3, false, "Execution content")
		Map moduleCron		= JUIFieldUtils.buildTF("Module CRON job", "modCron3", modCron3, false, "Advanced: Cron for the execution schedule")
		Map moduleDesc		= JUIFieldUtils.buildTF("Description", "modDesc3", modDesc3, false, "Notes")
		//Map mdlsRefMap 		= JUIFieldUtils.buildRangeBuilder("IoT Modules Refresh Time", "modulesRefreshInterval", modulesRefreshInterval, 60, 0, true, 1, false, "Groovy Refresh Advanced Settings");


		Map guiGroup = JUIFieldUtils.buildGroup("IoT Module", modNameMachine + " - Module configuration ")


		def fieldsList = ["items" : [guiGroup, modNameGrp, moduleActive, modulePath, moduleCron, moduleDesc]];


		log.info(JsonOutput.toJson(fieldsList))
		return JsonOutput.toJson(fieldsList);
	}
}
