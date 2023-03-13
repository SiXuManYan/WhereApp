package com.jiechengsheng.city.base.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.jiechengsheng.city.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw on 2020/5/27 16:40.
 * </br>
 */
public class CommonProgressBar extends FrameLayout {


    private List<ImageView> views = new ArrayList<>();

    private AnimatorSet animatorSet;

    public CommonProgressBar(Context context) {
        super(context);
        init();
    }

    public CommonProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void assignViews() {
        ImageView iv_blue = findViewById(R.id.iv_blue);
        ImageView iv_red = findViewById(R.id.iv_red);
        views.add(iv_red);
        views.add(iv_blue);
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_commom_progress_bar, this, true);
        assignViews();
    }

    /**
     * 开始执行动画
     */
    public void startAnimator() {

        int maxRadius = SizeUtils.dp2px(4 + 6 + 4);

        // 左移
        ObjectAnimator objectAnimatorLeft = ObjectAnimator.ofFloat(views.get(0), "translationX", 0, -maxRadius, 0);
        objectAnimatorLeft.setRepeatCount(-1);
        objectAnimatorLeft.setDuration(1400);

        // 右移
        ObjectAnimator objectAnimatorRight = ObjectAnimator.ofFloat(views.get(1), "translationX", 0, maxRadius, 0);
        objectAnimatorRight.setRepeatCount(-1);
        objectAnimatorRight.setDuration(1400);

        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
        }
        animatorSet.play(objectAnimatorRight).with(objectAnimatorLeft);
        animatorSet.setInterpolator(new LinearInterpolator());

        if (!animatorSet.isRunning()) {
            animatorSet.start();
        }
    }

    public void stopAnimator() {
        if (animatorSet != null) {
            animatorSet.end();
        }
    }


    /**
     * 在View销毁时停止动画
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }


}