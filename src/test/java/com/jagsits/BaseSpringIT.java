package com.jagsits;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/ac-main.xml")
public abstract class BaseSpringIT {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
}
