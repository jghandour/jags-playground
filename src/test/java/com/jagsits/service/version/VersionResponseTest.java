package com.jagsits.service.version;

import org.junit.Test;

import static org.junit.Assert.*;

public class VersionResponseTest {

    @Test
    public void testObject() {
        String version = "version";
        String buildTimestamp = "date";

        VersionResponse versionResponse = new VersionResponse(version, buildTimestamp);
        VersionResponse versionResponse2 = new VersionResponse(version, buildTimestamp);
        assertEquals(versionResponse.hashCode(), versionResponse2.hashCode());
        assertTrue(versionResponse.equals(versionResponse2));
        assertEquals(versionResponse.toString(), versionResponse2.toString());

        assertEquals(version, versionResponse.getVersion());
        assertEquals(buildTimestamp, versionResponse.getBuildTimestamp());

        assertTrue(versionResponse.equals(versionResponse));
        assertFalse(versionResponse.equals(""));
        assertFalse(versionResponse.equals(null));
    }
}
