
package myframelib.zkzhou.com.common.io;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class FileUtil {

    /**
     * 创建文件的模式，已经存在的文件要覆盖
     */
    public final static int MODE_COVER = 1;

    /**
     * 创建文件的模式，文件已经存在则不做其它事
     */
    public final static int MODE_UNCOVER = 0;

    private static final String TAG_KGMP3HASH = "kgmp3hash";

    public static final int TAG_KGMP3HASH_LENGTH = TAG_KGMP3HASH.length() + 32;

    private static void createParentIfNecessary(String path) {
        File file = new File(path);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
    }

    /**
     * 如果file不存在那么创建新的文件
     *
     * @param filePath
     * @return
     */
    public static File createFileIfFileNoExits(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    /**
     * 向文件的末尾添加数据
     *
     * @param path
     * @param data
     */
    public static boolean appendData(String path, byte[] data) {
        try {
            File file = new File(path);
            if (file.exists()) {
                createParentIfNecessary(path);
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(data);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 在文件末尾追加字符串，如果文件不存在，那么重新创建
     *
     * @param filePath
     * @param str
     * @return
     */
    public static boolean appendString(String filePath, String str) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = createFileIfFileNoExits(filePath);
        if (file != null) {
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(file, true);
                bw = new BufferedWriter(fw);
                bw.write(str);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 向文件末尾添加数据
     *
     * @param path
     * @param is
     */
    public static void appendData(String path, InputStream is) {
        try {
            File file = new File(path);
            if (file.exists()) {
                createParentIfNecessary(path);
                FileOutputStream fos = new FileOutputStream(file, true);
                byte[] data = new byte[1024];
                int receive = 0;
                while ((receive = is.read(data)) != -1) {
                    fos.write(data, 0, receive);
                    fos.flush();
                }
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 复制文件，sourcePath，targetPath必须是文件路径，否则没效
     *
     * @param sourcePath 原文件路径
     * @param targetPath 目标文件路径
     * @throws IOException
     */
    public static void copyFile(String sourcePath, String targetPath) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            File sourceFile = new File(sourcePath);
            File targetFile = new File(targetPath);
            if (!sourceFile.exists() || sourceFile.isDirectory())
                return;
            if (sourceFile.isDirectory())
                return;
            if (!targetFile.exists()) {
                createParentIfNecessary(targetPath);
                targetFile.createNewFile();
            }
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件目录
     *
     * @param filePath
     * @return
     */
    public static boolean createDirectory(String filePath) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return file.mkdirs();
    }

    /**
     * 创建一个空的文件(创建文件的模式，已经存在的是否要覆盖)
     *
     * @param path
     * @param mode
     */
    public static boolean createFile(String path, int mode) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                if (mode == FileUtil.MODE_COVER) {
                    FileUtil.deleteFile(file);
                    file.createNewFile();
                }
            } else {
                // 如果路径不存在，先创建路径
                File mFile = file.getParentFile();
                if (!mFile.exists()) {
                    mFile.mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 创建一个空的文件夹(创建文件夹的模式，已经存在的是否要覆盖)
     *
     * @param path
     * @param mode
     */
    public static void createFolder(String path, int mode) {
        try {
            // LogUtil.debug(path);
            File file = new File(path);
            if (file.exists()) {
                if (mode == FileUtil.MODE_COVER) {
                    FileUtil.deleteFile(file);
                    file.mkdirs();
                }
            } else {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除目录下文件，除了exceptFilePath文件
     *
     * @param dirPath
     * @param exceptFilePath 不删除的文件
     * @return
     */
    public static boolean deleteChilds(String dirPath, String exceptFilePath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }

        File dirFile = new File(dirPath);

        if (dirFile == null || !dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }

        File[] list = dirFile.listFiles();

        for (File file : list) {
            if (file.getAbsolutePath().equals(exceptFilePath)) {
                continue;
            }

            if (file.isDirectory()) {
                deleteDirectory(file.getAbsolutePath());
            } else {
                FileUtil.deleteFile(file);
            }
        }

        return true;
    }

    /**
     * 删除目录下所有文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteChilds(String filePath) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);

        if (file == null || !file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] list = file.listFiles();

            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
                    deleteDirectory(list[i].getAbsolutePath());
                } else {
                    FileUtil.deleteFile(list[i]);
                }
            }
        }

        return true;
    }

    /**
     * 删除目录 及其 子目录
     *
     * @param filePath
     * @return
     */
    public static boolean deleteDirectory(String filePath) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);

        if (file == null || !file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] list = file.listFiles();

            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
                    deleteDirectory(list[i].getAbsolutePath());
                } else {
                    FileUtil.deleteFile(list[i]);
                }
            }
        }
        FileUtil.deleteFile(file);
        return true;
    }

    /**
     * 删除文件或文件夹(包括目录下的文件)
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            File f = new File(filePath);
            if (f.exists()) {
                if (f.isDirectory()) {
                    File[] delFiles = f.listFiles();
                    if (delFiles != null) {
                        for (int i = 0; i < delFiles.length; i++) {
                            deleteFile(delFiles[i].getAbsolutePath());
                        }
                    }
                }
                FileUtil.deleteFile(f);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @param deleteParent 是否删除父目录
     * @deprecated
     */
    @Deprecated
    public static void deleteFile(String filePath, boolean deleteParent) {
        if (filePath == null) {
            return;
        }
        try {
            File f = new File(filePath);
            if (f.exists() && f.isDirectory()) {
                File[] delFiles = f.listFiles();
                if (delFiles != null) {
                    for (int i = 0; i < delFiles.length; i++) {
                        deleteFile(delFiles[i].getAbsolutePath(), deleteParent);
                    }
                }
            }
            if (deleteParent) {
                FileUtil.deleteFile(f);
            } else if (f.isFile()) {
                FileUtil.deleteFile(f);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean fileIsExist(String filePath) {
        if (filePath == null || filePath.length() < 1) {
            return false;
        }

        File f = new File(filePath);
        if (!f.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 调用File.delete()，不做任何其他判断
     * 目的只是统一删除文件的入口
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file != null) {
            return file.delete();
        }
        return false;
    }


    /**
     * 获取文件夹大小
     *
     * @param path
     * @return
     */
    public static long getAllSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        File file = new File(path);
        if (!file.exists())
            return 0;

        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getAllSize(child.getPath());
        return total;
    }

    /**
     * @param path hash.ext.kgtmp
     * @return
     */
    public static String getAudioMimeType(String path) {
        boolean isM4A = path.toLowerCase().endsWith(".m4a");
        return isM4A ? "audio/mp4" : "audio/mpeg";
    }


    /**
     * 获取指定路径的文件，若是用于jpgPng文件，允许后缀未指定。
     *
     * @param path 文件路径，允许png jpg后缀未指定
     * @return 如果文件存在，真实的文件。如果不存在 返回null.
     */
    public static File getExistJpgPngFile(String path) {
        File resultFile = null;

        File file = new File(path);
        if (file.exists()) {
            resultFile = file;
        } else {
            String jpgPath = path.trim() + ".jpg";
            File jpgFile = new File(jpgPath);
            if (jpgFile.exists()) {
                resultFile = jpgFile;
            } else {
                String pngPath = path.trim() + ".png";
                File pngFile = new File(pngPath);
                if (pngFile.exists()) {
                    resultFile = pngFile;
                }
            }
        }

        return resultFile;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getExtName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return fileName.substring(index + 1, fileName.length());
        }
    }

    /**
     * 获取文件的数据
     *
     * @param path
     * @return
     */
    public static byte[] getFileData(String path) {
        byte[] data = null;// 返回的数据
        try {
            File file = new File(path);
            if (file.exists()) {
                data = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(data);
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 将文件内容以字符串形式读出来
     *
     * @param filePath
     * @param charsetName The name of a supported
     *                    {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static String getFileData(String filePath, String charsetName) {
        File file = new File(filePath);
        String fileContent = "";
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            createParentIfNecessary(filePath);
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.equals("")) {
                    fileContent += "\r\n";
                }
                fileContent += line;
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 获取文件的输入流
     *
     * @param path
     * @return
     */
    public static FileInputStream getFileInputStream(String path) {
        FileInputStream fis = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                fis = new FileInputStream(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fis;
    }

    /**
     * 获取文件最后修改时间
     *
     * @param filePath
     * @return
     */
    public static long getFileModifyTime(String filePath) {
        if (null == filePath) {
            return 0;
        }

        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return 0;
        }

        return file.lastModified();
    }

    /**
     * 解析文件全名,带扩展名
     *
     * @param filePath 文件路径
     * @return 文件全名
     */
    public static String getFileNameWithExt(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        int last = filePath.lastIndexOf("/");
        int end = filePath.length() - 1;
        if (last == -1) {
            return filePath;
        } else if (last < end) {
            return filePath.substring(last + 1);
        } else {
            return filePath.substring(last);
        }
    }

    /**
     * 获取文件名，不带扩展名
     *
     * @param filePath
     * @return
     */
    public static String getFileNameWithoutExt(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        int last = filePath.lastIndexOf("/");
        int index = filePath.lastIndexOf(".");
        if (last == -1 && index == -1) {
            return filePath;
        } else if (index > last) {
            return filePath.substring(last + 1, index);
        } else {
            return filePath.substring(last);
        }
    }

    /**
     * 获取文件的输出流
     *
     * @param path
     * @return
     */
    public static OutputStream getFileOutputStream(String path) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                fos = new FileOutputStream(file);
            }
        } catch (Exception e) {
            return null;
        }
        return fos;
    }

    /**
     * 文件大小
     *
     * @param path
     * @return
     */
    public static int getFileSize(String path) {
        if (TextUtils.isEmpty(path))
            return -1;
        return (int) new File(path).length();
    }

    // 递归
    public static long getFileSizes(File f) {
        long size = 0;
        try {
            File flist[] = f.listFiles();
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static long getmem_UNUSED(Context mContext) {
        long MEM_UNUSED;
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建ActivityManager.MemoryInfo对象

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

        // 取得剩余的内存空间

        MEM_UNUSED = mi.availMem / 1024;
        return MEM_UNUSED;
    }

    /**
     * 获取父目录
     *
     * @param filePath
     * @return
     */
    public static String getParentPath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        int last = filePath.lastIndexOf("/");
        if (last == -1) {
            return null;
        }
        return filePath.substring(0, last + 1);
    }

    /**
     * 将文件内容以字符串形式读出来
     *
     * @param charsetName The name of a supported
     *                    {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static String getRawFileData(Context context, int resId, String charsetName) {

        String fileContent = "";
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(context.getResources().openRawResource(
                    resId), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent += "\r\n";
                }
                fileContent += line;
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 获取文件大小
     *
     * @param path
     * @return
     */
    public static long getSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        long size = 0;
        try {
            File file = new File(path);
            if (file.exists()) {
                size = file.length();
            }
        } catch (Exception e) {
            return 0;
        }
        return size;
    }


    /**
     * 某个文件夹下是否文件
     */
    public static boolean hasfile(String filepath) {
        boolean returnValue = false;
        File file = new File(filepath);
        if (!file.exists()) {
            returnValue = false;
        } else if (!file.isDirectory()) {
            if (file.length() > 0) {
                returnValue = true;
                return returnValue;
            }
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "/" + filelist[i]);
                if (!readfile.isDirectory()) {
                    if (readfile.length() > 0) {
                        returnValue = true;
                        return returnValue;
                    }
                } else {
                    if (hasfile(filepath + "/" + filelist[i])) {
                        returnValue = true;
                        return true;
                    }
                }
            }
        }
        return returnValue;
    }

    public static byte[] InputStreamToByte(InputStream iStrm) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = iStrm.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    public static byte[] InputStreamToByteNew(InputStream iStrm) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        byte[] buf = new byte[1024];
        while ((ch = iStrm.read(buf)) != -1) {
            bytestream.write(buf, 0, ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    /**
     * 判断是不是文件夹
     *
     * @param path
     * @return true 是文件夹
     */
    public static boolean isDirectory(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        boolean isDirectory = false;
        try {
            File file = new File(path);
            isDirectory = file.isDirectory();
        } catch (Exception e) {
            return false;
        }
        return isDirectory;
    }

    /**
     * 是否是下载出错文件（下到错误页面的数据）
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean isErrorFile(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        try {
            createParentIfNecessary(filePath);
            FileInputStream stream = new FileInputStream(new File(filePath));
            byte[] buffer = new byte[16];
            if (stream.read(buffer) == 16) {
                stream.close();
                return ((buffer[0] & 0xFF) == 0xFF && (buffer[1] & 0xFF) == 0xD8
                        && (buffer[2] & 0xFF) == 0xFF && (buffer[3] & 0xFF) == 0xE0
                        && (buffer[4] & 0xFF) == 0x00 && (buffer[5] & 0xFF) == 0x10
                        && (buffer[6] & 0xFF) == 0x4A && (buffer[7] & 0xFF) == 0x46
                        && (buffer[8] & 0xFF) == 0x49 && (buffer[9] & 0xFF) == 0x46
                        && (buffer[10] & 0xFF) == 0x00 && (buffer[11] & 0xFF) == 0x01
                        && (buffer[12] & 0xFF) == 0x02 && (buffer[13] & 0xFF) == 0x01
                        && (buffer[14] & 0xFF) == 0x00 && (buffer[15] & 0xFF) == 0x48);
            } else {
                stream.close();
                return false;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 判断文件或文件夹是否存在
     *
     * @param path
     * @return true 文件存在
     */
    public static boolean isExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        boolean exist = false;
        try {
            File file = new File(path);
            exist = file.exists();
            file.isDirectory();
        } catch (Exception e) {
            return false;
        }
        return exist;
    }

    /**
     * 判断是不是文件
     *
     * @param path
     * @return true 是文件
     */
    public static boolean isFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        boolean isFile = false;
        try {
            File file = new File(path);
            isFile = file.isFile();
        } catch (Exception e) {
            return false;
        }
        return isFile;
    }

    /**
     * 是否是m4a文件
     *
     * @param m4a m4a文件路径
     * @return
     */
    public static boolean isM4A(final String m4a) {
        if (TextUtils.isEmpty(m4a)) {
            return false;
        }
        try {
            createParentIfNecessary(m4a);
            FileInputStream stream = new FileInputStream(new File(m4a));
            byte[] buffer = new byte[8];
            if (stream.read(buffer) == 8) {
                stream.close();
                return (buffer[4] == 'f' && buffer[5] == 't' && buffer[6] == 'y' && buffer[7] == 'p');
            } else {
                stream.close();
                return false;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 列出目录文件
     *
     * @return
     */
    public static File[] listFiles(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists() && file.isDirectory()) {
                return file.listFiles();
            }
        }
        return null;
    }

    /**
     * 列出目录文件
     *
     * @return
     */
    public static File[] listFiles(String filePath, FileFilter filter) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists() && file.isDirectory()) {
                return file.listFiles(filter);
            }
        }
        return null;
    }

    /**
     * 列出目录文件
     *
     * @return
     */
    public static File[] listFiles(String filePath, FilenameFilter filter) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists() && file.isDirectory()) {
                return file.listFiles(filter);
            }
        }
        return null;
    }

    /**
     * 从assset资源中读取Properties
     *
     * @param context
     * @param assetPath
     * @return
     */
    public static Properties loadPropertiesFromAsset(Context context, String assetPath) {
        Properties result = null;
        if (context != null && !TextUtils.isEmpty(assetPath)) {
            InputStream ins = null;
            try {
                ins = context.getAssets().open(assetPath);
                result = new Properties();
                result.load(ins);
            } catch (IOException e) {
                result = null;
                e.printStackTrace();
            } finally {
                try {
                    if (ins != null) {
                        ins.close();
                    }
                } catch (Exception e2) {
                }
            }
        }
        return result;
    }

    /**
     * 从raw资源中读取Properties
     *
     * @param context
     * @param rawId
     * @return
     */
    public static Properties loadPropertiesFromRaw(Context context, int rawId) {
        Properties result = null;
        if (context != null) {
            InputStream ins = null;
            try {
                ins = context.getResources().openRawResource(rawId);
                result = new Properties();
                result.load(ins);
            } catch (IOException e) {
                result = null;
                e.printStackTrace();
            } finally {
                try {
                    if (ins != null) {
                        ins.close();
                    }
                } catch (Exception e2) {
                }
            }
        }
        return result;
    }

    /**
     * 移动文件
     *
     * @param oldFilePath 旧路径
     * @param newFilePath 新路径
     * @return
     */
    public static boolean moveFile(String oldFilePath, String newFilePath) {
        if (TextUtils.isEmpty(oldFilePath) || TextUtils.isEmpty(newFilePath)) {
            return false;
        }
        File oldFile = new File(oldFilePath);
        if (oldFile.isDirectory() || !oldFile.exists()) {
            return false;
        }
        try {
            File newFile = new File(newFilePath);
            if (!newFile.exists()) {
                createParentIfNecessary(newFilePath);
                newFile.createNewFile();
            }
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(oldFile));
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buf = new byte[1024];
            int read;
            while ((read = bis.read(buf)) != -1) {
                fos.write(buf, 0, read);
            }
            fos.flush();
            fos.close();
            bis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @return 输入流
     */
    public static InputStream readFile(String filePath) {
        if (null == filePath) {
            return null;
        }

        InputStream is = null;

        try {
            if (fileIsExist(filePath)) {
                File f = new File(filePath);
                is = new FileInputStream(f);
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
        return is;
    }

    /**
     * 读取文件转为字符串
     *
     * @param filePath
     * @return
     */
    public static String readFileToString(String filePath) {
        String str = "";
        try {
            File readFile = new File(filePath);
            if (!readFile.exists()) {
                return null;
            }
            FileInputStream inStream = new FileInputStream(readFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            str = stream.toString();
            stream.close();
            inStream.close();
            return str;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取输入流 转化为 byte[]
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static byte[] readIn(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        byte[] buf = new byte[1024];
        int c = is.read(buf);
        while (-1 != c) {
            baos.write(buf, 0, c);
            c = is.read(buf);
        }
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }

    /**
     * 从m4a读取mp3哈希值
     *
     * @param m4a m4a文件路径
     * @return
     */
    public static String readMp3HashFromM4a(final String m4a) {
        if (TextUtils.isEmpty(m4a)) {
            return null;
        }
        File m4afile = new File(m4a);
        RandomAccessFile accessFile = null;
        try {
            createParentIfNecessary(m4a);
            accessFile = new RandomAccessFile(m4afile, "r");
            accessFile.skipBytes((int) (m4afile.length() - TAG_KGMP3HASH_LENGTH));
            byte[] b = new byte[TAG_KGMP3HASH_LENGTH];
            if (accessFile.read(b) == TAG_KGMP3HASH_LENGTH) {
                String taghash = new String(b);
                if (!TextUtils.isEmpty(taghash) && taghash.startsWith(TAG_KGMP3HASH)) {
                    return taghash.substring(TAG_KGMP3HASH.length());
                }
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            if (accessFile != null) {
                try {
                    accessFile.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * 重命名文件/文件夹
     *
     * @param path
     * @param newName
     */
    public static boolean rename(final String path, final String newName) {
        boolean result = false;
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName)) {
            return result;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                result = file.renameTo(new File(newName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 重写文件的数据
     *
     * @param path
     * @param data
     */
    public static void rewriteData(String path, byte[] data) {
        try {
            File file = new File(path);
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file, false);
                fos.write(data);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重写文件的数据
     *
     * @param path
     * @param is
     */
    public static void rewriteData(String path, InputStream is) {
        try {
            File file = new File(path);
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file, false);
                byte[] data = new byte[1024];
                int receive = 0;
                while ((receive = is.read(data)) != -1) {
                    fos.write(data, 0, receive);
                    fos.flush();
                }
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 保存文件
     *
     * @param toSaveString
     * @param filePath
     */
    public static void saveFile(String toSaveString, String filePath) {
        try {
            File saveFile = new File(filePath);
            if (!saveFile.exists()) {
                File dir = new File(saveFile.getParent());
                dir.mkdirs();
                saveFile.createNewFile();
            }

            FileOutputStream outStream = new FileOutputStream(saveFile);
            outStream.write(toSaveString.getBytes());
            outStream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /**
     * 设置文件最后修改时间
     *
     * @param filePath
     * @param modifyTime
     * @return
     */
    public static boolean setFileModifyTime(String filePath, long modifyTime) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return false;
        }

        return file.setLastModified(modifyTime);
    }

    public static ArrayList<byte[]> split(String filePath, int startPos, int blockSize) {
        if (TextUtils.isEmpty(filePath) || blockSize <= 0) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }

        long fileLen = file.length();
        if (startPos >= fileLen) {
            return null;
        }

        int realLen = (int) (fileLen - startPos);
        if (realLen < blockSize) {
            blockSize = realLen;
        }

        int blocks = realLen / blockSize;
        int lastBlockSize = realLen % blockSize;
        ArrayList<byte[]> resultList = new ArrayList<byte[]>();

        RandomAccessFile randomFile = null;
        try {
            byte[] buffer = null;
            int pos = startPos;

            randomFile = new RandomAccessFile(file, "r");
            for (int i = 0; i < blocks; i++) {
                buffer = new byte[blockSize];
                randomFile.seek(pos);
                randomFile.read(buffer);
                pos += blockSize;
                resultList.add(buffer);
            }
            if (lastBlockSize > 0 && pos < (fileLen - 1)) {
                buffer = new byte[lastBlockSize];
                randomFile.seek(pos);
                randomFile.read(buffer);
                resultList.add(buffer);
            }
            return resultList;
        } catch (Exception e) {
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static final byte[] readFileData(String filePath, int offset, int size) {
        if (TextUtils.isEmpty(filePath) || size <= 0) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        long fileLen = file.length();
        if (offset >= fileLen) {
            return null;
        }

        RandomAccessFile randomFile = null;
        try {
            byte[] buffer = new byte[size];
            randomFile = new RandomAccessFile(file, "r");
            randomFile.seek(offset);
            int read = randomFile.read(buffer);
            if (read > 0) {
                return buffer;
            }
        } catch (Exception e) {
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * 解压路径为zipFile的.zip文件到路径folderPath下， 如果有同名folderPath的文件，则删除该文件，新建为文件夹
     * 解压过程使用buffer大小为5k
     *
     * @param zipFilePath
     * @param folderPath
     * @return
     * @throws ZipException
     * @throws IOException
     */
    public static boolean unZipFile(String zipFilePath, String folderPath) {
        // new ZipInputStream(null);
        if (!folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }
        ZipFile zfile = null;
        OutputStream os = null;
        ZipInputStream is = null;
        try {
            File folder = new File(folderPath);
            if (folder.exists() && !folder.isDirectory()) {
                FileUtil.deleteFile(folder);
            }
            if (!folder.exists()) {
                folder.mkdirs();
            }

            is = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry zipEntry;
            while ((zipEntry = is.getNextEntry()) != null) {
                String subfilename = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    File subDire = new File(folderPath + subfilename);
                    if (subDire.exists() && subDire.isDirectory()) {
                        continue;
                    } else if (subDire.exists() && subDire.isFile()) {
                        FileUtil.deleteFile(subDire);
                    }
                    subDire.mkdirs();
                } else {
                    File subFile = new File(folderPath + subfilename);
                    if (subFile.exists()) {
                        continue;
                    }
                    subFile.createNewFile();
                    os = new FileOutputStream(subFile);
                    int len;
                    byte[] buffer = new byte[5120];
                    while ((len = is.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                        os.flush();
                    }
                }
            }

        } catch (Exception e) {
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (zfile != null) {
                try {
                    zfile.close();
                } catch (IOException e) {
                }
            }

        }
        return true;
    }

    /**
     * 写入新文件
     */
    public static void writeData(String path, byte[] data) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                createParentIfNecessary(path);
                FileOutputStream fos = new FileOutputStream(file, false);
                fos.write(data);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文本内容
     *
     * @param path    保存路径
     * @param content 需要保存的文本内容
     */
    public static void writeData(String path, String content) {
        writeData(path, content.getBytes());
    }

    /**
     * 将byte[]写入文件
     *
     * @param filePath 格式如： /sdcard/abc/a.obj
     * @param content  写入内容byte[]
     * @return
     * @attention 当文件存在将被替换 当其所在目录不存在，将尝试创建
     */
    public static boolean writeFile(String filePath, byte[] content) {
        if (null == filePath || null == content) {
            return false;
        }

        FileOutputStream fos = null;
        try {
            String pth = filePath.substring(0, filePath.lastIndexOf("/"));
            File pf = null;
            pf = new File(pth);
            if (pf.exists() && !pf.isDirectory()) {
                FileUtil.deleteFile(pf);
            }
            pf = new File(filePath);
            if (pf.exists()) {
                if (pf.isDirectory())
                    deleteDirectory(filePath);
                else
                    FileUtil.deleteFile(pf);
            }

            pf = new File(pth + File.separator);
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                    Log.e("FileUtil", "Can't make dirs, path=" + pth);
                }
            }

            fos = new FileOutputStream(filePath);
            fos.write(content);
            fos.flush();
            fos.close();
            fos = null;
            pf.setLastModified(System.currentTimeMillis());

            return true;

        } catch (Exception ex) {
            Log.e("FileUtil", "Exception, ex: " + ex.toString());
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
        return false;
    }


    private FileUtil() {

    }

    public static byte[] getFileData(String path, int startPos) {
        byte[] data = null;// 返回的数据
        try {
            File file = new File(path);
            if (file.exists()) {
                data = new byte[(int) file.length() - startPos];
                FileInputStream fis = new FileInputStream(file);
                fis.skip(startPos);
                fis.read(data);
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] getFileData(String path, int startPos, int lengthAtMost) {
        byte[] data = null;// 返回的数据
        try {
            File file = new File(path);
            if (file.exists()) {
                int mostLength = (int) Math
                        .max(0, Math.min(lengthAtMost, file.length() - startPos));
                data = new byte[mostLength];
                FileInputStream fis = new FileInputStream(file);
                fis.skip(startPos);
                fis.read(data);
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String replaceIllegalChar(String fileName) {

        Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");
        return fileName == null ? "_" : FilePattern.matcher(fileName).replaceAll("_");
    }

    /**
     * 获取sdka的根目录，这种方式比较全面
     *
     * @return
     */
    public static String getRootPath() {
        String rootPath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            rootPath = Environment.getExternalStorageDirectory()// /mnt/sdcard0
                    .getAbsolutePath() + File.separator;
        } else if ((new File("/mnt/sdcard2")).exists()) {// 以下为一些手机常见的SD卡路径，无法用以上方法获取
            rootPath = "/mnt/sdcard2" + File.separator;
        } else if ((new File("/mnt/sdcard-ext")).exists()) {
            rootPath = "/mnt/sdcard-ext" + File.separator;
        } else if ((new File("/mnt/ext_sdcard")).exists()) {
            rootPath = "/mnt/ext_sdcard" + File.separator;
        } else if ((new File("/mnt/sdcard/SD_CARD")).exists()) {
            rootPath = "/mnt/sdcard/SD_CARD" + File.separator;
        } else if ((new File("/mnt/sdcard/extra_sd")).exists()) {
            rootPath = "/mnt/sdcard/extra_sd" + File.separator;
        } else if ((new File("/mnt/extrasd_bind")).exists()) {
            rootPath = "/mnt/extrasd_bind" + File.separator;
        } else if ((new File("/mnt/sdcard/ext_sd")).exists()) {
            rootPath = "/mnt/sdcard/ext_sd" + File.separator;
        } else if ((new File("/mnt/sdcard/external_SD")).exists()) {
            rootPath = "/mnt/sdcard/external_SD" + File.separator;
        } else if ((new File("/storage/sdcard1")).exists()) {
            rootPath = "/storage/sdcard1" + File.separator;
        } else if ((new File("/storage/extSdCard")).exists()) {
            rootPath = "/storage/extSdCard" + File.separator;
        }
        return rootPath;
    }
}
