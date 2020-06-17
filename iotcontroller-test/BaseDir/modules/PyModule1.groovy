
package com.sdx.platform.groovy.extensions

import com.sdx.platform.groovy.JUIFieldUtils
import com.sdx.platform.config.Constants
import com.sdx.platform.config.Memory

import groovy.json.JsonOutput

import groovy.util.logging.Slf4j

@Slf4j
class PyModule1 {

	private String modName = "Stamping IoT";
	
	private String modActive ;
	private String modPath ;
	private String modCron ;
	private String modDesc ;
	private String stationCode;
	private String stationName;
	//private int modulesRefreshInterval;

	def returnIoTModuleName(){
		return modName;
	}


	def refreshValues() {
		
		modName         = getFromAppStorage("modName", String.class) ?: "";
		modActive 		= getFromAppStorage("modActive", String.class) ?: "";
		modPath 		= getFromAppStorage("modPath", String.class) ?: "";
		modCron         = getFromAppStorage("modCron", String.class) ?: "";
		modDesc 	    = getFromAppStorage("modDesc", String.class) ?: "";
		stationCode     = getFromAppStorage("stationCode", String.class) ?: "";
		stationName     = getFromAppStorage("stationName", String.class) ?: "";
		
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
		
		Map modNameGrp		= JUIFieldUtils.buildTF("Module Display Name", "modName", modName, false, "Iot Module name")
		Map moduleActive    = JUIFieldUtils.buildSwitch("Active", "modActive", true, "Module activeness")
		Map modulePath		= JUIFieldUtils.buildTF("Module path", "modPath", modPath, false, "Execution content")
		Map moduleCron		= JUIFieldUtils.buildTF("Module CRON job", "modCron", modCron, false, "Advanced: Cron for the execution schedule")
		Map moduleDesc		= JUIFieldUtils.buildTF("Description", "modDesc", modDesc, false, "Notes")
		Map stationCode		= JUIFieldUtils.buildTF("Station Code", "stationCode", stationCode, false, "StationCode")
		Map stationName		= JUIFieldUtils.buildTF("Station Name", "stationName", stationName, false, "StationName")
		//Map mdlsRefMap 		= JUIFieldUtils.buildRangeBuilder("IoT Modules Refresh Time", "modulesRefreshInterval", modulesRefreshInterval, 60, 0, true, 1, false, "Groovy Refresh Advanced Settings");


		Map guiGroup = JUIFieldUtils.buildGroup("IoT Module", modName + " - Module configuration ")


		def fieldsList = ["items" : [guiGroup, modNameGrp, moduleActive, modulePath, moduleCron, moduleDesc, stationCode, stationName ]];


		log.info(JsonOutput.toJson(fieldsList))
		return JsonOutput.toJson(fieldsList);
	}
}
