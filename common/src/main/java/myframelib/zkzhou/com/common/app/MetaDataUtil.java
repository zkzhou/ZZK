
package myframelib.zkzhou.com.common.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * 描述:读取manifest文件中的meta-data项
 */
public class MetaDataUtil {
    private Context context = null;

    private Bundle appMetaData = null;

    private static MetaDataUtil instance = null;

    public static MetaDataUtil getInstance(Context context) {
        if (instance == null) {
            instance = new MetaDataUtil(context);
        }
        return instance;
    }

    private MetaDataUtil(Context context) {
        this.context = context;
        appMetaData = getAppMetaData();
    }

    private Bundle getAppMetaData() {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return appInfo.metaData;
        } catch (Exception e) {
        }
        return null;
    }

    public boolean readBoolean(String key) {
        if (appMetaData == null) {
            appMetaData = getAppMetaData();
        }
        boolean value = false;
        if (appMetaData != null) {
            value = appMetaData.getBoolean(key);
        }
        return value;
    }

    public Bundle readBundle(String key) {
        if (appMetaData == null) {
            appMetaData = getAppMetaData();
        }
        Bundle value = null;
        if (appMetaData != null) {
            value = appMetaData.getBundle(key);
        }
        return value;
    }

    public int readInteger(String key) {
        if (appMetaData == null) {
            appMetaData = getAppMetaData();
        }
        int value = 0;
        if (appMetaData != null) {
            value = appMetaData.getInt(key);
        }
        return value;
    }

    /**
     * 读取manifest文件中的meta-data项的String值
     *
     * @param key
     * @return
     */
    public String readString(String key) {
        if (appMetaData == null) {
            appMetaData = getAppMetaData();
        }
        String value = "";
        if (appMetaData != null) {
            value = appMetaData.getString(key);
        }
        return value;
    }
}
