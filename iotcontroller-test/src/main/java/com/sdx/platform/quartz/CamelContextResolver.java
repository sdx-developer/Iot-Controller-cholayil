package com.sdx.platform.quartz;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.apache.camel.spi.IdempotentRepository;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdx.platform.EventHandling.EventsFunction;
import com.sdx.platform.config.Memory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import lombok.extern.slf4j.Slf4j;

public class CamelContextResolver {

	private static IdempotentRepository<String> moduleRepo = null;

	public static DefaultCamelContext buildGlobalContext() {
		DefaultCamelContext camelContext = new DefaultCamelContext();

		moduleRepo = MemoryIdempotentRepository.memoryIdempotentRepository(10);

		IPRouter ipr = new IPRouter("IOTComp");

		try {
			camelContext.addRoutes(ipr);
			camelContext.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return camelContext;
	}

	public static class IPRouter extends RouteBuilder {

		private String idempotentModName = null;

		public String getIdempotentModName() {
			return idempotentModName;
		}

		public void setIdempotentModName(String pName) {
			this.idempotentModName = pName;
		}

		public IPRouter(String pModName) {
			setIdempotentModName(pModName);
		}

		@Override
		public void configure() throws Exception {

			from("direct:ip- IOTComp").process(new Processor() {
				public void process(Exchange exchange) throws Exception {
					log.info("--------------------------------------------------------------------");
					log.info("UUID    " + exchange.getIn().getBody(LinkedHashMap.class).get("randUUID"));
					log.info("modName " + exchange.getIn().getBody(LinkedHashMap.class).get("modName"));
					log.info("HDRc1   " + exchange.getIn().getHeader("Module"));
					log.info("HDRc1   " + header("Module"));
				}

			}).idempotentConsumer(header("Module"), moduleRepo).skipDuplicate(false)
					.filter(exchangeProperty(Exchange.DUPLICATE_MESSAGE).isEqualTo(true)).to("direct:logDuplicate")

					.stop().end()

					.to("direct:execModule" + getIdempotentModName()).process(new Processor() {
						public void process(Exchange exchange) throws Exception {

						}
					});

			from("direct:execModule" + getIdempotentModName()).process(new Processor() {

				public void process(Exchange exchange) throws Exception {

					String modName = (String) exchange.getIn().getHeader("Module");
					String modScriptURL = "";

					if (modName.equalsIgnoreCase("Stamping IOT")) {
						modScriptURL = "/scripts/SMCam.groovy";
						Memory.clearAndAddProperty("stampingIOTStatus", "started");
					} else if (modName.equalsIgnoreCase("Flow Wrap")) {
						modScriptURL = "/scripts/FWCam.groovy";
						Memory.clearAndAddProperty("flowwrapStatus", "started");
					}
					 else if (modName.equalsIgnoreCase("Machine")) {
							modScriptURL = "/scripts/Camfm.groovy";
							Memory.clearAndAddProperty("MachineStatus", "started");
						}
					EventsFunction.refreshPropertyFile();

					File script = new File(Memory.getExtBaseDir().getAbsolutePath() + modScriptURL);
					if (script != null && script.exists()) {
						final GroovyClassLoader classLoader = new GroovyClassLoader();
						Class<?> groovy = classLoader
								.parseClass(FileUtils.readFileToString(script, Charset.defaultCharset()));
						GroovyObject groovyObj = null;
						try {
							groovyObj = (GroovyObject) groovy.newInstance();
							groovyObj.invokeMethod("main", null);
							log.info("Get deployed Groovies:::::::::" + groovyObj);

						} catch (InstantiationException | IllegalAccessException e) {
							log.error("Existing Module Groovy Initialization Error ", e);
						}
					} else {
						log.error("No Modules find in the path " + modScriptURL);
					}

				}
			}).end();

// Duplicate module - check for any update in config propeties if yes restart the module else leave it
			from("direct:logDuplicate").process(new Processor() {
				@SuppressWarnings("deprecation")
				public void process(Exchange exchange) throws Exception {
					if (Memory.isCheckforUpdation() == true) {
					} else {
					}
					Thread.currentThread().stop();

				}
			}).end();

		}

	}

}
