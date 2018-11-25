package com.airland.simpledanmuku.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

public class SimpleDanmuView extends SimpleBaseDanmuView implements ISimpleDanmuView {

    private boolean fromUp2Down = true;
    private int speed;
    private long itemDuration;
    private Handler mHandler;

    public SimpleDanmuView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleDanmuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleDanmuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private void initData(Context context) {
        speed = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources().getDisplayMetrics());
        mHandler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void startItemDanmuView(final SimpleItemBaseView simpleItemBaseView) {
        if (simpleItemBaseView != null && currentState == RUNNING) {
            int row = simpleItemBaseView.rowNumber;
            if (row <= rowCount) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(everyRowHeight));
                if (fromUp2Down) {
                    layoutParams.topMargin = (row - 1) * dp2px(rowDistance + everyRowHeight);
                } else {
                    layoutParams.topMargin = (rowCount - row) * dp2px(rowDistance + everyRowHeight);
                }
                simpleItemBaseView.setTranslationX(getMeasuredWidth());
                addView(simpleItemBaseView, layoutParams);
                startScrollView(simpleItemBaseView);
                super.startItemDanmuView(simpleItemBaseView);
            }
        }
    }

    @Override
    public int getState() {
        return currentState;
    }

    private void startScrollView(final SimpleItemBaseView simpleItemBaseView) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    startAnimation(simpleItemBaseView);
                }
            });
        }
    }

    private void startAnimation(final SimpleItemBaseView simpleItemBaseView) {
        int width = simpleItemBaseView.getWidth();
        int startMargin = getMeasuredWidth();
        int endMargin = -width;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) simpleItemBaseView.getLayoutParams();
        layoutParams.width = width;
        simpleItemBaseView.setLayoutParams(layoutParams);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startMargin, endMargin);
        long scroll_duration;
        if (speed != 0) {
            scroll_duration = (long) (((width + startMargin) * 1.0f / speed) * 1000 + 0.5f);
        }else{
            scroll_duration = itemDuration;
        }
        valueAnimator.setDuration(scroll_duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (currentState == RUNNING) {
                    int value = (int) animation.getAnimatedValue();
                    simpleItemBaseView.setTranslationX(value);
                    if ((value <= getMeasuredWidth() - simpleItemBaseView.getWidth() - itemDistance) && simpleItemBaseView.getTag() == null) {
                        setNextIndicator(simpleItemBaseView.rowNumber, true);
                        simpleItemBaseView.setTag(true);
                    }
                    if (animation.getAnimatedFraction() == 1) {
                        removeView(simpleItemBaseView);
                        endItemDanmuView(simpleItemBaseView);
                    }
                } else {
                    if (animation.isRunning())
                        animation.cancel();
                }
            }
        });
        valueAnimator.start();

    }

    public void setRowDistance(int rowDistance) {
        this.rowDistance = rowDistance;
    }

    public void setFromUp2Down(boolean fromUp2Down) {
        this.fromUp2Down = fromUp2Down;
    }

    public void setDanmuViewHeight(int everyRowHeight) {
        if (this.everyRowHeight != everyRowHeight) {
            this.everyRowHeight = everyRowHeight;
            requestLayout();
        }
    }

    public void setISimpleBaseViewRank(ISimpleBaseViewRank iSimpleBaseViewRank) {
        this.simpleBaseViewRankImp = simpleBaseViewRankImp;
    }

    public void setSpeed(int speedDp) {
        this.speed = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, speedDp, getContext().getResources().getDisplayMetrics());
        this.itemDuration = 0;
    }

    public void setItemDuration(long itemDuration) {
        this.itemDuration = itemDuration;
        this.speed = 0;
        setEnableOverLayer(true);
    }

    public void stop() {
        currentState = STOP;
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        removeAllViews();
    }

    public void resume() {
        currentState = RUNNING;
        mHandler = new Handler(Looper.getMainLooper());
    }

}
