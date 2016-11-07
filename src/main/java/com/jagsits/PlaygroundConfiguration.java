package com.jagsits;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jagsits.util.JagsObjectMapperHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

@Configuration
@ComponentScan("com.jagsits.service")
@EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMBeanExport(defaultDomain = "com.jagsits.service", registration = RegistrationPolicy.IGNORE_EXISTING)
public class PlaygroundConfiguration {

    @Bean
    @Primary
    ObjectMapper objectMapper() {
        return JagsObjectMapperHolder.getInstance();
    }

    @Bean
    FilterRegistrationBean characterEncodingFilter() {
        FilterRegistrationBean result = new FilterRegistrationBean();
        result.setFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true));
        result.addUrlPatterns("/*");
        result.setOrder(1);
        return result;
    }

    @Configuration
    @EnableWebSecurity
    class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private static final String MANAGEMENT_BASE_CONTEXT_PATH = "/management";

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http

                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .enableSessionUrlRewriting(false)

                    .and()
                    .headers()
                    .addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(request -> !StringUtils.startsWithIgnoreCase(request.getRequestURI(), MANAGEMENT_BASE_CONTEXT_PATH), new ContentSecurityPolicyHeaderWriter("default-src 'none'; script-src 'self'; connect-src 'self'; img-src 'self' data:; style-src 'unsafe-inline' 'self' data:; font-src 'self' data:;")))

                    .and()
                    .httpBasic()

                    .and()
                    .csrf()

                    .and()
                    .authorizeRequests()
                    .antMatchers("*").permitAll();
        }

    }

}
