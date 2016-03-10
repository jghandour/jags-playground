package com.jagsits.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class CharsetUtils {

    public static final String UTF_8 = "UTF-8";
    public static final Charset CHARSET_UTF_8 = Charset.forName(UTF_8);

    public static final String UTF_16 = "UTF-16";
    public static final Charset CHARSET_UTF_16 = Charset.forName(UTF_16);

    private CharsetUtils() {
    }

    public static byte[] toUTF8(String input) {
        return to(CHARSET_UTF_8, input);
    }

    public static String toUTF8(byte[] input) {
        return to(CHARSET_UTF_8, input);
    }

    public static byte[] toUTF16(String input) {
        return to(CHARSET_UTF_16, input);
    }

    public static String toUTF16(byte[] input) {
        return to(CHARSET_UTF_16, input);
    }

    public static byte[] to(Charset charset, String input) {
        byte[] result = null;
        if (input != null && charset != null) {
            result = input.getBytes(charset);
        }
        return result;
    }

    public static String to(Charset charset, byte[] input) {
        String result = null;
        if (input != null && charset != null) {
            result = charset.decode(ByteBuffer.wrap(input)).toString();
        }
        return result;
    }

}