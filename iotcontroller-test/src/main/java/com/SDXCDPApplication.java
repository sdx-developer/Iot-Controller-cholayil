package com;

import java.io.IOException;
import java.text.ParseException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import com.sdx.platform.EventHandling.PerformanceEventGen;
import com.sdx.platform.config.Memory;
import com.sdx.platform.config.ShiftDetailsHandler;
import com.sdx.platform.config.ShiftSchedularModule;
import com.sdx.platform.quartz.DefaultActions;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class SDXCDPApplication {

    public static void main(String[] args) throws CompilationFailedException, ParseException, InterruptedException, IOException {
    	System.setProperty("server.servlet.context-path", "/rk");
    	Memory.init();
    	DefaultActions.init();
    	SpringApplication.run(SDXCDPApplication.class, args);
    	//ShiftDetailsHandler s= new ShiftDetailsHandler();
    	//s.run();
    	ShiftSchedularModule.main(args);
    	
    }
}
