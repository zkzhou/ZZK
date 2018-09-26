
package myframelib.zkzhou.com.common.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myframelib.zkzhou.com.common.net.UrlCoderUtil;


/**
 * 工具类
 */
public class Utils {

    /***
     * dip转px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /***
     * 获取文件编码格式
     * @param file
     * @return
     */
    public static String getEncoding(File file) {
        String encoding = "GB2312";
        byte[] first3bytes = new byte[3];
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(first3bytes, 0, 3);
            if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {
                encoding = "UNICODE";
            } else if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
                    && first3bytes[2] == (byte) 0xBF) {
                encoding = "UTF-8";
            }
        } catch (Exception e) {
            return encoding;
        }

        return encoding;
    }

    /**
     * 除以2以后四舍五入
     *
     * @param integer
     * @return
     */
    public static int getHalfUpInteger(int integer) {
        BigDecimal bd = new BigDecimal((double) integer / 2);
        return bd.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * 获取一个随机数不重复的随机数队列
     *
     * @param queueSize
     * @return
     */
    public static int[] getNoRepeatRandomQueue(int queueSize) {
        int[] mRandomIndexArray = new int[queueSize];
        int[] tempIndexArray = new int[queueSize];
        for (int i = 0; i < queueSize; i++) {
            tempIndexArray[i] = i;
        }
        Random random = new Random();
        int randomRange = queueSize;
        for (int j = 0; j < queueSize; j++) {
            int index = random.nextInt(randomRange);
            int num = tempIndexArray[index];
            mRandomIndexArray[j] = num;
            if (index < randomRange - 1) {
                tempIndexArray[index] = tempIndexArray[randomRange - 1];
            }

            randomRange--;
        }
        return mRandomIndexArray;
    }

    // 返回num长度大小的随机数组，从[0,num-1]中抽取
    public static int[] getRandomIntegerArray(int num) {
        int[] integerArray = new int[num];
        boolean[] b = new boolean[num];
        Random random = new Random();
        int index = num - 1;
        while (index >= 0) {
            // [0,num)的随机整形位置
            int randomPos = random.nextInt(num);
            if (!b[randomPos]) {
                integerArray[index] = randomPos;
                b[randomPos] = true;
                index--;
            }
        }
        return integerArray;
    }

    /**
     * 字符串分割 2009 - 02 - 12
     *
     * @param strSource 原字符串
     * @param paint
     * @param width     显示区域大小
     * @param strSplit  包含的可分割符(例如" ",",",".")
     * @return 存储了分割后子串的Vector容器
     */
    public static Vector<String> getSubsection(String strSource, Paint paint, int width,
                                               String strSplit) {
        // FontTable fTable = new FontTable(font);
        // **
        int partW = 0;
        Vector<String> v = new Vector<String>(1, 1); // 用于存储分割后的子串
        char c = '\0'; // 记录提取的字符
        int len = 0; // 用于宽度统计
        int beginIndex, endIndex; // 截取的起止标志
        beginIndex = endIndex = 0;
        int str_len = strSource.length();
        for (int index = 0; index < str_len; index++) {
            c = strSource.charAt(index); // 获取每个字符
            len += paint.measureText("" + c);// font.charWidth(c); // 累加字符宽度
            // System.out.println("c:"+c+" len:"+len);
            if (len >= width) { // 如果长度超出显示范围
                // System.out.println("outC:"+c);
                if (endIndex != beginIndex
                        && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character
                        .isDigit(c))) {// 如果在此前找到了分割符并且当前字符为字母或数字
                    // 截取从上一子串末尾(beginIndex)到最近一个可断点间的子串
                    v.addElement(strSource.substring(beginIndex, endIndex));
                    beginIndex = endIndex;
                    len = partW;
                    // System.out.println("begin:"+beginIndex+" p:"+partW);
                } else { // 如果(endIndex==beginIndex)前面没找到分割符 或者 非字母非数字
                    // 截取从上一子串末尾(beginIndex)到当前位置间的子串
                    v.addElement(strSource.substring(beginIndex, index));
                    beginIndex = index;
                    len = 0; // 归0,开始新的统计
                }
                partW = 0;
                index--; // 退回一位,使下次循环从当前位置开始
            } else {
                if (c == '\n' && beginIndex != index) { // 是否换行
                    v.addElement(strSource.substring(beginIndex, index));
                    beginIndex = index;
                    endIndex = beginIndex;
                    len = 0;
                } else {
                    partW += paint.measureText("" + c);// font.charWidth(c);
                    boolean hasFind = false;
                    // 遍历所设定的可分割字符
                    int split_len = strSplit.length();
                    for (int k = 0; k < split_len; k++) {
                        if (c == strSplit.charAt(k)) { // 如果等于设定的可分割符
                            // 标记可断点为当前位置+1
                            endIndex = Math.min(str_len, index + 1);
                            hasFind = true;
                            partW = 0;
                            break;
                        }
                    }
                    /***********************************************************
                     * * 以下为对字母和数字的处理,如果不需要可直接删除
                     **********************************************************/
                    // 如果没找到,判断是否字母或数字
                    char lastChar = strSource.charAt(Math.max(0, index - 1));
                    if (!hasFind) {
                        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) { // 如果当前字符为字母
                            // 并且前一字符非字母
                            if ((lastChar < 'A' || lastChar > 'Z')
                                    && (lastChar < 'a' || lastChar > 'z')) {
                                endIndex = Math.min(str_len, index);
                                hasFind = true;
                            }
                        }
                    }
                    if (!hasFind) {
                        // 如果当前字符为数字,并且前一字符非数字
                        if (Character.isDigit(c) && !Character.isDigit(lastChar)) {
                            endIndex = Math.min(str_len, index);
                            hasFind = true;
                        }
                    }
                    /***********************************************************
                     * * END
                     **********************************************************/
                }
            }
        }
        // 添加最后一行子串到容器
        v.addElement(strSource.substring(beginIndex));
        // fTable.release();
        return v;
    }


    /**
     * UTF-8编码格式判断
     *
     * @param rawtext 需要分析的数据
     * @return 是否为UTF-8编码格式
     */
    public static boolean isUTF8(byte[] rawtext) {
        int score = 0;
        int i, rawtextlen = 0;
        int goodbytes = 0, asciibytes = 0;
        // Maybe also use UTF8 Byte Order Mark: EF BB BF
        // Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length;
        for (i = 0; i < rawtextlen; i++) {
            if ((rawtext[i] & (byte) 0x7F) == rawtext[i]) { // One byte
                asciibytes++;
                // Ignore ASCII, can throw off count
            } else if (-64 <= rawtext[i] && rawtext[i] <= -33 && // Two bytes
                    i + 1 < rawtextlen && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65) {
                goodbytes += 2;
                i++;
            } else if (-32 <= rawtext[i] && rawtext[i] <= -17
                    && // Three bytes
                    i + 2 < rawtextlen && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65
                    && -128 <= rawtext[i + 2] && rawtext[i + 2] <= -65) {
                goodbytes += 3;
                i += 2;
            }
        }
        if (asciibytes == rawtextlen) {
            return false;
        }
        score = 100 * goodbytes / (rawtextlen - asciibytes);
        // If not above 98, reduce to zero to prevent coincidental matches
        // Allows for some (few) bad formed sequences
        if (score > 98) {
            return true;
        } else if (score > 95 && goodbytes > 30) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 修正连接使其符合网络协议
     *
     * @param url
     * @return
     */
    public static String modifyUrl(String url) {
        if (url == null)
            return url;
        String enc = "UTF-8";
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c == '\\')
                c = '/';
            if (c > 256 || c == ' ' || c == '[' || c == ']' || c == '.' || c == '(' || c == ')') {
                try {
                    strBuffer.append(UrlCoderUtil.encode("" + c, enc));
                } catch (Exception e) {
                    strBuffer.append(UrlCoderUtil.encode("" + c));
                }
            } else {
                strBuffer.append(c);
            }
        }
        return strBuffer.toString();
    }

    /**
     * 将字节数转换为单位有MB的字符串
     *
     * @param byteLength 字节长度
     * @return
     */
    public static String parseByteToMbString(long byteLength) {
        if (byteLength <= 0) {
            return "0.00MB";
        }
        final DecimalFormat format = new DecimalFormat("#######0.00");
        long k = byteLength / 1024;
        if (k < 1024) {
            return format.format(k / 1024.00) + "MB";
        } else {
            return format.format((k / 1024 + (k % 1024) / 1024.00f)) + "MB";
        }
    }

    /**
     * 将字节数转换为单位为MB的long
     *
     * @param byteLength 字节长度
     * @return
     */
    public static long parseByteToMbStringWithTail(long byteLength) {
        if (byteLength <= 0) {
            return 0;
        }
        long k = byteLength / 1024;
        if (k < 1024) {
            return 1;
        } else {
            return 1 + (k % 1024) / 1024;
        }
    }

    /**
     * 获取IMEI码
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        String imei = "0";
        if (context == null) {
            return imei;
        }
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            if (!TextUtils.isEmpty(deviceId)) {
                imei = deviceId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    /***
     * 获取IMSI号
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getSubscriberId();
    }

    /**
     * 获取OS信息
     *
     * @return
     */
    public static String getOSInfo() {
        StringBuilder osInfo = new StringBuilder();
        osInfo.append("Build.ID=").append(Build.ID).append('\r').append('\n')
                .append("Build.DISPLAY=").append(Build.DISPLAY).append('\r').append('\n')
                .append("Build.BOARD=").append(Build.BOARD).append('\r').append('\n')
                .append("Build.BRAND=").append(Build.BRAND).append('\r').append('\n')
                .append("Build.CPU_ABI=").append(Build.CPU_ABI).append('\r').append('\n')
                .append("Build.DEVICE=").append(Build.DEVICE).append('\r').append('\n')
                .append("Build.FINGERPRINT=").append(Build.FINGERPRINT).append('\r').append('\n')
                .append("Build.HOST=").append(Build.HOST).append('\r').append('\n')
                .append("Build.MANUFACTURER=").append(Build.MANUFACTURER).append('\r').append('\n')
                .append("Build.MODEL=").append(Build.MODEL).append('\r').append('\n')
                .append("Build.PRODUCT=").append(Build.PRODUCT).append('\r').append('\n')
                .append("Build.TAGS=").append(Build.TAGS).append('\r').append('\n')
                .append("Build.TIME=").append(Build.TIME).append('\r').append('\n')
                .append("Build.TYPE=").append(Build.TYPE).append('\r').append('\n')
                .append("Build.USER=").append(Build.USER).append('\r').append('\n')
                // .append("Build.OLD_VERSION=").append(CommonSettingPrefs.getInstance().getCoverDateTemp())
                .append('\r').append('\n');
        return osInfo.toString();
    }

    /**
     * 获取手机号码
     *
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tm.getLine1Number();
        if (TextUtils.isEmpty(phoneNumber))
            return "";
        if (isPhoneNumber(phoneNumber))
            return tm.getLine1Number();
        else
            return "";
    }

    /**
     * 判断是否手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isPhoneNumber(String mobiles) {
        Pattern p = Pattern.compile("^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param edit
     */
    public static void showSoftInput(Context context, View edit) {
        if (context == null || edit == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && inputManager.isActive(edit)) {
            inputManager.showSoftInput(edit, 0);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 多少时间后显示软键盘
     */
    public static void showInputMethod(final View view, long delayMillis) {
        // 显示输入法
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                showInputMethod(view);
            }
        }, delayMillis);
    }

    /**
     * 获取应用名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            appName = context.getString(ai.labelRes);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return appName;
    }

    /**
     * 获取手机内部存储
     *
     * @param context
     * @return
     */
    public static long getInternalAvailableBlocks(Context context) {
        StatFs statInternal = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = statInternal.getBlockSize();
        long availableBlocks = statInternal.getAvailableBlocks() * blockSize;
        return availableBlocks;
    }

    /**
     * 获取AndroidManifest.xml中的meta_date值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getMetaData(Context context, String key) {
        String metaData = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            metaData = bundle.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return metaData;
    }

    /***
     * 获取运营商
     * @param context
     * @return
     */
    public static String getMobileProvidersName(Context context) {
        String providersName = null;
        String imsi = getIMSI(context);
        if (TextUtils.isEmpty(imsi)) {
            return "";
        }
        // imsi号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            providersName = "中国移动";
        } else if (imsi.startsWith("46001")) {
            providersName = "中国联通";
        } else if (imsi.startsWith("46003")) {
            providersName = "中国电信";
        }
        return providersName;
    }

    /***
     * 获取屏幕宽高
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return new int[]{display.getWidth(), display.getHeight()};
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 安装APK
     *
     * @param context
     * @param filePath APK存放路径
     */
    public static void installApk(Context context, String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * 应用是否被隐藏<在后台运行>
     *
     * @param context
     * @return
     */
    public static boolean IsRunOnBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (context.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
            return false;
        }
        return true;
    }

    /***
     * Activity是否在后台
     * @param context
     * @return
     */
    public static boolean isActivityRunOnBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (context.getClass().getName().equals(tasksInfo.get(0).topActivity.getClassName())) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否锁屏
     *
     * @return true 锁屏状态， false 非锁屏状态
     */
    public static boolean isScreenOff(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    /**
     * 去除电量显示状态栏
     *
     * @param activity
     */
    public static void hideStatusBar(Activity activity) {
        final Window win = activity.getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 去除标题栏
     *
     * @param activity
     */
    public static void hideTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

}
