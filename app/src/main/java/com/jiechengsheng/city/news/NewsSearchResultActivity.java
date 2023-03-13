package com.jiechengsheng.city.news;

import com.jiechengsheng.city.R;
import com.jiechengsheng.city.base.BaseActivity;
import com.jiechengsheng.city.news.fragment.NewsFragment;

/**
 * 搜索新闻结果页
 * create by zyf on 2021/1/26 10:54 下午
 */
public class NewsSearchResultActivity extends BaseActivity {

    public static final String K_INPUT = "input";
    private String mInput;
    private NewsFragment mNewsFragment;


    @Override
    protected void initView() {
        mNewsFragment = (NewsFragment) getSupportFragmentManager().findFragmentById(R.id.newsFragment);
    }

    @Override
    protected void initData() {
        mInput = getIntent().getStringExtra(K_INPUT);
        mJcsTitle.setMiddleTitle(mInput);
        mNewsFragment.injectSearch(mInput);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_search_result;
    }
}
