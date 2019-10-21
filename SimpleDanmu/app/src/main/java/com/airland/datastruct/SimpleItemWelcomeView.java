package com.airland.datastruct;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airland.simpledanmuku.widget.base.SimpleItemBaseView;


/**
 * @author AirLand
 * @time on 2018/10/31 18:07
 * @email lish_air@163.com
 * @jianshu https://www.jianshu.com/u/816932948905
 * @gitHub https://github.com/LiShiHui24740
 * @describe:
 */
public class SimpleItemWelcomeView extends SimpleItemBaseView {

    private ImageView headPictureView;
    private TextView contentView;
    private TextView nameView;
    private ImageView backgroundView;
    private ImageView lightStarView;
    private ImageView lightLineView;
    private ImageView levelView;
    private boolean isStart;

    public SimpleItemWelcomeView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.simple_welcome_layout, this, true);
        backgroundView = rootView.findViewById(R.id.id_img_welcome_bg);
        headPictureView = rootView.findViewById(R.id.id_civ_welcome_headpic);
        lightStarView = rootView.findViewById(R.id.id_iv_welcome_light);
        lightLineView = rootView.findViewById(R.id.id_iv_welcome_line);
        levelView = rootView.findViewById(R.id.id_img_welcome_user_level);
        nameView = rootView.findViewById(R.id.id_tv_welcome_name);
        contentView = rootView.findViewById(R.id.id_tv_welcome_content);
    }

    public ImageView getHeadPictureView() {
        return headPictureView;
    }

    public TextView getNameView() {
        return nameView;
    }

    public TextView getContentView() {
        return contentView;
    }

    public ImageView getBackgroundView() {
        return backgroundView;
    }

    public ImageView getLevelView() {
        return levelView;
    }

    public ImageView getLightStarView() {
        return lightStarView;
    }

    public void startAnimation() {
        if (!isStart){
            isStart = true;
            ObjectAnimator alpha_objectAnimator = ObjectAnimator.ofFloat(lightStarView, "alpha", 0.2f, 1.0f);
            alpha_objectAnimator.setRepeatCount(4);
            ObjectAnimator scaleX_objectAnimator = ObjectAnimator.ofFloat(lightStarView, "scaleX", 0.2f, 1.2f);
            scaleX_objectAnimator.setRepeatCount(4);
            ObjectAnimator scaleY_objectAnimator = ObjectAnimator.ofFloat(lightStarView, "scaleY", 0.2f, 1.2f);
            scaleY_objectAnimator.setRepeatCount(4);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(600);
            animatorSet.playTogether(alpha_objectAnimator, scaleX_objectAnimator, scaleY_objectAnimator);
            animatorSet.start();
            lightStarView.setVisibility(VISIBLE);
            ObjectAnimator translationX_objectAnimator = ObjectAnimator.ofFloat(lightLineView, "translationX", 0, 0.832f * getWidth() - lightLineView.getX());
            translationX_objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    lightLineView.setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            translationX_objectAnimator.setRepeatCount(1);
            translationX_objectAnimator.setDuration(1200);
            translationX_objectAnimator.start();
            lightLineView.setVisibility(VISIBLE);
        }

    }

}
