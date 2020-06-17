package com.sdx.platform.EventHandling;

import java.util.ArrayList;
import com.alibaba.fastjson.JSON;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;

//import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import org.apache.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.text.ParseException;

import com.sdx.platform.config.Memory;
import com.sdx.platform.util.db.EventDbPush;
import com.sdx.platform.EventHandling.CommonUtil;

public class SedaEventDistributer extends RouteBuilder {
	CommonUtil commonUtil = new CommonUtil();

	static Logger LOG = LoggerFactory.getLogger(SedaEventDistributer.class);

	public static final String SEDA_START_ROUTE = "seda:start";
	public static final String SEDA_END_ROUTE = "seda:end";
	public static int threadCnt = 20;
	String currentDate = commonUtil.dateToStringConv(new Date());

	@Override
	public void configure() {

		if (Memory.getAppProperties().getString("streamFlavor").equals("HTTP(S)")) {
			System.out.println("inside the seda:::::::::::::::::::::::");

			from(SEDA_START_ROUTE).routeId("EventDistribution").threads()
					.executorService(Executors.newFixedThreadPool(threadCnt))
					.setHeader(Exchange.HTTP_METHOD, constant("POST"))
					.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
					.setHeader(Exchange.HTTP_QUERY, constant("userId=123&userName=cAMEL")).convertBodyTo(String.class)
					.log(LoggingLevel.INFO, "::::::::::::::::::::::::::::::::in Seda event distributer").choice()
					.when(header("OEE").isEqualTo("MachinePerformance"))
					.to((Memory.getAppProperties().getString("HttpsUrl")) + FieldConstants.stampingPerformanceURL)
					.process(new Processor() {
						public void process(Exchange exchange) throws IOException, NumberFormatException, ParseException { // TODO

							HttpRequest response = (HttpRequest) exchange.getIn().getBody(HttpServletResponse.class);
							Object res = exchange.getIn().getHeaders().get("OEE");
							System.out.println("::::header" + res);

							String resbody = exchange.getIn().getBody(String.class);
							Object obj;
							JSONObject jsonrep = null;
							JSONObject jsonrespbody = new JSONObject();
							obj = JSON.parse(resbody);

							jsonrep = (JSONObject) obj;
							jsonrespbody.put("stationCode", jsonrep.get("stationCode"));
							jsonrespbody.put("metricType", jsonrep.get("metricType"));
							jsonrespbody.put("totalProductionCount", jsonrep.get("totalProductionCount"));
							jsonrespbody.put("stationName", jsonrep.get("stationName"));
							jsonrespbody.put("shiftNo", jsonrep.get("shiftNo"));
							jsonrespbody.put("date", jsonrep.get("date"));
							jsonrespbody.put("time", jsonrep.get("time"));
							
							System.out.println("HTTPS RESPONSE BODY::::::::" + jsonrespbody);
							System.out.println("stampingcount" + jsonrep.get("totalProductionCount"));

							String stampingcount = jsonrep.get("totalProductionCount").toString();
							log.info(stampingcount);
							System.out.println("Date:::::::::::" + currentDate);
							EventsFunction.perfCount();
							EventsFunction.perPayloadUpdation(jsonrespbody, stampingcount);
							System.out.println("performancetest::::::::::::::" + jsonrespbody.toString());
							try {
								if (Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
									EventDbPush.getInstance().machineMetricInsert(jsonrespbody.toString(), "P","Success");
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}
					})

					.when(header("OEE").isEqualTo("MachineQuality"))
					.to((Memory.getAppProperties().getString("HttpsUrl")) + FieldConstants.stampingQualityURL)
					.process(new Processor() {
						public void process(Exchange exchange) throws IOException, NumberFormatException, ParseException { // TODO
							HttpRequest response = (HttpRequest) exchange.getIn().getBody(HttpServletResponse.class);
							Map<String, Object> res = exchange.getIn().getHeaders();
							String resbody = exchange.getIn().getBody(String.class);
							Object obj;
							JSONObject jsonrep = null;
							JSONObject jsonrespbody = new JSONObject();
							obj = JSON.parse(resbody);

							jsonrep = (JSONObject) obj;
							jsonrespbody.put("stationCode", jsonrep.get("stationCode"));
							jsonrespbody.put("metricType", jsonrep.get("metricType"));
							jsonrespbody.put("goodCount", jsonrep.get("goodCount"));
							jsonrespbody.put("stationName", jsonrep.get("stationName"));
							jsonrespbody.put("shiftNo", jsonrep.get("shiftNo"));
							jsonrespbody.put("date", jsonrep.get("date"));
							jsonrespbody.put("time", jsonrep.get("time"));
							
							String GoodCount = jsonrep.get("goodCount").toString();
							log.info(GoodCount);

							EventsFunction.QualCount();
							EventsFunction.qualPayloadUpdation(jsonrespbody,GoodCount);
							try {
								if (Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
									EventDbPush.getInstance().machineMetricInsert(jsonrespbody.toString(), "Q","Success");
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}


						}
					})

					.when(header("OEE").isEqualTo("MachineAvailability"))
					.to((Memory.getAppProperties().getString("HttpsUrl")) + FieldConstants.stampingAvailabilityURL)
					.process(new Processor() {
						public void process(Exchange exchange) throws IOException, NumberFormatException, ParseException { // TODO
							HttpRequest response = (HttpRequest) exchange.getIn().getBody(HttpServletResponse.class);
							Map<String, Object> res = exchange.getIn().getHeaders();
							String resbody = exchange.getIn().getBody(String.class);
							Object obj;
							JSONObject jsonrep = null;
							JSONObject jsonrespbody = new JSONObject();
							obj = JSON.parse(resbody);
							System.out.println("::::::::::::::::" + resbody);
							jsonrep = (JSONObject) obj;
							if (jsonrep.get("downEndTime") != null) {
								jsonrespbody.put("stationCode", jsonrep.get("stationCode"));
								jsonrespbody.put("metricType", "stop");
								jsonrespbody.put("stationName", jsonrep.get("stationName"));
								jsonrespbody.put("shiftNo", jsonrep.get("shiftNo"));
								jsonrespbody.put("downEndTime",jsonrep.get("downEndTime"));
								jsonrespbody.put("downtimeDate", jsonrep.get("downtimeDate"));
								System.out.println("inside stop" + jsonrespbody);
							} else {
								jsonrespbody.put("stationCode", jsonrep.get("stationCode"));
								jsonrespbody.put("metricType", "start");
								jsonrespbody.put("stationName", jsonrep.get("stationName"));
								jsonrespbody.put("shiftNo", jsonrep.get("shiftNo"));
								jsonrespbody.put("downStartTime", jsonrep.get("downStartTime"));
								jsonrespbody.put("downtimeDate", jsonrep.get("downtimeDate"));
								System.out.println("inside start" + jsonrespbody);

							}
							EventsFunction.AvailCount();
							EventsFunction.availPayloadUpdation(jsonrespbody);
							System.out.println("Availablility" + jsonrespbody);
							try {
								if (Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
									EventDbPush.getInstance().machineMetricInsert(jsonrespbody.toString(), "A","Success");
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							

						}
					})
					.when(header("OEE").isEqualTo("MachineShiftStatus"))
					.to((Memory.getAppProperties().getString("HttpsUrl")) +  FieldConstants.shifStatusurl)
					.process(new Processor() {
						public void process(Exchange exchange) throws IOException { // TODO
							HttpRequest response = (HttpRequest) exchange.getIn().getBody(HttpServletResponse.class);
							Map<String, Object> res = exchange.getIn().getHeaders();
							String resbody = exchange.getIn().getBody(String.class);
							System.out.println("responsebody::::::::::::::::for shift"+resbody);
							
							
							Object obj;
							JSONObject jsonrep = null;
							JSONObject jsonrespbody = new JSONObject();
							obj = JSON.parse(resbody);

							jsonrep = (JSONObject) obj;
							
							if (jsonrep.get("shiftEndTime") != null) {
								System.out.println("inside to stop");
								
								Memory.getAppProperties().setProperty("stampingIOTStatus", "stop");
						     }

							/*
							 * EventsFunction.QualCount(); EventsFunction.qualPayloadUpdation(jsonrespbody);
							 * 
							 * try { if
							 * (Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase(
							 * "True")) {
							 * EventDbPush.getInstance().machineMetricInsert(jsonrespbody.toString(), "Q");
							 * } } catch (Exception e1) { // TODO Auto-generated catch block
							 * e1.printStackTrace(); }
							 */

						}
					})

					
					.end();

		} else {

			log.info(":::::::::::::kafka");
			System.out.println("kafka:" + FieldConstants.kafkastampingPerformance + "?brokers="
					+ (Memory.getAppProperties().getString("kafka1Host")) + ":"
					+ (Memory.getAppProperties().getString("kafka1Port")) + "&groupId="
					+ (Memory.getAppProperties().getString("kafka1Params")));
			from(SEDA_START_ROUTE)

					.threads().executorService(Executors.newFixedThreadPool(threadCnt)).choice()
					.when(header("OEE").isEqualTo("MachinePerformance"))
					.to("kafka:" + FieldConstants.kafkastampingPerformance + "?brokers="
							+ (Memory.getAppProperties().getString("kafka1Host")) + ":"
							+ (Memory.getAppProperties().getString("kafka1Port")) + "&groupId="
							+ (Memory.getAppProperties().getString("kafka1Params")))
	
					.process(new Processor() {
						public void process(Exchange exchange) throws Exception { // TODO
							Map<String, Object> res = exchange.getIn().getHeaders();
							Object resbody = exchange.getIn().getBody();
							System.out.println("KAFHKHA RESPONSE BODY::::::::" + resbody);
							log.info("response ::::::::::" + res);
							log.info("httpRespCode ::::::::::" + exchange.getIn().getHeader("CamelHttpResponseCode"));
							System.out.println("Date:::::::::::" + currentDate);
							JSONObject jsonObject = null;
							jsonObject = (JSONObject) resbody;
							String stampingcount = (String) jsonObject.get("totalProductionCount");
							log.info(stampingcount);
							System.out.println("Date:::::::::::" + currentDate);
							EventsFunction.perfCount();
							EventsFunction.perPayloadUpdation(jsonObject, stampingcount);
							System.out.println("rsppppppppppppppp:::" + resbody.toString());
							try {
								if (Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
									EventDbPush.getInstance().machineMetricInsert(resbody.toString(), "P","Success");
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							

						}
					})

					.when(header("OEE").isEqualTo("MachineQuality"))
					.to("kafka:" + FieldConstants.kafkastampingQuality + "?brokers="
							+ (Memory.getAppProperties().getString("kafka1Host")) + ":"
							+ (Memory.getAppProperties().getString("kafka1Port")) + "&groupId="
							+ (Memory.getAppProperties().getString("kafka1Params")))
					.process(new Processor() {
						public void process(Exchange exchange) throws Exception { // TODO
							Map<String, Object> res = exchange.getIn().getHeaders();
							Object resbody = exchange.getIn().getBody();
							System.out.println("KAFHKHA RESPONSE BODY::::::::" + resbody);
							log.info("response ::::::::::" + res);
							log.info("httpRespCode ::::::::::" + exchange.getIn().getHeader("CamelHttpResponseCode"));
							JSONObject jsonObject = null;
							jsonObject = (JSONObject) resbody;
							String goodCount = (String) jsonObject.get("goodCount");
							log.info(goodCount);
							EventsFunction.QualCount();
							EventsFunction.qualPayloadUpdation(jsonObject,goodCount);
							try {
								if (Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
									EventDbPush.getInstance().machineMetricInsert(resbody.toString(), "Q","Success");
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					})

					.when(header("OEE").isEqualTo("MachineAvailability"))
					.to("kafka:" + FieldConstants.kafkastampingAvailability + "?brokers="
							+ (Memory.getAppProperties().getString("kafka1Host")) + ":"
							+ (Memory.getAppProperties().getString("kafka1Port")) + "&groupId="
							+ (Memory.getAppProperties().getString("kafka1Params")))
					.process(new Processor() {
						public void process(Exchange exchange) throws Exception { // TODO
							Map<String, Object> res = exchange.getIn().getHeaders();
							Object resbody = exchange.getIn().getBody();
							System.out.println("KAFHKHA RESPONSE BODY::::::::" + resbody);
							log.info("response ::::::::::" + res);
							log.info("httpRespCode ::::::::::" + exchange.getIn().getHeader("CamelHttpResponseCode"));
							JSONObject jsonObject = null;
							jsonObject = (JSONObject) resbody;
							EventsFunction.AvailCount();
							EventsFunction.availPayloadUpdation(jsonObject);
							try {
								if (Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
									EventDbPush.getInstance().machineMetricInsert(resbody.toString(), "A","Success");
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}

					})
					
					.when(header("OEE").isEqualTo("MachineShiftStatus"))
					.to("kafka:" + FieldConstants.kafkastampingshift + "?brokers="
							+ (Memory.getAppProperties().getString("kafka1Host")) + ":"
							+ (Memory.getAppProperties().getString("kafka1Port")) + "&groupId="
							+ (Memory.getAppProperties().getString("kafka1Params")))
					.process(new Processor() {
						public void process(Exchange exchange) throws Exception { // TODO
							Map<String, Object> res = exchange.getIn().getHeaders();
							Object resbody = exchange.getIn().getBody();
							System.out.println("KAFHKHA RESPONSE BODY::::::::" + resbody);
							log.info("response ::::::::::" + res);
							log.info("httpRespCode ::::::::::" + exchange.getIn().getHeader("CamelHttpResponseCode"));
							JSONObject jsonObject = null;
							jsonObject = (JSONObject) resbody;
							if (jsonObject.get("shiftEndTime") != null) {
								System.out.println("inside to stop");
								
								Memory.getAppProperties().setProperty("stampingIOTStatus", "stop");
								
							
							}
							
							/*try {
								if (Memory.getAppProperties().getString("enableDBStorage").equalsIgnoreCase("True")) {
									EventDbPush.getInstance().machineMetricInsert(resbody.toString(), "M");
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}*/
						}

					})
					
					.routeId("FromKafka").log("${body}").end();

			log.info(":::::::::::::end");
		}

	}
}