package com.sdx.platform.util.db.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.sdx.platform.util.db.repositories.MachineMetricFailureRepo;
import com.sdx.platform.util.db.entities.MachineMetricsFailures;
import com.sdx.platform.util.db.services.MachineMetricFailureService;

@Service
@Component
public class MachinemetricFailuresServiceImpl implements MachineMetricFailureService {

	@Autowired
	public MachineMetricFailureRepo MachineMetricFailureRepo;

	@Override
	public Iterable<MachineMetricsFailures> findAll() {
		return MachineMetricFailureRepo.findAll();

	}

	@Override
	public void delete(int id) {
		MachineMetricFailureRepo.deleteById(id);

	}

	@Override
	public MachineMetricsFailures save(MachineMetricsFailures MachineMetricsFailures) {
	//	System.out.println("machinemetric failue " + MachineMetricsFailures.getPayload());
		return MachineMetricFailureRepo.save(MachineMetricsFailures);
	}

	@Override
	public MachineMetricsFailures findById(int id) {
		return MachineMetricFailureRepo.findById(id).orElse(null);
	}

}
