package com.sdx.platform.util.db.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.sdx.platform.util.db.entities.MachineMetrics;
import com.sdx.platform.util.db.repositories.MachineMetricRepo;
import com.sdx.platform.util.db.services.MachineMetricService;

@Service
@Component
public class MachinemetricServiceImpl implements MachineMetricService {

	@Autowired
	public MachineMetricRepo machineMetricRepo;

	@Override
	public Iterable<MachineMetrics> findAll() {
		return machineMetricRepo.findAll();

	}

	@Override
	public MachineMetrics save(MachineMetrics MachineMetrics) {
		return machineMetricRepo.save(MachineMetrics);
	}

	@Override
	public void delete(int id) {
		machineMetricRepo.deleteById(id);

	}

	@Override
	public MachineMetrics findById(int id) {
		return machineMetricRepo.findById(id).orElse(null);

	}

}
