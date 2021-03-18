package com.jcs.where.features.splash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.StringUtils;
import com.jcs.where.R;

/**
 * Created by Wangsw  2021/3/18 16:42.
 */
public class SplashAdapter extends PagerAdapter {

    private TextView titleTv;
    private TextView contentTv;
    private ImageView bgIv;
    private TextView startTv;
    public View.OnClickListener onStartClickListener;

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_splash, container, false);
        titleTv = view.findViewById(R.id.title_tv);
        contentTv = view.findViewById(R.id.content_tv);
        bgIv = view.findViewById(R.id.bg_iv);
        startTv = view.findViewById(R.id.start_tv);
        initView(position);


        container.addView(view);
        return view;


    }

    private void initView(int position) {
        String[] title = StringUtils.getStringArray(R.array.splash_item_title);
        String[] content = StringUtils.getStringArray(R.array.splash_item_content);
        titleTv.setText(title[position]);
        contentTv.setText(content[position]);
        switch (position) {
            case 0:
                bgIv.setImageResource(R.mipmap.ic_splash_bg_00);
                startTv.setVisibility(View.GONE);
                break;
            case 1:
                bgIv.setImageResource(R.mipmap.ic_splash_bg_01);
                startTv.setVisibility(View.GONE);
                break;
            case 2:
                bgIv.setImageResource(R.mipmap.ic_splash_bg_02);
                startTv.setVisibility(View.VISIBLE);
                break;
        }

        if (onStartClickListener != null) {
            startTv.setOnClickListener(onStartClickListener);
        }
    }

}
