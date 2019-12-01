package com.abionics.imaxt.core;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class Imaginator {
    public static final int CHANNELS = 4;

    private final int width;
    private final int height;
    private BufferedImage image;
    private byte[][][] pixels;


    public Imaginator(File input) throws IOException {
        image = ImageIO.read(input);
        width = image.getWidth();
        height = image.getHeight();
        pixels = new byte[width][height][CHANNELS];

        int[] buff = image.getRGB(0, 0, width, height, null, 0, width);
        int k = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int color = buff[k++];
                pixels[j][i][0] = (byte) ((color >> 24) & 0xff);
                pixels[j][i][1] = (byte) ((color >> 16) & 0xff);
                pixels[j][i][2] = (byte) ((color >> 8) & 0xff);
                pixels[j][i][3] = (byte) (color & 0xff);
            }
    }

    public Imaginator(@NotNull byte[][][] pixels) {
        assert (pixels[0][0].length == CHANNELS) : "Imaginator: count of channels must be " + CHANNELS + ", you have " + pixels[0][0].length;

        width = pixels.length;
        height = pixels[0].length;
        image = new BufferedImage(width, height, TYPE_INT_ARGB);

        int[] buff = new int[width * height];
        int k = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int color = 0;
                for (int c = 0; c < CHANNELS; c++) {
                    color <<= 8;
                    color |= Byte.toUnsignedInt(pixels[j][i][c]);
                }
                buff[k++] = color;
            }
        image.setRGB(0, 0, width, height, buff, 0, width);
    }

    public byte[][][] pixels() {
        return pixels;
    }

    public void save(File output) throws IOException {
        ImageIO.write(image, "png", output);
    }
}
