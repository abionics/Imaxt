package com.abionics.imaxt.core.decoder;

import com.abionics.imaxt.core.CharacterRepresentation;
import com.abionics.imaxt.core.crypto.Crypto;
import com.abionics.imaxt.core.crypto.CryptoAES;
import com.abionics.imaxt.core.crypto.CryptoException;
import org.jetbrains.annotations.NotNull;

import java.security.GeneralSecurityException;

import static com.abionics.imaxt.core.Imcryptor.APPCODE;
import static com.abionics.imaxt.core.Imcryptor.METABYTES;

class Deconverter {
    private static final Crypto crypto = new CryptoAES();

    @NotNull
    static char[] deconvert(@NotNull byte[] code, String password) throws DecoderException, CryptoException, GeneralSecurityException {
        //header
        byte header = code[0];
        byte _appcode = (byte) ((header >> 4) & 0xf);
        byte _representation = (byte) ((header >> 1) & 7);
        byte _encryption = (byte) (header & 1);
        if (_appcode != APPCODE) throw new DecoderException("Appcode is not found", "Deconverter::deconvert");
        var representation = CharacterRepresentation.values()[_representation];
        var encryption = _encryption == 1;

        //version
        byte version = code[1];

        //size
        int size = (((code[2] & 0xff) << 24) | ((code[3] & 0xff) << 16) | ((code[4] & 0xff) << 8) | (code[5] & 0xff)) - METABYTES;

        //data
        byte[] data = new byte[size];
        System.arraycopy(code, METABYTES, data, 0, size);

        switch (version) {
            case 0: return new char[0];
            case 1: return deconvert1(data, password, representation, encryption);
            default: throw new DecoderException("Unsupported version", "Deconverter::deconvert");
        }
    }

    @NotNull
    private static char[] deconvert1(@NotNull byte[] data, String password, @NotNull CharacterRepresentation representation, boolean encryption) throws DecoderException, CryptoException, GeneralSecurityException {
        if (encryption) {
            data = crypto.decrypt(data, password);
        }

        int size = data.length;
        System.out.println("Decode v1: " + representation.toString() + ", size = " + size + ", encryption = " + encryption);
        switch (representation) {
            case ONE_BYTE: {
                char[] result = new char[size];
                for (int i = 0; i < result.length; i++)
                    result[i] = (char) Byte.toUnsignedInt(data[i]);
                return result;
            }
            case TWO_BYTES: {
                char[] result = new char[size / 2];
                for (int i = 0; i < result.length; i++)
                    result[i] = (char) ((Byte.toUnsignedInt(data[2 * i]) << 8) + Byte.toUnsignedInt(data[2 * i + 1]));
                return result;
            }
            default: throw new DecoderException("Unsupported type", "Deconverter::deconvert1");
        }
    }
}
