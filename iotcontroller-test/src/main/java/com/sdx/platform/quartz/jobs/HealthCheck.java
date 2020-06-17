package com.sdx.platform.quartz.jobs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HealthCheck {

	
	
	public static String getStatus(String url) throws IOException {

		String result = "";
		int code = 200;
		

		try {
			URL siteURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.connect();
			   
			code = connection.getResponseCode();
			
			if (code == 200) {
				result = "-> Green <-\t" + "Code: " + code;
		       log.info("Connection is Alive  ");
				
			} else {
				result = "-> Yellow <-\t" + "Code: " + code;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "-> Red <-\t" + "Wrong domain - Exception: " + e.getMessage();
			 log.info("Not connected - Wrong domain  " );

		}
		
		return result;
	}

	
	
}
