
package myframelib.zkzhou.com.common.app;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述: SDCard工具类
 */
public class SDCardUtil {

    /**
     * sdcard
     */
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().toString();

    /**
     * 格式化 转化为.MB格式
     *
     * @param context
     * @param size
     * @return
     */
    public static String formatSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * SDCard剩余大小
     *
     * @return 字节
     */
    public static long getAvailableSize() {
        if (hasSDCard()) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                return availableBlocks * blockSize;
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * SDCard总容量大小
     *
     * @return 字节
     */
    public static long getTotalSize() {
        if (hasSDCard()) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long totalBlocks = stat.getBlockCount();
                return totalBlocks * blockSize;

            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 是否有足够的空间
     *
     * @return
     */
    public static boolean hasEnoughSpace(int MB) {
        return getAvailableSize() < 1024 * 1024 * MB;
    }

    /**
     * 判断是否存在SDCard
     *
     * @return
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    /**
     * 检测储存卡是否可用
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static List<String> getAllExterSdcardPath() {
        List<String> SdList = new ArrayList<String>();

        String firstPath = getFirstExterPath();

        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                // 将常见的linux分区过滤掉
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("media"))
                    continue;
                if (line.contains("system") || line.contains("cache") || line.contains("sys")
                        || line.contains("data") || line.contains("tmpfs")
                        || line.contains("shell") || line.contains("root") || line.contains("acct")
                        || line.contains("proc") || line.contains("misc") || line.contains("obb")) {
                    continue;
                }

                if (line.contains("fat") || line.contains("fuse") || (line.contains("ntfs"))) {

                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        String path = columns[1];
                        if (path != null && !SdList.contains(path) && path.contains("sd"))
                            SdList.add(columns[1]);
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!SdList.contains(firstPath)) {
            SdList.add(firstPath);
        }

        return SdList;
    }

    public static String getFirstExterPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 解析文件所在的文件夹
     *
     * @param filePath 文件路径
     * @return 文件所在的文件夹路径
     */
    public static String getFileFolderPath(String filePath) {
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
     * 解析文件全名,不带扩展名
     *
     * @param filePath 文件路径
     * @return 文件全名
     */
    public static String getFileName(String filePath) {
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
     * 获取文件类型
     *
     * @param file 需要从中截取文件类型的路径
     * @return 截取到的文件类型，没有则返回""
     */
    public static String getFileType(String file) {
        if (file == null)
            return "";
        int index1 = file.lastIndexOf('/');
        int index2 = file.lastIndexOf('.');
        if (index2 < index1)
            index2 = -1;
        if (index2 == -1)
            return "";
        return file.substring(index2 + 1);
    }

    /**
     * 重名
     *
     * @param path
     * @param newname
     * @return
     */
    public static String rename(String path, String newname) {
        String fileType = getFileType(path);
        String newPath = getFatherPath(path) + newname + "." + fileType;
        return newPath;
    }

    /**
     * 获取上一层路径
     *
     * @param path
     * @return
     */
    public static String getFatherPath(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        // 判断是否为目录
        if (path.charAt(path.length() - 1) != '/') {
            return path.substring(0, path.lastIndexOf('/') + 1);
        } else {
            return path.substring(0, path.lastIndexOf('/', path.lastIndexOf('/') - 1) + 1);
        }
    }

    /**
     * 把文件分割为文件名和后缀名
     *
     * @param fileFullName 文件的全称
     * @return
     */
    public static String[] getFileNameAndExtName(String fileFullName) {
        if (fileFullName == null) {
            return null;
        }
        int indexDot = fileFullName.lastIndexOf(".");
        if (indexDot > 0) {
            String[] result = new String[2];
            String displayName = fileFullName.substring(0, indexDot);
            result[0] = displayName;
            result[1] = fileFullName.substring(indexDot + 1, fileFullName.length());
            return result;
        }
        return null;
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
        if (last == -1) {
            return filePath;
        } else {
            return filePath.substring(last + 1);
        }
    }

    /**
     * 返回文件大小表示
     *
     * @param context
     * @param bytes   字节数
     * @return
     */
    public static String getSizeText(Context context, long bytes) {
        String sizeText = "";
        if (bytes < 0) {
            return sizeText;
        } else {
            sizeText = Formatter.formatFileSize(context, bytes);
        }
        return sizeText;
    }

    /**
     * 获取文件扩展名 有.
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
            return fileName.substring(index, fileName.length());
        }
    }

    /**
     * 格式化文件路径（去除一些特殊字符）
     *
     * @param filePath
     * @return
     */
    public static String formatFilePath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return filePath.replace("\\", "").replace("/", "").replace("*", "").replace("?", "")
                .replace(":", "").replace("\"", "").replace("<", "").replace(">", "")
                .replace("|", "");
    }
}
