package com.jcs.where.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.jcs.where.R;

public abstract class BaseDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindListener();
    }

    protected abstract int getLayout();

    protected abstract int getHeight();

    protected int getWidth() {
        return 0;
    }

    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract void bindListener();


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        //dialog.setContentView(R.layout.dialog_fragment_holder);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        if (isBottom()) {
            wlp.gravity = Gravity.BOTTOM;
        }
        wlp.width = getWidth() == 0 ? WindowManager.LayoutParams.MATCH_PARENT : getDp(getWidth());
        wlp.height = getDp(getHeight());
        window.setAttributes(wlp);
        //以下三句代码等价于在theme中配置<item name="android:windowBackground">@null</item><item name="android:backgroundDimEnabled">true</item>
        window.setBackgroundDrawable(null);
        wlp.dimAmount = 0.5f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //这里可以配置弹出动画
//        window.setWindowAnimations(R.style.DataSheetAnimation);
        if (isTransparent()) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    protected boolean isWidthMatch() {
        return true;
    }

    protected boolean isTransparent() {
        return false;
    }

    protected int getDp(int height) {
        Context context = getContext();
        if (context == null) {
            return 0;
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
    }

    protected String getDialogTag() {
        return getClass().getSimpleName();
    }

    public void show(FragmentManager fm) {
        this.show(fm, getDialogTag());
    }

    protected boolean isBottom() {
        return false;
    }
}
