package com.jagsits;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.BeanIds;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.nio.charset.StandardCharsets;

public class PlaygroundWebApplicationInitializer implements WebApplicationInitializer {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onStartup(ServletContext container) throws ServletException {

        // Root Application
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(PlaygroundConfiguration.class);
        container.addListener(new ContextLoaderListener(rootContext));

        // Spring Dispatcher
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(PlaygroundWebMvcConfiguration.class);
        ServletRegistration.Dynamic springDispatcher = container.addServlet("springDispatcher", new DispatcherServlet(dispatcherContext));
        springDispatcher.setLoadOnStartup(1);
        springDispatcher.addMapping("/*");

        // Spring Security Filter
        FilterRegistration.Dynamic springSecurityFilterChain = container.addFilter(BeanIds.SPRING_SECURITY_FILTER_CHAIN, new DelegatingFilterProxy());
        springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");

        // UTF-8 Filter
        FilterRegistration.Dynamic encodingFilter = container.addFilter("encodingFilter", new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true));
        encodingFilter.addMappingForUrlPatterns(null, true, "/*");

        // Logback Context Listener
        container.addListener(new LogbackServletContextListener());
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