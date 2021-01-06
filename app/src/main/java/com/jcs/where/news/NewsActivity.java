package com.jcs.where.news;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.news.adapter.NewsViewPagerAdapter;
import com.jcs.where.news.fragment.NewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * author : hwd
 * date   : 2021/1/6-22:54
 */

public class NewsActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NewsViewPagerAdapter newsViewPagerAdapter;
    private ArrayList<String> titleList;
    /**
     * 新闻Fragment集合
     */
    private List<NewFragment> newFragments;

    @Override
    protected void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    @Override
    protected void initData() {
        titleList = new ArrayList<>();
        newFragments = new ArrayList<>();
        titleList.add("关注");
        titleList.add("推荐");
        titleList.add("巴丹");
        titleList.add("热点");
        titleList.add("抗击肺炎");
        titleList.add("视频");
        for (int i = 0; i < titleList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
            newFragments.add(NewFragment.newInstance());
        }
        newsViewPagerAdapter = new NewsViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        newsViewPagerAdapter.setNewsFragments(newFragments);
        newsViewPagerAdapter.setTabCategories(titleList);
        viewPager.setAdapter(newsViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(newsViewPagerAdapter.getCount());
    }

    @Override
    protected void bindListener() {
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                View customView = tab.getCustomView();
//                if (customView == null) {
//                    tab.setCustomView(R.layout.tab_normal_only_text);
//                }
//                TextView textView = tab.getCustomView().findViewById(R.id.tabTitle);
//                textView.setTextAppearance( R.style.TabLayoutTextSelected);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                View customView = tab.getCustomView();
//                if (customView == null) {
//                    tab.setCustomView(R.layout.tab_normal_only_text);
//                }
//                TextView textView = tab.getCustomView().findViewById(R.id.tabTitle);
//                textView.setTextAppearance( R.style.TabLayoutTextUnSelected);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }
}
