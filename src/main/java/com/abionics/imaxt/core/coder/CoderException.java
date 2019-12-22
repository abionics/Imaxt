package com.abionics.imaxt.core.coder;

public class CoderException extends Exception {
    private String place;

    CoderException(String reason, String place) {
        super(reason);
        this.place = place;
    }

    public String getPlace() {
        return place;
    }
}
