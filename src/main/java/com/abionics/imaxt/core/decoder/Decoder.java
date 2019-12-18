package com.abionics.imaxt.core.decoder;

import com.abionics.imaxt.core.ChannelsSpace;
import com.abionics.imaxt.core.Imaginator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class Decoder {
    @NotNull
    public static char[] decode(File image, String password, ChannelsSpace space) throws DecoderException, IOException {
        Imaginator imaginator = new Imaginator(image);
        byte[][][] pixels = imaginator.pixels();
        byte[] code = Deallocator.deallocate(pixels, space);
        return Deconverter.deconvert(code, password);
    }
}
