package com.jagsits.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public final class JagsObjectMapperHolder {

    private static final ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper();

        // Pretty Print
        getInstance().configure(SerializationFeature.INDENT_OUTPUT, true);

        // Do not write Null's
        getInstance().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        getInstance().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Handle JDK8
        getInstance().registerModule(new Jdk8Module());

        // Handle Parameter names
        getInstance().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    }

    private JagsObjectMapperHolder() {
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }
}
