
package myframelib.zkzhou.com.common.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 描述： 文件解压
 */
public class UnZip {

    public static interface UnZipProgressListener {

        void unZipError(Exception e);

        /**
         * 解压完成
         */
        void unZipFinish();

        void unZipProgress(int progress);

        void unZipStart();

        /**
         * 中断解压
         */
        void unZipStop();
    }

    /**
     * 是否中断解压
     */
    private boolean isStopUnZip = false;

    private void setFileWrite(File subFile) {

        try {

            String command = "chmod 660 " + subFile.getAbsolutePath();

            Runtime runtime = Runtime.getRuntime();

            runtime.exec(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStopUnZip(boolean isStopUnZip) {
        this.isStopUnZip = isStopUnZip;
    }

    /**
     * 解压路径为zipFile的.zip文件到路径folderPath下， 如果有同名folderPath的文件，则删除该文件，新建为文件夹;
     * 如果解压的文件已经存在，则删除文件，重新解压该文件
     *
     * @param inputStream
     * @param folderPath
     * @param unZipProgressListener
     * @return
     */
    public boolean unZipFile(InputStream inputStream, String folderPath,
                             UnZipProgressListener unZipProgressListener) {
        if (unZipProgressListener != null) {
            unZipProgressListener.unZipStart();
        }
        int total = 0;
        if (!folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }
        ZipFile zfile = null;
        OutputStream os = null;
        BufferedOutputStream buffOut = null;
        BufferedInputStream buffIn = null;
        ZipInputStream is = null;
        try {
            File folder = new File(folderPath);
            if (folder.exists() && !folder.isDirectory()) {
                FileUtil.deleteFile(folder);
            }
            if (!folder.exists()) {
                folder.mkdirs();
            }
            buffIn = new BufferedInputStream(inputStream);
            is = new ZipInputStream(buffIn);
            ZipEntry zipEntry;
            int unzipTotal = 0;
            int progressTemp = -1;
            unzipTotal = inputStream.available();
            // KGLog.d("111", "unzipTotal=" + unzipTotal);
            byte[] buffer = new byte[1024 * 32];

            // while ((zipEntry = is.getNextEntry()) != null) {
            while (true) {
                if ((zipEntry = is.getNextEntry()) == null) {
                    break;
                }
                if (isStopUnZip) {
                    if (unZipProgressListener != null) {
                        unZipProgressListener.unZipStop();
                    }
                    return false;
                }
                String subfilename = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    String subDireStr = folderPath + subfilename;
                    File subDire = new File(subDireStr);
                    if (subDire.exists() && subDire.isDirectory()) {
                        continue;
                    } else if (subDire.exists() && subDire.isFile()) {
                        FileUtil.deleteFile(subDire);
                    }
                    subDire.mkdirs();
                } else {
                    File subFile = new File(folderPath + subfilename);
                    if (!subFile.getParentFile().exists()) {
                        subFile.getParentFile().mkdirs();
                    }
                    if (!subFile.exists()) {
                        subFile.createNewFile();
                    } else {
                        FileUtil.deleteFile(subFile);
                    }
                    os = new FileOutputStream(subFile, false);
                    buffOut = new BufferedOutputStream(os);
                    int len;
                    while (!isStopUnZip && (len = is.read(buffer)) > 0) {
                        total += len;
                        int progress = (int) (total / (float) unzipTotal * 100);
                        if (progressTemp != progress && unZipProgressListener != null) {
                            unZipProgressListener.unZipProgress(progress);
                            progressTemp = progress;
                        }
                        buffOut.write(buffer, 0, len);
                    }
                    buffOut.flush();
                    if (subfilename.contains("resource.db")) {
                        setFileWrite(subFile);
                    }
                }
            }

        } catch (Exception e) {
            if (unZipProgressListener != null) {
                unZipProgressListener.unZipError(e);
            }
            return false;
        } finally {
            if (unZipProgressListener != null) {
                unZipProgressListener.unZipProgress(100);
            }
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
            if (buffOut != null) {
                try {
                    buffOut.close();
                } catch (IOException e) {
                }
            }

        }
        if (unZipProgressListener != null) {
            unZipProgressListener.unZipFinish();
        }
        return true;
    }

    /**
     * 解压路径为zipFile的.zip文件到路径folderPath下， 如果有同名folderPath的文件，则删除该文件，新建为文件夹;
     * 如果解压的文件已经存在，则删除文件，重新解压该文件
     *
     * @param zipFilePath
     * @param folderPath
     * @param unZipProgressListener
     * @return
     */
    public boolean unZipFile(String zipFilePath, String folderPath,
                             UnZipProgressListener unZipProgressListener) {
        try {
            unZipFile(new FileInputStream(zipFilePath), folderPath, unZipProgressListener);
        } catch (FileNotFoundException e) {
        }
        return false;
    }
}
