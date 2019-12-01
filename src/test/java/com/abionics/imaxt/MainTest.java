package com.abionics.imaxt;

import com.abionics.imaxt.core.ChannelsSpace;
import com.abionics.imaxt.core.Imaginator;
import com.abionics.imaxt.core.coder.Coder;
import com.abionics.imaxt.core.decoder.Decoder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    @Test
    void codeTextTest() {
        try {
            var file = new File(RandomString.nextIdentifier(5) + ".png");
            for (int textLength = 1; textLength < 20; textLength += 2) {
                var text = RandomString.nextString(textLength);
                for (int passwordLength = 1; passwordLength < 10; passwordLength += 2) {
                    var password = RandomString.nextString(passwordLength);
                    for (var space : ChannelsSpace.values()) {
                        Imaginator imaginator = Coder.code(text, password, space);
                        imaginator.save(file);

                        var chars = Decoder.decode(file, password, space);
                        var result = new String(chars).substring(1);

                        assertEquals(text, result);
                    }
                }
            }
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
