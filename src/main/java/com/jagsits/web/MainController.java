package com.jagsits.web;

import com.jagsits.service.version.VersionResponse;
import com.jagsits.service.version.VersionService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class MainController extends BaseController {

    static final String MAPPING_VERSION = "version";
    static final String MAPPING_CSRF = "csrf";

    static final String RESPONSE_VERSION = "version";
    static final String RESPONSE_TIMESTAMP = "timestamp";

    private static final String CSRF_CLASS_NAME = CsrfToken.class.getName();

    @Autowired
    private VersionService versionService;

    @RequestMapping(value = MAPPING_VERSION, method = RequestMethod.GET)
    public Object getVersion() {
        VersionResponse version = versionService.getVersion();
        Map<String, String> result = new HashedMap<>();
        result.put(RESPONSE_VERSION, version.getVersion());
        result.put(RESPONSE_TIMESTAMP, version.getBuildTimestamp());
        return buildResponse(result);
    }

    @RequestMapping(value = MAPPING_CSRF, method = RequestMethod.GET)
    public Object getCsrf(HttpServletRequest request) {
        return buildResponse(request.getAttribute(CSRF_CLASS_NAME));
    }

}
