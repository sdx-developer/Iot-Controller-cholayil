package com.sdx.platform.groovy.extensions

import com.sdx.platform.groovy.JUIFieldUtils
import com.sdx.platform.config.Constants
import com.sdx.platform.config.Memory
import java.lang.String;
import groovy.json.JsonOutput

import groovy.util.logging.Slf4j

@Slf4j
class AppProperties {

	//UI Related
	private String theme;

	//Platform Related
	private String streamFlavor;

	private String kafka1Host;
	private String kafka1Port;
	private String kafka1Params;

	private String kafka2Host;
	private String kafka2Port;
	private String kafka2Params;

	private String eventServiceURL;
	private String eventServiceOPR;

	private String eventServiceMandateHDRs;

	private String tenant;
	//private String uiTheme;

	//private int extensionsRefreshInterval;
	//private String extensionRefreshFormat ;
    private String enableDBStorage;
	private String enableDataRetry;
	//private int backupDataDur;
	
	private int modulesRefreshInterval;
	private String modulesRefreshFormat;
	private String HttpsUrl;

	//private int TimeToDelete;

	//private int MaxSize;

	def refreshValues() {

		theme = "white";

		//Platform Related
		streamFlavor 	= getFromAppStorage("streamFlavor", String.class) ?: "Kafka";

		kafka1Host 		= getFromAppStorage("kafka1Host", String.class) ?: "kafka-bootstrap-server.dev.servicedx.com";
		kafka1Port 		= getFromAppStorage("kafka1Port", String.class) ?: "9092";
		kafka1Params 	= getFromAppStorage("kafka1Params", String.class) ?: "";

		kafka2Host 		= getFromAppStorage("kafka2Host", String.class) ?: "kafka-bootstrap-server.dev.servicedx.com";
		kafka2Port 		= getFromAppStorage("kafka2Port", String.class) ?: "9093";
		kafka2Params 	= getFromAppStorage("kafka2Params", String.class) ?: "";

		eventServiceURL 	= getFromAppStorage("eventServiceURL", String.class) ?: "https://dev.servicedx.com";
		eventServiceOPR 	= getFromAppStorage("eventServiceOPR", String.class) ?: "SDX";

		eventServiceMandateHDRs 	= getFromAppStorage("eventServiceMandateHDRs", String.class) ?: "SDX";

		tenant 			= getFromAppStorage("tenant", String.class) ?: "servicedx";
		//uiTheme			= getFromAppStorage("uiTheme", String.class) ?: "Dark";

		//extensionsRefreshInterval	= getFromAppStorage("extensionsRefreshInterval", Integer.class) ?: 10;
		//extensionRefreshFormat   = getFromAppStorage("extensionRefreshFormat", String.class) ?: "Seconds";

		enableDBStorage = getFromAppStorage("enableDBStorage", String.class) ?: "";
		enableDataRetry = getFromAppStorage("enableDataRetry", String.class) ?: "";
		//backupDataDur = getFromAppStorage("backupDataDur", Integer.class) ?: 20;
		
		modulesRefreshInterval		= getFromAppStorage("modulesRefreshInterval", Integer.class) ?: 10;
		modulesRefreshFormat   	= getFromAppStorage("modulesRefreshFormat", String.class) ?: "Seconds";

		HttpsUrl = getFromAppStorage("HttpsUrl", String.class) ?: "";

		//TimeToDelete 	= getFromAppStorage("TimeToDelete", Integer.class) ?: "3";

		//MaxSize 	= getFromAppStorage("MaxSize", Integer.class) ?: "6";

		return null;

	}

	private Object getFromAppStorage(String pKey, Class<?> type) {

		if (Memory.getAppProperties().containsKey(pKey)) {
			log.info("SEARCH MODE: "+pKey+ " actual ["+Memory.getAppProperties().getProperty(pKey).getClass()+"] {"+Memory.getAppProperties().getProperty(pKey)+"} ");
			if (type != Memory.getAppProperties().getProperty(pKey).getClass()) {
				if (type.equals(Integer.class)) {
					log.info("CONVERSION HAPPENING >>>>>>>>>>>>>> while ACTUAL is "+Memory.getAppProperties().getProperty(pKey).getClass())
					return Integer.parseInt(Memory.getAppProperties().getProperty(pKey));
				}
			}
			return Memory.getAppProperties().getProperty(pKey);
		}
		return null;
	}

	def init() {
		log.info("****************** Initializing the AppProperties Groovy Extension")
	}

	def buildJUI( ) {

		init();
		refreshValues();

		def flavours  	= ["Kafka", "HTTP(S)"] as String[]
		//def themes    	= ["Dark", "Light"] as String[]
		
		def enableDbStorage = ["True","False"] as String[]
		def enableDataRtry = ["True","False"] as String[]
		def timeFormats = [ "Minutes"] as String[]

		Map platGroup 	= JUIFieldUtils.buildGroup("Platform Properties", "SDX Event plaform connection settings")
		Map flvrMap   	= JUIFieldUtils.buildSelectBox(Constants.SDX_PROP_KEY_EVENT_STREAM_FLAVOR, "streamFlavor", streamFlavor, flavours);

		Map primeHost		= JUIFieldUtils.buildTF("Primary Kafka Host", "kafka1Host", kafka1Host, false, "Primary Kafka event server host")
		Map primePort		= JUIFieldUtils.buildTF("Primary Kafka Port", "kafka1Port", kafka1Port, false, /*1024,*/ /*65535,*/ "Primary Kafka event server port")
		Map primeParams		= JUIFieldUtils.buildTF("Primary Kafka Parameters", "kafka1Params", kafka1Params, false, "Primary Kafka event parameters")

		//Map scndryHost		= JUIFieldUtils.buildTF("Alternative Kafka Host", "kafka2Host", kafka2Host, false, "Alternative Kafka event server host")
		//Map scndryPort		= JUIFieldUtils.buildTF("Alternative Kafka Port", "kafka2Port", kafka2Port, false,/*1024, 65535,*/ "Alternative Kafka event server port")
		//Map scndryParams	= JUIFieldUtils.buildTF("Alternative Kafka Parameters", "kafka2Params", kafka2Params, false, "Alternative Kafka event parameters")
		Map HttpsUrl	    = JUIFieldUtils.buildTF("HttpURL", "HttpsUrl", HttpsUrl, false, "HttpsUrl")

		Map advncdGroup 	= JUIFieldUtils.buildGroup("Advanced", "Advanced Application Settings")
		//Map extnRefMap 		= JUIFieldUtils.buildRangeBuilder("Properties Refresh Time", "extensionsRefreshInterval", extensionsRefreshInterval, 60, 0, true, 1, false, "Groovy Refresh Advanced Settings");
		//Map extnRefTFMap  	= JUIFieldUtils.buildSelectBox("Properties Refresh Time Format", "extensionRefreshFormat", extensionRefreshFormat, timeFormats);
		
		Map enableDbStrMap  	= JUIFieldUtils.buildSelectBox("Enable DB Storage", "enableDBStorage", enableDBStorage, enableDbStorage);
		Map enableDataRetryMap  	= JUIFieldUtils.buildSelectBox("Enable Data Retry", "enableDataRetry", enableDataRetry, enableDataRtry);
		//Map backupDataDuratMap 		= JUIFieldUtils.buildRangeBuilder("Backup Data Duration", "backupDataDur", backupDataDur, 60, 0, true, 1, false, "Backup Data Duration");
		
		Map mdlsRefMap 		= JUIFieldUtils.buildRangeBuilder("IoT Modules Refresh Time", "modulesRefreshInterval", modulesRefreshInterval, 180, 0, true, 1, false, "Groovy Refresh Advanced Settings");
		Map mdlsRefTFMap  	= JUIFieldUtils.buildSelectBox("IoT Modules Refresh Time Format", "modulesRefreshFormat", modulesRefreshFormat, timeFormats);
		//Map guiGroup = JUIFieldUtils.buildGroup("GUI Theme", "Application GUI Theme")
		//Map themeMap = JUIFieldUtils.buildSelectBox(Constants.SDX_PROP_KEY_UI_THEME, "theme", "Dark", themes);

		//Map logDetail		= JUIFieldUtils.buildTF("Log Time Delete", "TimeToDelete", TimeToDelete,true, "Log File Deleteion after certain days")
		//Map scndryLogDetail		= JUIFieldUtils.buildTF("Max Size Log", "MaxSize", MaxSize, true, "Maximum Size of Log File")


		def fieldsList = ["items" : [
				platGroup,
				flvrMap,
				primeHost,
				primePort,
				primeParams,
				HttpsUrl,
				advncdGroup,
				enableDbStrMap,
				enableDataRetryMap,
				//backupDataDuratMap,
				//mdlsRefMap,
				//mdlsRefTFMap
			]];


		log.info(JsonOutput.toJson(fieldsList))
		return JsonOutput.toJson(fieldsList);

	}

	def buildFIO() {
		return "buildFIO";
	}

}