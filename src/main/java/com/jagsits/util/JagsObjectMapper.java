package com.jagsits.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class JagsObjectMapper extends ObjectMapper {

    public JagsObjectMapper() {
        super();

        // Pretty Print
        configure(SerializationFeature.INDENT_OUTPUT, true);

        // Do not write Null's
        configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Handle JDK8
        registerModule(new Jdk8Module());

        // Handle Parameter names
        registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    }

}
