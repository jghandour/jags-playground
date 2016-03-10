package com.jagsits.web;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainFT extends BaseFT {

    @Test
    public void testVersion() {
        Map response = getResultMap(doGet(MainController.MAPPING_VERSION));
        assertTrue(response.containsKey(MainController.RESPONSE_VERSION));
        assertTrue(response.containsKey(MainController.RESPONSE_TIMESTAMP));
    }

    @Test
    public void testCsrf() {
        Map response = getResultMap(doGet(MainController.MAPPING_CSRF));
        assertEquals(3, response.size());
    }

}
