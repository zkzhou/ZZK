
package myframelib.zkzhou.com.common.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 描述:获取手机设置的APN
 */
public class APNManager {

    /**
     * 当前手机所设置的APN
     */
    static Uri CURRENT_PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    static String[] APN_COLUMN = {
            "apn"
    };

    private static boolean sWap = true;

    public static String getAPNName(Context context) {
        ContentResolver cResolver = context.getContentResolver();
        Cursor cr = null;
        try {
            cr = cResolver.query(CURRENT_PREFERRED_APN_URI, APN_COLUMN, null, null, null);
            if (cr != null && cr.moveToFirst()) {
                String apn = cr.getString(cr.getColumnIndex("apn"));
                return apn;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cr != null) {
                cr.close();
            }
        }
        return null;
    }

    /**
     * 默认缓存为wap，根据APN判断是否为wap，如果获取失败使用缓存值
     */
    public static boolean isWap(Context context) {
        ContentResolver cResolver = context.getContentResolver();
        Cursor cr = null;
        try {
            cr = cResolver.query(CURRENT_PREFERRED_APN_URI, APN_COLUMN, null, null, null);
            if (cr != null && cr.moveToFirst()) {
                String apn = cr.getString(cr.getColumnIndex("apn"));
                if (TextUtils.isEmpty(apn)) {
                    sWap = true;
                } else if (apn.toLowerCase().endsWith("net")) {
                    sWap = false;
                } else {
                    sWap = true;
                }
                // KGLog.d(Constants.UNICOM_LOG_TAG, "apn : " + apn +
                // " APN count: " + cr.getCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cr != null) {
                cr.close();
            }
        }
        return sWap;
    }

    public static void printInfo(Context context) {
        ContentResolver cResolver = context.getContentResolver();
        Cursor cr = null;
        try {
            cr = cResolver.query(CURRENT_PREFERRED_APN_URI, null, null, null, null);
            if (cr.moveToFirst()) {
                do {
                    String apn = cr.getString(cr.getColumnIndex("apn"));
                    String proxy = cr.getString(cr.getColumnIndex("proxy"));
                    String port = cr.getString(cr.getColumnIndex("port"));
                } while (cr.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cr != null) {
                cr.close();
            }
        }
    }
}
