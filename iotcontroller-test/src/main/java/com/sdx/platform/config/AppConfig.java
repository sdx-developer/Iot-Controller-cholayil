package com.sdx.platform.config;

/*import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*@Document(collection = "sdx-platform-appconfig")*/
public class AppConfig {
	
	/*@Id*/
	private String _id;
	
	private boolean active = false;
	
	private String configName;
	private String configValue;
	
	private ConfigModule configModule;
	
	private ConfType type;
	

}
	