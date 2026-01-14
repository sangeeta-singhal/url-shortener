package com.example.urlShortener.util;

public class Base62Encoder {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;

    public Base62Encoder() {
    }

    public String generateShortCode(long value) {
        if (value == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % BASE)));
            value /= BASE;
        }

        return sb.reverse().toString();
    }
}
