package com.jiechengsheng.city.base;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.SizeUtils;
import com.jiechengsheng.city.R;

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
        wlp.width = getWidth() == 0 ? WindowManager.LayoutParams.MATCH_PARENT : SizeUtils.dp2px(getWidth());
        wlp.height = SizeUtils.dp2px(getHeight());
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


    protected boolean isTransparent() {
        return false;
    }



    protected String getDialogTag() {
        return getClass().getSimpleName();
    }



    protected boolean isBottom() {
        return false;
    }

    /**
     *  fix 外部快速点击时 ：Android java.lang.IllegalStateException: Fragment already added
     */
    public void show(FragmentManager fm) {

        String tag = getDialogTag()  ;
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment!=null) {
            return;
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(this,tag);
        fragmentTransaction.commitAllowingStateLoss();
    }


}
