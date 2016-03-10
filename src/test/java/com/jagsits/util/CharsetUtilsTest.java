package com.jagsits.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharsetUtilsTest {

    @Test
    public void test() {
        String testStr = "This is a test!";
        byte[] encoded;
        String decoded;

        // UTF 8
        encoded = CharsetUtils.toUTF8(testStr);
        decoded = CharsetUtils.toUTF8(encoded);
        assertEquals(testStr, decoded);
        assertNull(CharsetUtils.toUTF8((String) null));
        assertNull(CharsetUtils.toUTF8((byte[]) null));

        // UTF 16
        encoded = CharsetUtils.toUTF16(testStr);
        decoded = CharsetUtils.toUTF16(encoded);
        assertEquals(testStr, decoded);
        assertNull(CharsetUtils.toUTF16((String) null));
        assertNull(CharsetUtils.toUTF16((byte[]) null));

        // Nulls Charset
        assertNull(CharsetUtils.to(null, testStr));
        assertNull(CharsetUtils.to(null, new byte[]{}));

    }
}
