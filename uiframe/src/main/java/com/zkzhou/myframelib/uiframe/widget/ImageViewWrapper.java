package com.zkzhou.myframelib.uiframe.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @auther zhouzhankun
 * @time 18/9/26 15:52
 **/

public class ImageViewWrapper extends ImageView {

    private int mSrcResouceId = 0;

    private int mBackDrawableResouceId = 0;

    public ImageViewWrapper(Context context) {
        super(context);
    }

    public ImageViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBackDrawableResouceId(int backDrawableResouceId) {
        this.mBackDrawableResouceId = backDrawableResouceId;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable d = getDrawable();
        if (d != null && d instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            if (bitmap != null && bitmap.isRecycled()) {
                if (mSrcResouceId != 0) {
                    setImageResource(mSrcResouceId);
                } else {
                    setImageDrawable(null);
                }
            }
        }
        try {
            super.onDraw(canvas);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Drawable d = getBackground();
        if (d != null && d instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            if (bitmap != null && bitmap.isRecycled()) {
                if (mBackDrawableResouceId != 0) {
                    setBackgroundResource(mBackDrawableResouceId);
                } else {
                    setBackgroundDrawable(null);
                }
            }
        }
        try {
            super.draw(canvas);
        } catch (RuntimeException e) {

        }
    }

    @Override
    public void setImageResource(int resId) {
        try {
            super.setImageResource(resId);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    public void setDefaultImageResource(int resId) {
        mSrcResouceId = resId;
    }