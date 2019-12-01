package com.abionics.imaxt.core;

import org.jetbrains.annotations.Contract;

public enum ChannelsSpace {
    ARGB(0, 3),
    RGB(1, 3),
    A(0, 0),
    R(1, 1),
    G(2, 2),
    B(3, 3),
    AR(0, 1),
    RG(1, 2),
    GB(2, 3),
    ARG(0, 2);

    public final int begin;
    public final int end;

    @Contract(pure = true)
    ChannelsSpace(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }
}
