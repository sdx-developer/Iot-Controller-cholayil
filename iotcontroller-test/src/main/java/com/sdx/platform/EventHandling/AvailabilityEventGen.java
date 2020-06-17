package com.sdx.platform.EventHandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.camel.CamelExecutionException;
import org.apache.commons.io.Charsets;
import org.json.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.mchange.v2.cfg.PropertiesConfigSource.Parse;
import com.sdx.platform.config.Memory;
import com.sdx.platform.quartz.service.impl.AppProperties;
import com.sdx.platform.util.db.EventDbPush;
import com.sdx.platform.util.db.entities.MachineMetricsFailures;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvailabilityEventGen {
	static JSONObject perPayloadJson = new JSONObject();

	ShiftDetails shiftdetails = new ShiftDetails();

	public static JSONObject buildAvailabilityPayloadModule(String status)
			throws IOException, InterruptedException, JSONException, ParseException {

		Date date1 = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(date1);
		String shift = ShiftDetails.shiftTiming();
		if((ShiftDetails.shiftTiming())!=null) {
		perPayloadJson.put("metricType", "AVAILABILITY");
		perPayloadJson.put("shiftNo", shift);
		perPayloadJson.put("stationCode", Memory.getIotProperties().getString("stationCode1"));
		perPayloadJson.put("stationName", Memory.getIotProperties().getString("stationName1"));
		perPayloadJson.put("downtimeDate", strDate);
		perPayloadJson.put("action", status);
		perPayloadJson.put("downtimeReason"," ");
		if (status.equalsIgnoreCase("start")) {
			LocalTime time = LocalTime.now();
			perPayloadJson.put("downStartTime",
					String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()));
			perPayloadJson.put("downEndTime", "");

		} else if (status.equalsIgnoreCase("stop")) {
			LocalTime time = LocalTime.now();
			perPayloadJson.put("downStartTime", "");
			perPayloadJson.put("downEndTime",
					String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()));

		}
		
		

		
		log.info("payloadJson :::::::::::::" + perPayloadJson);

		try {
			RunSedaApp.producerTemplate = RunSedaApp.camelContext.createProducerTemplate();
			RunSedaApp.producerTemplate.requestBodyAndHeader(SedaEventDistributer.SEDA_START_ROUTE, perPayloadJson,
					"OEE", "MachineAvailability");
			if(status.equalsIgnoreCase("start")) {
				LocalTime time = LocalTime.now();
				Memory.setStatus("Downtime Started for shift " + shift + " " + String.format("%02d", time.getHour()) + ":"
						+ String.format("%02d", time.getMinute()));
				Memory.setRealTimeMachineSDT(String.format("%02d", time.getHour()) + ":"
						+ String.format("%02d", time.getMinute()));

			}else if(status.equalsIgnoreCase("stop")) {
				LocalTime time = LocalTime.now();
				Memory.setStatus("Downtime Ended for shift " + shift + " " + String.format("%02d", time.getHour()) + ":"
						+ String.format("%02d", time.getMinute()));
				Memory.setRealTimeMachineEDT(String.format("%02d", time.getHour()) + ":"
						+ String.format("%02d", time.getMinute()));
				Memory.setDownTimeReason(Memory.getAppProperties().getString("unPlannedDownTimereason"));


			}
			

		} catch (CamelExecutionException e) { 
			if(Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
				EventDbPush.getInstance().machineMetricFailureInsert(perPayloadJson.toString(), "A",
						"Connection time out Exception");
			}

		}
	}else {
		log.info("There is no shift to send payloadEvents");
	}
		
		return perPayloadJson;

	}

	public static void buildFailureAvailabilityPayload(JSONObject PayloadJson, MachineMetricsFailures machineMetrics)
			throws SQLException {
		boolean pushStatus;
		String streamType = machineMetrics.getStreamtype();
		log.info("payloadJson for retry Availability :::::::::::::" + PayloadJson);
		try {
			pushStatus = true;

			RunSedaApp.producerTemplate = RunSedaFailApp.camelContext.createProducerTemplate();
			RunSedaApp.producerTemplate.requestBodyAndHeader(FailureEventDistributer.SEDA_START_ROUTE, PayloadJson,
					"OEE", "MachineAvailability");

		} catch (CamelExecutionException e) {
			// TODO Auto-generated catch block
			pushStatus = false;
			e.printStackTrace();
			EventDbPush.getInstance().update(machineMetrics.getTransId(), machineMetrics.getRetrycount() + 1);

		}
		if (pushStatus == true) {
			System.out.println("i am true");
			EventDbPush.getInstance().delete(machineMetrics.getTransId());
		} else {
			System.out.println("i am not true");
		}
	}

}
