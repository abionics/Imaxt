package com.abionics.imaxt.core.decoder;

import com.abionics.imaxt.core.CharacterRepresentation;
import com.abionics.imaxt.core.Encryptor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.abionics.imaxt.core.Imcryptor.APPCODE;
import static com.abionics.imaxt.core.Imcryptor.METABYTES;

class Deconverter {
    @NotNull
    static char[] deconvert(@NotNull byte[] code, String password) throws IOException {
        //header
        byte header = code[0];
        byte _appcode = (byte) ((header >> 4) & 0xf);
        byte _representation = (byte) ((header >> 1) & 7);
        byte _encryption = (byte) (header & 1);
        if (_appcode != APPCODE) throw new IOException("Decode: appcode is not found");
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
            default: throw new IOException("Deconverter::deconvert: unsupported version");
        }
    }

    @NotNull
    private static char[] deconvert1(@NotNull byte[] data, String password, @NotNull CharacterRepresentation representation, boolean encryption) throws IOException {
        int size = data.length;
        System.out.println("Decode v1: " + representation.toString() + ", size = " + size + ", encryption = " + encryption);
        if (encryption) {
            Encryptor.encrypt(data, password);
        }
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
            default: throw new IOException("Deconverter::deconvert1: unsupported type");
        }
    }
}
