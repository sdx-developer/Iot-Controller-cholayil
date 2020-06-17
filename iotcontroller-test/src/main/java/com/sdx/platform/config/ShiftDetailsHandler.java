package com.sdx.platform.config;

import java.io.IOException;
import java.text.ParseException;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sdx.platform.EventHandling.EventsFunction;
import com.sdx.platform.EventHandling.ShiftEventGen;

public class ShiftDetailsHandler extends TimerTask {

	public static void refreshPropertyFile() {
		if (Memory.getiotPropBuilder() != null) {
		try {
		Memory.getiotPropBuilder().save();
		Memory.refreshIotProperties();
		} catch (ConfigurationException e) {
		e.printStackTrace();
		}
		}
		}
	
	@Override
	public  void run() {
		
		
			//ShiftEventGen.buildShiftStatusPayload();
		
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
			
			try {
			//new GroovyShell().parse( new File("src/main/resources/SMCam.groovy") ).invokeMethod( "run", null ) ;
				String scheduleResult = "";
				JSONObject shiftObject = new JSONObject();

				HttpGet get = new HttpGet("https://qa.servicedx.com/manufacture/shift/details"
				);
				//"https://qa.servicedx.com/sender/departments/Stamping_IOT/users/Jane%20Tony/schedules"
				try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {

				scheduleResult = EntityUtils.toString(response.getEntity());
				//System.out.println("RESULT IS:::::::: " + scheduleResult);

				JSONArray array = new JSONArray(scheduleResult);
				for (int i = 0; i < array.length(); i++) {
				shiftObject = array.getJSONObject(i);

				String shiftFromSchedule = (String) shiftObject.get("shiftName");
				String timeFromSchedule = (String) shiftObject.get("shiftStartTime") + "-" +(String) shiftObject.get("shiftEndTime");
				//System.out.println(timeFromSchedule);

				Memory.clearAndAddIOTProperty(shiftFromSchedule, timeFromSchedule);
				refreshPropertyFile();
				//System.out.println(shiftObject);


				}


				}



				

				System.out.println(":::::::::::::::::::::::::::::::::::::::inside for stop");

			
			//t1.sleep(1);

			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} 
			}
			});
			//t.sleep(2);
			t2.start();
		//Memory.getIotProperties();
		
		//ShiftEventGen.refreshPropertyFile();
		
		
		
	}

}
