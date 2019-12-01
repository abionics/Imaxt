package com.abionics.imaxt.core.decoder;

import com.abionics.imaxt.core.ChannelsSpace;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.abionics.imaxt.core.Imaginator.CHANNELS;

class Deallocator {
    @NotNull
    @Contract(pure = true)
    static byte[] deallocate(@NotNull byte[][][] pixels, @NotNull ChannelsSpace space) {
        assert (pixels[0][0].length == CHANNELS) : "Deallocator::deallocate: count of channels must be " + CHANNELS + ", you have " + pixels[0][0].length;

        int firstChannel = space.begin;
        int lastChannel = space.end;
        int channels = lastChannel - firstChannel + 1; // = space.toString().length();

        int width = pixels.length;
        int height = pixels[0].length;
        byte[] data = new byte[width * height * channels];

        int k = 0;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                for (int c = firstChannel; c <= lastChannel; c++) {
                    data[k++] = pixels[i][j][c];
                }
        return data;
    }
}
