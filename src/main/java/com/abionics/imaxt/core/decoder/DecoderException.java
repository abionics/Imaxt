package com.abionics.imaxt.core.decoder;

public class DecoderException extends Exception {
    DecoderException(String reason, String place) {
        super(place + ": " + reason);
    }
}
