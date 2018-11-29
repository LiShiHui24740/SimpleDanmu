# SimpleDanmu
简单弹幕库，先看一下效果:  


![image](https://github.com/LiShiHui24740/SimpleDanmu/blob/master/SimpleDanmu/app/Untitled.gif)   

该库主要是控制消息队列，当将消息添加进队列后，何时触发弹幕，如果来一个消息就触发一条弹幕可能会出现弹幕重叠，不合产品需要，例如只有当前一个弹幕的尾部完全进入屏幕后才能进行下一个弹幕，再例如直播间送特效礼物的时候，不能来一个特效礼物就播放一个，而是要保证前一个播放完成，才能进行下一个。等等诸如此类的场景很多。

使用方法:  

```
//先在gradle中添加依赖  

compile 'com.github.airland:simpledanmu:1.0.3'
```
接着布局文件中：   
```
<com.airland.simpledanmuku.widget.SimpleDanmuView
        android:id="@+id/id_sd"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true" />
```
# 简单用法
在Activity中,和使用ListView类似，首先findview然后给view设置adapter，这里的adapter要传入泛型，也就是弹幕上数据的bean对象，这个bean要继承AbstractMessage例如下面例子中的TestMessage，getView返回的则是对应的弹幕view：  
```
 SimpleDanmuView simpleDanmuView = findViewById(R.id.id_sd);
 simpleDanmuView.setMessageAdapter(new ISimpleMessageAdapter<TestMessage>() {
            @Override
            public int getRowCount() {
                return 1;//弹幕有几个轨道
            }

            @Override
            public SimpleItemBaseView getView(TestMessage testMessage) {
                //弹幕view
                SimpleItemPictureView simpleItemPictureView = new SimpleItemPictureView(MainActivity.this);
                simpleItemPictureView.getContentView().setText(testMessage.getContent());
                return simpleItemPictureView;
            }
        });
//在destory的时候调用endDealWithMessage释放资源
@Override
protected void onDestroy() {
        super.onDestroy();
        simpleDanmuView.endDealWithMessage();
 }
```
在需要发送弹幕的地方调用：  
```
simpleDanmuView.addInMessageQueue(testMessage);
```
# 自定义效果
自定义效果分为：自定义ItemView和ContainerView,如果你需要的是上面的弹幕滚动效果，只需要定义自己的ItemView即可，ContainerView可以使用SimpleDanmuView，如果你需要定义自己的滚动方式可以自定义ContainerView。
你可以自定义自己的ContainerView和弹幕中的ItemView，ContainerView需要继承SimpleBaseDanmuView，重写startItemDanmuView方法，ItemView需要继承SimpleItemBaseView，从而实现自己需要的动画效果，例如上方Gif图中，实现花椒直播欢迎条效果：
以下是一个例子
//自定义ContainerView
```
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

```
//自定义ItemView（花椒的子View也有动画实现：左上角高光闪动的星星，还有扫过整个欢迎条的高光条）：
```
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
```
然后在Activity中和上面使用方法类似：
```
SimpleWelcomeView simpleWelcomeView = findViewById(R.id.id_sw);
simpleWelcomeView.setMessageAdapter(new ISimpleMessageAdapter<TestMessage>() {
            @Override
            public int getRowCount() {
                return 1;
            }

            @Override
            public SimpleItemBaseView getView(TestMessage testMessage) {
                SimpleItemWelcomeView simpleItemWelcomeView = new SimpleItemWelcomeView(MainActivity.this);
                simpleItemWelcomeView.getNameView().setText(testMessage.getContent());
                return simpleItemWelcomeView;
            }
        });
```
# 注意点
继承SimpleBaseDanmuView的弹幕容器view除了重写startItemDanmuView（）开始动画，还需要在动画结束时候调用endItemDanmuView（）。弹幕显示下一条的时机问题，例如，只有当前一个弹幕完全进入屏幕，后一个弹幕才能进来，再或者说只有当前一个欢迎条完全离开屏幕后，下一个才能进来。这个时机是由使用者定，只需在可以显示下一个弹幕的时候调用setNextIndicator(simpleItemBaseView.rowNumber, true);在不调用的情况下默认是一条弹幕结束后下一个才会开始。

