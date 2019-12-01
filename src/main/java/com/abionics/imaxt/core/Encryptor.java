package com.abionics.imaxt.core;

import org.jetbrains.annotations.NotNull;

public class Encryptor {
    public static void encrypt(@NotNull byte[] value, @NotNull String password) {
        final int magic = 145665451;
        if (password.isEmpty()) return;
        byte[] key = password.getBytes();
        int z = 0;
        for (int i = 0; i < value.length; i++) {
            z = (z + magic) % value.length;
            value[z] = (byte) (value[z] ^ ~key[i % key.length]);
        }
    }
}
