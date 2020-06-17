package com.sdx.platform.quartz.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdx.platform.quartz.component.JobScheduleCreator;
import com.sdx.platform.quartz.enitiy.SchedulerJobInfo;
import com.sdx.platform.quartz.service.SchedulerService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobScheduleCreator scheduleCreator;

    @SuppressWarnings("unchecked")
	@Override
    public void startAllSchedulers() {
        List<SchedulerJobInfo> jobInfoList = buildAllJobDetails();
        if (jobInfoList != null) {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            jobInfoList.forEach(jobInfo -> {
                try {
                    JobDetail jobDetail = JobBuilder.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
                            .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
                    if (!scheduler.checkExists(jobDetail.getKey())) {
                        Trigger trigger;
                        jobDetail = scheduleCreator.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()),
                                false, context, jobInfo.getJobName(), jobInfo.getJobGroup());

                        if (jobInfo.getCronJob() && CronExpression.isValidExpression(jobInfo.getCronExpression())) {
                            trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
                                    jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                        } else {
                            trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
                                    jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                        }

                        scheduler.scheduleJob(jobDetail, trigger);
                        log.info("jobDetail "+jobDetail+" been scheduled............");

                    }
                    log.info("Class Initialized ", jobInfo.getJobClass());
                } catch (ClassNotFoundException e) {
                    log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
                } catch (SchedulerException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }

    private List<SchedulerJobInfo> buildAllJobDetails() {
		ArrayList<SchedulerJobInfo> jobs = new ArrayList<>();
		
		SchedulerJobInfo extnScheduler = new SchedulerJobInfo();
		extnScheduler.setId("ExtnScheduler");
		extnScheduler.setCronExpression("0 0/2 * 1/1 * ? *");
		extnScheduler.setCronJob(true);
		extnScheduler.setJobClass("com.sdx.platform.quartz.jobs.ExtendsCompilerJob");
		extnScheduler.setJobGroup("CompilerGroup");
		extnScheduler.setJobName("ExtendsCompilerJob");
		

		SchedulerJobInfo modulesScheduler = new SchedulerJobInfo();
		modulesScheduler.setId("ModulesScheduler");
		modulesScheduler.setCronExpression("0 0/1 * 1/1 * ? *");
		modulesScheduler.setCronJob(true);
		modulesScheduler.setJobClass("com.sdx.platform.quartz.jobs.ModulesJob");
		modulesScheduler.setJobGroup("ModulesGroup");
		modulesScheduler.setJobName("ModulesJob");
		
		SchedulerJobInfo HealthCheckScheduler = new SchedulerJobInfo();
		HealthCheckScheduler.setId("healthcheckScheduler");
		HealthCheckScheduler.setCronExpression("0 0/1 * 1/1 * ? *");
		HealthCheckScheduler.setCronJob(true);
		HealthCheckScheduler.setJobClass("com.sdx.platform.quartz.jobs.SimpleJob");
		HealthCheckScheduler.setJobGroup("HealthGroup");
		HealthCheckScheduler.setJobName("HealthJob");
		
		SchedulerJobInfo EventDistributerScheduler = new SchedulerJobInfo();
		EventDistributerScheduler.setId("EventDistributerScheduler");  
		EventDistributerScheduler.setCronExpression("0 0/1 * 1/1 * ? *");//0 0/10  1/1  ? 
		EventDistributerScheduler.setCronJob(true);
		EventDistributerScheduler.setJobClass("com.sdx.platform.quartz.jobs.FailureEventdistJob");
		EventDistributerScheduler.setJobGroup("EventdistGroup");
		EventDistributerScheduler.setJobName("EventdistJob");
		
		jobs.add(extnScheduler);
		jobs.add(modulesScheduler);
		jobs.add(HealthCheckScheduler);
		jobs.add(EventDistributerScheduler);
		return jobs;
	}

	@SuppressWarnings("unchecked")
	@Override
    public void scheduleNewJob(SchedulerJobInfo jobInfo) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
            if (!scheduler.checkExists(jobDetail.getKey())) {

                jobDetail = scheduleCreator.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()),
                        false, context, jobInfo.getJobName(), jobInfo.getJobGroup());

                Trigger trigger;
                if (jobInfo.getCronJob()) {
                    trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(), jobInfo.getCronExpression(),
                            SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                } else {
                    trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(),
                            SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                }

                scheduler.scheduleJob(jobDetail, trigger);
                
            } else {
                log.error("scheduleNewJobRequest.jobAlreadyExist");
            }
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
        
    }

    @Override
    public void updateScheduleJob(SchedulerJobInfo jobInfo) {
        Trigger newTrigger;
        if (jobInfo.getCronJob()) {
            newTrigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(), jobInfo.getCronExpression(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        } else {
            newTrigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }
        try {
            schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
            
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }


    @Override
    public boolean unScheduleJob(String jobName) {
        try {
            return schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName));
        } catch (SchedulerException e) {
            log.error("Failed to un-schedule job - {}", jobName, e);
            return false;
        }
    }

    @Override
    public boolean deleteJob(SchedulerJobInfo jobInfo) {
        try {
            return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @Override
    public boolean pauseJob(SchedulerJobInfo jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @Override
    public boolean resumeJob(SchedulerJobInfo jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @Override
    public boolean startJobNow(SchedulerJobInfo jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to start new job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }
}
