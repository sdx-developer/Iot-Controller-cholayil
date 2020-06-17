package com.sdx.platform.EventHandling;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.juli.logging.Log;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;
import com.sdx.platform.quartz.service.impl.AppProperties;


public class RunSedaApp {
	
	public static CamelContext camelContext = null;
	public static ProducerTemplate producerTemplate = null;
	
	static {
		//System.out.println(" Camel context invoke :::::::::::::::::");
		camelContext = new DefaultCamelContext();
		try {
			camelContext.addRoutes(new SedaEventDistributer());
			HttpComponent httpComponent = new HttpComponent();
			camelContext.addComponent("https", httpComponent);
			camelContext.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
}
	
	}