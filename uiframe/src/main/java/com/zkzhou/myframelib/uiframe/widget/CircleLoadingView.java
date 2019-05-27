package com.zkzhou.myframelib.uiframe.widget;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zkzhou.myframelib.uiframe.R;

import java.lang.ref.WeakReference;

/**
 * 带进度的加载动画
 */
public class CircleLoadingView extends View {

    private Paint mCirclePaint;
    private float mCircleStrokeWidth;
    private int mCircleColor;

    private Paint mArcPaint;
    private float mArcStrokeWidth;
    private int mArcColor;

    private Paint mTextPaint;
    private float mTextSize;
    private int mTextColor;
    private float mTextStrokeWidth;

    private String mProgressText;
    private boolean mDrawText;
    private int mSize;

    private RectF mRecF;
    private float mStartAngle = -90;
    private float mSweepAngle = 90;
    private float mFactor;

    private ValueAnimator animator;
    private int mDuration = 800;

    static class RotateUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        WeakReference<CircleLoadingView> weakReference;

        public RotateUpdateListener(CircleLoadingView view) {
            weakReference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (weakReference == null || weakReference.get() == null
                    || (weakReference.get().getContext() instanceof Activity
                    && ((Activity) weakReference.get().getContext()).isFinishing()))
                return;
            CircleLoadingView view = weakReference.get();
            view.mFactor = (float) animation.getAnimatedValue();
            view.invalidate();
        }
    }

    public CircleLoadingView(@NonNull Context context) {
        this(context, null);
    }

    public CircleLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);
        if (typedArray != null) {
            mCircleColor = typedArray.getColor(R.styleable.CircleLoadingView_circle_color,
                    getResources().getColor(android.R.color.white));
            mCircleStrokeWidth = typedArray.getDimension(R.styleable.CircleLoadingView_circle_stroke_width, 2);
            mArcColor = typedArray.getColor(R.styleable.CircleLoadingView_arc_color,
                    getResources().getColor(android.R.color.holo_green_light));
            mArcStrokeWidth = typedArray.getDimension(R.styleable.CircleLoadingView_arc_stroke_width, 2);
            mDrawText = typedArray.getBoolean(R.styleable.CircleLoadingView_draw_text, false);
            mTextColor = typedArray.getColor(R.styleable.CircleLoadingView_text_color,
                    getResources().getColor(android.R.color.darker_gray));
            mTextSize = typedArray.getDimension(R.styleable.CircleLoadingView_text_size, 14);
            mTextStrokeWidth = typedArray.getDimension(R.styleable.CircleLoadingView_text_stroke_width, 2);
            typedArray.recycle();
        }

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mSize = Math.min(Math.max(24, width), Math.max(24, height));
        setMeasuredDimension(mSize, mSize);
    }

    private void init() {

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeWidth(mArcStrokeWidth);
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStyle(Paint.Style.STROKE);

        if (mDrawText) {
            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setColor(mTextColor);
            mTextPaint.setStrokeWidth(mTextStrokeWidth);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
        }

        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(mDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new RotateUpdateListener(this));

    }

    public void setProgressText(String progressText) {
        this.mProgressText = progressText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1、画圆
        canvas.drawCircle(mSize / 2, mSize / 2, mSize / 2 - mCircleStrokeWidth, mCirclePaint);
        //2、画弧
        mRecF = new RectF(mArcStrokeWidth, mArcStrokeWidth, mSize - mArcStrokeWidth, mSize - mArcStrokeWidth);
        mStartAngle = -90 + 360 * mFactor;
        canvas.drawArc(mRecF, mStartAngle, mSweepAngle, false, mArcPaint);
        if (mDrawText && !TextUtils.isEmpty(mProgressText)) {
            //3、画文字
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float top = fontMetrics.top;
            float bottom = fontMetrics.bottom;
            canvas.drawText(mProgressText, mSize / 2, mRecF.centerY() + Math.abs(top + bottom) / 2, mTextPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }

    private void startLoading() {
        if (animator != null) {
            animator.start();
        }
    }

    private void stopLoading() {
        if (animator != null) {
            animator.cancel();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        boolean isVisible = ((visibility == View.VISIBLE) && (getVisibility() == View.VISIBLE));
        if (!isVisible) {
            stopLoading();
            return;
        }
        startLoading();
    }
}
