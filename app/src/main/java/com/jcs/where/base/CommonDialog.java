package com.jcs.where.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.jcs.where.R;

/**
 * Created by Wangsw  2021/3/12 11:56.
 */
public abstract class CommonDialog extends Dialog {

    public CommonDialog(@NonNull Context context) {
        super(context , R.style.dialog_theme);
        initView();
    }


    private void initView() {
        // 动画
        getWindow().setWindowAnimations(R.style.dialog_animation);
        View view = getLayoutInflater().inflate(getLayoutId(), null);
        setContentView(view);
    }

    abstract int getLayoutId() ;


}
