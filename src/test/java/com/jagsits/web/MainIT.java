package com.jagsits.web;

import com.jagsits.util.JagsUtils;
import org.junit.Test;

import java.util.Map;

import static com.jagsits.web.BaseController.RESPONSE_STATUS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void test404() {
        Map map = doGet("/INVALID_URL");
        assertEquals(map.get(RESPONSE_STATUS), 404);
    }

}
