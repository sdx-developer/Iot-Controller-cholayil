
package com.sdx.platform.groovy.extensions

import com.sdx.platform.groovy.JUIFieldUtils
import com.sdx.platform.config.Constants
import com.sdx.platform.config.Memory

import groovy.json.JsonOutput

import groovy.util.logging.Slf4j

@Slf4j
class PyModule2 {

	private String modNameFlow = "Flow Wrap";
	
	private String modActive1 ;
	private String modPath1 ;
	private String modCron1 ;
	private String modDesc1 ;
	private String stationCode1;
	private String stationName1;
    //private int modulesRefreshInterval1;
	
	def returnIoTModuleName(){
		return modNameFlow;
	}


	def refreshValues() {
		
		modNameFlow      = getFromAppStorage("modNameFlow", String.class) ?: "";
		modActive1 		 = getFromAppStorage("modActive1", String.class) ?: "";
		modPath1 		 = getFromAppStorage("modPath1", String.class) ?: "";
		modCron1         = getFromAppStorage("modCron1", String.class) ?: "";
		modDesc1 	     = getFromAppStorage("modDesc1", String.class) ?: "";
		stationCode1     = getFromAppStorage("stationCode1", String.class) ?: "";
		stationName1     = getFromAppStorage("stationName1", String.class) ?: "";
        //modulesRefreshInterval1 =  getFromAppStorage("IoTModRefreshTime1", Integer.class)?: "3";
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
		Map modNameGrp		= JUIFieldUtils.buildTF("Module Display Name", "modNameFlow", modNameFlow, false, "Iot Module name")
		Map moduleActive    = JUIFieldUtils.buildSwitch("Active", "modActive1", true, "Module activeness")
		Map modulePath		= JUIFieldUtils.buildTF("Module path", "modPath1", modPath1, false, "Execution content")
		Map moduleCron		= JUIFieldUtils.buildTF("Module CRON job", "modCron1", modCron1, false, "Advanced: Cron for the execution schedule")
		Map moduleDesc		= JUIFieldUtils.buildTF("Description", "modDesc1", modDesc1, false, "Notes")
		Map stationCode		= JUIFieldUtils.buildTF("Station Code", "stationCode1", stationCode1, false, "StationCode")
		Map stationName		= JUIFieldUtils.buildTF("Station Name", "stationName1", stationName1, false, "StationName")
        //Map mdlsRefMap 		= JUIFieldUtils.buildRangeBuilder("IoT Modules Refresh Time", "modulesRefreshInterval1", modulesRefreshInterval1, 60, 0, true, 1, false, "Groovy Refresh Advanced Settings");

		Map guiGroup = JUIFieldUtils.buildGroup("IoT Module", modNameFlow + " - Module configuration ")


		def fieldsList = ["items" : [guiGroup, modNameGrp, moduleActive, modulePath, moduleCron, moduleDesc, stationCode, stationName ]];


		log.info(JsonOutput.toJson(fieldsList))
		return JsonOutput.toJson(fieldsList);
	}
}
