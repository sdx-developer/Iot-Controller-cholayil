package com.sdx.platform.quartz.service.impl;

import com.sdx.platform.EventHandling.CommonUtil;
import com.sdx.platform.EventHandling.EventsFunction;
import com.sdx.platform.EventHandling.PerformanceEventGen;
import com.sdx.platform.EventHandling.QualityEventGen;
import com.sdx.platform.EventHandling.SedaEventDistributer;
import com.sdx.platform.EventHandling.ShiftDetails;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdx.platform.config.Memory;

import com.sdx.platform.domain.User;

import groovy.lang.GroovyObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/sdx/cds/properties")
public class AppProperties {

	String perLine;

	@javax.ws.rs.core.Context
	ServletContext context;
	static CommonUtil commonUtil = new CommonUtil();
	String currentDate = commonUtil.dateToStringConv(new Date());

	@GET
	@Produces("application/json")
	public String getAppProperties() {

		// InputStream in =
		// AppProperties.class.getResourceAsStream("/com/sdx/platform/jsons/propsArray.json");
		try {

			GroovyObject gryObject = Memory.getGroovyObjects().get("AppProperties");
			String json = (String) gryObject.invokeMethod("buildJUI", null);

			log.info("JSON is :::: " + json);
			return json;

			/*
			 * StringWriter writer = new StringWriter(); IOUtils.copy(in, writer,
			 * Charset.defaultCharset()); return writer.toString();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * //src\main\resources\com\sdx\platform\jsons\propsArray.json String base =
		 * context.getRealPath(""); System.out.println("base >>>>>>>>>>>>>> "+base);
		 * File findFile = new File(String.format("%s/%s", base, ""));
		 * System.out.println("Finding File "+findFile.getAbsolutePath()); try { return
		 * FileUtils.readFileToString(findFile, Charset.defaultCharset()); } catch
		 * (IOException e) { log.error("Error Occured", e); }
		 */
		return "{Error Occured}";
	}

	@GET
	@Produces("application/json")
	@Path("/shiftDetails")
	public String getShiftDetails() {

	//	System.out.println("INSIDE SHIFT CHECK::::::::::::::::::");

		try {

			GroovyObject gryObject = Memory.getGroovyObjects().get("ShiftDetails");
			String json = (String) gryObject.invokeMethod("buildJUI", null);

			log.info("JSON is :::: " + json);
			return json;

			/*
			 * StringWriter writer = new StringWriter(); IOUtils.copy(in, writer,
			 * Charset.defaultCharset()); return writer.toString();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{Error Occured}";
	}

	@POST
	@Consumes("application/json")
	@Path("/updateShiftDetails")
	public Response updateShiftdetailsandRUn(String receiveObject)
			throws URISyntaxException, CompilationFailedException, ParseException, InterruptedException, IOException {
		log.info("ReceiveObject STR {" + receiveObject.getClass() + "} " + receiveObject);
		ObjectMapper mapper = new ObjectMapper();

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(receiveObject, Map.class);
			map.entrySet().stream().forEach(

					es -> {
						log.info("ES " + es.getKey() + ", {" + es.getValue().getClass() + "} " + es.getValue());
						Memory.clearAndAddProperty(es.getKey(), es.getValue());
					}

			);
			Memory.getAppProperties().getKeys().forEachRemaining(
					prop -> log.info("Added " + prop + ", value " + Memory.getAppProperties().getProperty(prop)));
			if (Memory.getAppPropBuilder() != null) {
				try {
					Memory.getAppPropBuilder().save();
					Memory.refreshProperties();
				} catch (ConfigurationException e) {
					e.printStackTrace();
				}
			}
			String[] args = null;
			//ShiftScheduler.main(args);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return Response.status(201).entity("success:updated").build();
	}

	@POST
	@Consumes("application/json")
	public Response createUser(User user) throws URISyntaxException {
		if (user.getFName() == null || user.getLName() == null) {
			return Response.status(400).entity("Please provide all mandatory inputs").build();
		}
		// Mongo Insert
		return Response.status(201).build();
	}

	@POST
	@Consumes("application/json")
	@Path("/loginScreen")
	public String login(String data) throws URISyntaxException {

		HashMap<String, String> myMap = new HashMap<String, String>();
		String[] pairs = data.split("&");
		for (int i = 0; i < pairs.length; i++) {
			String pair = pairs[i];
			String[] keyValue = pair.split("=");
			myMap.put(keyValue[0], String.valueOf(keyValue[1]));
		}

		if (Memory.getAppProperties().getString("userName").equals(myMap.get("username"))
				&& Memory.getAppProperties().getString("password").equals(myMap.get("password"))) {

			return "true";
		}

		return "false";

	}

	@POST
	@Consumes("application/json")
	@Path("/update")
	public Response updateProperties(String receiveObject) throws URISyntaxException {
		log.info("ReceiveObject STR {" + receiveObject.getClass() + "} " + receiveObject);
		ObjectMapper mapper = new ObjectMapper();

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(receiveObject, Map.class);
			map.entrySet().stream().forEach(

					es -> {
						log.info("ES " + es.getKey() + ", {" + es.getValue().getClass() + "} " + es.getValue());
						Memory.clearAndAddProperty(es.getKey(), es.getValue());
					}

			);
			Memory.getAppProperties().getKeys().forEachRemaining(
					prop -> log.info("Added " + prop + ", value " + Memory.getAppProperties().getProperty(prop)));
			if (Memory.getAppPropBuilder() != null) {
				try {
					Memory.getAppPropBuilder().save();
					Memory.refreshProperties();
				} catch (ConfigurationException e) {
					e.printStackTrace();
				}
			}

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return Response.status(201).entity("success:updated").build();
	}

	@GET
	@Produces("application/json")
	@Path("/StampingEventsToday")
	public String getStampingEventsToday() {
		String stampingCount=null;
        if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("date")) {
		if (currentDate.equals(Memory.getAppProperties().getString("perCurrentDate"))) {
			stampingCount = Memory.getAppProperties().getString("stampingEventsToday");
		} else {
			stampingCount = "0";
		}
}
        else if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("shift")){
	
	try {
		if (Memory.getAppProperties().getString("perShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
			stampingCount = Memory.getAppProperties().getString("stampingEventsToday");
		} else {
			stampingCount = "0";
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
		return stampingCount;
		
		
	}

	@GET
	@Produces("application/json")
	@Path("/AllEventsToday")
	public int getAllEventsToday() throws IOException, ParseException {
		int allEventsToday=0;
		String performanceCount;
		String qualityCount;
		String availabilityCount;
		if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("date")) {
		if (currentDate.equals(Memory.getAppProperties().getString("perCurrentDate"))) {
			performanceCount = Memory.getAppProperties().getString("stampingEventsToday");
		} else {
			performanceCount = "0";

		}
		if (currentDate.equals(Memory.getAppProperties().getString("qualCurrentDate"))) {
			qualityCount = Memory.getAppProperties().getString("qualityCount");
		} else {
			qualityCount = "0";

		}
		if (currentDate.equals(Memory.getAppProperties().getString("availCurrentDate"))) {
			availabilityCount = Memory.getAppProperties().getString("availCount");
		} else {
			availabilityCount = "0";

		}int macPerCount = Integer.parseInt(performanceCount);
		int macQualCount = Integer.parseInt(qualityCount);
		int macAvailCount = Integer.parseInt(availabilityCount);
		allEventsToday = macPerCount + macQualCount + macAvailCount;

		
		}   else if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("shift")){
			
			if (Memory.getAppProperties().getString("perShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
				performanceCount = Memory.getAppProperties().getString("stampingEventsToday");
			} else {
				performanceCount = "0";

			}
			if  (Memory.getAppProperties().getString("qualShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
				qualityCount = Memory.getAppProperties().getString("qualityCount");
			} else {
				qualityCount = "0";

			}
			if  (Memory.getAppProperties().getString("availShift").equalsIgnoreCase(ShiftDetails.shiftTiming())){
				availabilityCount = Memory.getAppProperties().getString("availCount");
			} else {
				availabilityCount = "0";

			}int macPerCount = Integer.parseInt(performanceCount);
			int macQualCount = Integer.parseInt(qualityCount);
			int macAvailCount = Integer.parseInt(availabilityCount);
			allEventsToday = macPerCount + macQualCount + macAvailCount;

		}
		
		return allEventsToday;
	}

	@GET
	@Produces("application/json")
	@Path("/LatestStampingCount")
	public String getLatestStampingCount() {

		return EventsFunction.totalProdCount;

	}

	@GET
	@Produces("application/json")
	@Path("/LatestFlowWrapCount")
	public String getLatestFlowWrapCount() {

		return EventsFunction.totalGoodCount;

	}

	@GET
	@Produces("application/json")
	@Path("/performance")
	public String PerformancePayload() {

		return EventsFunction.perLine;
	}

	@GET
	@Produces("application/json")
	@Path("/quality")
	public String QualityPayload() throws IOException {
		perLine = EventsFunction.getEventsToday();
		// System.out.println("Read File ::::::: " + perLine);
		return perLine;
	}

	@GET
	@Produces("application/json")
	@Path("/errorCount")
	public int getErrorCount() throws IOException, ParseException {
		int ErrorCount=0;
		String ErrCountPer;
		String ErrCountQual;
		// log.info("::::::::::errorCountPer" +
		// Memory.getAppProperties().getString("errorCountPer"));
		// log.info("::::::::::errorCountQual" +
		// Memory.getAppProperties().getString("errorCountQual"));
		if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("date")) {
		if (currentDate.equals(Memory.getAppProperties().getString("perErrCountCurrentDate"))) {
			ErrCountPer = Memory.getAppProperties().getString("errorCountPer");
		} else {
			ErrCountPer = "0";
		}

		if (currentDate.equals(Memory.getAppProperties().getString("qualErrCountCurrentDate"))) {
			ErrCountQual = Memory.getAppProperties().getString("errorCountQual");
		} else {
			ErrCountQual = "0";
		}

		int macErrorCountQual = Integer.parseInt(ErrCountQual);
		int macErrorCountPer = Integer.parseInt(ErrCountPer);
		ErrorCount = macErrorCountQual + macErrorCountPer;
		}else if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("shift")){
			if (Memory.getAppProperties().getString("perErrorShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
				ErrCountPer = Memory.getAppProperties().getString("errorCountPer");
			} else {
				ErrCountPer = "0";
			}

			if  (Memory.getAppProperties().getString("qualErrorShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
				ErrCountQual = Memory.getAppProperties().getString("errorCountQual");
			} else {
				ErrCountQual = "0";
			}

			int macErrorCountQual = Integer.parseInt(ErrCountQual);
			int macErrorCountPer = Integer.parseInt(ErrCountPer);
			ErrorCount = macErrorCountQual + macErrorCountPer;
			}
		return ErrorCount;
	}

	/*
	 * @GET
	 * 
	 * @Produces("application/json")
	 * 
	 * @Path("/realTimeStatus") public String getrealTimeStatus() { String status =
	 * Memory.getStatus();
	 * //System.out.println("HET REAL TIME STATUS::::::"+status);
	 * 
	 * return status;
	 * 
	 * }
	 */

	@GET
	@Produces("application/json")
	@Path("/realTimeStats")
	public HashMap realTimeStatus() {
		String status = Memory.getStatus();
		HashMap realTimeData = new HashMap();

		/*
		 * if (Memory.getRealTimestatus().equalsIgnoreCase("Reset")) {
		 * 
		 * Memory.setRealTimeShiftST(" "); Memory.setRealTimeShiftET(" ");
		 * Memory.setRealTimeTotalPC(" "); Memory.setRealTimeTotalGC(" ");
		 * Memory.setRealTimeMachineSDT(" "); Memory.setRealTimeMachineEDT(" ");
		 * Memory.setRealTimeFlowrapCount(" "); Memory.setRealTimeShiftNo(" ");
		 * Memory.setRealTimestatus(" "); Memory.setDownTimeReason(" ");
		 * Memory.setRealTimeStampingCount("");
		 * 
		 * }
		 */

		int allEventsToday;
		String performanceCount;
		String qualityCount;
		String availabilityCount;

		performanceCount = Memory.getAppProperties().getString("stampingEventsToday");

		qualityCount = Memory.getAppProperties().getString("qualityCount");

		availabilityCount = Memory.getAppProperties().getString("availCount");

		int macPerCount = Integer.parseInt(performanceCount);
		int macQualCount = Integer.parseInt(qualityCount);
		int macAvailCount = Integer.parseInt(availabilityCount);
		allEventsToday = macPerCount + macQualCount + macAvailCount;

		realTimeData.put("shiftStartTime", Memory.getRealTimeShiftST());
		realTimeData.put("shiftEndTime", Memory.getRealTimeShiftET());
		realTimeData.put("TotalProduction", Memory.getRealTimeTotalPC());
		realTimeData.put("totalGoodCount", Memory.getRealTimeTotalGC());
		realTimeData.put("machineSDT", Memory.getRealTimeMachineSDT());
		realTimeData.put("machineEDT", Memory.getRealTimeMachineEDT());
		realTimeData.put("flowrapCount", Memory.getRealTimeFlowrapCount());
		realTimeData.put("stampingCount", Memory.getRealTimeStampingCount());
		realTimeData.put("shiftNo", "ShiftNo:" + Memory.getRealTimeShiftNo());
		realTimeData.put("status", Memory.getStatus());
		realTimeData.put("downTimeReason", Memory.getDownTimeReason());
		realTimeData.put("stampingEventToday", Memory.getAppProperties().getString("stampingEventsToday"));
		realTimeData.put("allEventsToday", allEventsToday);
		System.out.println("Realtime/00000000000000" + realTimeData);

		return realTimeData;

	}

	public String StampingStatus() {

		String Stampingstatus = " ";
		return Stampingstatus;
	}

	public String FlowrapStatus() {

		String Flowrapstatus = "";
		return Flowrapstatus;
	}

}
