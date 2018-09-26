
package myframelib.zkzhou.com.common.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class NetworkUtil {

    /**
     * 获取当前移动网络的类型（cmnet 或 cmwap 及 ctnet、ctwap、3gnet、3gwap 等）。
     * 需要注意的是，此名字可以被用户改成其它文字
     */
    public static String getCurrentMobileNetworkType(Context context) {
        String netType = "";
        try {
            ConnectivityManager connectMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectMgr != null) {
                NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mobNetInfo != null && mobNetInfo.isConnected()) {
                    netType = mobNetInfo.getExtraInfo();
                }
            }
        } catch (Exception e) {
        }
        return netType;
    }

    /**
     * 获取当前2G网络的类型（cmnet或cmwap）
     *
     * @param context
     * @return
     */
    public static String getCurrentNet2GType(Context context) {
        String net2gType = NetworkType.NET_2G_CMNET;
        try {
            ConnectivityManager connectMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectMgr != null) {
                NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mobNetInfo != null && mobNetInfo.isConnected()) {
                    if ("cmwap".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
                        net2gType = NetworkType.NET_2G_CMWAP;
                    }
                }
            }
        } catch (Exception e) {
        }
        return net2gType;
    }

    public static String getCurrentNetworkIdentifier(Context context) {
        String networkType = getNetworkType(context);
        if (NetworkType.WIFI == networkType)
            return NetworkType.WIFI;
        else if (NetworkType.UNKNOWN == networkType)
            return "";

        String netName = getCurrentMobileNetworkType(context);

        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            int netType = tm != null ? tm.getNetworkType() : 0;
            if (netType != TelephonyManager.PHONE_TYPE_NONE) {
                if (TextUtils.isEmpty(netName)) {
                    netName = "Mobile" + netType;
                } else {
                    netName += "(" + netType + ")";
                }
            }
        } catch (Exception e) {
        }

        return netName;
    }

    /**
     * 获取当前移动网络类型（xxNET 及 xxWAP 等） 。当前为 WiFi 则返回空
     */
    public static String getMobileNetworkType(Context context) {
        if (getNetworkType(context).equals(NetworkType.WIFI))
            return "";
        return getCurrentMobileNetworkType(context);
    }

    /**
     * 获取2g网络类型（cmnet或cmwap）
     *
     * @param context
     * @return
     */
    public static String getNetwork2gType(Context context) {
        String net2gType = NetworkType.NET_2G_CMNET;
        if (getNetworkType(context).equals(NetworkType.NET_2G)) {
            net2gType = getCurrentNet2GType(context);
        }
        return net2gType;
    }


    /**
     * 获取网络类型,最高支持4G网络
     *
     * @param context
     * @return 网络类型
     */
    public static String getNetworkType(Context context) {
        return getNetworkType4G(context);
    }

    public static int getNetWorkType(Context context) {
        String netWorkType = getNetworkType(context);
        if (netWorkType == NetworkType.WIFI) {
            return NetworkType.NET_WIFI_INT;
        } else if (netWorkType == NetworkType.NET_2G) {
            return NetworkType.NET_2G_INT;
        } else if (netWorkType == NetworkType.NET_3G || netWorkType == NetworkType.NET_4G) {
            return NetworkType.NET_3G_INT;
        } else {
            return NetworkType.NET_OTHER_INT;
        }
    }

    public static int getNetWorkType4G(Context context) {
        String netWorkType = getNetworkType(context);
        if (netWorkType == NetworkType.WIFI) {
            return NetworkType.NET_WIFI_INT;
        } else if (netWorkType == NetworkType.NET_2G) {
            return NetworkType.NET_2G_INT;
        } else if (netWorkType == NetworkType.NET_3G) {
            return NetworkType.NET_3G_INT;
        } else if (netWorkType == NetworkType.NET_4G) {
            return NetworkType.NET_4G_INT;

        } else {
            return NetworkType.NET_OTHER_INT;
        }
    }

    public static String getNetworkTypeForStatistics(Context context) {
        String nettype = getNetworkType(context);
        if (NetworkType.NET_2G.equals(nettype)) {
            return "1";
        } else if (NetworkType.WIFI.equals(nettype)) {
            return "2";
        } else if (NetworkType.NET_3G.equals(nettype)) {
            return "3";
        } else if (NetworkType.NET_4G.equals(nettype)) {
            return "4";
        } else {
            return "5";
        }
    }


    public static String getNetworkTypeForApm(Context context) {
        String nettype = getNetworkType(context);
        if (NetworkType.WIFI.equals(nettype)) {
            return "1";
        } else if (NetworkType.NET_2G.equals(nettype)) {
            return "2";
        } else if (NetworkType.NET_3G.equals(nettype)) {
            return "3";
        } else if (NetworkType.NET_4G.equals(nettype)) {
            return "4";
        } else {
            return "0";
        }
    }

    /**
     * 获取网络类型，最高支持4G网络.
     *
     * @param context
     * @return 网络类型
     */
    private static String getNetworkType4G(Context context) {
        ConnectivityManager cm = null;
        if (context != null) {
            try {
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cm == null) {
            return NetworkType.UNKNOWN;
        }
        NetworkInfo netInfo = null;
        try {
            netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
            // 修改崩溃 机型8185
            return NetworkType.UNKNOWN;
        }
        if (netInfo == null) {
            return NetworkType.UNKNOWN;
        }
        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.WIFI;
        }
        // 手机通过数据线连接电脑上网，网络类型当做WiFi处理。
        if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            return NetworkType.WIFI;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int netType = tm.getNetworkType();

        switch (netType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NetworkType.NET_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case 17: // Sony XPERIA 移动4G手机强指3G网络
                return NetworkType.NET_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NetworkType.NET_4G;
            default:
                Log.d("net", "getNetworkType returns a unknown value:" + netType);
                return NetworkType.NET_3G;
        }
    }

    public static String getMobileNetworkStandard(Context context) {
        ConnectivityManager cm = null;
        if (context != null) {
            try {
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cm == null) {
            return NetworkType.UNKNOWN;
        }
        NetworkInfo netInfo = null;
        try {
            netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
            // 修改崩溃 机型8185
            return NetworkType.UNKNOWN;
        }
        if (netInfo == null) {
            return NetworkType.UNKNOWN;
        }
        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.WIFI;
        }
        // 手机通过数据线连接电脑上网，网络类型当做WiFi处理。
        if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            return NetworkType.WIFI;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int netType = tm.getNetworkType();

        switch (netType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "gprs";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "edge";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "cdma";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xrtt";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iden";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "umts";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "evdo0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "evdoa";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "hsdpa";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "hsupa";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "hspa";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "evdob";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "ehrpd";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "hspap";
            case 17: // Sony XPERIA 移动4G手机强指3G网络
                return "sony(17)";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "lte";
            default:
                Log.d("Net", "getNetworkType returns a unknown value:" + netType);
                return "unknow(" + netType + ")";
        }
    }

    /**
     * 判断是不是有效的网络链接 <br/>
     * 3G，wifi，GPRS（歌词，头像）
     *
     * @param context
     * @return
     */
    public static boolean isAvalidNetSetting(Context context) {
        return !(NetworkType.UNKNOWN.endsWith(getNetworkType(context)));
    }

    /**
     * 是否cmwap
     *
     * @param context
     * @return
     */
    public static boolean isCmwap(Context context) {
        return NetworkType.NET_2G_CMWAP.equals(getNetwork2gType(context));
    }


    /**
     * 是否连网
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isAvailable());
        }
        return false;
    }

    /**
     * 是否wap
     *
     * @param context
     * @return
     */
    public static boolean isWap(Context context) {
        String net = getMobileNetworkType(context);
        if (TextUtils.isEmpty(net)) {
            return APNManager.isWap(context);
        } else if (net.toLowerCase().endsWith("net")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        return NetworkType.WIFI.equals(getNetworkType(context));
    }

    /**
     * 是否在wifi网络中
     *
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 获取手机IP地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "";
    }


    public static String getHostIP(String url) {
        String domain = getHostOfUrl(url);
        InetAddress[] ips;
        try {
            ips = InetAddress.getAllByName(domain);
            if (ips == null || ips.length <= 0)
                return "";

            return ips[0].getHostAddress();
        } catch (UnknownHostException e) {
        } catch (Exception e) {
        }
        return "";
    }

    public static String cutHTML(String html) {
        return cutHTML(html, 75);
    }

    public static String cutHTML(String html, int maxLen) {
        if (TextUtils.isEmpty(html))
            return "";

        String htmlLine = removeLines(html);
        if (htmlLine.length() < maxLen)
            return html;

        return htmlLine.substring(0, maxLen) + "...";
    }

    public static String removeLines(String html) {
        return html.replace("\r", "").replace("\n", "");
    }

    public static String timeString(long time) {
        Date now = new Date(time);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(now);
    }

    public static long timeInUNIX(long time) {
        return new Date(time).getTime() / 1000;
    }

    public static String getServerOfUrl(String url) {
        try {
            URI uri = new URI(url);
            int port = uri.getPort();
            if (port < 0) {
                return uri.getHost();
            } else {
                return uri.getHost() + ":" + port;
            }
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static String getHostFromServer(String server) {
        if (TextUtils.isEmpty(server))
            return "";

        int c = server.indexOf(':');
        if (c < 0) {
            return server;
        } else {
            return server.substring(0, c);
        }
    }

    public static String getHostOfUrl(String url) {
        if (TextUtils.isEmpty(url))
            return "";

        return getHostFromServer(getServerOfUrl(url));
    }

    public static String getFirstIP(String ips) {
        if (TextUtils.isEmpty(ips))
            return "";

        int p = ips.indexOf(',');
        if (p < 0)
            return ips;

        return ips.substring(0, p);
    }

    public static String dataToHTML(byte[] data) {
        String text = null;
        try {
            text = new String(data, "UTF-8");
        } catch (Exception e) {
            try {
                text = new String(data, "GBK");
            } catch (Exception e2) {
            }
        }

        if (!TextUtils.isEmpty(text))
            text = removeLines(text);

        return text;
    }

    public static int getNetType(Context context) {
        String netWorkType = getNetworkType(context);
        if (netWorkType == NetworkType.WIFI) {
            return NetworkType.NET_WIFI_INT;
        } else if (netWorkType == NetworkType.NET_2G) {
            return NetworkType.NET_2G_INT;
        } else if (netWorkType == NetworkType.NET_3G) {
            return NetworkType.NET_3G_INT;
        } else if (netWorkType == NetworkType.NET_4G) {
            return NetworkType.NET_4G_INT;
        } else {
            return NetworkType.NET_OTHER_INT;
        }
    }


    public static boolean isNetWork(Context context) {
        int netType = NetworkUtil.getNetType(context);
        boolean isAvalid = NetworkUtil.isAvalidNetSetting(context);
        if (isAvalid && NetworkType.NET_WIFI_INT != netType) {
            //有效运营商网络下
            return true;
        } else {
            return false;
        }
    }
}
