package com.example.sodrulaminshaon.wifitestapp1;

/**
 * Created by Sodrul Amin Shaon on 13-Feb-18.
 */

public class Base64 {

    private static final String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static byte[] base64Decode(String input) {
        if (input.length() % 4 != 0) {
            throw new IllegalArgumentException("Invalid base64 input");
        }
        byte decoded[] = new byte[((input.length() * 3) / 4) - (input.indexOf('=') > 0 ? (input.length() - input.indexOf('=')) : 0)];
        char[] inChars = input.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4) {
            // This could be made faster (but more complicated) by precomputing these index locations.
            b[0] = CODES.indexOf(inChars[i + 0]);
            b[1] = CODES.indexOf(inChars[i + 1]);
            b[2] = CODES.indexOf(inChars[i + 2]);
            b[3] = CODES.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64) {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64) {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }

        return decoded;
    }
    public static int base64Encode(byte[] data, int offset, int len) {

        StringBuilder out = new StringBuilder((len * 4) / 3);
        int b;
        for (int i = 0; i < len; i += 3) {
            b = (data[i + offset] & 0xFC) >> 2;
            out.append(CODES.charAt(b));
            b = (data[i + offset] & 0x03) << 4;
            if (i + 1 < len) {
                b |= (data[i + offset + 1] & 0xF0) >> 4;
                out.append(CODES.charAt(b));
                b = (data[i + offset + 1] & 0x0F) << 2;
                if (i + 2 < len) {
                    b |= (data[i + offset + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = data[i + offset + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }
        byte[] output = String.valueOf(out).getBytes();

        System.arraycopy(output, 0, data, offset, output.length);

        return output.length;
    }

    public static byte [] encode(byte[] in) {
        StringBuilder out = new StringBuilder((in.length * 4) / 3);
        int b;
        for (int i = 0; i < in.length; i += 3) {
            b = (in[i] & 0xFC) >> 2;
            out.append(CODES.charAt(b));
            b = (in[i] & 0x03) << 4;
            if (i + 1 < in.length) {
                b |= (in[i + 1] & 0xF0) >> 4;
                out.append(CODES.charAt(b));
                b = (in[i + 1] & 0x0F) << 2;
                if (i + 2 < in.length) {
                    b |= (in[i + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = in[i + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }

        return out.toString().getBytes();
    }

}
