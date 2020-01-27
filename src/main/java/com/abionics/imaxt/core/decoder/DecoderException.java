package com.abionics.imaxt.core.decoder;

public class DecoderException extends Exception {
    private String place;

    DecoderException(String reason, String place) {
        super(reason);
        this.place = place;
    }

    public String getPlace() {
        return place;
    }
}
