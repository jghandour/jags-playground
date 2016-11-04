package com.jagsits.web;

import com.jagsits.util.JagsUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

import static org.junit.Assert.*;

public class MainIT extends BaseIT {

    @Test
    public void testVersion() {
        Map response = getResultMap(doGet(JagsUtils.URL_SEPARATOR + MainController.MAPPING_VERSION));
        assertTrue(response.containsKey(MainController.RESPONSE_VERSION));
        assertTrue(response.containsKey(MainController.RESPONSE_TIMESTAMP));
    }

    @Test
    public void testCsrf() {
        Map response = getResultMap(doGet(JagsUtils.URL_SEPARATOR + MainController.MAPPING_CSRF));
        assertEquals(3, response.size());
    }

    // FIXME
    @Ignore
    public void test404() throws Exception {
        try {
            doGet("/INVALID_URL");
            fail();
        } catch (HttpClientErrorException hcee) {
            assertEquals(HttpStatus.NOT_FOUND, hcee.getStatusCode());
        }
    }

}
