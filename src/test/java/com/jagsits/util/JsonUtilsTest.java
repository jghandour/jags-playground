package com.jagsits.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonUtilsTest {

    @Test
    public void testToJson() {
        Map<String,Object> map = new HashMap<>();
        map.put("KEY_1", "VALUE_1");
        map.put("KEY_2", null);

        String jsonString = CharsetUtils.toUTF8(JsonUtils.toJson(map));
        assertEquals("{\r\n" +
                "  \"KEY_1\" : \"VALUE_1\"\r\n" +
                "}", jsonString);
    }

    @Test
    public void testFromJson() {
        String jsonString = "{\"KEY_1\":\"VALUE_1\", \"KEY_2\":null}";

        Map map = JsonUtils.fromJson(CharsetUtils.toUTF8(jsonString), Map.class);
        assertFalse(map.isEmpty());
        assertEquals(2, map.size());
        assertEquals("VALUE_1", map.get("KEY_1"));
        assertEquals(null, map.get("KEY_2"));
    }

}
