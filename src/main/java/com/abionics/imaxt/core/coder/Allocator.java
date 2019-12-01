package com.abionics.imaxt.core.coder;

import com.abionics.imaxt.core.ChannelsSpace;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

import static com.abionics.imaxt.core.Imaginator.CHANNELS;
import static com.abionics.imaxt.core.Imcryptor.SIDE_RATIO;

class Allocator {
    @NotNull
    static byte[][][] allocate(@NotNull byte[] data, @NotNull ChannelsSpace space) {
        int firstChannel = space.begin;
        int lastChannel = space.end;
        int channels = lastChannel - firstChannel + 1; // = space.toString().length();

        int pixelsCount = (data.length + channels - 1) / channels;
        double len = Math.sqrt(pixelsCount / SIDE_RATIO);
        int width = (int) Math.ceil(len * SIDE_RATIO);
        int height = (int) Math.ceil(len);
        byte[][][] pixels = new byte[width][height][CHANNELS];

        int count = width * height * CHANNELS;
        byte[] dataFit = copyAndFit(data, count);

        int k = 0;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                pixels[i][j][0] = (byte) 0xff;
                for (int c = firstChannel; c <= lastChannel; c++) {
                    pixels[i][j][c] = dataFit[k++];
                }
            }
        return pixels;
    }

    private static byte[] copyAndFit(@NotNull final byte[] data, int size) {
        if (data.length == size) {
            return data.clone();
        }
        byte[] dataFit = new byte[size];
        System.arraycopy(data, 0, dataFit, 0, data.length);
        byte[] randoms = new byte[size - data.length];
        ThreadLocalRandom.current().nextBytes(randoms);
        System.arraycopy(randoms, 0, dataFit, data.length, randoms.length);
        return dataFit;
    }
}
