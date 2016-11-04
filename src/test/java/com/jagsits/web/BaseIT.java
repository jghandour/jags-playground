package com.jagsits.web;

import com.jagsits.util.JagsUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

    @Autowired
    private TestRestTemplate restTemplate;

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
        return restTemplate.getForObject(path, Map.class);
    }

    protected Map doGet(String path, Map<String, ?> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path);
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            builder = builder.queryParam(entry.getKey(), entry.getValue());
        }
        return restTemplate.getForObject(builder.build().encode().toString(), Map.class);
    }

    protected String buildPathSuffix(String... pathVariables) {
        StringBuilder result = new StringBuilder();
        for (String pathVariable : pathVariables) {
            pathVariable = StringUtils.trimLeadingCharacter(pathVariable, JagsUtils.URL_SEPARATOR);
            pathVariable = StringUtils.trimTrailingCharacter(pathVariable, JagsUtils.URL_SEPARATOR);
            result.append(JagsUtils.URL_SEPARATOR).append(pathVariable);
        }
        return result.toString();
    }
}
