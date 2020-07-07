package com.jcs.where.home;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcs.where.R;

import java.util.ArrayList;
import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;

public class HomeActivity extends BaseActivity implements View.OnClickListener{
    private ImageView icon1,icon2,icon3;
    private TextView text1,text2,text3;
    private LinearLayout tab1,tab2,tab3;
    FragmentManager fm;
    private ArrayList<Fragment> frlist = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        fullScreen(this);
        fm = getSupportFragmentManager();

        initView();
    }

    private void initView() {
        icon1 = V.f(this,R.id.icon1);
        icon2 = V.f(this,R.id.icon2);
        icon3 = V.f(this,R.id.icon3);
        text1 = V.f(this,R.id.text1);
        text2 = V.f(this,R.id.text2);
        text3 = V.f(this,R.id.text3);
        tab1 = V.f(this,R.id.tab1);
        tab1.setOnClickListener(this);
        tab2 = V.f(this,R.id.tab2);
        tab2.setOnClickListener(this);
        tab3 = V.f(this,R.id.tab3);
        tab3.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();

        frlist.add(fm.findFragmentById(R.id.home_fragment));
        frlist.add(fm.findFragmentById(R.id.order_fagment));
        frlist.add(fm.findFragmentById(R.id.mine_fragment));
        setTab(0);
    }

    private void setTab(int index) {
        FragmentTransaction trans = fm.beginTransaction();
        for (int i = 0; i < frlist.size(); i++) {
            if (i == index) {
                trans.show(frlist.get(i));
            } else {
                trans.hide(frlist.get(i));
            }
        }
        trans.commitAllowingStateLoss();
        switch (index) {
            case 0: {
                icon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_press));
                icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_order_normal));
                icon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_mine_normal));
                text1.setSelected(true);
                text2.setSelected(false);
                text3.setSelected(false);
                break;
            }
            case 1: {
                icon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_normal));
                icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_order_press));
                icon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_mine_normal));
                text1.setSelected(false);
                text2.setSelected(true);
                text3.setSelected(false);
                break;
            }
            case 2: {
                icon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_normal));
                icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_order_normal));
                icon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_mine_press));
                text1.setSelected(false);
                text2.setSelected(false);
                text3.setSelected(true);
                break;
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                setTab(0);
                break;
            case R.id.tab2:
                setTab(1);
                break;
            case R.id.tab3:
                setTab(2);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏为透明，否则在部分手机上会呈现系统默认的浅灰色
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以考虑设置为透明色
                //window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}
