package myframelib.zkzhou.com.common.app;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

/**
 * @auther zhouzhankun
 * @time 18/9/26 16:09
 **/

public class ActivityHandlerUtil {

    /**
     * 封装了UIhandler的常见用法
     * @param <T>
     */
    public static abstract class UIHandler<T extends Activity> extends Handler {

        public final WeakReference<T> activityReference;

        public UIHandler(T activity) {
            activityReference = new WeakReference<T>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final T activity = activityReference.get();
            if (activity == null) {
                return;
            }
            handleToMessage(activity,msg);
        }

        protected abstract void handleToMessage(T activity,Message msg);
    }

    /**
     * 封装了WorkHandler的常见用法
     * @param <T>
     */
    public static abstract class WorkHandler<T extends Activity> extends Handler{

        public final WeakReference<T> activityReference;

        public WorkHandler(T activity,Looper looper) {
            super(looper);
            activityReference = new WeakReference<T>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final T activity = activityReference.get();
            if (activity == null) {
                return;
            }
            handleToMessage(activity,msg);
        }

        protected abstract void handleToMessage(T activity,Message msg);
    }
}