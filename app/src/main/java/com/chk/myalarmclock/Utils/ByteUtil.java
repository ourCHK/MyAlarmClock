package com.chk.myalarmclock.Utils;

/**
 * Created by chk on 17-10-19.
 * Byte工具操作类
 */

public class ByteUtil {

    public static byte stringToByte(String s) {
        byte b = Byte.parseByte(s);
        return b;
    }

    public static String byteToString(byte b) {
        String s = Byte.toString(b);
        return s;
    }
}
