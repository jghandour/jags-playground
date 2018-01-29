package com.jagsits.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode;
import static com.fasterxml.jackson.annotation.JsonInclude.Include;

public final class JagsObjectMapperHolder {

    private static final ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper();

        // Pretty Print
        getInstance().configure(SerializationFeature.INDENT_OUTPUT, true);

        // Do not write Null's
        getInstance().setSerializationInclusion(Include.NON_EMPTY);
        getInstance().setSerializationInclusion(Include.NON_NULL);

        // Handle JDK8
        getInstance().registerModule(new Jdk8Module());
        getInstance().registerModule(new JavaTimeModule());

        // Handle Parameter names
        getInstance().registerModule(new ParameterNamesModule(Mode.PROPERTIES));
    }

    private JagsObjectMapperHolder() {
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }
}
