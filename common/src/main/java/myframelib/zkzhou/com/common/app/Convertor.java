
package myframelib.zkzhou.com.common.app;

import android.content.Context;
import android.text.TextUtils;


import java.io.ByteArrayInputStream;

/**
 * 转换类
 */
public class Convertor {
    /**
     * byte[]转hash字符串
     *
     * @param hashByte
     * @return 哈希值
     */
    public static String byteHashToString(byte[] hashByte) {
        if (hashByte == null || hashByte.length != 16) {
            return null;
        }
        StringBuilder sb = new StringBuilder(32);
        for (byte b : hashByte) {
            sb.append(MD5Util.byteHEX(b));
        }
        return sb.toString().toLowerCase();
    }

    /**
     * byte转为int
     *
     * @param bytes
     * @return
     */
    public static int bytesToInt(byte[] bytes) {
        int addr = 0;
        int len = bytes.length;
        if (len == 1) {
            addr = bytes[0] & 0xFF;
        } else if (len == 2) {
            addr = bytes[0] & 0xFF;
            addr |= ((bytes[1] << 8) & 0xFF00);
        } else if (len == 3) {
            addr = bytes[0] & 0xFF;
            addr |= ((bytes[1] << 8) & 0xFF00);
            addr |= ((bytes[2] << 16) & 0xFF0000);
        } else if (len == 4) {
            addr = bytes[0] & 0xFF;
            addr |= ((bytes[1] << 8) & 0xFF00);
            addr |= ((bytes[2] << 16) & 0xFF0000);
            addr |= ((bytes[3] << 24) & 0xFF000000);
        }

        return addr;
    }

    /***
     * byte转boolean值
     * @param x
     * @return
     */
    public static boolean byteToBoolean(byte x) {
        boolean bs[] = new boolean[4];
        bs[0] = ((x & 0x01) != 0);
        bs[1] = ((x & 0x02) != 0);
        bs[2] = ((x & 0x04) != 0);
        bs[3] = ((x & 0x08) != 0);
        return bs[0] && bs[1] && bs[2] && bs[3];
    }

    /**
     * 注释：字节数组到int的转换！
     *
     * @param b
     * @return
     */
    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;// 最低位
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }

    // byte数组转成long
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * 注释：字节数组到short的转换！
     *
     * @param b
     * @return
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    public static byte[] doubleToByte(double x) {
        byte[] b = new byte[8];
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < 8; i++) {
            b[i] = new Long(l).byteValue();
            l = l >> 8;
        }
        return b;
    }

    /**
     * 把float转为低字节在前，高字节在后的byte数组
     *
     * @param i
     * @return
     */
    public static byte[] getBytes(float i) {
        return getBytes(Float.floatToRawIntBits(i));
    }

    /**
     * 把整型数转为低字节在前，高字节在后的byte数组
     *
     * @param i
     * @return
     */
    public static byte[] getBytes(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (i & 0xff);
        b[1] = (byte) (i >> 8 & 0xff);
        b[2] = (byte) (i >> 16 & 0xff);
        b[3] = (byte) (i >> 24 & 0xff);

        return b;
    }

    public static byte[] getBytes(int i, int len) {
        byte[] b = new byte[len];
        if (len == 1) {
            b[0] = (byte) (0xff & i);
        } else if (len == 2) {
            b[0] = (byte) (0xff & i);
            b[1] = (byte) ((0xff00 & i) >> 8);
        } else if (len == 3) {
            b[0] = (byte) (0xff & i);
            b[1] = (byte) ((0xff00 & i) >> 8);
            b[2] = (byte) ((0xff0000 & i) >> 16);
        } else {
            b[0] = (byte) (0xff & i);
            b[1] = (byte) ((0xff00 & i) >> 8);
            b[2] = (byte) ((0xff0000 & i) >> 16);
            b[3] = (byte) ((0xff000000 & i) >> 24);
        }

        return b;
    }

    /**
     * 获取哈希值
     *
     * @param hashValue
     * @return
     */
    public static byte[] getHashCode(String hashValue) {
        if (TextUtils.isEmpty(hashValue)) {
            return null;
        }
        int len = hashValue.length();
        byte[] bytes = new byte[len / 2];
        int offset = 2;
        int counter = 0;
        for (int i = 0; i < len; i = i + 2, offset = offset + 2) {
            String temp = hashValue.substring(i, offset);
            int n = getInt(temp);
            bytes[counter++] = (byte) n;
        }
        return bytes;
    }

    // 2位16进制数转为整数
    public static int getInt(String str) {
        try {
            str = str.toLowerCase();
            int hightValue = Integer.parseInt(str.substring(0, 1), 16);
            int lowValue = Integer.parseInt(str.substring(1, 2), 16);
            return hightValue * 16 + lowValue;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 注释：int到字节数组的转换！
     *
     * @param number
     * @return
     */
    public static byte[] intToByte(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    // long类型转成byte数组
    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * 注释：short到字节数组的转换！
     *
     * @param
     * @return
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * 字符转unicode
     *
     * @param s
     * @return
     */
    public static byte[] toUnicode(char s) {
        short unicode = (short) s;
        return shortToByte(unicode);
    }

    /**
     * 多个unicode转字符串
     *
     * @param bytes
     * @return
     */
    public static String unicodeArrayToString(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        StringBuilder builder = new StringBuilder();
        byte[] temp = new byte[2];
        for (int i = 0; i < bytes.length / 2; i++) {
            bis.read(temp, 0, temp.length);
            char c = unicodeToChar(temp);
            builder.append(c);
        }
        return builder.toString();
    }

    /**
     * 单个unicoed转字符
     *
     * @param bytes
     * @return
     */
    public static char unicodeToChar(byte[] bytes) {
        short unicode = byteToShort(bytes);
        return (char) unicode;
    }



    /**
     * 整形特征字符串转整形
     *
     * @param tmp
     * @return
     */
    public static int strToInt(String tmp) {
        if (tmp == null || !tmp.matches("[+-]?\\d+"))
            return Integer.MIN_VALUE;
        try {
            return Integer.valueOf(tmp);

        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }

    }

}
