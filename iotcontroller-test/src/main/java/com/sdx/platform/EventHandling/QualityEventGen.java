package com.sdx.platform.EventHandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.camel.CamelExecutionException;
import org.json.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.sdx.platform.config.Memory;
import com.sdx.platform.quartz.service.impl.AppProperties;
import com.sdx.platform.util.db.EventDbPush;
import com.sdx.platform.util.db.entities.MachineMetricsFailures;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QualityEventGen {

	static JSONObject payloadJson = new JSONObject();
	ShiftDetails shiftdetails = new ShiftDetails();
	
	public static JSONObject buildQualityPayloadModule(String stampingGoodCount)
			throws IOException, JSONException, ParseException {
		 Date date1 = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = dateFormat.format(date1);
			LocalTime time = LocalTime.now();
			if((ShiftDetails.shiftTiming())!=null) {
		payloadJson.put("shiftNo", ShiftDetails.shiftTiming());
		payloadJson.put("stationCode", Memory.getIotProperties().getString("stationCode1"));
		payloadJson.put("stationName", Memory.getIotProperties().getString("stationName1"));
		payloadJson.put("date",strDate );
		payloadJson.put("goodCount", stampingGoodCount);
		payloadJson.put("metricType", "QUALITY");
		payloadJson.put("time",String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()) );
		
		log.info("Machine payloadJson :::::::::::::" + payloadJson);
		try {
			RunSedaApp.producerTemplate = RunSedaApp.camelContext.createProducerTemplate();
			RunSedaApp.producerTemplate.requestBodyAndHeader(SedaEventDistributer.SEDA_START_ROUTE, payloadJson, "OEE",
					"MachineQuality");
			Memory.setStatus("Flowwrap Machine is running and GoodPieces are " +stampingGoodCount);
		} catch (CamelExecutionException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.debug(stack.toString());
			if(Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
				EventDbPush.getInstance().machineMetricFailureInsert(payloadJson.toString(), "Q",
						"Connection time out Exception");
			}
			
		}
			}else {
				log.info("There is no shift to send payloadEvents");
			}
				
		return payloadJson;

	}


	
	public static void buildFailureQualityPayload(JSONObject qualPayloadJson, MachineMetricsFailures machineMetrics)
			throws SQLException {

		boolean pushStatus;
		String streamType = machineMetrics.getStreamtype();
		log.info("payloadJson for retry quality :::::::::::::" + qualPayloadJson);
		try {
			pushStatus = true;
			RunSedaApp.producerTemplate = RunSedaFailApp.camelContext.createProducerTemplate();
			RunSedaApp.producerTemplate.requestBodyAndHeader(FailureEventDistributer.SEDA_START_ROUTE, qualPayloadJson,
					"OEE", "MachineQuality");

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