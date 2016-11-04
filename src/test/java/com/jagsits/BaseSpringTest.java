package com.jagsits;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PlaygroundConfiguration.class}, loader = AnnotationConfigWebContextLoader.class)
public abstract class BaseSpringTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());
}
