package com.jagsits.web;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LogbackContextListener implements ServletContextListener {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Stopping Logger Context to enure there are no memory leaks.");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.stop();
    }

    public void contextInitialized(ServletContextEvent sce) {
    }
} 