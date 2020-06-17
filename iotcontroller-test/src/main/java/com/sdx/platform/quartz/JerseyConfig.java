package com.sdx.platform.quartz;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;
import com.sdx.platform.quartz.service.impl.AppProperties;
import com.sdx.platform.quartz.service.impl.ModulesServices;
import com.sdx.platform.quartz.service.impl.UserResource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JerseyConfig extends ResourceConfig
{
    public JerseyConfig()
    {
    	log.info("REGISTERING the Resources, AppProperties, ModulesServices");
        register(UserResource.class);
        register(AppProperties.class);
        register(ModulesServices.class);
        
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }
}
