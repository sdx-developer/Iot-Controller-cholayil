package com.sdx.platform.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.sdx.platform.EventHandling.FailureEventRetry;
import com.sdx.platform.config.Memory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.IntStream;

/**
 * @author Chamith
 */
@Slf4j
public class FailureEventdistJob extends QuartzJobBean {
	FailureEventRetry eventDistributer = new FailureEventRetry();
	    
	    @Override
	    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	        
	    	
	    		try {
	    			System.out.println("COMING INSIDE SCHEDULER FAILYRE EVENT DISTRIBUTER");
					eventDistributer.failureEvent();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
			
			
	        
	    }
	}
