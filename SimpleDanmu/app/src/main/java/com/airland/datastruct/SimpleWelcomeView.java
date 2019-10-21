package com.airland.datastruct;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.airland.simpledanmuku.widget.base.SimpleBaseDanmuView;
import com.airland.simpledanmuku.widget.base.SimpleItemBaseView;

/**
 * @author AirLand
 * @time on 2018/11/7 16:40
 * @email lish_air@163.com
 * @jianshu https://www.jianshu.com/u/816932948905
 * @gitHub https://github.com/LiShiHui24740
 * @describe:
 */
public class SimpleWelcomeView extends SimpleBaseDanmuView {
    private Handler mHandler;

    public SimpleWelcomeView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleWelcomeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleWelcomeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        everyRowHeight = 30;
        mHandler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void startItemDanmuView(final SimpleItemBaseView simpleItemBaseView) {
        super.startItemDanmuView(simpleItemBaseView);
        if (simpleItemBaseView != null && currentState == RUNNING) {
            int row = simpleItemBaseView.getRowNumber();
            if (row <= rowCount) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(everyRowHeight));
                addView(simpleItemBaseView, layoutParams);
                simpleItemBaseView.setTranslationX(getMeasuredWidth());
                startScrollView(simpleItemBaseView);
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
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator valueAnimator_one = ValueAnimator.ofInt(startMargin, 50);
        ValueAnimator valueAnimator_two = ValueAnimator.ofInt(50, 10);
        ValueAnimator valueAnimator_three = ValueAnimator.ofInt(10, endMargin);
        valueAnimator_one.setDuration(300);
        valueAnimator_two.setDuration(3000);
        valueAnimator_three.setDuration(300);
        valueAnimator_one.setInterpolator(new AccelerateInterpolator());
        valueAnimator_two.setInterpolator(new LinearInterpolator());
        valueAnimator_three.setInterpolator(new AccelerateInterpolator());
        valueAnimator_one.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateLeft(simpleItemBaseView, animation, false);
            }
        });
        valueAnimator_two.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                SimpleItemWelcomeView simpleItemWelcomeView = (SimpleItemWelcomeView) simpleItemBaseView;
                simpleItemWelcomeView.startAnimation();
                updateLeft(simpleItemBaseView, animation, false);
            }
        });
        valueAnimator_three.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateLeft(simpleItemBaseView, animation, true);
            }
        });
        animatorSet.playSequentially(valueAnimator_one, valueAnimator_two, valueAnimator_three);
        animatorSet.start();
    }

    private void updateLeft(SimpleItemBaseView simpleItemBaseView, ValueAnimator animation, boolean isRemove) {
        if (currentState == RUNNING) {
            int value = (int) animation.getAnimatedValue();
            simpleItemBaseView.setTranslationX(value);
            if (isRemove && animation.getAnimatedFraction() == 1) {
                removeView(simpleItemBaseView);
                endItemDanmuView(simpleItemBaseView);
            }
        } else {
            if (animation.isRunning())
                animation.cancel();
        }
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
