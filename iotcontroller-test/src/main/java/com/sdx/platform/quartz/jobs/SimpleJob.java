package com.sdx.platform.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.sdx.platform.config.Memory;

import java.io.IOException;
import java.util.stream.IntStream;

/**
 * @author Chamith
 */
@Slf4j
public class SimpleJob extends QuartzJobBean {
	 HealthCheck healthcheck = new HealthCheck();
	    private static String host = Memory.getAppProperties().getString("HealthcheckUrl");
	    
	    @Override
	    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	        
	    	try {
				healthcheck.getStatus(host);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	        
	    }
	}
