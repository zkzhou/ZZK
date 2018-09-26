
package myframelib.zkzhou.com.common.app;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.SurfaceHolder;


public class WakeLockManager {

    private static WakeLockManager mSelf = null;

    private WakeLock mWakeLock = null;

    private byte[] lock = new byte[0];

    public static WakeLockManager getInstance(Context context) {
        if (mSelf == null) {
            mSelf = new WakeLockManager();
            mSelf.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        }
        return mSelf;
    }

    /**
     * Set the low-level power management behavior for this MediaPlayer. This
     * can be used when the MediaPlayer is not playing through a SurfaceHolder
     * set with {@link #setDisplay(SurfaceHolder)} and thus can use the
     * high-level {@link #setScreenOnWhilePlaying(boolean)} feature.
     * <p/>
     * This function has the MediaPlayer access the low-level power manager
     * service to control the device's power usage while playing is occurring.
     * The parameter is a combination of {@link PowerManager} wake
     * flags. Use of this method requires
     * {@link android.Manifest.permission#WAKE_LOCK} permission. By default, no
     * attempt is made to keep the device awake during playback.
     *
     * @param context the Context to use
     * @param mode    the power/wake mode to set
     * @see PowerManager
     */
    private void setWakeMode(Context context, int mode) {
        synchronized (lock) {
            try {
                boolean washeld = false;
                if (mWakeLock != null) {
                    if (mWakeLock.isHeld()) {
                        washeld = true;
                        mWakeLock.release();
                    }
                    mWakeLock = null;
                }
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                mWakeLock = pm.newWakeLock(mode | PowerManager.ON_AFTER_RELEASE,
                        WakeLockManager.class.getName());
                mWakeLock.setReferenceCounted(false);
                if (washeld) {
                    mWakeLock.acquire();
                }
            } catch (SecurityException e) {
                // 防止某些机型没有电源管理权限
            }
        }
    }

    private int mCount = 0;

    private void stayAwake(boolean awake) {
        synchronized (lock) {
            try {
                if (mWakeLock != null) {
                    if (awake) {
                        if (mCount++ == 0 && !mWakeLock.isHeld()) {
                            mWakeLock.acquire();
                        }
                    } else {
                        if (--mCount == 0 && mWakeLock.isHeld()) {
                            mWakeLock.release();
                        }
                    }
                }
            } catch (SecurityException e) {
                // 防止某些机型没有电源管理权限
            }
        }
    }

    public void release() {
        synchronized (lock) {
            try {
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
            } catch (SecurityException e) {
                // 防止某些机型没有电源管理权限
            }
        }
    }

    public static class KGWakeLocker {

        private WakeLockManager manager;

        private boolean locked;

        public KGWakeLocker(WakeLockManager manager) {
            this.manager = manager;
        }

        public synchronized void lock() {
            if (!locked) {
                manager.stayAwake(true);
                locked = true;
            }
        }

        public synchronized void unlock() {
            if (locked) {
                manager.stayAwake(false);
                locked = false;
            }
        }
    }

    public KGWakeLocker getLocker() {
        return new KGWakeLocker(this);
    }

}
