package com.sdx.platform.config;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.JSONObject;

import com.sdx.platform.quartz.CamelContextResolver;

import groovy.lang.GroovyObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Memory {

	private static DefaultCamelContext globalContext = CamelContextResolver.buildGlobalContext();
	private static File extBaseDir = null;
	private static PropertiesConfiguration appProperties = new PropertiesConfiguration();
	private static PropertiesConfiguration IotProperties = new PropertiesConfiguration();
	private static FileBasedConfigurationBuilder<PropertiesConfiguration> appPropBuilder = null;
	private static FileBasedConfigurationBuilder<PropertiesConfiguration> iotPropBuilder = null;
	private static ConcurrentHashMap<String, String> moduleGroovyMapping = new ConcurrentHashMap<>();

	static {
		log.info("************** KICK Start MEMORY *****************");
		/*
		 * ConnectionString connString = new
		 * ConnectionString("mongodb://localhost:27017"); MongoClientSettings settings =
		 * MongoClientSettings.builder().applyConnectionString(connString).retryWrites(
		 * true).build();
		 * 
		 * mongoClient = MongoClients.create(settings); sdxBaseDB =
		 * Memory.getMongoClient().getDatabase("SDXBase");
		 * log.info("Built MongoClient "+mongoClient);
		 */

		String baseDir = System.getProperty("SDXcdpExtBase");
		extBaseDir = new File(baseDir);
		log.info("SDXcdpExtBase ::::::: " + System.getProperty("SDXcdpExtBase") + ", DIR::: " + extBaseDir.exists());
		loadAppProperties();
		loadIotProperties();
	}
	public static void init() {	
	}
	private static void loadAppProperties() {
		File propFile = new File(extBaseDir.getAbsolutePath() + "/config/sdxcdpconfig.properties");
		if (propFile != null && propFile.exists()) {

			appPropBuilder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
					.configure(new Parameters().properties().setFile(propFile).setThrowExceptionOnMissing(true)
							.setListDelimiterHandler(new DefaultListDelimiterHandler('=')).setIncludesAllowed(false));
			try {
				appProperties = appPropBuilder.getConfiguration();
			} catch (ConfigurationException e) {	
			}
		} else {
			loadHardcodedDefaultProperties();
		}
	}
	private static void loadIotProperties() {
		File propFile = new File(extBaseDir.getAbsolutePath() + "/config/IOTmodule.properties");
		if (propFile != null && propFile.exists()) {

			iotPropBuilder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
					.configure(new Parameters().properties().setFile(propFile).setThrowExceptionOnMissing(true)
							.setListDelimiterHandler(new DefaultListDelimiterHandler('=')).setIncludesAllowed(false));
			try {
				IotProperties = iotPropBuilder.getConfiguration();
			} catch (ConfigurationException e) {
	}
		} else {
			
		}
	}
	private static ConcurrentHashMap<String, String> groovyContent = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, GroovyObject> groovyObjects = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, LinkedHashMap<String, Object>> modulesConfig = new ConcurrentHashMap<>();
	private static LinkedHashMap<String, Object> outValue = new LinkedHashMap<>();
	private static boolean checkforUpdation = false;
	private static volatile boolean exit = false;
	private static volatile boolean paused = false;
	private static String status=" ";
	private static String realTimeShiftST=" ";
	private static String realTimeShiftET=" ";
	private static String realTimeTotalPC=" ";
	private static String realTimeTotalGC=" ";
	private static String realTimeMachineSDT="";
	private static String realTimeMachineEDT=" ";
	private static String realTimeFlowrapCount=" ";
	private static String realTimeStampingCount=" ";
	public static String getRealTimeStampingCount() {
		return realTimeStampingCount;
	}
	public static void setRealTimeStampingCount(String realTimeStampingCount) {
		Memory.realTimeStampingCount = realTimeStampingCount;
	}
	private static String realTimeShiftNo=" ";
	private static String downTimeReason=" ";
	private static String realTimestatus=" ";
	public static String getRealTimeShiftST() {
		return realTimeShiftST;
	}
	public static void setRealTimeShiftST(String realTimeShiftST) {
		Memory.realTimeShiftST = realTimeShiftST;
	}
	public static String getRealTimeShiftET() {
		return realTimeShiftET;
	}
	public static void setRealTimeShiftET(String realTimeShiftET) {
		Memory.realTimeShiftET = realTimeShiftET;
	}
	public static String getRealTimeTotalPC() {
		return realTimeTotalPC;
	}
	public static void setRealTimeTotalPC(String realTimeTotalPC) {
		Memory.realTimeTotalPC = realTimeTotalPC;
	}
	public static String getRealTimeTotalGC() {
		return realTimeTotalGC;
	}
	public static void setRealTimeTotalGC(String realTimeTotalGC) {
		Memory.realTimeTotalGC = realTimeTotalGC;
	}
	public static String getRealTimeMachineSDT() {
		return realTimeMachineSDT;
	}
	public static void setRealTimeMachineSDT(String realTimeMachineSDT) {
		Memory.realTimeMachineSDT = realTimeMachineSDT;
	}
	public static String getRealTimeMachineEDT() {
		return realTimeMachineEDT;
	}
	public static void setRealTimeMachineEDT(String realTimeMachineEDT) {
		Memory.realTimeMachineEDT = realTimeMachineEDT;
	}
	public static String getRealTimeFlowrapCount() {
		return realTimeFlowrapCount;
	}
	public static void setRealTimeFlowrapCount(String realTimeFlowrapCount) {
		Memory.realTimeFlowrapCount = realTimeFlowrapCount;
	}
	public static String getRealTimeShiftNo() {
		return realTimeShiftNo;
	}
	public static void setRealTimeShiftNo(String realTimeShiftNo) {
		Memory.realTimeShiftNo = realTimeShiftNo;
	}
	public static String getRealTimestatus() {
		return realTimestatus;
	}
	public static void setRealTimestatus(String realTimestatus) {
		Memory.realTimestatus = realTimestatus;
	}
	
	
	
	public static String getDownTimeReason() {
		return downTimeReason;
	}
	public static void setDownTimeReason(String downTimeReason) {
		Memory.downTimeReason = downTimeReason;
	}
	
 
	public static String getStatus() {
		return status;
	}
	public static void setStatus(String status) {
		Memory.status = status;
	}
	public static void add2AppProperties(String pKey, Object pValue) {
		// getAppProperties().put(pKey, pValue);
	}

	public static ConcurrentHashMap<String, GroovyObject> getGroovyObjects() {
		return groovyObjects;
	}

	public static void setGroovyObjects(ConcurrentHashMap<String, GroovyObject> pMap) {
		Memory.groovyObjects = pMap;
	}

	private static void loadHardcodedDefaultProperties() {
		appProperties = new PropertiesConfiguration();
	}

	public static PropertiesConfiguration getAppProperties() {
		return appProperties;
	}

	public static void setAppProperties(PropertiesConfiguration appProperties) {
		Memory.appProperties = appProperties;
	}

	public static PropertiesConfiguration getIotProperties() {
		return IotProperties;
	}

	public static void setIotProperties(PropertiesConfiguration iotProperties) {
		IotProperties = iotProperties;
	}

	public static void clearAndAddProperty(String key, Object value) {
		if (getAppProperties().containsKey(key)) {
			getAppProperties().clearProperty(key);;
		}
		getAppProperties().addProperty(key, value);
	}

	public static void clearAndAddIOTProperty(String key, Object value) {
		if (getIotProperties().containsKey(key)) {
			getIotProperties().clearProperty(key);
		}
		getIotProperties().addProperty(key, value);
	}

	public static FileBasedConfigurationBuilder<PropertiesConfiguration> getAppPropBuilder() {
		return appPropBuilder;
	}

	public static FileBasedConfigurationBuilder<PropertiesConfiguration> getiotPropBuilder() {
		return iotPropBuilder;
	}

	public static void refreshProperties() {
		try {
			appProperties = appPropBuilder.getConfiguration();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static void refreshIotProperties() {
		try {
			IotProperties = iotPropBuilder.getConfiguration();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static ConcurrentHashMap<String, String> getGroovyContent() {
		return groovyContent;
	}

	public static void setGroovyContent(ConcurrentHashMap<String, String> groovyContent) {
		Memory.groovyContent = groovyContent;
	}

	public static File getExtBaseDir() {
		return extBaseDir;
	}

	public static void setExtBaseDir(File extBaseDir) {
		Memory.extBaseDir = extBaseDir;
	}

	public static ConcurrentHashMap<String, String> getModuleGroovyMapping() {
		return moduleGroovyMapping;
	}

	public static void setModuleGroovyMapping(ConcurrentHashMap<String, String> moduleGroovyMapping) {
		Memory.moduleGroovyMapping = moduleGroovyMapping;
	}

	public static ConcurrentHashMap<String, LinkedHashMap<String, Object>> getModulesConfig() {
		return modulesConfig;
	}

	public static void setModulesConfig(ConcurrentHashMap<String, LinkedHashMap<String, Object>> modulesConfig) {
		Memory.modulesConfig = modulesConfig;
	}

	public static DefaultCamelContext getGlobalContext() {
		return globalContext;
	}

	public static void setGlobalContext(DefaultCamelContext globalContext) {
		Memory.globalContext = globalContext;
	}

	public static LinkedHashMap<String, Object> getOutValue() {
		return outValue;
	}

	public static void setOutValue(LinkedHashMap<String, Object> outValue) {
		Memory.outValue = outValue;
	}

	public static boolean isCheckforUpdation() {
		return checkforUpdation;
	}

	public static void setCheckforUpdation(boolean checkforUpdation) {
		Memory.checkforUpdation = checkforUpdation;
	}

	public static boolean isExit() {
		return exit;
	}

	public static void setExit(boolean exit) {
		Memory.exit = exit;
	}
	
	public static boolean isPaused() {
		return paused;
	}

	public static void setPaused(boolean paused) {
		Memory.paused = paused;
	}

}
