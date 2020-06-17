package com.sdx.platform.domain;

/*import org.bson.Document;*/
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.sdx.platform.config.Memory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseDomainUtil {

	/*public static JSONObject buildUserEditDetails(Document pDocument) throws JSONException {
		log.info("Available :::::::::: "+Memory.getExtensionObjects().containsKey("UserExtension"));
		if (Memory.getExtensionObjects().containsKey("UserExtension")) {
			GroovyObject rfJson = (GroovyObject) JSON.parseObject(pDocument.toJson(), Memory.getExtensionObjects().get("UserExtension").getClass());
	        log.info("RF-JSON"+rfJson);
			
	        rfJson.invokeMethod("setUpdateMode", true);
	        rfJson.invokeMethod("convertToJSON", null);
	        String json = (String) rfJson.invokeMethod("buildFIO", null);
	        
	        log.info("JSON >>>>>>>>>> "+json);
	        return new JSONObject(json);
		} 
		return null;
	}*/

}
