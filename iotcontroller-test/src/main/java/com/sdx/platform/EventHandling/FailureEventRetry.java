package com.sdx.platform.EventHandling;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdx.platform.util.db.EventDbPush;
import com.sdx.platform.util.db.entities.MachineMetricsFailures;

public class FailureEventRetry {
	@Autowired
	static MachineMetricsFailures machineFail;

	public static void failureEvent() throws SQLException {

		/*
		 * System.out.println(
		 * ":::::::::::::::::::::::::::::::::::::::::::::::inside machinemetrics::::::::::::::::::::::::::"
		 * );
		 */
		List<Object> metricsList = EventDbPush.getInstance().getMachineMetricsFailureDetails();

		try {

			for (int i = 0; i < metricsList.size(); i++) {
				System.out.println("inside the for loop");

				machineFail = (MachineMetricsFailures) metricsList.get(i);
				System.out.println("id" + machineFail.getTransId());
				String payload = machineFail.getPayload();
				Object resbody = (Object) payload;
				System.out.println("object" + resbody);
				resbody = JSON.parse(payload);
                JSONObject jsonrep = (JSONObject) resbody;

				if (machineFail.getPayloadtype().equalsIgnoreCase("P")) {
					System.out.println("perf" + machineFail.getPayload());
					PerformanceEventGen.buildFailurePerformancePayload(jsonrep, machineFail);

				} else if (machineFail.getPayloadtype().equalsIgnoreCase("Q")) {
					System.out.println("qual" + machineFail.getPayload());
					QualityEventGen.buildFailureQualityPayload(jsonrep, machineFail);

				} else {
					System.out.println("availability payload cannot be appent");
				}
				/*
				 * else if (machineFail.getPayloadtype().equalsIgnoreCase("A")) {
				 * System.out.println("avail" + machineFail.getPayload());
				 * AvailabilityEventGen.buildFailureAvailabilityPayload(jsonrep, machineFail);
				 * 
				 * }
				 */
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}
