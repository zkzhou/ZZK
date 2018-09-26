package com.zkzhou.myframelib.uiframe.widget;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 描述:
 * 此代码由于内部引用handler容易造成内存泄漏，请用这个类的时候，一定要在界面关闭的时候调用
 * {@link AutoRunViewPager.stopRunning}.切记！切记
 *
 */
public class AutoRunViewPager extends ViewPager {

    private OnAutoRunViewPagerClickListener mOnClickListener;

    public AutoRunViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAutoRunHandler.sendEmptyMessageDelayed(AutoRunHandler.MSG_NEXT_PAGE,mAutoRunHandler.interval);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        startRunning();
    }

    private float downX;

    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
            downY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            float deltaX = Math.abs(downX - ev.getX());
            float deltaY = Math.abs(downY - ev.getY());

            if (mOnClickListener != null && deltaX < 50 && deltaY < 50) {
                View view = getChildAt(getRealPos());
                mOnClickListener.onClick(view, getRealPos());
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }

    public void setOnClickListener(OnAutoRunViewPagerClickListener listener) {
        mOnClickListener = listener;
    }

    public void setAutoRunInterval(int interval) {
        if (interval > 0) {
            mAutoRunHandler.interval = interval;
        }
    }

    public void startRunning() {
        mKeepRunning = true;
        mAutoRunHandler.sendEmptyMessageDelayed(AutoRunHandler.MSG_NEXT_PAGE,
                mAutoRunHandler.interval);
    }

    public void restartRunning() {
        mKeepRunning = true;
        mAutoRunHandler.removeMessages(AutoRunHandler.MSG_NEXT_PAGE);
        mAutoRunHandler.sendEmptyMessageDelayed(AutoRunHandler.MSG_NEXT_PAGE,
                mAutoRunHandler.interval);
    }

    public void stopRunning() {
        mKeepRunning = false;
        mAutoRunHandler.removeMessages(AutoRunHandler.MSG_NEXT_PAGE);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                stopRunning();
                break;
            }
            case MotionEvent.ACTION_UP: {
                restartRunning();
                break;
            }
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    private boolean mKeepRunning = true;

    private AutoRunHandler mAutoRunHandler = new AutoRunHandler();

    private class AutoRunHandler extends Handler {

        public static final int MSG_NEXT_PAGE = 1;

        private static final int AUTO_RUN_INTERVAL = 5000;

        public int interval = AUTO_RUN_INTERVAL;

        @Override
        public void handleMessage(Message msg) {
            PagerAdapter adapter = getAdapter();
            int pageCount = -1;
            if (adapter != null) {
                pageCount = adapter.getCount();
            }

            if (pageCount <= 0) {
                return;
            }

            switch (msg.what) {
                case MSG_NEXT_PAGE: {
                    if (null == preNextPageListener) {
                        changeNextPage(pageCount);
                        sendChangeNextPageMsg();
                        break;
                    }
                    if (preNextPageListener.canChangeNextPage()) {
                        changeNextPage(pageCount);
                    }
                    sendChangeNextPageMsg();
                    break;
                }
                default: {
                    break;
                }
            }
        }

        private void changeNextPage(int pageCount) {
            int nextPageIndex = AutoRunViewPager.this.getCurrentItem() + 1;
            nextPageIndex %= pageCount;
            // KGLog.i("BLUE", "next page is " + nextPageIndex);
            AutoRunViewPager.this.setCurrentItem(nextPageIndex);
        }

        private void sendChangeNextPageMsg() {
            removeMessages(MSG_NEXT_PAGE);
            if (mKeepRunning) {
                sendEmptyMessageDelayed(MSG_NEXT_PAGE, interval);
            }
        }
    }

    private OnPreNextPageListener preNextPageListener;

    public void setOnPreNextPageListener(final OnPreNextPageListener listener) {
        this.preNextPageListener = listener;
    }

    public interface OnPreNextPageListener {
        public boolean canChangeNextPage();
    }

    public int getRealPos() {
        if (getAdapter() instanceof InfiniteLoopViewPagerAdapter) {
            InfiniteLoopViewPagerAdapter infiniteAdapter = (InfiniteLoopViewPagerAdapter) getAdapter();
            int count = infiniteAdapter.getRealCount();
            if (count > 0)
                return getCurrentItem() % count;
        }
        return 0;
    }

    public interface OnAutoRunViewPagerClickListener {
        public void onClick(View view, int realPos);
    }
}

