package com.abionics.imaxt.core.coder;

import com.abionics.imaxt.core.CharacterRepresentation;
import com.abionics.imaxt.core.Encryptor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.abionics.imaxt.core.Imcryptor.*;

class Converter {
    @NotNull
    static byte[] convert(final @NotNull File file, String password) throws CoderException {
        try {
            byte[] title = file.getName().getBytes();
            byte[] content = Files.readAllBytes(file.toPath());
            byte[] data = new byte[title.length + 1 + content.length];
            System.arraycopy(title, 0, data, 0, title.length);
            System.arraycopy(content, 0, data, title.length + 1, content.length);
            var representation = CharacterRepresentation.ONE_BYTE;
            return convert(data, representation, password);
        } catch (IOException e) {
            throw new CoderException(e.getMessage(), "Converter::convert");
        }
    }

    @NotNull
    static byte[] convert(@NotNull final String text, String password) {
        char[] chars = text.toCharArray();
        boolean ascii = true;
        for (char ch : chars)
            if (ch > 255) {
                ascii = false;
                break;
            }
        //add 1 byte (or 2 bytes if not only ascii symbols (representation == TWO_BYTES)) to code "0" at the begin (plain text title)
        if (ascii) {
            byte[] data = new byte[chars.length + 1];
            for (int i = 0; i < chars.length; i++)
                data[i + 1] = (byte) chars[i];
            var representation = CharacterRepresentation.ONE_BYTE;
            return convert(data, representation, password);
        } else {
            byte[] data = new byte[2 * chars.length + 2];
            for (int i = 0; i < chars.length; i++) {
                char ch = chars[i];
                data[2 * i + 2] = (byte) (ch >> 8 & 0xff);
                data[2 * i + 3] = (byte) (ch & 0xff);
            }
            var representation = CharacterRepresentation.TWO_BYTES;
            return convert(data, representation, password);
        }
    }

    @NotNull
    static private byte[] convert(byte[] data, CharacterRepresentation representation, @NotNull String password) {
        boolean encryption = !password.isEmpty();
        if (encryption) {
            Encryptor.encrypt(data, password);
        }

        int size = METABYTES + data.length;
        byte[] code = new byte[size];
        System.out.println("Code: " + representation.toString() + ", size = " + size + ", encryption = " + encryption);
        code[0] = (byte) ((APPCODE << 4) + (representation.ordinal() << 1) + (encryption ? 1 : 0));
        code[1] = (byte) VERSION;
        code[2] = (byte) ((size >> 24) & 0xff);
        code[3] = (byte) ((size >> 16) & 0xff);
        code[4] = (byte) ((size >> 8) & 0xff);
        code[5] = (byte) (size & 0xff);
        System.arraycopy(data, 0, code, METABYTES, data.length);
        return code;
    }
}
