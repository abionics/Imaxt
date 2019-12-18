package com.abionics.imaxt.core.coder;

public class CoderException extends Exception {
    CoderException(String reason, String place) {
        super(place + ": " + reason);
    }
}
