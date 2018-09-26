
package myframelib.zkzhou.com.common.net;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码解码类，简单对系统api进行封装，解决把空格转为+号，导致某些接口处理异常的问题
 */
public class UrlCoderUtil {

    /****
     * 解码
     * @param str
     * @param enc 格式
     * @return
     */
    public static String decode(String str, String enc) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            return URLDecoder.decode(str, enc);
        } catch (UnsupportedEncodingException e) {
            return decode(str);
        }
    }

    /****
     * 解码（默认utf-8）
     * @param str
     * @return
     */
    public static String decode(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /***
     * 编码，默认utf-8
     * @param str
     * @return
     */
    public static String encode(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            return URLEncoder.encode(str, "utf-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /***
     * 编码
     * @param str
     * @param enc
     * @return
     */
    public static String encode(String str, String enc) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            return URLEncoder.encode(str, enc).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            return encode(str);
        }
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuilder tmp = new StringBuilder(20);
        if (src != null) {
            tmp.ensureCapacity(src.length() * 6);
            for (i = 0; i < src.length(); i++) {
                j = src.charAt(i);
                if (Character.isDigit(j) || Character.isLowerCase(j)
                        || Character.isUpperCase(j))
                    tmp.append(j);
                else if (j < 256) {
                    tmp.append("%");
                    if (j < 16)
                        tmp.append("0");
                    tmp.append(Integer.toString(j, 16));    //16进制
                } else {
                    tmp.append("%u");
                    tmp.append(Integer.toString(j, 16));
                }
            }
        }
        return tmp.toString();
    }
}
