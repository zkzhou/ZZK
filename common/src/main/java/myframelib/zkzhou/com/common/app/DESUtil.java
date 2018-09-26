
package myframelib.zkzhou.com.common.app;


import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESUtil {
    public static byte ivec[] = {
            99, 22, 22, 33, 46, 33, 22, 44
    };

    public static byte key[] = {
            21, 21, 21, 55, 76, 60, 33, 22
    };

    public static byte vip_key[]={
            55,46,13,17,28,39,7,3
    };
    public static byte vip_iv[]={
            26,59,48,68,35,24,11,77
    };

    /**
     * Description 根据键值进行加密
     * 
     * @param data
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String str, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bt = cipher.doFinal(str.getBytes("UTF-8"));
            String strs = Base64Util.encode(bt);
            return strs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description 根据键值进行加密
     * 
     * @param data
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String str, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec ivs = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivs);
            byte[] bt = cipher.doFinal(str.getBytes("UTF-8"));
            String strs = Base64Util.encode(bt);
            return strs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String vipencrypt(String str, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec ivs = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivs);
            byte[] bt = cipher.doFinal(str.getBytes("UTF-8"));
            String strs = new BigInteger(1, bt).toString(16);//转换为16进制
            return strs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description 根据键值进行解密
     * 
     * @param data
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    public static String decrypt(String message, byte[] key) {
        if (message == null)
            return null;
        try {
            byte[] buf = Base64Util.decode(message);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            DESKeySpec desKeySpec = new DESKeySpec(key);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] bt = cipher.doFinal(buf);
            return new String(bt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
