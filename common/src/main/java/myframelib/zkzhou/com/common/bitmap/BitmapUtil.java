
package myframelib.zkzhou.com.common.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import myframelib.zkzhou.com.common.io.FileUtil;


public final class BitmapUtil {

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
                                                int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h
                / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength,
                                         int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height) {
        Bitmap bitmap = null;
        if (source != null && !source.isRecycled()) {
            try {
                bitmap = Bitmap.createBitmap(source, x, y, width, height);
            } catch (OutOfMemoryError e) {
                System.gc();
                try {
                    bitmap = Bitmap.createBitmap(source, x, y, width, height);
                } catch (OutOfMemoryError e1) {
                }
            } catch (Exception e2) {
            }
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        }
        return bitmap;
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m,
                                      boolean filter) {
        Bitmap bitmap = null;
        if (source != null && !source.isRecycled()) {
            try {
                bitmap = Bitmap.createBitmap(source, 0, 0, width, height, m, true);
            } catch (OutOfMemoryError e) {
                System.gc();
                try {
                    bitmap = Bitmap.createBitmap(source, 0, 0, width, height, m, true);
                } catch (OutOfMemoryError e1) {
                }
            } catch (Exception e2) {
            }
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        }
        return bitmap;
    }

    public static Bitmap createBitmap(int width, int height, Config config) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                bitmap = Bitmap.createBitmap(width, height, config);
            } catch (OutOfMemoryError e1) {
            }
        } catch (Exception e2) {
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        }
        return bitmap;
    }

    public static Bitmap createBitmap(int[] colors, int width, int height, Config config) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(colors, width, height, config);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                bitmap = Bitmap.createBitmap(colors, width, height, config);
            } catch (OutOfMemoryError e1) {
            }
        } catch (Exception e2) {
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        }
        return bitmap;
    }

    public static Bitmap decodeFile(String pathName) {
        Bitmap bitmap = null;
        if (FileUtil.isExist(pathName)) {
            InputStream is = FileUtil.getFileInputStream(pathName);
            bitmap = decodeStream(is);
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        }
        return bitmap;
    }

    public static Bitmap decodeFile(String pathName, int width, int height) {
        Bitmap bitmap = null;
        if (FileUtil.isExist(pathName)) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, opts);
            opts.inSampleSize = computeSampleSize(opts, -1, width * height);
            opts.inJustDecodeBounds = false;
            try {
                bitmap = BitmapFactory.decodeFile(pathName, opts);
            } catch (OutOfMemoryError e) {
                System.gc();
                try {
                    bitmap = BitmapFactory.decodeFile(pathName, opts);
                } catch (OutOfMemoryError e1) {
                }
            } catch (Exception e2) {
            }
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        }
        return bitmap;
    }

    public static Bitmap decodeResource(Resources res, int id) {
        Bitmap bitmap = null;
        InputStream is = res.openRawResource(id);
        bitmap = decodeStream(is);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        }
        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromFile(String resPath, int maxWidth) {
        if (maxWidth <= 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(resPath, options);
        int oldHeight = options.outHeight;
        int oldWidth = options.outWidth;
        int oldMaxWidth = (oldHeight > oldWidth) ? oldHeight : oldWidth;

        if (oldMaxWidth > maxWidth) {
            options.inSampleSize = Math.round((float) oldMaxWidth / (float) maxWidth);
        } else {
            options.inSampleSize = 1;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(resPath, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String resPath, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(resPath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(resPath, options);
    }

    /**
     * mImageView.setImageBitmap(
     * decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));
     * 
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                         int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeStream(InputStream is) {
        Bitmap bitmap = null;
        if (is != null) {
            try {
                bitmap = BitmapFactory.decodeStream(is);
            } catch (OutOfMemoryError e) {
                System.gc();
                try {
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (OutOfMemoryError e1) {
                }
            } catch (Exception e2) {
            }
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        }
        return bitmap;
    }
    
    public static Bitmap imageZoom(Bitmap bitMap, double maxSize) {
		// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = (double)(b.length / 1024);
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			double height = bitMap.getHeight() / Math.sqrt(i);
			double width = bitMap.getWidth() / Math.sqrt(i);
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bitMap = zoomImage(bitMap, width,
					height>(2*width)?(2*width):height);
		}
		return bitMap;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
		return bitmap;
	}

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable {@link Drawable}
     * @return {@link Bitmap}
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = BitmapUtil.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                        : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 将Bitmap转为Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        return (Drawable) bitmapDrawable;
    }

    /**
     * 获得圆角图片的方法
     *
     * @param bitmap  源图片
     * @param roundPx 圆角半径
     * @return 新图片
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = BitmapUtil.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 获得圆形的方法
     *
     * @param bitmap  源图片
     * @param roundPx 圆角半径
     * @return 新图片
     */
    public static Bitmap getRoundedBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = BitmapUtil.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 获得带边框圆形的方法
     *
     * @param bitmap     源图片
     * @param roundPx    圆角半径
     * @param boderPx    背景边框宽度
     * @param boderColor 背景边框颜色
     * @return 新图片
     */
    public static Bitmap getRoundedBitmapWithBorder(Bitmap bitmap, float roundPx, int boderPx,
                                                    int boderColor) {
        Bitmap tmpBitmap = getRoundedBitmap(bitmap, roundPx);
        if (tmpBitmap == null) {
            return null;
        }

        Bitmap output = BitmapUtil.createBitmap(tmpBitmap.getWidth() + boderPx * 2,
                tmpBitmap.getHeight() + boderPx * 2, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint borderpaint = new Paint();
        final Rect borderrect = new Rect(0, 0, tmpBitmap.getWidth() + boderPx * 2,
                tmpBitmap.getHeight() + boderPx * 2);
        final RectF borderrectF = new RectF(borderrect);
        borderpaint.setAntiAlias(true);
        borderpaint.setColor(boderColor);
        // drawOval方法用于绘制一个包含borderrectF的圆
        canvas.drawOval(borderrectF, borderpaint);

        final Paint paint = new Paint();
        final Rect src = new Rect(0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight());
        final Rect dst = new Rect(boderPx, boderPx, tmpBitmap.getWidth() + boderPx,
                tmpBitmap.getHeight() + boderPx);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // src参数是图片原来的大小，dst参数是 绘画该图片需显示多大
        canvas.drawBitmap(tmpBitmap, src, dst, paint);
        return output;
    }

    /**
     * 获得带倒影的图片方法
     *
     * @param bitmap 源图片
     * @return 带倒影图片
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = BitmapUtil.createBitmap(bitmap, 0, height / 2, width,
                height / 2, matrix, false);
        Bitmap bitmapWithReflection = BitmapUtil.createBitmap(width, (height + height / 2),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
                Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * 把bitmap保存到SD卡上
     *
     * @param bitmap   源图片
     * @param savePath 保存路径
     * @param format   图片格式
     */
    public static boolean saveBitmap(Bitmap bitmap, String savePath, Bitmap.CompressFormat format) {
        if (bitmap == null || TextUtils.isEmpty(savePath)) {
            return false;
        }
        // TODO 尺寸 质量 小图不压缩
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savePath);
            bitmap.compress(format, 80, fos);
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 保存bitmap,会自动创建文件
     *
     * @param bitmap   源图片
     * @param savePath 保存路径
     * @param format   图片格式
     */
    public static boolean saveBitmapWithCreateFile(Bitmap bitmap, String savePath, Bitmap.CompressFormat format) {
        if (bitmap == null || TextUtils.isEmpty(savePath)) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            File file = new File(savePath);
            if (!file.exists()) {
                String parentPath = file.getParent();
                File parent = new File(parentPath);
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(format, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 图片压缩--判断图片尺寸是否小于指定尺寸,如果大于则进行压缩，否则不压缩
     *
     * @param path     图片路径
     * @param savePath 保存路径
     * @param format   格式
     * @return
     */
    public static String encodeImage(String path, String savePath, Bitmap.CompressFormat format) {
        try {
            // 压缩图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bgimage = BitmapFactory.decodeFile(path, options);
            int inSampleSize = 1;
            if (options.outHeight > options.outWidth) {
                inSampleSize = Math.round(options.outHeight / (float) 1024);
            } else {
                inSampleSize = Math.round(options.outWidth / (float) 1024);
            }
            inSampleSize = inSampleSize == 0 ? 1 : inSampleSize;
            if (options.outWidth / inSampleSize > 1024 || options.outHeight / inSampleSize > 1024) {
                inSampleSize = inSampleSize + 1;
            }
            if (inSampleSize > 1) {
                options.inSampleSize = inSampleSize;
                options.inJustDecodeBounds = false;
                bgimage = BitmapFactory.decodeFile(path, options);
                if (bgimage == null)
                    return "";
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 得到输出流
                bgimage.compress(format, 100, baos);
                // 转输入流
                InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                bgimage.recycle();
                bgimage = null;
                File sendFilePath = new File(savePath);
                writeToFile(sendFilePath, isBm);
            } else {
                if (bgimage != null && !bgimage.isRecycled()) {
                    bgimage.recycle();
                    bgimage = null;
                }
            }
            return savePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savePath;
    }

    /***
     * 获取网络图片
     * @param imageUrl
     * @return
     */
    public static Bitmap readNetworkBitmap(final String imageUrl) {
        if (imageUrl == null || imageUrl.length() == 0) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getImageStream(imageUrl));
        } catch (Exception e) {
        }
        return bitmap;
    }

    private static InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 读取
     *
     * @param filePath 文件路径
     * @return
     */
    public static Bitmap readBitmap(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        } else {
            return BitmapUtil.decodeFile(filePath);
        }
    }

    /**
     * 写入图片
     *
     * @param saveFile
     * @param in
     * @return
     */
    public static boolean writeToFile(File saveFile, InputStream in) {
        FileOutputStream fout = null;
        boolean success = true;
        try {
            fout = new FileOutputStream(saveFile);
            int len = -1;
            byte[] buff = new byte[4096];
            for (; (len = in.read(buff)) != -1; ) {
                fout.write(buff, 0, len);
            }
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        } finally {
            if (fout != null)
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return success;
    }

    /**
     * 根据图片，自动算出匹配的颜色值
     *
     * @param bm 图片对象
     * @return 颜色值
     */
    public static int matchColorByImage(Bitmap bm) {
        if (bm == null) {
            return Color.WHITE;
        }
        int w = bm.getWidth();
        int h = bm.getHeight();
        int[] RGB;
        if (w > 100 && h > 100) {
            Bitmap temp = zoomImage(bm, w / 8, h / 8);
            w = temp.getWidth();
            h = temp.getHeight();
            RGB = new int[w * h];
            temp.getPixels(RGB, 0, w, 0, 0, w, h);
            temp.recycle();
        } else {
            RGB = new int[w * h];
            bm.getPixels(RGB, 0, w, 0, 0, w, h);
        }
        int color = matchColorByImage(RGB);
        RGB = null;
        return color;
    }

    /**
     * 根据图片，自动算出匹配的颜色值r,g,b
     *
     * @param RGB 图片的像素数组,int里的每4个字节代表rgba中的一个值
     * @return 颜色值
     */
    private static int matchColorByImage(int[] RGB) {

        int[] rgb = new int[3];

        int ignore = 0;
        int MaxPosNumber = 10000;// 最多取一万个点求平均值，
        ignore = RGB.length / MaxPosNumber;
        if (ignore == 0) {
            ignore = 1;
        }
        int totalr = 0;
        int totalg = 0;
        int totalb = 0;
        int PosNumber = 0;
        int len = RGB.length - 1;
        for (int i = len; i >= 0; i -= ignore) {

            rgb[0] = (RGB[i] & 0x00ff0000) >> 16;
            rgb[1] = (RGB[i] & 0x0000ff00) >> 8;
            rgb[2] = (RGB[i] & 0x000000ff);

            // //一半的几率，剔除黑白色系颜色。start
            // if(PosNumber%2 == 0 && rgb[0] == rgb[1] && rgb[1] == rgb[2]){
            // PosNumber++;
            // continue;
            // }
            // end
            totalr += rgb[0];
            totalg += rgb[1];
            totalb += rgb[2];

            PosNumber++;
        }

        rgb[0] = totalr / PosNumber;
        rgb[1] = totalg / PosNumber;
        rgb[2] = totalb / PosNumber;

        int returnColor = Color.rgb(rgb[0], rgb[1], rgb[2]);
        // 从给定的颜色数组里匹配一个最接近的颜色值
        returnColor = matchNearColor(returnColor);
        // //增加亮度值
        // return ChangeColorLightByPercent(returnColor, 20);

        return returnColor;
    }

    private final static int[] colorArr = {
            0xFFFFB6C1, // LightPink 浅粉红
            0xFFFFC0CB, // Pink 粉红
            0xFFDC143C, // Crimson 深红/猩红
            0xFFFFF0F5, // LavenderBlush 淡紫红
            0xFFDB7093, // PaleVioletRed 弱紫罗兰红
            0xFFFF69B4, // HotPink 热情的粉红
            0xFFFF1493,// DeepPink 深粉红
            0xFFC71585,// MediumVioletRed 中紫罗兰红
            0xFFDA70D6,// Orchid 暗紫色/兰花紫
            0xFFDDA0DD,// Plum 洋李色/李子紫
            0xFFEE82EE,// Violet 紫罗兰
            0xFFFF00FF, // Magenta 洋红/玫瑰红
            0xFFFF00FF, // Fuchsia 紫红/灯笼海棠
            0xFF8B008B, // DarkMagenta 深洋红
            0xFF800080, // Purple 紫色
            0xFFBA55D3, // MediumOrchid 中兰花紫
            0xFF9400D3, // DarkViolet 暗紫罗兰
            0xFF9932CC,// DarkOrchid 暗兰花紫
            0xFF4B0082, // Indigo 靛青/紫兰色
            0xFF8A2BE2, // BlueViolet 蓝紫罗兰
            0xFF9370DB, // MediumPurple 中紫色
            0xFF7B68EE, // MediumSlateBlue 中暗蓝色/中板岩蓝
            0xFF6A5ACD, // SlateBlue 石蓝色/板岩蓝
            0xFF483D8B, // DarkSlateBlue 暗灰蓝色/暗板岩蓝
            0xFF0000FF, // 纯蓝
            0xFF0000CD, // MediumBlue 中蓝色
            0xFF191970, // MidnightBlue 午夜蓝
            0xFF00008B, // DarkBlue 暗蓝色
            0xFF000080, // Navy 海军蓝
            0xFF4169E1, // RoyalBlue 皇家蓝/宝蓝
            0xFF6495ED, // CornflowerBlue 矢车菊蓝
            0xFF778899, // LightSlateGray 亮蓝灰/亮石板灰
            0xFF708090, // SlateGray 灰石色/石板灰
            0xFF1E90FF, // DodgerBlue 闪兰色/道奇蓝
            0xFFF0F8FF, // AliceBlue 爱丽丝蓝
            0xFF4682B4, // SteelBlue 钢蓝/铁青
            0xFF87CEFA, // LightSkyBlue 亮天蓝色
            0xFF87CEEB, // SkyBlue 天蓝色
            0xFF00BFFF, // DeepSkyBlue 深天蓝
            0xFFADD8E6, // LightBlue 亮蓝
            0xFFB0E0E6, // PowderBlue 粉蓝色/火药青
            0xFF5F9EA0, // CadetBlue 军兰色/军服蓝
            0xFFAFEEEE, // PaleTurquoise 弱绿宝石
            0xFF00FFFF, // Cyan 青色
            0xFF00FFFF, // Aqua 浅绿色/水色
            0xFF00CED1, // DarkTurquoise 暗绿宝石
            0xFF2F4F4F, // DarkSlateGray 暗瓦灰色/暗石板灰
            0xFF008B8B, // DarkCyan 暗青色
            0xFF008080, // Teal 水鸭色
            0xFF48D1CC,// MediumTurquoise 中绿宝石
            0xFF20B2AA,// LightSeaGreen 浅海洋绿
            0xFF40E0D0, // Turquoise 绿宝石
            0xFF7FFFD4, // Aquamarine 宝石碧绿
            0xFF66CDAA,// MediumAquamarine 中宝石碧绿
            0xFF00FA9A,// MediumSpringGreen 中春绿色
            0xFFF5FFFA,// MintCream 薄荷奶油
            0xFF00FF7F, // SpringGreen 春绿色
            0xFF3CB371,// MediumSeaGreen 中海洋绿
            0xFF2E8B57,// SeaGreen 海洋绿
            0xFFF0FFF0,// Honeydew 蜜色/蜜瓜色
            0xFF90EE90,// LightGreen 淡绿色
            0xFF98FB98,// PaleGreen 弱绿色
            0xFF8FBC8F, // DarkSeaGreen 暗海洋绿
            0xFF32CD32, // LimeGreen 闪光深绿
            0xFF00FF00, // Lime 闪光绿
            0xFF228B22, // ForestGreen 森林绿
            0xFF008000, // Green 纯绿
            0xFF006400, // DarkGreen 暗绿色
            0xFF7FFF00, // Chartreuse 黄绿色/查特酒绿
            0xFF7CFC00, // LawnGreen 草绿色/草坪绿
            0xFFADFF2F,// GreenYellow 绿黄色
            0xFF556B2F,// DarkOliveGreen 暗橄榄绿
            0xFF9ACD32,// YellowGreen 黄绿色
            0xFF6B8E23, // OliveDrab 橄榄褐色
            0xFFF5F5DC, // Beige 米色/灰棕色
            0xFFFAFAD2, // LightGoldenrodYellow 亮菊黄
            0xFFFFFFF0, // Ivory 象牙色
            0xFFFFFFE0,// LightYellow 浅黄色
            0xFFFFFF00,// Yellow 纯黄
            0xFF808000, // Olive 橄榄
            0xFFBDB76B, // DarkKhaki 暗黄褐色/深卡叽布
            0xFFEEE8AA, // PaleGoldenrod 灰菊黄/苍麒麟色
            0xFFF0E68C, // Khaki 黄褐色/卡叽布
            0xFFFFD700, // Gold 金色
            0xFFFFF8DC, // Cornsilk 玉米丝色
            0xFFDAA520,// Goldenrod 金菊黄
            0xFFB8860B, // DarkGoldenrod 暗金菊黄
            0xFFFFE4B5,// Moccasin 鹿皮色/鹿皮靴
            0xFFFFA500, // Orange 橙色
            0xFFFFEFD5, // PapayaWhip 番木色/番木瓜
            0xFFD2B48C, // Tan 茶色
            0xFFDEB887, // BurlyWood 硬木色
            0xFFFFE4C4,// Bisque 陶坯黄
            0xFFFF8C00,// DarkOrange 深橙色
            0xFFFAF0E6,// Linen 亚麻布
            0xFFCD853F, // Peru 秘鲁色
            0xFFFFDAB9, // PeachPuff 桃肉色
            0xFFF4A460, // SandyBrown 沙棕色
            0xFFD2691E, // Chocolate 巧克力色
            0xFF8B4513, // SaddleBrown 重褐色/马鞍棕色
            0xFFFFF5EE, // Seashell 海贝壳
            0xFFA0522D, // Sienna 黄土赭色
            0xFFFFA07A, // LightSalmon 浅鲑鱼肉色
            0xFFFF7F50, // Coral 珊瑚
            0xFFFF4500, // OrangeRed 橙红色
            0xFFE9967A,// DarkSalmon 深鲜肉/鲑鱼色
            0xFFFF6347, // Tomato 番茄红
            0xFFFFE4E1, // MistyRose 浅玫瑰色/薄雾玫瑰
            0xFFFA8072, // Salmon 鲜肉/鲑鱼色
            0xFFFFFAFA, // Snow 雪白色
            0xFFF08080, // LightCoral 淡珊瑚色
            0xFFBC8F8F, // RosyBrown 玫瑰棕色
            0xFFCD5C5C, // IndianRed 印度红
            0xFFFF0000, // Red 纯红
            0xFFA52A2A, // Brown 棕色
            0xFFB22222, // FireBrick 火砖色/耐火砖
            0xFF8B0000, // DarkRed 深红色
            0xFF800000, // Maroon 栗色
            0xFFC0C0C0, // Silver 银灰色
            0xFF696969, // DimGray 暗淡灰
            0xFF000000
            // Black 纯黑
    };

    private static int matchNearColor(int color) {
        // 默认值是原值
        int nearColor = color;

        int[] rgb = new int[3];
        rgb[0] = Color.red(color);
        rgb[1] = Color.green(color);
        rgb[2] = Color.blue(color);

        // 差距值
        double curDis = 255 * 3;
        int[] rgbItem = new int[3];
        for (int i = 0; i < colorArr.length; i++) {
            rgbItem[0] = Color.red(colorArr[i]);
            rgbItem[1] = Color.green(colorArr[i]);
            rgbItem[2] = Color.blue(colorArr[i]);
            int abs1 = rgb[0] - rgbItem[0];
            int abs2 = rgb[1] - rgbItem[1];
            int abs3 = rgb[2] - rgbItem[2];
            double dis = StrictMath
                    .sqrt((Math.pow(abs1, 2) + Math.pow(abs2, 2) + Math.pow(abs3, 2)) / 3);
            // int dis = Math.abs(rgb[0] - rgbItem[0]) + Math.abs(rgb[1] -
            // rgbItem[1])
            // + Math.abs(rgb[2] - rgbItem[2]);
            if (dis < curDis) {
                curDis = dis;
                nearColor = colorArr[i];
            }
        }

        return nearColor;
    }

    /**
     * 裁剪图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap cropBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        Bitmap newBitmap = BitmapUtil.createBitmap(bitmap, 0, 0, width, height, null, true);
        return newBitmap;
    }

    /**
     * 图片平铺
     */
    public static Bitmap bitmapCreateRepeater(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }

    /**
     * 按照bitmap的宽度先把原图等比缩放，然后裁剪处一个边长为宽度的矩形
     *
     * @param bitmap
     * @param w
     * @return
     */
    public static Bitmap zoomAndCropSquareBitmap(Bitmap bitmap, int w) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap newbmpTmp = BitmapUtil.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (newbmpTmp == null || newbmpTmp.isRecycled()) {
            return null;
        }
        Bitmap newBitmap = BitmapUtil.createBitmap(newbmpTmp, 0, 0, w, w, null, true);
        return newBitmap;
    }

    public static Bitmap decodeResizedFile(String pathName, int width, int height) {
        Bitmap bitmap = null;

        try {
            BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;
            if (width == 0) {
                width = actualWidth;
            }
            if (height == 0) {
                height = actualHeight;
            }

            decodeOptions.inJustDecodeBounds = false;

            int desiredWidth = getResizedDimension(width, height, actualWidth, actualHeight);
            int desiredHeight = getResizedDimension(width, height, actualHeight, actualWidth);
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
                    desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeFile(pathName, decodeOptions);
            if (tempBitmap != null
                    && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)
                    && desiredWidth > 0 && desiredHeight > 0) {
                // desiredWidth和desiredHeight必须确保大于0
                // ,不然会报java.lang.IllegalArgumentException: width and height
                // must be > 0
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
                                   int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling
        // ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth,
                                  int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }
        return (int) n;
    }

    /***
     * 旋转图片
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float degree) {
        if (bitmap == null) {
            return null;
        }

        Matrix m = new Matrix();
        m.setRotate(degree, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return bitmap;
    }

    public static Bitmap decodeResizedResource(Resources res, int id, int width, int height) {
        Bitmap bitmap = null;

        try {
            BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, id, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;
            if (width == 0) {
                width = actualWidth;
            }
            if (height == 0) {
                height = actualHeight;
            }

            decodeOptions.inJustDecodeBounds = false;

            int desiredWidth = getResizedDimension(width, height, actualWidth, actualHeight);
            int desiredHeight = getResizedDimension(width, height, actualHeight, actualWidth);
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
                    desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeResource(res, id, decodeOptions);
            if (tempBitmap != null
                    && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 把bitmap保存到SD卡上
     *
     * @param bitmap   源图片
     * @param savePath 保存路径
     * @param format   图片格式
     */
    public static boolean writeBitmapToFile(Bitmap bitmap, String savePath, Bitmap.CompressFormat format) {
        if (bitmap == null || TextUtils.isEmpty(savePath)) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savePath);
            bitmap.compress(format, 100, fos);
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Bitmap circleHead(Bitmap source, int width, int height, int borderSize,
                                    int circleColor) {
        Bitmap ret = null;
        if (source != null) {
            Bitmap zoomBitmap = zoomImage(source, width, width);
            Bitmap roundedCornerBitmap = getRoundedBitmapWithBorder(zoomBitmap, 5.0f,
                    borderSize, circleColor);
            ret = BitmapUtil.createBitmap(roundedCornerBitmap, 0, 0, width + borderSize * 2,
                    height + borderSize * 2);
            zoomBitmap.recycle();
            roundedCornerBitmap.recycle();
        }
        return ret;
    }

    public static Bitmap centerCropImage(Bitmap bitmap, int outWidth, int outHeight) {
        if (bitmap == null || outWidth <= 0 || outHeight <= 0) {
            return bitmap;
        }

        Bitmap croppedImage = Bitmap.createBitmap(outWidth, outHeight, Config.RGB_565);
        Canvas canvas = new Canvas(croppedImage);

        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect dstRect = new Rect(0, 0, outWidth, outHeight);

        float dstRate = (float) (dstRect.width() / dstRect.height());
        float srcRate = (float) (srcRect.width() / srcRect.height());

        int deltaW;
        int deltaH;
        if (dstRate > srcRate) {
            deltaW = 0;
            deltaH = (int) ((srcRect.height() - (float) srcRect.width() / dstRate) / 2);
        } else {
            deltaW = (int) ((srcRect.width() - dstRate * (float) srcRect.height()) / 2);
            deltaH = 0;
        }

        srcRect.inset(deltaW, deltaH);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);

        return croppedImage;
    }

    /**
     * 改变图片像素的颜色
     *
     * @param bm    图片
     * @param color 目标颜色值
     * @return
     */
    public static Bitmap changeImageColor(Bitmap bm, int color) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        int[] RGB = new int[w * h];
        bm.getPixels(RGB, 0, w, 0, 0, w, h);
        bm.getConfig();
        int[] rgb = new int[3];
        rgb[0] = Color.red(color);
        rgb[1] = Color.green(color);
        rgb[2] = Color.blue(color);
        RGB = changeImageColor(RGB, rgb);
        Bitmap newBM = BitmapUtil.createBitmap(RGB, w, h, bm.getConfig());
        RGB = null;
        rgb = null;
        return newBM;
    }

    /**
     * 改变图片像素的颜色
     *
     * @param RGB       图片的像素数组,int里的每4个字节代表rgba中的一个值
     * @param targetrgb 目标颜色值 rgb,rgb各占一个int空间
     * @return
     */
    //
    private static int[] changeImageColor(int[] RGB, int[] targetrgb) {

        float h = 0.0f;
        float l = 0.0f;
        float s = 0.0f;
        float hls[] = new float[3];
        hls = RGBtoHLS(targetrgb[0], targetrgb[1], targetrgb[2]);
        h = hls[0];
        l = hls[1];
        s = hls[2];
        float powl = CalPowl(l);
        int[] rgb = new int[3];
        int oldRgbSrc = 999;
        int oldRgbDes = 999;
        int len = RGB.length - 1;
        for (int i = len; i >= 0; i--) {
            if (oldRgbSrc == RGB[i] && oldRgbDes != 999) {
                RGB[i] = oldRgbDes;
            } else {
                rgb[0] = (RGB[i] & 0x00ff0000) >> 16;
                rgb[1] = (RGB[i] & 0x0000ff00) >> 8;
                rgb[2] = (RGB[i] & 0x000000ff);

                oldRgbSrc = RGB[i];

                rgb = CalMergeColor(rgb, powl, h, l, s);

                RGB[i] = (RGB[i] & 0xff000000) | ((rgb[0] & 0xff) << 16) | ((rgb[1] & 0xff) << 8)
                        | (rgb[2] & 0xff);
                oldRgbDes = RGB[i];
            }
        }
        return RGB;
    }

    /**
     * @param h
     * @param l
     * @param s
     * @return
     */
    private static int[] HLStoRGB(float h, float l, float s) {

        int[] rgb = new int[3];
        float m1, m2;
        float R, G, B;
        if (l <= 0.5)
            m2 = l * (1.0f + s);
        else
            m2 = l + s - l * s;
        m1 = 2.0f * l - m2;
        R = Value(m1, m2, h + 120.0f);
        G = Value(m1, m2, h);
        B = Value(m1, m2, h - 120.0f);
        int iR = (int) (R * 255.0);
        int iG = (int) (G * 255.0);
        int iB = (int) (B * 255.0);
        rgb[0] = (int) BOUND(iR, 0, 255);
        rgb[1] = (int) BOUND(iG, 0, 255);
        rgb[2] = (int) BOUND(iB, 0, 255);
        return rgb;
    }

    /**
     * @param r
     * @param g
     * @param b
     * @return
     */
    private static float[] RGBtoHLS(int r, int g, int b) {

        float[] hls = new float[3];
        float mx, mn, delta;
        float R, G, B;
        R = (float) (r / 255.0);
        G = (float) (g / 255.0);
        B = (float) (b / 255.0);
        mx = Math.max(R, Math.max(G, B));
        mn = Math.min(R, Math.min(G, B));
        hls[1] = (mx + mn) / 2.0f;
        if (mx == mn) {
            hls[2] = 0.0f;
            hls[0] = 0.0f; // undefined!
        } else {
            delta = mx - mn;
            if (hls[1] < 0.5)
                hls[2] = delta / (mx + mn);
            else
                hls[2] = delta / (2.0f - mx - mn);
            if (R == mx)
                hls[0] = (G - B) / delta;
            else if (G == mx)
                hls[0] = 2.0f + (B - R) / delta;
            else if (B == mx)
                hls[0] = 4.0f + (R - G) / delta;
            hls[0] *= 60.0;
            if (hls[0] < 0.0)
                hls[0] += 360.0;
            else if (hls[0] > 360.0)
                hls[0] -= 360.0;
        }
        return hls;
    }

    private final static float BOUND(float x, float mn, float mx) {

        return ((x) < (mn) ? (mn) : ((x) > (mx) ? (mx) : (x)));
    }

    // local function used in HLStoRGB
    private final static float Value(float n1, float n2, float hue) {

        if (hue > 360.0)
            hue -= 360.0;
        else if (hue < 0.0)
            hue += 360.0;
        if (hue < 60.0)
            return (n1 + (n2 - n1) * hue / 60.0f);
        else if (hue < 180.0)
            return n2;
        else if (hue < 240.0)
            return (n1 + (n2 - n1) * (240.0f - hue) / 60.0f);
        else
            return n1;
    }

    /**
     * 这个是对一个颜色算出灰阶
     *
     * @param r
     * @param g
     * @param b
     * @return 灰度
     */
    private static int RGB2Gray(int r, int g, int b) {
        int gray = 0;
        gray = (((b) * 117 + (g) * 601 + (r) * 306) >> 10);

        return gray;
    }

    /**
     * 判断是否使用亮色的文字
     *
     * @param
     * @return true, 使用亮色文字;flase,使用暗色文字.
     */
    public static boolean IsUseLightFont(int color) {

        boolean useLightFont = false;
        int gray = RGB2Gray(Color.red(color), Color.green(color), Color.blue(color));

        if (gray <= 150) {
            useLightFont = true;
        }

        return useLightFont;
    }

    /**
     * 计算出新图的亮度偏移值
     *
     * @return
     */
    private static float CalPowl(float TargetL) {
        double powl = 0;
        if (TargetL - 50.0f > 0)
            powl = Math.pow(TargetL - 50.0f, 0.88f);
        else
            powl = -Math.pow(50.0f - TargetL, 0.88f);
        return (float) powl;
    }

    /**
     * 改变图片像素的颜色
     *
     * @param Source 目标颜色值 rgb,rgb各占一个int空间
     * @return
     */
    private static int[] CalMergeColor(int[] Source, float Powl, float SelectH, float SelectS,
                                       float SelectL) {
        int[] DColor = new int[3];

        float hls[] = new float[3];
        hls = RGBtoHLS(Source[0], Source[1], Source[2]);
        // float h = hls[0];
        float l = hls[1];
        float s = hls[2];

        float DL, DS, DH;
        if (l >= 100.0f || l <= 0.0f) {
            DColor = Source;
            return DColor;
        }

        DL = l;
        DS = s;
        DH = SelectH;

        DColor = HLStoRGB(DH, DL, DS);

        return DColor;
    }

    /**
     * 改变图片像素的颜色
     *
     * @param Source 颜色值
     * @param L      要改变的亮度值增量 （0.0f - 1。0f）
     * @return 改变后的颜色值
     */
    public static int ChangeColorLight(int Source, float L) {
        int[] rgb = new int[3];
        rgb[0] = Color.red(Source);
        rgb[1] = Color.green(Source);
        rgb[2] = Color.blue(Source);
        int alpha = Color.alpha(Source);
        float hls[] = new float[3];

        hls = RGBtoHLS(rgb[0], rgb[1], rgb[2]);
        float h = hls[0];
        float l = hls[1];
        float s = hls[2];
        l += L;
        if (l > 1.0f) {
            l = 1.0f;
        } else if (l < 0.0f) {
            l = 0.0f;
        }

        rgb = HLStoRGB(h, l, s);
        int DColor = Color.argb(alpha, rgb[0], rgb[1], rgb[2]);
        return DColor;
    }

    /**
     * 改变图片像素的颜色
     *
     * @param Source 颜色值
     * @param L      0~100 百分比
     * @return 改变后的颜色值
     */
    public static int ChangeColorLightByPercent(int Source, int L) {
        int[] rgb = new int[3];
        rgb[0] = Color.red(Source);
        rgb[1] = Color.green(Source);
        rgb[2] = Color.blue(Source);
        int alpha = Color.alpha(Source);
        float hls[] = new float[3];

        hls = RGBtoHLS(rgb[0], rgb[1], rgb[2]);
        float h = hls[0];
        float l = hls[1];
        float s = hls[2];
        l += l * L / 100f;
        if (l > 1.0f) {
            l = 1.0f;
        } else if (l < 0.0f) {
            l = 0.0f;
        }

        rgb = HLStoRGB(h, l, s);
        int DColor = Color.argb(alpha, rgb[0], rgb[1], rgb[2]);
        return DColor;
    }


}
