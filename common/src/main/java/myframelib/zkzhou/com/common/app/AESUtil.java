
package myframelib.zkzhou.com.common.app;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    public static String getAESKey() {
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance("AES");
            kg.init(64);// 要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            return parseByte2HexStr(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5(String input) {
        return new MD5Util().getMD5ofStr(input);
    }

    public static String toHexStringXiao(byte[] b) throws Exception {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexCharXiao[(b[i] & 0xf0) >>> 4]);
            sb.append(hexCharXiao[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static char[] hexCharXiao = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static byte[] encrypt(String sSrc, String sKey) throws Exception {
        return encrypt(sSrc, "utf-8", getMd5(sKey).substring(0, 16), getMd5(sKey).substring(16, 32));
    }

    public static byte[] encrypt(String sSrc, String encodingFormat, String sKey, String ivParameter)
            throws Exception {

        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return encrypted;
    }

    public static String decrypt(byte[] sSrc, String sKey) throws Exception {
        return decrypt(sSrc, "utf-8", getMd5(sKey).substring(0, 16), getMd5(sKey).substring(16, 32));
    }

    public static String decrypt(String sSrc, String sKey) throws Exception {
        return decrypt(parseHexStr2Byte(sSrc), "utf-8", getMd5(sKey).substring(0, 16), getMd5(sKey)
                .substring(16, 32));
    }

    public static String decrypt(byte[] sSrc, String encodingFormat, String sKey, String ivParameter)
            throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int ivSize = 16;

            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes(), 0, ivSize);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // byte[] encrypted1 = Base64Util.decode(parseByte2HexStr(sSrc));
            byte[] original = cipher.doFinal(sSrc);
            String originalString = new String(original, "UTF-8").trim();
            // String string2 = StringUtil.bytesToHexString(original);
            // String string1 = new String(original, HTTP.ASCII);
            return originalString;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt1(String aesKey, String str) {
        try {

            byte[] data = str.getBytes();
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes("utf-8"), "AES");
            // NoPadding,ZeroBytePadding
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int ivSize = 16;
            IvParameterSpec ivParameterSpec = new IvParameterSpec(data, 0, ivSize);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            byte[] plainText = cipher.doFinal(data, ivSize, data.length - ivSize);
            return new String(plainText, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] parseHexStr2Byte(String hexStr) {

        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
