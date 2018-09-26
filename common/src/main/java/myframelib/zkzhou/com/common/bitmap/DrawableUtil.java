
package myframelib.zkzhou.com.common.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 图片获取工具类
 */
public class DrawableUtil {

    private static HashMap<String, WeakReference<Bitmap>> mBitmapCache = new HashMap<String, WeakReference<Bitmap>>(
            0);

    @Deprecated
    public static Bitmap createBitmapFromPath(String pathName) {
        if (pathName == null) {
            return null;
        }
        recycle(pathName);
        if (mBitmapCache.containsKey(pathName)) {
            Bitmap bm = mBitmapCache.get(pathName).get();
            if (bm != null) {
                return bm;
            } else {
                try {
                    bm = BitmapUtil.decodeFile(pathName);
                } catch (Exception e) {
                }
                if (bm != null) {
                    mBitmapCache.put(pathName, new WeakReference<Bitmap>(bm));
                    return bm;
                }
            }

        } else {
            Bitmap bm = null;
            try {
                bm = BitmapUtil.decodeFile(pathName);
                if (bm != null) {
                    mBitmapCache.put(pathName, new WeakReference<Bitmap>(bm));
                    return bm;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Deprecated
    public static Bitmap createBitmapFromResId(Context context, int resId) {
        if (resId <= 0) {
            return null;
        }
        recycle("" + resId);
        if (mBitmapCache.containsKey("" + resId)) {
            Bitmap bm = mBitmapCache.get("" + resId).get();
            if (bm != null) {
                return bm;
            } else {
                // BitmapFactory.Options opts = new BitmapFactory.Options();
                // opts.inJustDecodeBounds = true;
                try {
                    bm = BitmapUtil.decodeResource(context.getApplicationContext().getResources(), resId);
                } catch (Exception e) {
                }
                if (bm != null) {
                    mBitmapCache.put("" + resId, new WeakReference<Bitmap>(bm));
                    return bm;
                }
            }

        } else {
            Bitmap bm;
            // BitmapFactory.Options opts = new BitmapFactory.Options();
            // opts.inJustDecodeBounds = true;
            try {
                bm = BitmapUtil.decodeResource(context.getApplicationContext().getResources(), resId);
                if (bm != null) {
                    mBitmapCache.put("" + resId, new WeakReference<Bitmap>(bm));
                    return bm;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 获取背景图
     *
     * @param pathName 自定义背景图路径
     * @return
     */
    @Deprecated
    public static Drawable createDrawableFromPath(String pathName) {
        if (pathName == null) {
            return null;
        }
        recycle(pathName);
        if (mBitmapCache.containsKey(pathName)) {
            Bitmap bm = mBitmapCache.get(pathName).get();
            if (bm != null) {
                return drawableFromBitmap(bm);
            } else {
                try {
                    bm = BitmapUtil.decodeFile(pathName);
                } catch (Exception e) {
                }
                if (bm != null) {
                    mBitmapCache.put(pathName, new WeakReference<Bitmap>(bm));
                    return drawableFromBitmap(bm);
                }
            }

        } else {
            Bitmap bm = null;
            try {
                bm = BitmapUtil.decodeFile(pathName);
                if (bm != null) {
                    mBitmapCache.put(pathName, new WeakReference<Bitmap>(bm));
                    return drawableFromBitmap(bm);
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Deprecated
    public static Drawable createDrawableFromResId(Context context, int resId) {
        if (resId <= 0) {
            return null;
        }
        recycle("" + resId);
        if (mBitmapCache.containsKey("" + resId)) {
            Bitmap bm = mBitmapCache.get("" + resId).get();
            if (bm != null) {
                return drawableFromBitmap(bm);
            } else {
                // BitmapFactory.Options opts = new BitmapFactory.Options();
                // opts.inJustDecodeBounds = true;
                try {
                    bm = BitmapUtil.decodeResource(context.getApplicationContext().getResources(), resId);
                } catch (Exception e) {
                }
                if (bm != null) {
                    mBitmapCache.put("" + resId, new WeakReference<Bitmap>(bm));
                    return drawableFromBitmap(bm);
                }
            }

        } else {
            Bitmap bm;
            // BitmapFactory.Options opts = new BitmapFactory.Options();
            // opts.inJustDecodeBounds = true;
            try {
                bm = BitmapUtil.decodeResource(context.getApplicationContext().getResources(), resId);
                if (bm != null) {
                    mBitmapCache.put("" + resId, new WeakReference<Bitmap>(bm));
                    return drawableFromBitmap(bm);
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    private static Drawable drawableFromBitmap(Bitmap bm) {
        final BitmapDrawable drawable = new BitmapDrawable(bm);
        return drawable;
    }

    public static Drawable getStateDrawable(Drawable defaultDrawable, Drawable pressedDrawable) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{
                android.R.attr.state_pressed
        }, pressedDrawable);
        drawable.addState(new int[]{
                android.R.attr.state_selected
        }, pressedDrawable);
        drawable.addState(new int[]{
                android.R.attr.state_focused, android.R.attr.state_enabled
        }, pressedDrawable);
        drawable.addState(new int[]{
                android.R.attr.state_enabled
        }, defaultDrawable);
        drawable.addState(new int[]{
                android.R.attr.state_focused
        }, pressedDrawable);
        drawable.addState(StateSet.WILD_CARD, defaultDrawable);
        return drawable;
    }

    /**
     * 把之前产生的bitmap回收掉，防止内存溢出
     *
     * @param remainKey 不回收的key名，为图片路径名
     */
    @Deprecated
    public static void recycle(String remainKey) {
        for (Iterator<String> keys = mBitmapCache.keySet().iterator(); keys.hasNext(); ) {
            String key = keys.next();
            if (key.equals(remainKey)) {
                continue;
            }
            WeakReference<Bitmap> item = mBitmapCache.get(key);
            if (item != null) {
                Bitmap bitmap = item.get();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                mBitmapCache.remove(key);
            }
        }
    }

}
