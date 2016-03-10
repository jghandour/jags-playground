package com.jagsits.web;

import com.jagsits.util.JagsObjectMapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public abstract class BaseFT {

    // TODO: Read these externally
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    private RestTemplate restTemplate;

    public BaseFT() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(new JagsObjectMapper());
        restTemplate = new RestTemplate(Arrays.asList(new HttpMessageConverter[]{converter}));
    }

    protected List getResultList(Map map) {
        return getResultList(map, true);
    }

    protected List getResultList(Map map, boolean validate) {
        return (List) getResult(map, true);
    }

    protected String getResultString(Map map) {
        return getResultString(map, true);
    }

    protected String getResultString(Map map, boolean validate) {
        return (String) getResult(map, true);
    }

    protected Map getResultMap(Map map) {
        return getResultMap(map, true);
    }

    protected Map getResultMap(Map map, boolean validate) {
        return (Map) getResult(map, true);
    }

    private Object getResult(Map map, boolean validate) {
        if (validate) {
            validate(map);
        }
        return map.get(BaseController.RESPONSE_RESULT);
    }

    private void validate(Map map) {
        assertEquals(200, map.get(BaseController.RESPONSE_STATUS));
        assertEquals(2, map.size());
    }

    protected Map doGet(String path) {
        return restTemplate.getForObject(buildUrlString(path), Map.class);
    }

    protected Map doGet(String path, Map<String, ?> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(buildUrlString(path));
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            builder = builder.queryParam(entry.getKey(), entry.getValue());
        }
        return restTemplate.getForObject(builder.build().encode().toUri(), Map.class);
    }

    private String buildUrlString(String path) {
        return String.format("http://%s:%s/%s", HOST, PORT, path);
    }

    protected String buildPathSuffix(String... pathVariables) {
        StringBuilder sb = new StringBuilder();
        for (String pathVariable : pathVariables) {
            sb.append("/").append(pathVariable);
        }
        return sb.toString();
    }
}
