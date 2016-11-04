package com.jagsits.util;

import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public final class JagsUtils {

    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String DEFAULT_CHAR_VALUE = "\0";
    public static final char URL_SEPARATOR = '/';

    private JagsUtils() {
    }

    public static int[][] transpose(int[][] input) {
        int[][] result = new int[input[0].length][input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                result[j][i] = input[i][j];
            }
        }
        return result;
    }

    public static <E extends Enum<E>> Collection<String> getEnumNames(Class<E> type) {
        return getEnumNames(EnumSet.allOf(type));
    }

    public static <E extends Enum<E>> Collection<String> getEnumNames(EnumSet<E> enumSet) {
        List<String> result = new LinkedList<>();
        enumSet.forEach(e -> result.add(e.name()));
        return result;
    }

    /**
     * Repeat a given string, count number of times.
     *
     * @param inputString, must not be null
     * @param count,       must be greater than zero.
     * @return a string containing inputString, count number of times.
     */
    public static String repeatString(String inputString, int count) {
        if (inputString == null) {
            throw new IllegalArgumentException("inputString must not be null!");
        }
        if (count < 0) {
            throw new IllegalArgumentException("count must be greater than zero.");
        }
        return new String(new char[count]).replace(DEFAULT_CHAR_VALUE, inputString);
    }
}
