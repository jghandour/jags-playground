package com.jagsits;

import com.jagsits.util.JagsObjectMapper;
import org.springframework.context.annotation.*;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;

@Configuration
@ComponentScan("com.jagsits.service")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMBeanExport(defaultDomain = "com.jagsits.service", registration = RegistrationPolicy.IGNORE_EXISTING)
public class PlaygroundConfiguration {

    @Bean
    @Primary
    JagsObjectMapper objectMapper() {
        return new JagsObjectMapper();
    }

    @Configuration
    @EnableWebSecurity
    class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http

                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .enableSessionUrlRewriting(false)

                    .and()
                    .headers()
                    .addHeaderWriter(new ContentSecurityPolicyHeaderWriter("default-src 'none'; script-src 'self'; connect-src 'self'; img-src 'self' data:; style-src 'unsafe-inline' 'self' data:; font-src 'self' data:;"))

                    .and()
                    .httpBasic()

                    .and()
                    .csrf()

                    .and()
                    .authorizeRequests()
                    .antMatchers("*").permitAll()


            ;
        }

    }

}
