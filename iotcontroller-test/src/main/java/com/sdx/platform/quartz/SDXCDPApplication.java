/*
 * package com.sdx.platform.quartz;
 * 
 * import org.springframework.boot.SpringApplication; import
 * org.springframework.boot.autoconfigure.EnableAutoConfiguration; import
 * org.springframework.boot.autoconfigure.SpringBootApplication; import
 * org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
 * import org.springframework.boot.autoconfigure.jdbc.
 * DataSourceTransactionManagerAutoConfiguration; import
 * org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
 * 
 * import com.sdx.platform.EventHandling.PerformanceEventGen; import
 * com.sdx.platform.config.Memory;
 * 
 * @SpringBootApplication
 * 
 * @EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
 * DataSourceTransactionManagerAutoConfiguration.class,
 * HibernateJpaAutoConfiguration.class}) public class SDXCDPApplication {
 * 
 * public static void main(String[] args) {
 * System.setProperty("server.servlet.context-path", "/rk"); Memory.init();
 * DefaultActions.init(); SpringApplication.run(SDXCDPApplication.class, args);
 * } }
 */