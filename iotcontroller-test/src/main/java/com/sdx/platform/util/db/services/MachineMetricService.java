package com.sdx.platform.util.db.services;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.sdx.platform.util.db.entities.MachineMetrics;

@Service
@Component
public interface MachineMetricService {

	public Iterable<MachineMetrics> findAll();

	public MachineMetrics save(MachineMetrics MachineMetrics);

	public void delete(int id);
	
	public MachineMetrics findById(int id);

	// public Product update(ObjectId _id,Product product);

}
