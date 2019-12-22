package com.abionics.imaxt.core.crypto;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public interface Crypto {
    byte[] encrypt(byte[] value, String password) throws GeneralSecurityException;
    byte[] decrypt(byte[] value, String password) throws CryptoException, GeneralSecurityException;
}
