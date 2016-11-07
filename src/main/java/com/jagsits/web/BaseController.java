package com.jagsits.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class BaseController {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String MAPPING_API_PREFIX = "/api/v1/";
    static final String RESPONSE_STATUS = "status";
    static final String RESPONSE_RESULT = "result";

    @ExceptionHandler
    public Object handleException(Exception e) {
        log.warn("Exception in controller...", e);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
    }

    ResponseEntity<Map> buildResponse(Object object) {
        return buildResponse(HttpStatus.OK, object);
    }

    ResponseEntity<Map> buildResponse(HttpStatus httpStatus, Object object) {
        Map<String, Object> body = new HashMap<>();
        body.put(RESPONSE_STATUS, httpStatus.value());
        body.put(RESPONSE_RESULT, object);

        return ResponseEntity.status(httpStatus).body(body);
    }

}
