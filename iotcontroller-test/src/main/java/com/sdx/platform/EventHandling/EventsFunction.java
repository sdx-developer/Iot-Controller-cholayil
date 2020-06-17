package com.sdx.platform.EventHandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.ietf.jgss.Oid;
//import org.json.JSONObject;
import com.alibaba.fastjson.JSONObject;
import com.sdx.platform.config.Memory;
import com.sdx.platform.EventHandling.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventsFunction {
	static Logger log = LoggerFactory.getLogger(EventsFunction.class);
	static CommonUtil commonUtil = new CommonUtil();
	static File fp = new File(FieldConstants.filepath);
	public static int threadCnt = 20;
	public static int macPerfCount = 0;
	public static int macQualityCount = 0;
	public static int macAvailCount = 0;
	public static int macErrorCount = 0;
	public static String performanceCount;
	public static String qualityCount;
	public static String availabilityCount;
	static ArrayList<String> bandWidth;
	private static File extBaseDir = null;
	static String currentDate = commonUtil.dateToStringConv(new Date());

	private static Object totalProductionCount;
	private static Object goodCount;
	public static String perLine;
	public static String qualLine;
	public static String eventGenPer;
	public static String eventGenQual;
	public static String eventGenAvail;
	public static String totalProdCount;
	public static String totalGoodCount;

	public static void write(String pp, File F) throws IOException {
		FileWriter fw = new FileWriter(F, true);
		fw.write(pp + "\n");
		fw.close();

	}

	public static void main(String[] args) {
		File f = new File(FieldConstants.path);
		try {
			write(EventsFunction.eventGenPer, f);
			
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public static void refreshPropertyFile() {
		if (Memory.getAppPropBuilder() != null) {
			try {
				Memory.getAppPropBuilder().save();
				Memory.refreshProperties();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	public static void status() {
	}

	public static void perfCount() throws NumberFormatException, IOException, ParseException {
		if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("date")) {
		if (currentDate.equals(Memory.getAppProperties().getString("perCurrentDate"))) {
			macPerfCount = Integer.parseInt(Memory.getAppProperties().getString("stampingEventsToday"));
			macPerfCount++;
			String macPerfCnt = Integer.toString(macPerfCount++);
			//log.info("STAMPINGEVENTTODAY before :::::::::: "
				//	+ Memory.getAppProperties().getString("stampingEventsToday"));
			Memory.clearAndAddProperty("stampingEventsToday", macPerfCnt);
			refreshPropertyFile();
			//log.info("STAMPINGEVENTTODAY:::::::::: " + Memory.getAppProperties().getString("stampingEventsToday"));
		} else {
			macPerfCount=0;
			macPerfCount++;
			String macPerfCnt = Integer.toString(macPerfCount++);
			Memory.clearAndAddProperty("stampingEventsToday", macPerfCnt);
			Memory.clearAndAddProperty("perCurrentDate", currentDate);
			refreshPropertyFile();
		}}
		else if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("shift")) {
			if (Memory.getAppProperties().getString("perShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
				macPerfCount = Integer.parseInt(Memory.getAppProperties().getString("stampingEventsToday"));
				macPerfCount++;
				String macPerfCnt = Integer.toString(macPerfCount++);
				//log.info("STAMPINGEVENTTODAY before :::::::::: "
						//+ Memory.getAppProperties().getString("stampingEventsToday"));
				Memory.clearAndAddProperty("stampingEventsToday", macPerfCnt);
				refreshPropertyFile();
				//log.info("STAMPINGEVENTTODAY:::::::::: " + Memory.getAppProperties().getString("stampingEventsToday"));
			} else {
				macPerfCount=0;
				macPerfCount++;
				String macPerfCnt = Integer.toString(macPerfCount++);
				Memory.clearAndAddProperty("stampingEventsToday", macPerfCnt);
				Memory.clearAndAddProperty("perShift",ShiftDetails.shiftTiming() );
				
				refreshPropertyFile();
			}
		}
	}

	public static void QualCount() throws NumberFormatException, IOException, ParseException {
		if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("date")) {
		if (currentDate.equals(Memory.getAppProperties().getString("qualCurrentDate"))) {
			macQualityCount = Integer.parseInt(Memory.getAppProperties().getString("qualityCount"));
			macQualityCount++;
			//log.info("STAMPINGQUALITYEVENTTODAY before :::::::::: "+ Memory.getAppProperties().getString("qualityCount"));
			String macQualCount = Integer.toString(macQualityCount++);
			Memory.clearAndAddProperty("qualityCount", macQualCount);
			refreshPropertyFile();
			//log.info("STAMPINGQualityEVENTTODAY:::::::::: " + Memory.getAppProperties().getString("qualityCount"));
		} else {
			macQualityCount=0;
			macQualityCount++;
			String macQualCount = Integer.toString(macQualityCount++);
			Memory.clearAndAddProperty("qualityCount", macQualCount);
			Memory.clearAndAddProperty("qualCurrentDate", currentDate);
			refreshPropertyFile();
		}}else if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("shift")) {
			if (Memory.getAppProperties().getString("qualShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
				macQualityCount = Integer.parseInt(Memory.getAppProperties().getString("qualityCount"));
				macQualityCount++;
				//log.info("STAMPINGQUALITYEVENTTODAY before :::::::::: "+ Memory.getAppProperties().getString("qualityCount"));
				String macQualCount = Integer.toString(macQualityCount++);
				Memory.clearAndAddProperty("qualityCount", macQualCount);
				refreshPropertyFile();
				//log.info("STAMPINGQualityEVENTTODAY:::::::::: " + Memory.getAppProperties().getString("qualityCount"));
			} else {
				macQualityCount=0;
				macQualityCount++;
				String macQualCount = Integer.toString(macQualityCount++);
				Memory.clearAndAddProperty("qualityCount", macQualCount);
				//Memory.clearAndAddProperty("qualCurrentDate", currentDate);
				Memory.clearAndAddProperty("qualShift",ShiftDetails.shiftTiming() );
				refreshPropertyFile();
			}}
	}

	public static void AvailCount() throws NumberFormatException, IOException, ParseException {
		if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("date")) {
		if (currentDate.equals(Memory.getAppProperties().getString("availCurrentDate"))) {
			macAvailCount = Integer.parseInt(Memory.getAppProperties().getString("availCount"));
			macAvailCount++;
			//log.info("STAMPINGAvailabityEVENTTODAY before :::::::::: "+ Memory.getAppProperties().getString("availCount"));
			String macAvailabilityCount = Integer.toString(macAvailCount++);
			Memory.clearAndAddProperty("availCount", macAvailabilityCount);
			refreshPropertyFile();
			//log.info("STAMPINGAvailabityEVENTTODAY:::::::::: " + Memory.getAppProperties().getString("availCount"));
		} else {
			macAvailCount=0;
			macAvailCount++;
			String macAvailabilityCount = Integer.toString(macAvailCount++);
			Memory.clearAndAddProperty("availCount", macAvailabilityCount);
			Memory.clearAndAddProperty("availCurrentDate", currentDate);
			refreshPropertyFile();
		}
		}else if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("shift")) {
			if (Memory.getAppProperties().getString("availShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
				macAvailCount = Integer.parseInt(Memory.getAppProperties().getString("availCount"));
				macAvailCount++;
				//log.info("STAMPINGAvailabityEVENTTODAY before :::::::::: "+ Memory.getAppProperties().getString("availCount"));
				String macAvailabilityCount = Integer.toString(macAvailCount++);
				Memory.clearAndAddProperty("availCount", macAvailabilityCount);
				refreshPropertyFile();
				//log.info("STAMPINGAvailabityEVENTTODAY:::::::::: " + Memory.getAppProperties().getString("availCount"));
			} else {
				macAvailCount=0;
				macAvailCount++;
				String macAvailabilityCount = Integer.toString(macAvailCount++);
				Memory.clearAndAddProperty("availCount", macAvailabilityCount);
				//Memory.clearAndAddProperty("availCurrentDate", currentDate);
				Memory.clearAndAddProperty("availShift",ShiftDetails.shiftTiming() );
				refreshPropertyFile();
			}
				
			}
	}

public static void perPayloadUpdation(JSONObject payload,String StampingCount) throws IOException {

		
		totalProdCount =StampingCount;
		eventFileWrite(payload);

	}

	public static void qualPayloadUpdation(JSONObject payload,String GoodCount) throws IOException {

		//goodCount = QualityEventGen.payloadJson.get("goodCount");
		totalGoodCount=GoodCount;
		eventFileWrite(payload);

	}

	public static void availPayloadUpdation(JSONObject payload) throws IOException {
		eventFileWrite(payload);

	}

	public static String getEventsToday() throws IOException {

		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		FileInputStream file = null;
		ArrayList<String> bandWidth;
		File f = new File(FieldConstants.path);
		bandWidth = new ArrayList<String>();
		FileInputStream in = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String tmp;
		try {
			while ((tmp = br.readLine()) != null) {
				bandWidth.add(tmp);
				if (bandWidth.size() == 11)

					bandWidth.remove(0);
				perLine = bandWidth.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> reversedFive = new ArrayList<String>();
		for (int i = bandWidth.size() - 1; i >= 0; i--)
			reversedFive.add(bandWidth.get(i));
		in.close();

		return bandWidth.toString();
	}

	public static void eventFileWrite(JSONObject payload) {

		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		File f = new File(FieldConstants.path);
		//eventGenPer = "[" + sdf.format(date) + "] : " + payload;
		eventGenPer = " " + payload;
		try {
			EventsFunction.write(eventGenPer, f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
