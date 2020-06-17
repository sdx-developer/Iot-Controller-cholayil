package com.sdx.platform.EventHandling;

import com.sdx.platform.util.db.EventDbPush;

import com.sdx.platform.util.db.entities.MachineMetricsFailures;

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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.camel.CamelExecutionException;
import org.apache.commons.io.Charsets;
import org.json.JSONException;
//import org.json.JSONObject;
import com.alibaba.fastjson.JSONObject;

import com.mchange.v2.cfg.PropertiesConfigSource.Parse;
import com.sdx.platform.config.Memory;
import com.sdx.platform.quartz.service.impl.AppProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PerformanceEventGen {
	static JSONObject perPayloadJson = new JSONObject();
	ShiftDetails shiftdetails = new ShiftDetails();
	

	public static JSONObject buildPerformancePayloadmodule(String StampingCount)
			throws IOException, InterruptedException, JSONException, ParseException, SQLException {
		 Date date1 = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = dateFormat.format(date1);
	if((ShiftDetails.shiftTiming())!=null) {
		LocalTime time = LocalTime.now();
		perPayloadJson.put("shiftNo", ShiftDetails.shiftTiming());
		perPayloadJson.put("stationCode", Memory.getIotProperties().getString("stationCode1"));
		perPayloadJson.put("stationName", Memory.getIotProperties().getString("stationName1"));
		perPayloadJson.put("date",strDate );
		perPayloadJson.put("time",String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()) );
		perPayloadJson.put("totalProductionCount", StampingCount);
		perPayloadJson.put("metricType", "PERFORMANCE");
		 log.info("payloadJson :::::::::::::" + perPayloadJson);
	  
		
		try {
			RunSedaApp.producerTemplate = RunSedaApp.camelContext.createProducerTemplate();
			RunSedaApp.producerTemplate.requestBodyAndHeader(SedaEventDistributer.SEDA_START_ROUTE, perPayloadJson,
					"OEE", "MachinePerformance");
			Memory.setStatus("Stamping machine is running and Total Produced pieces are " +StampingCount);
			System.out.println(":::::::::::::::::::::::::::::::::::::"+StampingCount);
			System.out.println(":::::after");
		} catch (CamelExecutionException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.debug(stack.toString());
			if(Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
				EventDbPush.getInstance().machineMetricFailureInsert(perPayloadJson.toString(), "P",
						"Connection time out Exception");
			}
			}
	}else {
		log.info("There is no shift to send payloadEvents");
	}
		return perPayloadJson;

	}

	public static void buildFailurePerformancePayload(JSONObject PayloadJson, MachineMetricsFailures machineMetrics)
			throws SQLException {

		boolean pushStatus;
		 String streamType = machineMetrics.getStreamtype();
		log.info("payloadJson for retry performance :::::::::::::" + PayloadJson);
		try {
			pushStatus = true;
            RunSedaApp.producerTemplate = RunSedaFailApp.camelContext.createProducerTemplate();
			RunSedaApp.producerTemplate.requestBodyAndHeader(FailureEventDistributer.SEDA_START_ROUTE, PayloadJson,
					"OEE", "MachinePerformance");
			
		}

		catch (Exception e) {
			pushStatus = false;
			e.printStackTrace();
			EventDbPush.getInstance().update(machineMetrics.getTransId(), machineMetrics.getRetrycount() + 1);

		}

		if (pushStatus == true) {
			System.out.println("i am true");
			System.out.println("id" + machineMetrics.getTransId());
			EventDbPush.getInstance().delete(machineMetrics.getTransId());
		} else {
			System.out.println("i am not true");
		}
	}

}
