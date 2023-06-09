package com.jiechengsheng.city.news;

import com.jiechengsheng.city.R;
import com.jiechengsheng.city.frames.common.Html5Url;
import com.jiechengsheng.city.utils.MobUtil;

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
        return R.layout.activity_news_detail2;
    }


    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void bindListener() {
        super.bindListener();
        mJcsTitle.setFirstRightIvClickListener(v -> {
            String url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_NEWS, mNewsId);
            MobUtil.shareFacebookWebPage(url, this);
        });
    }
}
