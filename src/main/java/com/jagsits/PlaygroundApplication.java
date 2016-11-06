package com.jagsits;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

@SpringBootApplication
public class PlaygroundApplication extends SpringBootServletInitializer {

    private Logger log = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PlaygroundApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PlaygroundApplication.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new LogbackServletContextListener());
    }

    private class LogbackServletContextListener implements ServletContextListener {
        @Override
        public void contextDestroyed(ServletContextEvent sce) {
            log.info("Stopping Logger Context to ensure there are no memory leaks.");
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            lc.stop();
        }

        @Override
        public void contextInitialized(ServletContextEvent sce) {
        }
    }
}