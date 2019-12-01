package com.abionics.imaxt;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

class RandomString {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final String identifiers = "0123456789" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz";

    @NotNull
    static String nextString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char character = (char) random.nextInt(32, Character.MAX_CODE_POINT);
            result.append(character);
        }
        return result.toString();
    }

    @NotNull
    static String nextIdentifier(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(identifiers.length());
            result.append(identifiers.charAt(index));
        }
        return result.toString();
    }
}
