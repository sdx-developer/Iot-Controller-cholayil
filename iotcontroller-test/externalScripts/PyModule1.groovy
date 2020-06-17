package com.sdx.platform.groovy.extensions

import com.sdx.platform.groovy.JUIFieldUtils
import com.sdx.platform.config.Constants
import com.sdx.platform.config.Memory

import groovy.json.JsonOutput

import groovy.util.logging.Slf4j

@Slf4j
class PyModule1 {
	
	private String moduleName = "Stamping IoT";
	
	def returnIoTModuleName(){
		return moduleName;
	}
	
	def buildJUI() {
		
		
		Map modNameGrp		= JUIFieldUtils.buildTF("Module Display Name", "modName", moduleName, false, "Iot Module name")
		Map moduleActive    = JUIFieldUtils.buildSwitch("Active", "modActive", true, "Module activeness")
		Map modulePath		= JUIFieldUtils.buildTF("Module path", "modPath", "/script/SMCam.groovy", false, "Execution content")
		Map moduleCron		= JUIFieldUtils.buildTF("Module CRON job", "modCron", "CRON String", false, "Advanced: Cron for the execution schedule")
		Map moduleDesc		= JUIFieldUtils.buildTF("Description", "modDesc", "Description for the Module", false, "Notes")
		
		
		Map guiGroup = JUIFieldUtils.buildGroup("IoT Module", moduleName + " - Module configuration ")
		
		
		def fieldsList = ["items" : [
			guiGroup, modNameGrp, moduleActive, modulePath, moduleCron, moduleDesc ]];
		
		
		log.info(JsonOutput.toJson(fieldsList))
		return JsonOutput.toJson(fieldsList);
		
	}
	
	
	
}
