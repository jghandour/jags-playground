package com.jagsits.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

import static com.jagsits.util.CharsetUtils.toUTF8;

public final class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = JagsObjectMapperHolder.INSTANCE;

    private JsonUtils() {
    }

    public static String toJsonString(Object object) {
        return toUTF8(toJson(object));
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

    public static <T> T fromJson(String jsonContent, Class<T> clazz) {
        return fromJson(toUTF8(jsonContent), clazz);
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


