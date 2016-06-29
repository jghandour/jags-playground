package com.jagsits.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

public final class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final JagsObjectMapper OBJECT_MAPPER = new JagsObjectMapper();

    private JsonUtils() {
    }

    public static byte[] toJson(Object object) {
        ByteArrayOutputStream result = new ByteArrayOutputStream(0);
        try {
            OBJECT_MAPPER.writeValue(result, object);
        } catch (Exception e) {
            log.warn("Unable to convert to JSON.", e);
        }
        return result.toByteArray();
    }

    public static <T> T fromJson(byte[] bytes, Class<T> valueType) {
        T result = null;
        try {
            result = OBJECT_MAPPER.readValue(bytes, valueType);
        } catch (Exception e) {
            log.warn("Unable to convert from JSON.", e);
        }
        return result;
    }
}


