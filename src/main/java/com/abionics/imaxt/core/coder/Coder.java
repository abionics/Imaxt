package com.abionics.imaxt.core.coder;

import com.abionics.imaxt.core.ChannelsSpace;
import com.abionics.imaxt.core.Imaginator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.security.GeneralSecurityException;

public class Coder {
    @NotNull
    @Contract("null, _, _ -> fail")
    public static Imaginator code(Object input, String password, ChannelsSpace space) throws CoderException, GeneralSecurityException {
        byte[] data;
        if (input instanceof File) {
            File file = (File) input;
            data = Converter.convert(file, password);
        } else if (input instanceof String) {
            String text = (String) input;
            data = Converter.convert(text, password);
        } else {
            throw new CoderException("Undefined input type", "Coder::code");
        }

        byte[][][] pixels = Allocator.allocate(data, space);
        return new Imaginator(pixels);
    }
}
