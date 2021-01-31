package com.jcs.where.news;

import com.jcs.where.R;

/**
 * 新闻详情页面（不是展示视频的页面）
 * create by zyf on 2021/1/24 11:58 下午
 */
public class NewsDetailActivity extends BaseNewsDetailActivity {

    @Override
    protected void dealDiff() {
    }

    @Override
    protected int getUncollectedIcon() {
        return R.mipmap.ic_uncollected_black;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }
}
