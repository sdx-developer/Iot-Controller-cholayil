package com.sdx.platform.util.db;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.sdx.platform.config.ApplicationContextProvider;
import com.sdx.platform.config.Memory;
import com.sdx.platform.util.db.entities.*;
import com.sdx.platform.util.db.repositories.MachineMetricFailureRepo;
import com.sdx.platform.util.db.services.*;

@ComponentScan("com.sdx.platform.util.db.DBEvents")
@Component
public class EventDbPush {

	private static EventDbPush eventDBPush = null;

	public static EventDbPush getInstance() {
		if (eventDBPush == null) {
			eventDBPush = ApplicationContextProvider.getBean("eventDbPush", EventDbPush.class);
		}
		return eventDBPush;
	}

	@Autowired
	MachineMetricService MachineService;

	@Autowired
	MachineMetricFailureService MachineFailureService;

	@Autowired
	public MachineMetricFailureRepo MachineMetricFailureRepo;

	public void machineMetricInsert(String payload, String Payloadtype,String status) {
		MachineMetrics MachineMetrics = new MachineMetrics();
		Date date1 = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate = dateFormat.format(date1);
		Object streamType = Memory.getAppProperties().getString("streamFlavor");
		MachineMetrics.setPayload(payload);
		MachineMetrics.setPayloadtype(Payloadtype);
		MachineMetrics.setStatusflag("s");
		MachineMetrics.setStatus(status);
		MachineMetrics.setStreamtype(streamType.toString());
		MachineMetrics.setTrandtm(strDate);
		MachineService.save(MachineMetrics);
	}

	public void machineMetricFailureInsert(String payload, String Payloadtype, String Failuereason) {
		MachineMetricsFailures machineMetricsFailures = new MachineMetricsFailures();
		Date date1 = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate = dateFormat.format(date1);
		Object streamType = Memory.getAppProperties().getString("streamFlavor");
		machineMetricsFailures.setPayload(payload);
		machineMetricsFailures.setPayloadtype(Payloadtype);
		machineMetricsFailures.setFailurereason(Failuereason);
		machineMetricsFailures.setStreamtype(streamType.toString());
		machineMetricsFailures.setStatusflag("E");
		machineMetricsFailures.setTrandtm(strDate);
		MachineFailureService.save(machineMetricsFailures);

	}

	public List getMachineMetricsFailureDetails() {
		List machinemetricfailure = new ArrayList();

		for (MachineMetricsFailures metrics : MachineFailureService.findAll()) {
			MachineMetricsFailures machinemetrics = new MachineMetricsFailures();
			machinemetrics.setTransId((metrics.getTransId()));
			machinemetrics.setPayload(metrics.getPayload());
			machinemetrics.setPayloadtype(metrics.getPayloadtype());
			machinemetrics.setTrandtm(metrics.getTrandtm());
			machinemetrics.setStreamtype(metrics.getStreamtype());
			machinemetrics.setStatusflag(metrics.getStatusflag());

			machinemetricfailure.add(machinemetrics);

		}
		return machinemetricfailure;

	}

	public void delete(int id) {
		MachineFailureService.delete(id);
	}

	public void update(int transid, int retrycount) {
		MachineMetricsFailures machineMetricsFailures = MachineFailureService.findById(transid);
		machineMetricsFailures.setRetrycount(retrycount);
		System.out.println("inside update");
		MachineFailureService.save(machineMetricsFailures);

	}

}
