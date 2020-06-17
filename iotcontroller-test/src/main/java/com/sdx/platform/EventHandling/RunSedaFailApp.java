package com.sdx.platform.EventHandling;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class RunSedaFailApp {

	public static CamelContext camelContext = null;
	public static ProducerTemplate producerTemplate = null;
	
	static {
		//System.out.println(" Camel context invoke :::::::::::::::::");
		camelContext = new DefaultCamelContext();
		try {
			camelContext.addRoutes(new FailureEventDistributer());
			HttpComponent httpComponent = new HttpComponent();
			camelContext.addComponent("https", httpComponent);
			camelContext.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
}
	
	}
