package myframelib.zkzhou.com.common.app;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作类，如截取、合并等操作
 */
public class StringUtil {

    /**
     * 小于10的正整数前面补0
     *
     * @param i
     * @return
     */
    public static String add0IfLgTen(int i) {
        if (0 < i && i < 10) {
            return "0" + i + ".";
        } else {
            return i + ".";
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 统计字符串(汉字、数字和英文)中字节个数
     *
     * @param str
     * @return
     */
    public static int countWords(String str) {
        int len = 0;
        try {
            if (!TextUtils.isEmpty(str)) {
                len = str.getBytes("GBK").length;
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return len;
    }

    /**
     * 对乱码进行转码
     *
     * @param s
     * @return
     */
    public static String errEncode(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5\\u0800-\\u4e00]+");
        Matcher matcher = pattern.matcher(s);
        if (!matcher.find()) {
            try {
                return s = new String(s.getBytes("iso-8859-1"), "GBK");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return s;
    }

    public static String formatSize(long size) {
        long SIZE_KB = 1024;
        long SIZE_MB = SIZE_KB * 1024;
        long SIZE_GB = SIZE_MB * 1024;

        // if (size < SIZE_KB) {
        // return String.format("%dB", (int) size);
        // } else
        if (size < SIZE_MB) {
            return String.format("%.2fKB", (float) size / SIZE_KB);
        } else if (size < SIZE_GB) {
            return String.format("%.2fMB", (float) size / SIZE_MB);
        } else {
            return String.format("%.2fGB", (float) size / SIZE_GB);
        }
    }

    public static String getCharSize(long size) {
        String charsize = "";
        double doublesize = 0d;
        DecimalFormat df = new DecimalFormat("#.0");
        if (size / (1024 * 1024 * 1024) >= 1) {
            doublesize = size / (1024 * 1024 * 1024d);
            charsize = df.format(doublesize) + "G";
        } else if (size / (1024 * 1024) >= 1) {
            doublesize = size / (1024 * 1024d);
            charsize = df.format(doublesize) + "M";
        } else {
            charsize = "0K";
            if (size != 0L) {
                doublesize = size / 1024d;
                charsize = df.format(doublesize) + "K";
            }
        }
        return charsize;
    }

    public static String getExceptionString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString().replace("\n", "<br />");
    }

    /**
     * 从字符串中获取第1组符合正则表达式的内容
     *
     * @param regex
     * @param source
     * @return
     */
    public static String getMatcher(String regex, String source) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result = matcher.group(1);// 只取第一组
        }
        return result;
    }

    /**
     * 产生一个描写长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random(System.currentTimeMillis());
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);// 0~61
            sbf.append(str.charAt(number));
        }
        return sbf.toString();
    }

    /**
     * 单位换算
     *
     * @param fileSize
     * @return
     */
    public static String getSizeText(long fileSize) {
        if (fileSize <= 0) {
            return "0M";
        }
        if (fileSize > 0 && fileSize < 1024 * 0.1) {
            float result = 0.1f;
            String suffix = "K";
            return String.format("%.1f", result) + suffix;
        }
        if (fileSize >= 1024 * 0.1 && fileSize < 1024 * 1024) {
            float result = fileSize;
            String suffix = "K";
            result = result / 1024;
            return String.format("%.1f", result) + suffix;
        }
        float result = fileSize;
        String suffix = "M";
        result = result / 1024 / 1024;
        return String.format("%.1f", result) + suffix;
    }

    /**
     * 通过图像地址和hash值获得由hash值和图片扩展名组成的一个文件名
     *
     * @param imageUrl
     * @param hash
     * @return
     */
    public static String hashImageName(String imageUrl, String hash) {
        if (TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(hash)) {
            return null;
        }
        imageUrl = imageUrl.toLowerCase();
        hash = hash.toLowerCase();

        int index = imageUrl.indexOf(".jpg");
        if (index == -1) {
            index = imageUrl.indexOf(".png");
        }
        String imageName = hash;
        if (index >= 0) {
            imageName += imageUrl.substring(index);
        }
        return imageName;
    }


    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s) || "null".equals(s);
    }

    /**
     * 是否乱码
     *
     * @param s
     * @return true 乱码
     */
    public static boolean isErrCode(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(s);
        return !matcher.find();
    }

    public static String loadStringFromRaw(Context context, int resId) throws Exception {
        InputStream in = context.getResources().openRawResource(resId);
        return new String(toByteArray(in));
    }

    /**
     * 截取图片名称
     *
     * @param imageurl
     * @return
     */
    public static String spiltImageName(String imageurl) {
        if (TextUtils.isEmpty(imageurl)) {
            return null;
        }
        imageurl = imageurl.toLowerCase();
        int start = imageurl.lastIndexOf("filename");
        if (start == -1) {
            start = imageurl.lastIndexOf("/");
            if (start == -1) {
                return null;
            } else {
                start += 1;
            }
        } else {
            start += 9;
        }
        int end = imageurl.indexOf(".jpg", start);
        if (end == -1) {
            end = imageurl.indexOf(".png", start);
            if (end == -1) {
                return null;
            } else {
                end += 4;
            }
        } else {
            end += 4;
        }
        return imageurl.substring(start, end);
    }

    /**
     * 分割字符串
     *
     * @param src
     * @param delimiter
     * @return
     */
    public static String[] split(String src, String delimiter) {
        if (src == null || delimiter == null || src.trim().equals("")
                || delimiter.trim().equals("")) {
            return new String[]{
                    src
            };
        }
        ArrayList<String> list = new ArrayList<String>();
        int lengthOfDelimiter = delimiter.length();
        int pos = 0;
        while (true) {
            if (pos < src.length()) {
                int indexOfDelimiter = src.indexOf(delimiter, pos);
                if (indexOfDelimiter < 0) {
                    list.add(src.substring(pos));
                    break;
                } else {
                    list.add(src.substring(pos, indexOfDelimiter));
                    pos = indexOfDelimiter + lengthOfDelimiter;
                }
            } else if (pos == src.length()) {
                list.add("");
                break;
            } else {
                break;
            }
        }
        String[] result = new String[list.size()];
        list.toArray(result);
        return result;
    }

    /**
     * 将input流转为byte数组，自动关闭
     *
     * @param input
     * @return
     */
    public static byte[] toByteArray(InputStream input) throws Exception {
        if (input == null) {
            return null;
        }
        ByteArrayOutputStream output = null;
        byte[] result = null;
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 100];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            result = output.toByteArray();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 字符串转长整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static long toLong(String str, long defValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 将String转换为适合url使用的unicode
     *
     * @param src
     * @return
     */
    public static String toUnicode(String src) {
        if (TextUtils.isEmpty(src)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int length = src.length();
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(src.charAt(i));
            if (hex.length() <= 2) {
                builder.append("%").append(hex);
            } else {
                builder.append("%u").append(hex);
            }
        }
        return builder.toString();
    }

    public static String truncateUTF8String(String str) {
        final int DISPLAYNAME_MAX_COUNT = 150;
        final boolean enabled = true;
        if (enabled) {
            return truncateUTF8String(str, DISPLAYNAME_MAX_COUNT);
        } else {
            return str;
        }
    }

    /**
     * @hide
     */
    public static String truncateUTF8String(String str, int maxCount) {
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes();
        if (bytes.length <= maxCount) {
            return str;
        }
        int pos = maxCount;
        while (pos >= 0) {
            int v = bytes[pos] & 0xFF;
            if (v < 0x80 || v >= 0xC0) {
                break;
            }
            --pos;
        }
        byte[] newBytes = new byte[Math.max(0, pos)];
        System.arraycopy(bytes, 0, newBytes, 0, newBytes.length);
        return new String(newBytes);
    }


    /**
     * unicode转utf字符串，如果失败返回源字符串
     *
     * @param theUnicode
     * @return
     */
    public static String unicodeToUtf8(String theUnicode) {
        if (isEmpty(theUnicode)) {
            return theUnicode;
        }
        char aChar;
        int len = theUnicode.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theUnicode.charAt(x++);
            if (aChar == '\\') {
                aChar = theUnicode.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theUnicode.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    private static boolean is_number(String number) {
        if (number == null)
            return false;
        return number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");
    }

    /**
     * 将字节数转换为单位有MB的字符串
     *
     * @param byteLength 字节长度
     * @return
     */
    public static String parseByteToMbString(long byteLength) {
        if (byteLength <= 0) {
            return "0.00MB";
        }
        final DecimalFormat format = new DecimalFormat("#######0.00");
        long k = byteLength / 1024;
        if (k < 1024) {
            return format.format(k / 1024.00) + "MB";
        } else {
            return format.format((k / 1024 + (k % 1024) / 1024.00f)) + "MB";
        }
    }

    public static String replaceURLChar(String ch) {
        ch = ch.replace("%", "%25");

        ch = ch.replace("+", "%2B");
        ch = ch.replace(" ", "%20");
        ch = ch.replace("/", "%2F");
        ch = ch.replace("?", "%3F");
        ch = ch.replace("#", "%23");
        ch = ch.replace("&", "%26");
        ch = ch.replace("=", "%3D");
        return ch;
    }

    /**
     * 过滤中文字符
     *
     * @param source
     * @return
     */
    public static String filterChineseChar(String source) {
        String regEx = "[\u4e00-\u9fa5]";
        return source.replaceAll(regEx, " ");
    }

    /**
     * 只保留中文字符
     */
    public static String retentionChineseChar(String source) {
        String regEx = "[^\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);
        return m.replaceAll("").trim();
    }

    /**
     * 根据空格拆分成字符串数组
     *
     * @param source
     * @return
     */
    public static String[] spitStringBlank(String source) {
        Pattern pattern = Pattern.compile(" ");
        return pattern.split(source);
    }

    /**
     * 清除掉所有特殊字符
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
