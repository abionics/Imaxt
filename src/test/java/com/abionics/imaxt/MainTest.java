package com.abionics.imaxt;

import com.abionics.imaxt.core.ChannelsSpace;
import com.abionics.imaxt.core.Imaginator;
import com.abionics.imaxt.core.coder.Coder;
import com.abionics.imaxt.core.decoder.Decoder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    @Test
    void codeTextTest() {
        var imageFile = new File(RandomString.nextIdentifier(5) + ".png");
        try {
            for (int textLength = 1; textLength < 20; textLength += 2) {
                var text = RandomString.nextString(textLength);
                for (int passwordLength = 0; passwordLength < 10; passwordLength += 2) {
                    var password = RandomString.nextString(passwordLength);
                    for (var space : ChannelsSpace.values()) {
                        System.out.println(text + " | " + password + " | " + space);
                        Imaginator imaginator = Coder.code(text, password, space);
                        imaginator.save(imageFile);

                        var chars = Decoder.decode(imageFile, password, space);
                        var result = new String(chars).substring(1);

                        assertEquals(text, result);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            imageFile.delete();
        }
    }

    @Test
    void codeFileTest() {
        var imageFile = new File(RandomString.nextIdentifier(5) + ".png");
        try {
            var file = new File("src/test/resources/image.png");
            for (int passwordLength = 0; passwordLength < 10; passwordLength += 2) {
                var password = RandomString.nextString(passwordLength);
                for (var space : ChannelsSpace.values()) {
                    System.out.println(file.getName() + " | " + password + " | " + space);
                    Imaginator imaginator = Coder.code(file, password, space);
                    imaginator.save(imageFile);

                    var chars = Decoder.decode(imageFile, password, space);
                    var resultFile = new File("src/test/resources/image_decode.png");
                    saveToFile(chars, resultFile);

                    assertEquals(readFile(file), readFile(resultFile));
                    resultFile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            imageFile.delete();
        }
    }

    @Test
    void codeBigFileTest() {
        var imageFile = new File(RandomString.nextIdentifier(5) + ".png");
        try {
            var file = new File("src/test/resources/image_big.png");
            var password = RandomString.nextString(12);
            var space = ChannelsSpace.G;

            System.out.println(file.getName() + " | " + password + " | " + space);
            Imaginator imaginator = Coder.code(file, password, space);
            imaginator.save(imageFile);

            var chars = Decoder.decode(imageFile, password, space);
            var resultFile = new File("src/test/resources/image_big_decode.png");
            saveToFile(chars, resultFile);

            assertEquals(readFile(file), readFile(resultFile));
            resultFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            imageFile.delete();
        }
    }

    private void saveToFile(@NotNull char[] data, File file) throws IOException {
        int separator = 0;
        for (int i = 0; i < data.length; i++)
            if (data[i] == 0) {
                separator = i;
                break;
            }
        separator++;
        byte[] content = convertToBytes(data, separator);
        var writer = new FileOutputStream(file);
        writer.write(content);
        writer.close();
    }

    @NotNull
    @Contract(pure = true)
    private byte[] convertToBytes(@NotNull char[] data, int from) {
        byte[] content = new byte[data.length - from];
        for (int i = 0; i < data.length - from; i++)
            content[i] = (byte) data[i + from];
        return content;
    }

    String readFile(@NotNull File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }
}
