package com.sdx.platform.quartz;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.ws.rs.*;

import com.sdx.platform.quartz.service.impl.AppProperties;
@Slf4j
@Path("/SDX")
public class HTMLFileService {
	
	@javax.ws.rs.core.Context 
	ServletContext context;
	
	public HTMLFileService() {
		log.info("HTML File serices Initialized");
	}
	
	@GET
	@Path("/{id}")
	public InputStream getHTMLFile(@PathParam("id") String fileName) {
		log.info("Look for is "+fileName);
		
		try {
            String base = context.getRealPath("");
            
            log.info("BASE is "+base);
            
            File findFile = new File(String.format("%s/%s", base, fileName));
            log.info("Finding File "+findFile.getAbsolutePath());
            return new FileInputStream(findFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
	}

}