package com.sdx.platform.quartz.jobs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.sdx.platform.config.Memory;
import com.sdx.platform.util.ChecksumCalculator;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModulesJob extends QuartzJobBean {
	
	private static String[] typeArray = new String[] {"groovy"}; 

	@SuppressWarnings("resource")
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		log.info("\n\n---> ModulesJob starts");
		
		File modDir = new File(Memory.getExtBaseDir().getAbsolutePath()+"/modules");
		if (modDir!= null && modDir.exists() && modDir.isDirectory()) {
			Collection<File> groovies = FileUtils.listFiles(modDir, typeArray, false);
		
			
			groovies.stream().forEach(groovyFile -> {
				try {
					String groovyName 		= "Module-"+FilenameUtils.getBaseName(groovyFile.getName());
					String groovyContent 	= FileUtils.readFileToString(groovyFile, Charset.defaultCharset());
					String groovyMD5 		= ChecksumCalculator.getMD5Hash(groovyContent);
					
					boolean contentExist = Memory.getGroovyContent().containsKey(groovyName);
					if (contentExist) {
						String existMD5	= ChecksumCalculator.getMD5Hash(Memory.getGroovyContent().get(groovyName));
						
						if (! StringUtils.equals(groovyMD5, existMD5)) {
							
							final GroovyClassLoader classLoader = new GroovyClassLoader();
							Class<?> groovy = classLoader.parseClass(groovyContent);
							GroovyObject groovyObj = null;
							try {
								groovyObj = (GroovyObject) groovy.newInstance();
								Memory.getGroovyContent().put(groovyName, groovyContent);
								Memory.getGroovyObjects().put(groovyName, groovyObj);
								
							} catch (InstantiationException | IllegalAccessException e) {
								
							}
							
							if (groovyObj !=null) {
								String modName = (String) groovyObj.invokeMethod("returnIoTModuleName", null);
								Memory.getModuleGroovyMapping().put(modName, groovyName);
							}
						} else {
							
						}
						
						
					} else {
						final GroovyClassLoader classLoader = new GroovyClassLoader();
						Class<?> groovy = classLoader.parseClass(groovyContent);
						GroovyObject groovyObj = null;
						try {
							groovyObj = (GroovyObject) groovy.newInstance();
							Memory.getGroovyContent().put(groovyName, groovyContent);
							Memory.getGroovyObjects().put(groovyName, groovyObj);
							
							
						} catch (InstantiationException | IllegalAccessException e) {
							
						}
						
						if (groovyObj !=null) {
							String modName = (String) groovyObj.invokeMethod("returnIoTModuleName", null);
							Memory.getModuleGroovyMapping().put(modName, groovyName);
						}
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			});
			
			
			
			
			
			
		}
		
		log.info("ModulesJob ends");

	}

}
