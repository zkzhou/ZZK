
package myframelib.zkzhou.com.common.app;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * 描述:SHA_256 加密
 */
public class StringEncryptUtil {

    public static final String SHA_256 = "SHA-256";

    private static String bytes2Hex(byte[] bytes) {
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & bytes[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        return md5StrBuff.toString();
    }

    public static String computeDigest(byte[] bData, String encName) {
        MessageDigest md = null;
        String strDes = null;
        try {
            if (TextUtils.isEmpty(encName)) {
                encName = "SHA-256";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bData);
            strDes = bytes2Hex(md.digest());
        } catch (Exception e) {
            return null;
        }
        return strDes;
    }

    public static String computeDigest(File file) {
        if (file.exists() && file.isFile()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                byte[] b = new byte[in.available()];
                in.read(b);
                return StringEncryptUtil.computeDigest(b, "SHA-256");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static String computeDigest(String strSrc, String encName) {
        byte[] bt = strSrc.getBytes();
        return computeDigest(bt, encName);
    }
}
