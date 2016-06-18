package com.jagsits.web;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LogbackServletContextListener implements ServletContextListener {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

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