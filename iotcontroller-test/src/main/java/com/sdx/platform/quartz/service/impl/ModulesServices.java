
package com.sdx.platform.quartz.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdx.platform.EventHandling.EventsFunction;
import com.sdx.platform.config.Memory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/sdx/cds/modules")
public class ModulesServices {

	@javax.ws.rs.core.Context
	ServletContext context;

	@GET
	@Path("/moduleList")
	@Produces("application/json")
	public String getListOfModules() {
		// System.out.println("**************** AVAILABLE MODULES *************
		// "+Memory.getModuleGroovyMapping().keySet());
		if (CollectionUtils.isNotEmpty(Memory.getModuleGroovyMapping().keySet())) {
			final StringWriter sw = new StringWriter();
			final ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(sw, Memory.getModuleGroovyMapping().keySet());
				return sw.toString();
			} catch (IOException e) {
				log.error("Error occured ", e);
			}

		}
		return "{\"error\" : \"No Modules available\"}";
	}

	@POST
	@Path("/fetch")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces("text/html")
	public String fetchModule(String receiveObject) {
		System.out.println("**************** AVAILABLE MODULES ************* "
				+ Memory.getModuleGroovyMapping().keySet() + ", receiveObject " + receiveObject);
		if (CollectionUtils.isNotEmpty(Memory.getModuleGroovyMapping().keySet())) {

			String base = context.getRealPath("");
			System.out.println("base >>>>>>>>>>>>>> " + base);
			File findFile = new File(String.format("%s/%s", base, "htmls/iotmodule.html"));
			System.out.println("Finding File " + findFile.getAbsolutePath());
			try {
				String retContent = FileUtils.readFileToString(findFile, Charset.defaultCharset());
				retContent = StringUtils.replaceAll(retContent, "FETCH_MOD_NAME", receiveObject);
				return retContent;
			} catch (IOException e) {
				log.error("Error Occured", e);
			}
		}
		return "{\"error\" : \"No Modules available\"}";
	}

	@GET
	@Path("/displayModule/{modName}")
	@Produces("application/json")
	public String displayModule(@PathParam("modName") String modName) {
		System.out.println("**************** AVAILABLE MODULES ************* "
				+ Memory.getModuleGroovyMapping().keySet() + ", frequested Module " + modName);
		if (CollectionUtils.isNotEmpty(Memory.getModuleGroovyMapping().keySet())) {
			try {
				String groovyIdentifier = Memory.getModuleGroovyMapping().get(modName);
				GroovyObject gryObject = Memory.getGroovyObjects().get(groovyIdentifier);
				String json = (String) gryObject.invokeMethod("buildJUI", null);

				return json;

				/*
				 * StringWriter writer = new StringWriter(); IOUtils.copy(in, writer,
				 * Charset.defaultCharset()); return writer.toString();
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "{\"error\" : \"No Modules available\"}";
	}

	// UPDATING MODULE NEW

	@SuppressWarnings("unchecked")
	@POST
	@Consumes("application/json")
	@Path("/update/{modname}")
	public Response updateIotProperties(@PathParam("modName") String modName, String receiveObject)
			throws URISyntaxException, JsonMappingException, JsonProcessingException {
		System.out.println("VOLATILE BOOLEAN VALUE*******" + Memory.isExit());
		log.info("ReceiveObject STR {" + receiveObject.getClass() + "} " + receiveObject);
		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(receiveObject, Map.class);
			map.entrySet().stream().forEach(

					es -> {
						System.out
								.println("ES " + es.getKey() + ", {" + es.getValue().getClass() + "} " + es.getValue());
						Memory.clearAndAddIOTProperty(es.getKey(), es.getValue());
					}

			);
			Memory.getIotProperties().getKeys().forEachRemaining(
					prop -> log.info("Added " + prop + ", value " + Memory.getIotProperties().getProperty(prop)));
			if (Memory.getiotPropBuilder() != null) {
				try {
					Memory.getiotPropBuilder().save();
					Memory.refreshIotProperties();
				} catch (ConfigurationException e) {
					e.printStackTrace();
				}
			}

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// get the Linked hap map
		System.out.println("************GETTING FROM MEMORY****** " + Memory.getOutValue());
		LinkedHashMap updatedValue = mapper.readValue(receiveObject, LinkedHashMap.class);
		System.out.println("IOTPROPERTIES***********" + Memory.getIotProperties());

		if (Memory.getOutValue().equals(updatedValue)) {
			System.out.println("Not updated ::::::::::::::::::::::::::::::::::::::");
			Memory.setCheckforUpdation(false);
			// System.out.println("UPDATION CHECKING ***********" +
			// Memory.isCheckforUpdation());
		} else {
			System.out.println(" updated ::::::::::::::::::::::::::::::::::::::");
			Memory.setCheckforUpdation(true);
			// System.out.println("UPDATION CHECKING ***********" +
			// Memory.isCheckforUpdation());
		}
		// set into setLinked (outvalue)
		Memory.setOutValue(updatedValue);

		// Memory.getModulesConfig().put(modName, outValue1);

		return Response.status(201).entity("success:updated").build();

	}

	// STARTING MODULE
	@SuppressWarnings("unchecked")
	@POST
	@Path("/updateRun/{modName}")
	@Produces("application/json")
	public String updateRunModule(@PathParam("modName") String modName, String jsonPropertyString) {
		System.out.println("Update AND RUN the Module " + modName + ", JSON " + jsonPropertyString);
		final ObjectMapper mapper = new ObjectMapper();
        if (modName.equalsIgnoreCase("Stamping IOT")) {
			Memory.clearAndAddProperty("stampingIOTStatus", "started");
		} else if (modName.equalsIgnoreCase("Flow Wrap")) {
			Memory.clearAndAddProperty("flowwrapStatus", "started");
		}
		EventsFunction.refreshPropertyFile();
			try {
				LinkedHashMap<String, Object> outValue = mapper.readValue(jsonPropertyString, LinkedHashMap.class);
				String requestUUID = UUID.randomUUID().toString();
				log.info("\n\n\nREQUEST UUID : " + requestUUID);
				outValue.put("randUUID", requestUUID);
				System.out.println("Map in Request " + outValue);
				Memory.getModulesConfig().put(modName, outValue);
				System.out
						.println("***********getting all the values*************" + Memory.getModulesConfig().values());
				System.out.println("jsonPropertyString geetting into module :::::::::" + jsonPropertyString);
				System.out.println("modName ::::::" + modName);

				// System.out.println("modPath " + outValue.get("modPath"));
				String modScriptURL = (String) outValue.get("modPath");
				try {

					System.out.println("jsonPropertyString  into module :::::::::" + jsonPropertyString);
					ProducerTemplate prodTemplate = Memory.getGlobalContext().createProducerTemplate();
					Object output = prodTemplate.requestBodyAndHeader("direct:ip- IOTComp", outValue, "Module",
							modName);

					System.out.println("Execution Output : " + output);

					/*
					 * File script = new
					 * File(Memory.getExtBaseDir().getAbsolutePath()+modScriptURL); if (script!=null
					 * && script.exists()) { final GroovyClassLoader classLoader = new
					 * GroovyClassLoader(); Class<?> groovy =
					 * classLoader.parseClass(FileUtils.readFileToString(script,
					 * Charset.defaultCharset())); GroovyObject groovyObj = null; try { groovyObj =
					 * (GroovyObject) groovy.newInstance(); groovyObj.invokeMethod("main", null);
					 * log.info("Get deployed Groovies:::::::::"+groovyObj);
					 * 
					 * } catch (InstantiationException | IllegalAccessException e) {
					 * log.error("Existing Module Groovy Initialization Error ", e); } } else {
					 * log.error("No Modules find in the path "+modScriptURL); }
					 */
				} catch (Exception e) {
					e.printStackTrace();
				}

				// }

				return "{\"success\" : \"Done\"}";
			} catch (IOException e) {
				log.error("Error occured ", e);
			}
			return "{\"success\" : \"Done\"}";
		//}return null;

	}

	@SuppressWarnings("deprecation")
	@POST
	@Path("/stop/{modName}")
	@Produces("application/json")
	public String updateStopModule(@PathParam("modName") String modName, String jsonPropertyString) {
		
		final ObjectMapper mapper = new ObjectMapper();
		if (modName.equals("Stamping IoT")) {
			Memory.clearAndAddProperty("stampingIOTStatus", "paused");
			
		} else if (modName.equals("Flow Wrap")) {
			Memory.clearAndAddProperty("flowwrapStatus", "paused");
		}else if (modName.equals("Machine")) {
			Memory.clearAndAddProperty("MachineStatus", "paused");
		}
		EventsFunction.refreshPropertyFile();

		return "{\"success\" : \"Done\"}";
	}
}

/*
 * @SuppressWarnings("deprecation")
 * 
 * @POST
 * 
 * @Path("/stop/{modName}")
 * 
 * @Produces("application/json") public String
 * updateStopModule(@PathParam("modName") String modName, String
 * jsonPropertyString) { System.out.println("inside stop  Module " + modName +
 * ", JSON " + jsonPropertyString); final ObjectMapper mapper = new
 * ObjectMapper();
 * 
 * try { LinkedHashMap<String, Object> outValue1 =
 * mapper.readValue(jsonPropertyString, LinkedHashMap.class);
 * Memory.getModulesConfig().put(modName, outValue1); String modScriptURL =
 * (String) outValue1.get("modPath"); try {
 * 
 * 
 * ProducerTemplate prodTemplate =
 * Memory.getGlobalContext().createProducerTemplate(); Object output =
 * prodTemplate.requestBodyAndHeader("direct:ip-" + modName, outValue1,
 * "Module", modName);
 * 
 * 
 * File script = new File(Memory.getExtBaseDir().getAbsolutePath() +
 * modScriptURL); if (script != null && script.exists()) { final
 * GroovyClassLoader classLoader = new GroovyClassLoader(); Class<?> groovy =
 * classLoader .parseClass(FileUtils.readFileToString(script,
 * Charset.defaultCharset())); GroovyObject groovyObj = null; try { groovyObj =
 * (GroovyObject) groovy.newInstance(); groovyObj.invokeMethod("StopThread",
 * null);
 * 
 * 
 * } catch (InstantiationException | IllegalAccessException e) {
 * log.error("Existing Module Groovy Initialization Error ", e); } } else {
 * log.error("No Modules find in the path " + modScriptURL); }
 * 
 * } catch (Exception e) { e.printStackTrace(); }
 * 
 * return "{\"success\" : \"Done\"}"; } catch (IOException e) {
 * log.error("Error occured ", e); } return "{\"success\" : \"Done\"}"; }
 * 
 * }
 */

/*
 * @SuppressWarnings("unchecked")
 * 
 * @POST
 * 
 * @Consumes("application/json")
 * 
 * @Path("/update1/{modname}") public Response
 * updateIotProperties(@PathParam("modName") String modName, String
 * receiveObject) throws URISyntaxException, JsonMappingException,
 * JsonProcessingException {
 * 
 * log.info("ReceiveObject STR {" + receiveObject.getClass() + "} " +
 * receiveObject); ObjectMapper mapper = new ObjectMapper(); try {
 * 
 * @SuppressWarnings("unchecked") Map<String, Object> map =
 * mapper.readValue(receiveObject, Map.class); map.entrySet().stream().forEach(
 * 
 * es -> { System.out .println("ES " + es.getKey() + ", {" +
 * es.getValue().getClass() + "} " + es.getValue());
 * Memory.clearAndAddIOTProperty(es.getKey(), es.getValue()); }
 * 
 * ); Memory.getIotProperties().getKeys().forEachRemaining( prop ->
 * log.info("Added " + prop + ", value " +
 * Memory.getIotProperties().getProperty(prop))); if (Memory.getAppPropBuilder()
 * != null) { try { Memory.getAppPropBuilder().save();
 * Memory.refreshIotProperties(); } catch (ConfigurationException e) {
 * e.printStackTrace(); } }
 * 
 * } catch (JsonMappingException e) { e.printStackTrace(); } catch
 * (JsonProcessingException e) { e.printStackTrace(); }
 * 
 * System.out.println("outValue before" + outValue1); if (outValue1 == null) {
 * outValue1 = mapper.readValue(receiveObject, LinkedHashMap.class); }
 * System.out.println("outValue after" + outValue1); outUpdatedValue =
 * mapper.readValue(receiveObject, LinkedHashMap.class);
 * System.out.println("outUpdatedValue after" + outUpdatedValue); if
 * (outValue1.equals(outUpdatedValue)) {
 * System.out.println(" object same ::::::::::::::::::::");
 * Memory.getModulesConfig().put(modName, outUpdatedValue);
 * 
 * } else { System.out.println(" object different  ::::::::::::::::::::" +
 * outValue1); // outValue = outUpdatedValue;
 * Memory.getModulesConfig().put(modName, outValue1); }
 * 
 * // Memory.getModulesConfig().put(modName, outUpdatedValue);
 * 
 * return Response.status(201).entity("success:updated").build(); }
 */
