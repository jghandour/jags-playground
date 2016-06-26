package com.jagsits.web;

import com.jagsits.BaseSpringIT;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@WebAppConfiguration
@ContextHierarchy({@ContextConfiguration("classpath:spring/ac-web-mvc.xml")})
public abstract class BaseMockMvcSpringControllerIT extends BaseSpringIT {

    private static final String EXPECTED_CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String EXPECTED_CHARACTER_ENCODING = StandardCharsets.UTF_8.name();

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private HttpMessageConverter httpMessageConverter;
    @Autowired
    @Qualifier("springSecurityFilterChain")
    private Filter springSecurityFilterChain;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).addFilter(springSecurityFilterChain, "/").build();
    }

    protected List getResultList(MvcResult mvcResult) {
        return getResultList(mvcResult, true);
    }

    @SuppressWarnings("unchecked")
    protected List getResultList(MvcResult mvcResult, boolean validateStatus) {
        return (List) getObjectFromResult(mvcResult, validateStatus);
    }

    protected Map<String, String> getResultMap(MvcResult mvcResult) {
        return getResultMap(mvcResult, true);
    }

    @SuppressWarnings("unchecked")
    protected Map<String, String> getResultMap(MvcResult mvcResult, boolean validateStatus) {
        return (Map<String, String>) getObjectFromResult(mvcResult, validateStatus);
    }

    protected String getResultString(MvcResult mvcResult) {
        return getResultString(mvcResult, true);
    }

    protected String getResultString(MvcResult mvcResult, boolean validate) {
        return (String) getObjectFromResult(mvcResult, validate);
    }

    protected Object getObjectFromResult(MvcResult mvcResult) {
        return getObjectFromResult(mvcResult, true);
    }

    protected Object getObjectFromResult(MvcResult mvcResult, boolean validate) {
        return getMap(mvcResult, validate).get(BaseController.RESPONSE_RESULT);
    }

    private Map getMap(MvcResult mvcResult, boolean validate) {
        validateMvcResult(mvcResult);
        Map result = null;
        try {
            // TODO: Is there a cleaner way of doing this?
            HttpInputMessage inputMessage = new MockClientHttpResponse(mvcResult.getResponse().getContentAsByteArray(), HttpStatus.valueOf(mvcResult.getResponse().getStatus()));
            result = (Map) httpMessageConverter.read(Map.class, inputMessage);
            if (validate) {
                assertEquals(HttpStatus.OK.value(), result.get(BaseController.RESPONSE_STATUS));
            }
        } catch (Exception e) {
            log.error("Unable to parse result: {}. ", mvcResult, e);
        }
        return result;
    }

    protected void validateMvcResult(MvcResult mvcResult) {
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(EXPECTED_CONTENT_TYPE, mvcResult.getResponse().getContentType());
        assertEquals(EXPECTED_CHARACTER_ENCODING, mvcResult.getResponse().getCharacterEncoding());
    }
}