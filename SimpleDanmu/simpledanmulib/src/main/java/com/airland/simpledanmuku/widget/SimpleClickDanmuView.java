package com.airland.simpledanmuku.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.airland.simpledanmuku.R;
import com.airland.simpledanmuku.widget.base.ISimpleBaseViewRank;
import com.airland.simpledanmuku.widget.base.ISimpleDanmuView;
import com.airland.simpledanmuku.widget.base.SimpleBaseDanmuView;
import com.airland.simpledanmuku.widget.base.SimpleItemBaseView;

public class SimpleClickDanmuView extends SimpleBaseDanmuView implements ISimpleDanmuView {

    private boolean fromUp2Down = true;
    private int speed;
    private long itemDuration;
    private boolean oneByOneEnter = true;
    private Handler mHandler;

    public SimpleClickDanmuView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleClickDanmuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleClickDanmuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleDanmuView);
        fromUp2Down = a.getBoolean(R.styleable.SimpleDanmuView_fromUp2Down, true);
        oneByOneEnter = a.getBoolean(R.styleable.SimpleDanmuView_oneByOneEnter, true);
        isEnableOverLayer = a.getBoolean(R.styleable.SimpleDanmuView_isEnableOverLayer, false);
        speed = a.getDimensionPixelSize(R.styleable.SimpleDanmuView_speed, dp2px(70));
        rowCount = a.getDimensionPixelSize(R.styleable.SimpleDanmuView_rowCount, 1);
        rowDistance = a.getDimensionPixelSize(R.styleable.SimpleDanmuView_rowDistance, dp2px(5));
        itemDistance = a.getDimensionPixelSize(R.styleable.SimpleDanmuView_itemDistance, dp2px(8));
        everyRowHeight = a.getDimensionPixelSize(R.styleable.SimpleDanmuView_everyRowHeight, dp2px(40));
        itemDuration = a.getInteger(R.styleable.SimpleDanmuView_itemDuration, 0);
        a.recycle();
        initData(context);
    }

    private void initData(Context context) {
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
                layoutParams.leftMargin = getMeasuredWidth();
                startScrollView(simpleItemBaseView);
                addView(simpleItemBaseView, layoutParams);
                super.startItemDanmuView(simpleItemBaseView);
            }
        }
    }

    @Override
    public int getState() {
        return currentState;
    }

    private void startScrollView(final SimpleItemBaseView simpleItemBaseView) {
        simpleItemBaseView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Log.d("SimpleClickDanmuView", "simpleItemBaseView.getWidth():" + simpleItemBaseView.getWidth());
                simpleItemBaseView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (mHandler != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            startAnimation(simpleItemBaseView);
                        }
                    });
                }
                return true;
            }
        });

    }

    private void startAnimation(final SimpleItemBaseView simpleItemBaseView) {
        int width = simpleItemBaseView.getWidth();
        Log.d("SimpleClickDanmuView", "width:" + width);
        int startMargin = getMeasuredWidth();
        int endMargin = -width;
        LayoutParams layoutParams = (LayoutParams) simpleItemBaseView.getLayoutParams();
        layoutParams.width = width;
        simpleItemBaseView.setLayoutParams(layoutParams);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startMargin, endMargin);
        long scroll_duration;
        if (speed != 0) {
            scroll_duration = (long) (((width + startMargin) * 1.0f / speed) * 1000 + 0.5f);
        } else {
            scroll_duration = itemDuration;
        }
        valueAnimator.setDuration(scroll_duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (currentState == RUNNING) {
                    if (simpleItemBaseView.getVisibility() != VISIBLE) {
                        simpleItemBaseView.setVisibility(VISIBLE);
                    }
                    int value = (int) animation.getAnimatedValue();
                    LayoutParams layoutParams = (LayoutParams) simpleItemBaseView.getLayoutParams();
                    layoutParams.leftMargin = value;
                    simpleItemBaseView.setLayoutParams(layoutParams);
                    if (oneByOneEnter) {
                        if ((value <= getMeasuredWidth() - simpleItemBaseView.getWidth() - itemDistance) && simpleItemBaseView.getTag() == null) {
                            setNextIndicator(simpleItemBaseView.rowNumber, true);
                            simpleItemBaseView.setTag(true);
                        }
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

    public void setOneByOneEnter(boolean oneByOneEnter) {
        this.oneByOneEnter = oneByOneEnter;
    }


    public void setRowDistance(int rowDistance) {
        if (this.rowDistance != rowDistance) {
            this.rowDistance = rowDistance;
            requestLayout();
        }

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
