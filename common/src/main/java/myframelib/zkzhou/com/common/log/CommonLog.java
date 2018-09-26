package myframelib.zkzhou.com.common.log;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by liucg on 15/11/15.
 */
public class CommonLog {

    public static final String TAG = "CommonLog";

    public static boolean ISDEBUG = true;

    public static void d(String tag, String msg) {
        if (!ISDEBUG) {
            return;
        }
        Log.d(tag, msg);
    }

    public static void d(String msg) {
        if (!ISDEBUG) {
            return;
        }
        d(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (!ISDEBUG) {
            return;
        }
        Log.e(tag, msg);
    }

    public static void e(String msg) {
        if (!ISDEBUG) {
            return;
        }
        e(TAG, msg);
    }

    public static void s(String tag, String msg) {
        if (!ISDEBUG) {
            return;
        }
        e(tag, msg);
        iLFCurrentStack(tag);
    }

    /**
     * 打印：当前堆栈信息
     *
     * @param tag
     */
    public static void iLFCurrentStack(String tag) {
        if (!ISDEBUG) {
            return;
        }
        String stack = Log.getStackTraceString(new RuntimeException("KGLog_StackTrace"));
        Log.i(tag, stack);
    }

    public static void printException(String tag, Throwable e) {
        if (!ISDEBUG) {
            return;
        }
        if (e == null || tag == null)
            return;
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        Log.e(tag, stringWriter.toString());
        e.printStackTrace();
    }

}
