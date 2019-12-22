package com.abionics.imaxt.core.crypto;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class CryptoAES implements Crypto {
    private static final String WRONG_PASSWORD_MESSAGE = "Wrong password";

    @NotNull
    @Contract("_ -> new")
    private SecretKeySpec setupSecretKey(@NotNull String password) throws NoSuchAlgorithmException {
        byte[] key = password.getBytes(StandardCharsets.UTF_8);
        var sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 32);
        return new SecretKeySpec(key, "AES");
    }

    public byte[] encrypt(@NotNull byte[] value, @NotNull String password) throws GeneralSecurityException {
        try {
            var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            var secretKey = setupSecretKey(password);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryption = cipher.doFinal(value);
            return Base64.getEncoder().encode(encryption);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            throw new GeneralSecurityException(e);
        }
    }

    public byte[] decrypt(byte[] value, @NotNull String password) throws GeneralSecurityException, CryptoException {
        if (password.isEmpty()) {
            throw new CryptoException(WRONG_PASSWORD_MESSAGE);
        }
        try {
            var cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            var secretKey = setupSecretKey(password);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] base64 = Base64.getDecoder().decode(value);
            return cipher.doFinal(base64);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException e) {
            throw new GeneralSecurityException(e);
        } catch (BadPaddingException e) {
            throw new CryptoException(WRONG_PASSWORD_MESSAGE);
        }
    }
}
