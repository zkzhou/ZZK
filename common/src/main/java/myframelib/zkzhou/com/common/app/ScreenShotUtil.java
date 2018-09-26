
package myframelib.zkzhou.com.common.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 进行截屏工具类
 */
public class ScreenShotUtil {
    /**
     * 保存图片到sdcard中
     *
     * @param pBitmap
     */
    public static boolean savePic(Bitmap pBitmap, String strName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strName);
            if (null != fos && pBitmap != null) {
                pBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.flush();
                fos.close();
                return true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 截图
     *
     * @param pActivity
     * @return 截图并且保存sdcard成功返回true，否则返回false
     */
    public static boolean shotBitmap(Activity pActivity, String savePath) {
        Bitmap b = takeScreenShot(pActivity);
        return ScreenShotUtil.savePic(b, savePath);
    }

    /**
     * 进行截取屏幕
     *
     * @param pActivity
     * @return bitmap
     */
    public static Bitmap takeScreenShot(Activity pActivity) {
        Bitmap bitmap = null;
        View view = pActivity.getWindow().getDecorView();
        //清理缓存，否则有时会出现截图保存的还是缓存中的图。
        view.destroyDrawingCache();
        // 设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        // 如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();
        // 返回这个缓存视图
        bitmap = view.getDrawingCache();
        if (bitmap == null) {
            return null;
        }
        // 获取状态栏高度
        Rect frame = new Rect();
        // 测量屏幕宽和高
        view.getWindowVisibleDisplayFrame(frame);
        int stautsHeight = frame.top;
//        KGLog.d("xiaoyulong", "状态栏的高度为:" + stautsHeight);
        int width = pActivity.getWindowManager().getDefaultDisplay().getWidth();
        int height = pActivity.getWindowManager().getDefaultDisplay().getHeight();
        //判断高度（魅族适配）
        if (height > bitmap.getHeight()) {
            height = bitmap.getHeight();
        }
        // 根据坐标点和需要的宽和高创建bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height - stautsHeight);
        return bitmap;
    }

}
