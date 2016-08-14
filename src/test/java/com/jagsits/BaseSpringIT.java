package com.jagsits;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PlaygroundConfiguration.class}, loader = AnnotationConfigWebContextLoader.class)
public abstract class BaseSpringIT {
    protected final Logger log = LoggerFactory.getLogger(getClass());
}
