package com.airland.simpledanmuku.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class SimpleCircleImageView extends ImageView {
    private Paint mPaint;
    private Bitmap mBitmap;
    private int mSize;
    private int centerX;
    private int centerY;
    private int radius;

    public SimpleCircleImageView(Context context) {
        this(context, null);
    }

    public SimpleCircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleCircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(255);
        //PorterDuffXfermode使用时候要去掉硬件加速,避免黑色背景
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width_mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height_mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mSize = Math.min(width, height);
        int mode;
        if (width_mode != MeasureSpec.UNSPECIFIED || height_mode != MeasureSpec.UNSPECIFIED) {
            mode = MeasureSpec.EXACTLY;
        } else {
            mode = MeasureSpec.UNSPECIFIED;
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(mSize, mode), MeasureSpec.makeMeasureSpec(mSize, mode));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //祛除padding造成的影响，根据padding来调整圆心和半径
        centerX = w / 2 + getPaddingLeft() - getPaddingRight();
        centerY = h / 2 + getPaddingTop() - getPaddingBottom();
        int radiusX = (w - getPaddingLeft() - getPaddingRight()) / 2;
        int radiusY = (h - getPaddingTop() - getPaddingBottom()) / 2;
        radius = Math.min(radiusX, radiusY);
        //半径减去2避免太靠边缘
        createBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    private void createBitmap() {
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mBitmap);
            canvas.drawCircle(centerX, centerY,  radius, mPaint);
            canvas.setBitmap(null);
        }
    }


}

