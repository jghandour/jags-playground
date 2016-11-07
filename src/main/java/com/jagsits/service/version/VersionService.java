package com.jagsits.service.version;

import com.jagsits.service.BaseService;
import com.jagsits.util.JagsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VersionService extends BaseService {

    @Autowired
    private BuildProperties buildProperties;

    @Cacheable("version")
    public VersionResponse getVersion() {
        return new VersionResponse(buildProperties.getVersion(), JagsUtils.ISO_8601_FORMAT.format(buildProperties.getTime()));
    }

}
