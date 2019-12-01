package com.abionics.imaxt;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

class RandomString {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    @NotNull
    static String next(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char character = (char) random.nextInt(32, Character.MAX_CODE_POINT);
            result.append(character);
        }
        return result.toString();
    }
}
