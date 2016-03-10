package com.jagsits.service.version;

import com.jagsits.service.BaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class VersionService extends BaseService {

    @Value("${build.version:UNKNOWN}")
    private String buildVersion;
    @Value("${build.timestamp:UNKNOWN}")
    private String buildTimestamp;

    private VersionResponse cache;

    @PostConstruct
    private void setup() {
        cache = new VersionResponse(buildVersion, buildTimestamp);
    }

    public VersionResponse getVersion() {
        return cache;
    }

}
