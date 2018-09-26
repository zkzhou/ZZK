
package myframelib.zkzhou.com.common.app;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import myframelib.zkzhou.com.common.io.FileUtil;


public class PhotoSaveUtil {
    /**
     * 获取当前时间
     * 
     * @return
     */
    private static String getDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
                Locale.CHINA);
        String s = simpleDateFormat.format(date);
        date = null;
        simpleDateFormat = null;
        return s;
    }

    public static String getSystemAlbum(Context context) {
    	if (hasSDCard()) {
    		return obtainSystemAlbumPath(context);
    	} else {
    		return "";
    	}
    }

    /**
     * 判断当前是否有sd卡，且可读写
     * 
     * @return 有则返回true，无则返回false
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取系统相册路径, 耗时操作
     * 
     * @param context
     * @return
     */
    public static String obtainSystemAlbumPath(Context context) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaColumns.TITLE, "title");
        values.put(ImageColumns.DESCRIPTION, "description");
        values.put(MediaColumns.MIME_TYPE, "image/jpeg");
        Uri url = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
        // 查询系统相册数据
        Cursor cursor = Images.Media.query(cr, url, new String[] {
            MediaColumns.DATA
        });
        String albumPath = null;
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            albumPath = cursor.getString(column_index);
            try {
                cursor.close();
            } catch (Exception e) {
            }
        }
        cr.delete(url, null, null);
        if (albumPath == null) {
            return null;
        }

        File albumDir = new File(albumPath);
        albumDir = albumDir.getParentFile();
        // 如果系统相册目录不存在,则创建此目录
        if (!albumDir.exists()) {
            albumDir.mkdirs();
        }
        albumPath = albumDir.getAbsolutePath();
        return albumPath;
    }

    public static boolean saveImageToExternalStorage(Context context, Bitmap finalBitmap) {
        boolean flag = false;
        if (hasSDCard()) {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString();// Environment.DIRECTORY_PICTURES
            File myDir = new File(root);// root + "/fanxing"
            myDir.mkdirs();
            // Random generator = new Random();
            // int n = 10000;
            // n = generator.nextInt(n);
            // String fname = "Image-" + n + ".jpg";
            String fname = getDate() + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) {
                FileUtil.deleteFile(file);
            }
            FileOutputStream out = null;

            try {
                out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (flag) {
                // 通知MediaScanner去扫描新添加的文件，以便用户能够立即查看到，若不发通知，需要重启手机才能看到
                MediaScannerConnection.scanFile(context, new String[]{
                        file.toString()
                }, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            }
        }
        return flag;
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static long getBitmapsize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
        	return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
