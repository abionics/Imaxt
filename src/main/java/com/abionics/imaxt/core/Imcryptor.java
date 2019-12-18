package com.abionics.imaxt.core;

import com.abionics.imaxt.core.coder.Coder;
import com.abionics.imaxt.core.coder.CoderException;
import com.abionics.imaxt.core.decoder.Decoder;
import com.abionics.imaxt.core.decoder.DecoderException;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Imcryptor {
    public static final int APPCODE = 0b1011;
    public static final int METABYTES = 6;
    public static final int VERSION = 1;

    public static final double SIDE_RATIO = 1.0;

    private final Window window;
    private final String directory;
    public boolean isCreateFile;


    @Contract(pure = true)
    public Imcryptor(Window window, String directory, boolean isCreateFile) {
        this.window = window;
        this.directory = directory;
        this.isCreateFile = isCreateFile;
    }

    public void code(Object input, String password, ChannelsSpace space) throws IOException, CoderException {
        Imaginator imaginator = Coder.code(input, password, space);

        var chooser = new FileChooser();
        chooser.setInitialFileName("encoded.png");
        chooser.setInitialDirectory(new File(directory));
        File file = chooser.showSaveDialog(window);
        if (file == null) return;
        imaginator.save(file);
    }

    public String decode(File input, String password, ChannelsSpace space) throws IOException, DecoderException {
        char[] data = Decoder.decode(input, password, space);

        int separator = 0;
        for (int i = 0; i < data.length; i++)
            if (data[i] == 0) {
                separator = i;
                break;
            }
        var titleBytes = new byte[separator];
        for (int i = 0; i < separator; i++)
            titleBytes[i] = (byte) data[i];
        String title = new String(titleBytes);
        if (separator == 0) {
            // text
            char[] content = new char[data.length - 1];
            System.arraycopy(data, 1, content, 0, content.length);
            return new String(content);
        } else {
            // file
            separator++;
            byte[] content = new byte[data.length - separator];
            for (int i = 0; i < data.length - separator; i++)
                content[i] = (byte) data[i + separator];
            FileOutputStream writer;
            if (isCreateFile) {
                FileChooser chooser = new FileChooser();
                chooser.setInitialFileName(title);
                File file = chooser.showSaveDialog(window);
                if (file == null) return "";
                writer = new FileOutputStream(file);
            } else {
                writer = new FileOutputStream(new File(directory + title));
            }
            writer.write(content);
            writer.close();
            return "";
        }
    }
}
