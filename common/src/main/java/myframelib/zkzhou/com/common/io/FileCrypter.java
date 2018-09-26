
package myframelib.zkzhou.com.common.io;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * 描述:文件加密
 */
public class FileCrypter {

    private static final String TAG = "FileCrypter";

    private static final byte[] magic = {
            0x6b, 0x75, 0x67, 0x6f, 0x75, 0x41, 0x70, 0x72, 0x69, 0x6c, 0x32, 0x30, 0x31, 0x34
    };

    private static final int BUF_SIZE = 1024 * 1024 * 4; // 4m

    private static final int MAX_ENCRYPT_SIZE = 45;

    private static String bytesToHexString(byte[] bytes) {
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

    public static boolean decrypt(String srcFile, String destFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fic = null;
        FileChannel foc = null;
        try {
            fis = new FileInputStream(srcFile);
            fic = fis.getChannel();
            fos = new FileOutputStream(destFile);
            foc = fos.getChannel();

            final int bufSize = Math.max(Math.min((int) fic.size(), BUF_SIZE), magic.length);
            Log.d(TAG, "bufSize:" + bufSize);
            ByteBuffer buf = ByteBuffer.allocate(bufSize);

            boolean hasReadHead = false;
            while (fic.read(buf) != -1 || buf.position() > 0) {
                buf.flip();
                if (!hasReadHead) {
                    byte[] sign = new byte[magic.length];
                    buf.get(sign);
                    if (!equals(sign, getMixedMagic(2))) {
                        throw new IllegalAccessException("Not encrypted.");
                    }

                    int count = buf.get();
                    byte[] decrypt = new byte[count];
                    buf.get(decrypt);
                    int index = 0;
                    final byte[] _magic = getMixedMagic(1);
                    for (int i = 0; i < decrypt.length; i++) {
                        decrypt[i] ^= _magic[index];
                        index %= ++index;
                    }
                    foc.write(ByteBuffer.wrap(decrypt));

                    hasReadHead = true;
                }
                foc.write(buf);
                buf.compact();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                // close quietly.
            }
        }
        return false;
    }

    public static boolean encrypt(String srcFile, String destFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fic = null;
        FileChannel foc = null;
        try {
            fis = new FileInputStream(srcFile);
            fic = fis.getChannel();
            fos = new FileOutputStream(destFile);
            foc = fos.getChannel();

            long ficSize = fic.size();
            long ficPosition = fic.position();
            int count = (int) Math.min(MAX_ENCRYPT_SIZE, ficSize - ficPosition);

            final int bufSize = Math.max(Math.min((int) ficSize, BUF_SIZE), magic.length);
            Log.d(TAG, "bufSize:" + bufSize);
            ByteBuffer buf = ByteBuffer.allocate(bufSize);

            buf.put(getMixedMagic(2));
            buf.flip();
            foc.write(buf);
            buf.compact();

            buf.put((byte) (count & 0xff));
            buf.flip();
            foc.write(buf);
            buf.compact();

            boolean hasEncrypted = false;
            while (fic.read(buf) != -1 || buf.position() > 0) {
                buf.flip();
                if (!hasEncrypted) {
                    byte[] encrypt = new byte[count];
                    buf.get(encrypt);
                    int index = 0;
                    final byte[] _magic = getMixedMagic(1);
                    for (int i = 0; i < encrypt.length; i++) {
                        encrypt[i] ^= _magic[index];
                        index %= ++index;
                    }
                    foc.write(ByteBuffer.wrap(encrypt));

                    hasEncrypted = true;
                }
                foc.write(buf);
                buf.compact();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (fic != null) {
                    fic.close();
                }
                if (foc != null) {
                    foc.close();
                }
            } catch (Exception e) {
                // close quietly.
            }
        }
        return false;
    }

    private static boolean equals(byte[] src, byte[] target) {
        if (src != null && target != null && src.length == target.length) {
            for (int i = 0; i < src.length; i++) {
                if (src[i] != target[i])
                    return false;
            }
            return true;
        }
        return false;
    }

    public static byte[] getFileSHA1(String file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            FileInputStream fis = new FileInputStream(file);
            int read = 0;
            byte[] buf = new byte[1024];
            while ((read = fis.read(buf)) != -1) {
                digest.update(buf, 0, read);
            }
            fis.close();
            return digest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFileSHA1String(String file) {
        byte[] bytes = getFileSHA1(file);
        if (bytes != null) {
            return bytesToHexString(bytes);
        }
        return null;
    }

    private static byte[] getMixedMagic(int gap) {
        final int len = magic.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) ((magic[i] ^ magic[(i + gap) % len]) & 0xff);
        }
        return bytes;
    }

    public static boolean isEncrypted(String file) {
        FileInputStream fis = null;
        FileChannel fic = null;
        try {
            fis = new FileInputStream(file);
            fic = fis.getChannel();
            ByteBuffer isEncrypt = ByteBuffer.allocate(magic.length);
            fic.read(isEncrypt);
            if (equals(isEncrypt.array(), getMixedMagic(2))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fic != null) {
                    fic.close();
                }
            } catch (Exception e) {
                // close quietly.
            }
        }
        return false;
    }
}
