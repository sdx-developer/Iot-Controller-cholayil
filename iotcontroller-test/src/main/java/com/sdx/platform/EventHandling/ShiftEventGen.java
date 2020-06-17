package com.sdx.platform.EventHandling;

import java.io.IOException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpMessage;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelExecutionException;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;

import com.alibaba.fastjson.JSONObject;
import com.sdx.platform.config.Memory;
import com.sdx.platform.config.*;
import com.sdx.platform.util.db.EventDbPush;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShiftEventGen {
	static JSONObject ShiftStatusPayloadJson = new JSONObject();

	ShiftDetails shiftdetails = new ShiftDetails();

	public static void refreshPropertyFile() {
		// System.out.println("::::::::::::::::");
		if (Memory.getAppPropBuilder() != null) {
			try {
				Memory.getAppPropBuilder().save();
				Memory.refreshProperties();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	public static JSONObject buildShiftStatusPayloadModule() throws Exception {
		Date date1 = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(date1);
		String shift = ShiftDetails.shiftTiming();
		ShiftStatusPayloadJson.put("shiftNo", shift);
		ShiftStatusPayloadJson.put("stationCode", Memory.getIotProperties().getString("stationCode1"));
		ShiftStatusPayloadJson.put("stationName", Memory.getIotProperties().getString("stationName1"));
		ShiftStatusPayloadJson.put("date", strDate);
		ShiftStatusPayloadJson.put("machineStartTime", "");
		ShiftStatusPayloadJson.put("machineStopTime", "");
		// ShiftStatusPayloadJson.put("totalProductionPieces",
		// Memory.getAppProperties().getString("stampingCount"));
		// ShiftStatusPayloadJson.put("totalGoodPieces",
		// Memory.getAppProperties().getString("flowwrapCount"));

		if ("start".equalsIgnoreCase(Memory.getAppProperties().getString("shiftStatus"))) {
			ShiftStatusPayloadJson.put("action", "stop");
			Memory.clearAndAddProperty("shiftStatus", "stop");
			refreshPropertyFile();

		} else {
			ShiftStatusPayloadJson.put("action", "start");
			Memory.clearAndAddProperty("shiftStatus", "start");
			refreshPropertyFile();
		}

		if ((ShiftStatusPayloadJson.getString("action")).equalsIgnoreCase("start")) {
			LocalTime time = LocalTime.now();
			ShiftStatusPayloadJson.put("shiftStartTime",
					String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()));
			ShiftStatusPayloadJson.put("shiftEndTime", "");
			Memory.setStatus("Shift " + shift + " has started at " + String.format("%02d", time.getHour()) + ":"
					+ String.format("%02d", time.getMinute()));
			Memory.setRealTimeShiftST(
					String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()));
			Memory.setRealTimeShiftNo(ShiftDetails.shiftTiming());
		} else if ((ShiftStatusPayloadJson.getString("action")).equalsIgnoreCase("stop")) {
			LocalTime time = LocalTime.now();
			ShiftStatusPayloadJson.put("shiftStartTime", "");
			ShiftStatusPayloadJson.put("shiftEndTime",
					String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()));
			Memory.setStatus("Shift " + shift + " ended at " + String.format("%02d", time.getHour()) + ":"
					+ String.format("%02d", time.getMinute()));
			Memory.setRealTimeShiftET(
					String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()));
			Memory.setRealTimestatus("Reset");

		}

		if (ShiftStatusPayloadJson.getString("action").equalsIgnoreCase("start")) {
			ShiftStatusPayloadJson.put("totalProductionPieces", Memory.getAppProperties().getString("stampingCount"));
			ShiftStatusPayloadJson.put("totalGoodPieces", Memory.getAppProperties().getString("flowwrapCount"));

		} else {
			ShiftStatusPayloadJson.put("totalProductionPieces", " ");
			ShiftStatusPayloadJson.put("totalGoodPieces", " ");

		}

	
		log.info("ShiftStatusPayloadJson :::::::::::::" + ShiftStatusPayloadJson);

		try {
			RunSedaApp.producerTemplate = RunSedaApp.camelContext.createProducerTemplate();
			RunSedaApp.producerTemplate.requestBodyAndHeader(SedaEventDistributer.SEDA_START_ROUTE,
					ShiftStatusPayloadJson, "OEE", "MachineShiftStatus");
			try {

				// buildMachineAndShiftStatus("Shift_Status",
				// "shiftAction",ShiftStatusPayloadJson.getString("action") ,
				// ShiftStatusPayloadJson.getString("shiftNo"),ShiftStatusPayloadJson.getString("stationCode"),ShiftStatusPayloadJson.getString("stationName"));
				/*
				 * buildMachineAndShiftStatus("Shift_Status",
				 * "shiftAction",ShiftStatusPayloadJson.getString("action") ,
				 * ShiftStatusPayloadJson.getString("shiftNo"),ShiftStatusPayloadJson.getString(
				 * "stationCode"),ShiftStatusPayloadJson.getString("stationName"),
				 * ShiftStatusPayloadJson.getString("machineStartTime"),
				 * ShiftStatusPayloadJson.getString("machineStopTime")
				 * ,ShiftStatusPayloadJson.getString("shiftStartTime"),
				 * ShiftStatusPayloadJson.getString("shiftEndTime"),ShiftStatusPayloadJson.
				 * getString("date"));
				 * 
				 */
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		} catch (CamelExecutionException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.debug(stack.toString());

		}

		return ShiftStatusPayloadJson;

	}

	/*
	 * public static void buildMachineAndShiftStatus(String event, String action,
	 * String value,String shiftNo,String stationCode,String stationName) throws
	 * Exception {
	 * 
	 * String result = ""; String postEvent = "\"" + event + "\""; String postAction
	 * = "\"" + action + "\""; String postValue = "\"" + value + "\""; String
	 * postShift = "\"" + shiftNo + "\""; String postStationName = "\"" +
	 * stationName + "\""; String postStationCode = "\"" + stationCode + "\"";
	 * //String postTime = "\"" + time + "\"";
	 * 
	 * 
	 * HttpPost post = new
	 * HttpPost("https://qa.servicedx.com/subscription/events/execute");
	 * 
	 * post.addHeader("username", "superadmin"); post.addHeader("content-type",
	 * "application/json");
	 * 
	 * //String test =
	 * "{ \"application\": \"StampingMachine\", \"asset\": \"TAD/EQP/084\", \"event\": "
	 * + postEvent //+ ", \"tenant\": \"string\", \"variables\": {" + postAction +
	 * ":" + postValue + "}}";
	 * 
	 * String test =
	 * "{ \"application\": \"StampingMachine\", \"asset\": \"TAD/EQP/084\", \"event\": "
	 * +postEvent+", \"tenant\": \"string\", \"variables\": {"+postAction+":"+
	 * postValue+",\"shiftNo\":"+postShift+",\"stationName\":"+postStationName+
	 * ",\"stationCode\":"+postStationCode+"}}";
	 * 
	 * System.out.println(); post.setEntity(new StringEntity(test,
	 * Charset.defaultCharset())); System.out.println(":::::::::values" + test);
	 * 
	 * try (CloseableHttpClient httpClient = HttpClients.createDefault();
	 * CloseableHttpResponse response = httpClient.execute(post)) {
	 * 
	 * System.out.println("response.getAllHeaders() " + Arrays.asList(
	 * response.getStatusLine() +
	 * "*****************************************************************")); //
	 * result = EntityUtils.toString(response.getEntity());
	 * 
	 * } }
	 */

	public static void buildMachineAndShiftStatus(String event, String action, String value, String shiftNo,
			String stationCode, String stationName, String machineStart, String machineStop, String shiftStart,
			String shiftStop, String date) throws Exception {

		String result = "";
		String postEvent = "\"" + event + "\"";
		String postAction = "\"" + action + "\"";
		String postValue = "\"" + value + "\"";
		String postShift = "\"" + shiftNo + "\"";
		String postStationName = "\"" + stationName + "\"";
		String postStationCode = "\"" + stationCode + "\"";
		String postMachineStart = "\"" + machineStart + "\"";
		String postMachineStop = "\"" + machineStop + "\"";
		String postShiftStart = "\"" + shiftStart + "\"";
		String postshiftEnd = "\"" + shiftStop + "\"";
		String postDate = "\"" + date + "\"";

		HttpPost post = new HttpPost("https://qa.servicedx.com/subscription/events/execute");

		post.addHeader("username", "superadmin");
		post.addHeader("content-type", "application/json");

		/*
		 * String test =
		 * "{ \"application\": \"StampingMachine\", \"asset\": \"TAD/EQP/084\", \"event\": "
		 * + postEvent + ", \"tenant\": \"string\", \"variables\": {" + postAction + ":"
		 * + postValue + ",\"shiftNo\":" + postShift + ",\"stationName\":" +
		 * postStationName + ",\"stationCode\":" + postStationCode + "}}";
		 */

		String test = "{ \"application\": \"StampingMachine\", \"asset\": \"TAD/EQP/084\", \"event\": " + postEvent
				+ ", \"tenant\": \"string\", \"variables\": {" + postAction + ":" + postValue + ",\"machineStartTime\":"
				+ postMachineStart + ",\"shiftNo\":" + postShift + ",\"stationCode\":" + postStationCode
				+ ",\"stationName\":" + postStationName + ",\"shiftStartTime\":" + postShiftStart + ",\"date\":"
				+ postDate + ",\"shiftEndTime\":" + postshiftEnd + ",\"machineStopTime\":" + postMachineStop + "}}";

		System.out.println();
		post.setEntity(new StringEntity(test, Charset.defaultCharset()));
		System.out.println(":::::::::values" + test);

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {

			System.out.println("response.getAllHeaders() " + Arrays.asList(
					response.getStatusLine() + "*****************************************************************")); //

		}
	}

}
