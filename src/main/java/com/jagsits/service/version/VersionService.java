package com.jagsits.service.version;

import com.jagsits.service.BaseService;
import com.jagsits.util.JagsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class VersionService extends BaseService {

    @Autowired
    private BuildProperties buildProperties;

    private VersionResponse cache;

    @PostConstruct
    private void setup() {
        cache = new VersionResponse(buildProperties.getVersion(), JagsUtils.ISO_8601_FORMAT.format(buildProperties.getTime()));
    }

    public VersionResponse getVersion() {
        return cache;
    }

}
