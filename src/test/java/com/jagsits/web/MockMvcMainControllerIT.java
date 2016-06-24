package com.jagsits.web;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class MockMvcMainControllerIT extends BaseMockMvcSpringControllerIT {

    @Test
    public void testVersion() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/version").with(csrf().asHeader())).andReturn();
        Map responseMap = getResultMap(mvcResult);
        assertNotNull(responseMap.get(MainController.RESPONSE_VERSION).toString());
        assertNotNull(responseMap.get(MainController.RESPONSE_TIMESTAMP).toString());
    }

}

