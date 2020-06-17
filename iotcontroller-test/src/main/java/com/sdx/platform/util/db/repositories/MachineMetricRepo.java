package com.sdx.platform.util.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.sdx.platform.util.db.entities.MachineMetrics;



@Repository
@Component
public interface MachineMetricRepo extends CrudRepository<MachineMetrics, Integer>{
	
	
	
	

}
