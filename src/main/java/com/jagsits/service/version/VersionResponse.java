package com.jagsits.service.version;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class VersionResponse {

    private String version;
    private String buildTimestamp;

    public VersionResponse(String version, String buildTimestamp) {
        this.version = version;
        this.buildTimestamp = buildTimestamp;
    }

    public String getVersion() {
        return version;
    }

    public String getBuildTimestamp() {
        return buildTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VersionResponse that = (VersionResponse) o;
        return new EqualsBuilder().append(getVersion(), that.getVersion()).append(getBuildTimestamp(), that.getBuildTimestamp()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getVersion()).append(getBuildTimestamp()).toHashCode();
    }

}
