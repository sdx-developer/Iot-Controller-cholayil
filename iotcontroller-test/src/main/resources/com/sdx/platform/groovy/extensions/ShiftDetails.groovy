package com.sdx.platform.groovy.extensions

import com.sdx.platform.groovy.JUIFieldUtils
import com.sdx.platform.config.Constants
import com.sdx.platform.config.Memory
import java.lang.String;
import groovy.json.JsonOutput

import groovy.util.logging.Slf4j



@Slf4j
class ShiftDetails {
	private String theme;
	
	private String shift1Time;
	private String unPlannedDownTimeShift1;
	private String unPlannedDownTimereason;
	private String foodBreak;
	private String noOFfoodBreak;
	private String shortBreak;
	private String noOFshortBreak;
	private String totalfoodBreak;
	private String totalshortBreak;
	

	def refreshValues() {

		theme = "white";
		shift1Time =getFromAppStorage("shift1Time", String.class) ?: "";
		unPlannedDownTimeShift1 = getFromAppStorage("unPlannedDownTimeShift1", String.class) ?: "";
		unPlannedDownTimereason = getFromAppStorage("unPlannedDownTimereason", String.class) ?: "";
		foodBreak = getFromAppStorage("foodBreak", String.class) ?: "30";
		noOFfoodBreak = getFromAppStorage("noOFfoodBreak", String.class) ?: "1";
		shortBreak = getFromAppStorage("shortBreak", String.class) ?: "30";
		noOFshortBreak = getFromAppStorage("noOFshortBreak", String.class) ?: "1";
		totalfoodBreak = getFromAppStorage("totalfoodBreak", String.class) ?: "30";
		totalshortBreak = getFromAppStorage("totalshortBreak", String.class) ?: "30";
		

		return null;

	}

	private Object getFromAppStorage(String pKey, Class<?> type) {

		if (Memory.getAppProperties().containsKey(pKey)) {
			log.info("SEARCH MODE: "+pKey+ " actual ["+Memory.getAppProperties().getProperty(pKey).getClass()+"] {" +Memory.getAppProperties().getProperty(pKey)+"} ");
			if (type !=Memory.getAppProperties().getProperty(pKey).getClass()) {
				if(type.equals(Integer.class)) {
					log.info("CONVERSION HAPPENING >>>>>>>>>>>>>> while ACTUAL is "+Memory.getAppProperties().getProperty(pKey).getClass())
					return Integer.parseInt(Memory.getAppProperties().getProperty(pKey));
				}
			}
			return Memory.getAppProperties().getProperty(pKey);
		}
		return null;
	}

	def init() {
		log.info("****************** Initializing the AppProperties Groovy Extension" ) }

	def buildJUI( ) {

		init();
		refreshValues();

		//def downTime = ["2","5","10","15","20","25","30"] as String[]
		//def durtion = ["3","5","10","15","20"] as String[]

		Map shift1NmeMap = JUIFieldUtils.buildTF("First Shift Time", "shift1Time", shift1Time,false,"Shift Time");
		//Map shift2NmeMap = JUIFieldUtils.buildTF("Second Shift", "shift2Time", shift2Time,false,"Second shift");
		Map downTimeshift1Map = JUIFieldUtils.buildTF("First Shift UnPlanned DownTime ", "unPlannedDownTimeShift1", unPlannedDownTimeShift1,false,"Shift UnPlanned DownTime");
		//Map downTimeshift2Map  = JUIFieldUtils.buildTF("Second Shift UnPlanned DownTime ", "unPlannedDownTimeShift2", unPlannedDownTimeShift2,false,"Second Shift UnPlanned DownTime");
		Map downtimereason  = JUIFieldUtils.buildTF("First Shift UnPlanned DownTime Reason ", "unPlannedDownTimereason", unPlannedDownTimereason,false,"UnPlanned DownTime Reason");
		Map advncdGroup 	= JUIFieldUtils.buildGroup("Planned Downtime Settings", " ")
		Map foodBreak = JUIFieldUtils.buildTF("Food Break ", "foodBreak", foodBreak,false,"Food Break ");
		Map noOFfoodBreak  = JUIFieldUtils.buildTF("No OF FoodBreak ", "noOFfoodBreak", noOFfoodBreak,false,"No OF FoodBreak");
		Map shortBreak = JUIFieldUtils.buildTF("Short Break ", "shortBreak", shortBreak,false,"Short Break");
		Map noOFshortBreak  = JUIFieldUtils.buildTF("No OF ShortBreak ", "noOFshortBreak", noOFshortBreak,false,"No OF ShortBreak ");
		Map totalfoodBreak = JUIFieldUtils.buildTF("Total Food Break", "totalfoodBreak", totalfoodBreak,false,"Total Food Break");
		Map totalshortBreak  = JUIFieldUtils.buildTF("Total Short Break", "totalshortBreak",totalshortBreak,false,"Total Short Break");
		//Map downTimeOccurenceMap =JUIFieldUtils.buildSelectBox("DownTime Occurence(minutes)", "downTimeOccurence", downTimeOccurence,downTime);
		//Map downTimeDurationMap = JUIFieldUtils.buildSelectBox("DownTime Duration(minutes) ", "downTimeDuration", downTimeDuration,durtion);
		

		def fieldsList = ["items" : [shift1NmeMap,downTimeshift1Map,downtimereason,advncdGroup,foodBreak,noOFfoodBreak,totalfoodBreak,shortBreak,noOFshortBreak,totalshortBreak]];


		log.info(JsonOutput.toJson(fieldsList))
		return JsonOutput.toJson(fieldsList);

	} 

	def buildFIO()
	{
		return "buildFIO";
	}
}