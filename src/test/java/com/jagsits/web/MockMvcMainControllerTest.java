package com.jagsits.web;

import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebAppConfiguration
@EnableAutoConfiguration
public class MockMvcMainControllerTest extends BaseMockMvcSpringControllerTest {

    @Test
    public void testVersion() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/version").with(csrf().asHeader())).andReturn();
        Map responseMap = getResultMap(mvcResult);
        assertNotNull(responseMap.get(MainController.RESPONSE_VERSION).toString());
        assertNotNull(responseMap.get(MainController.RESPONSE_TIMESTAMP).toString());
    }

}

