
package myframelib.zkzhou.com.common.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 创建快捷方式工具类
 */
public class ShortCutUtil {

    /**
     * 创建快捷方式
     *
     * @param context
     */
    public static void createShortcut(Activity context, String shortcutName, int iconRes) {
        String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        /**
         * 是否可以有多个快捷方式的副本
         */
        String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        shortcutIntent.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
        Intent intent2 = new Intent(Intent.ACTION_MAIN);
        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
        intent2.setComponent(new ComponentName(context.getPackageName(), context.getPackageName()
                + "." + context.getLocalClassName()));
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent2);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, iconRes));
        context.sendBroadcast(shortcutIntent);
    }

    public static void createShortcut(Context context, Class<?> cls, String shortcutName, int iconRes) {
        final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        // 是否可以有多个快捷方式的副本
        final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";

        Intent target = new Intent(Intent.ACTION_MAIN);
        target.addCategory(Intent.CATEGORY_LAUNCHER);
        target.setComponent(new ComponentName(context.getPackageName(), cls.getName()));
        target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        Intent createIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        createIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        createIntent.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
        createIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, target);
        createIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, iconRes));

        context.sendBroadcast(createIntent);
    }

    /**
     * 创建快捷方式
     *
     * @param context
     */
    public static void createShortcut(Context context, String clx, String shortcutName, int iconRes) {
        String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        /**
         * 是否可以有多个快捷方式的副本
         */
        String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        shortcutIntent.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
        Intent intent2 = new Intent(Intent.ACTION_MAIN);
        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
        intent2.setComponent(new ComponentName(context.getPackageName(), clx));
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent2);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, iconRes));
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * 删除快捷方式
     *
     * @param context
     */
    public static void deleteShortcut(Context context, Class<?> cls, String shortcutName, int iconRes) {
        final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
        // 是否可以有多个快捷方式的副本
        final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";

        Intent target = new Intent(Intent.ACTION_MAIN);
        target.addCategory(Intent.CATEGORY_LAUNCHER);
        target.setComponent(new ComponentName(context.getPackageName(), cls.getName()));
        target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        Intent removeIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        removeIntent.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, target);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, iconRes));

        context.sendBroadcast(removeIntent);
    }

}
