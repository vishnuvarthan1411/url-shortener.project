package com.example.URL.Util;

public class Base62Encoder {

    private static final String CHARACTERS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String encode(long number) {
        StringBuilder sb = new StringBuilder();
        if (number == 0) {
            return "0";
        }
        while (number > 0) {
            int remainder = (int) (number % 62);
            sb.append(CHARACTERS.charAt(remainder));
            number = number / 62;
        }
        sb.reverse();
        return sb.toString();
    }
}