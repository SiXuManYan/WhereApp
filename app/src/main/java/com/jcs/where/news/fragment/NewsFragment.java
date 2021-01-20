package com.jcs.where.news.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.jzvd.Jzvd;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.NewsTabResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.news.adapter.NewsFragmentAdapter;
import com.jcs.where.news.item_decoration.NewsListItemDecoration;
import com.jcs.where.news.model.NewsFragModel;

import org.jetbrains.annotations.NotNull;

/**
 * 新闻列表
 * author : hwd
 * date   : 2021/1/6-23:19
 */
public class NewsFragment extends BaseFragment {

    private final static String K_NEW_TAB_RESPONSE = "newTabResponse";
    private final static String K_IS_FIRST = "isFirst";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private NewsFragmentAdapter mAdapter;
    private NewsTabResponse mTabResponse;
    private NewsFragModel mModel;
    private PageResponse<NewsResponse> mPageNews;
    private boolean mIsFirst = false;
    private boolean mIsLoaded = false;

    public static NewsFragment newInstance(NewsTabResponse tabResponse, boolean isFirst) {
        Bundle args = new Bundle();
        args.putSerializable(K_NEW_TAB_RESPONSE, tabResponse);
        args.putBoolean(K_IS_FIRST, isFirst);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        mSwipeLayout = view.findViewById(R.id.swipeLayout);
        mRecyclerView = view.findViewById(R.id.newsRecycler);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTabResponse = (NewsTabResponse) arguments.getSerializable(K_NEW_TAB_RESPONSE);
            mIsFirst = arguments.getBoolean(K_IS_FIRST);
        }
        mAdapter = new NewsFragmentAdapter();
        mRecyclerView.addItemDecoration(new NewsListItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new NewsFragModel();
        if (mIsFirst && !mIsLoaded) {
            getNewsList();
            mIsLoaded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirst && !mIsLoaded) {
            showLoading();
            getNewsList();
            mIsLoaded = true;
        }
    }

    private void getNewsList() {
        if (mTabResponse != null) {
            mModel.getNews(mTabResponse.getId(), new BaseObserver<PageResponse<NewsResponse>>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                    mSwipeLayout.setRefreshing(false);
                }

                @Override
                public void onNext(@NotNull PageResponse<NewsResponse> newsResponsePageResponse) {
                    stopLoading();
                    mPageNews = newsResponsePageResponse;
                    mAdapter.getData().clear();
                    mAdapter.addData(newsResponsePageResponse.getData());
                    mSwipeLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    protected void bindListener() {
        mAdapter.addChildClickViewIds(R.id.newsVideoDurationView);
        mSwipeLayout.setOnRefreshListener(this::onRefreshListener);
        mAdapter.setOnItemClickListener(this::onNewsItemClicked);
        mAdapter.setOnItemChildClickListener(this::onNewsItemChildClicked);
    }

    private void onNewsItemChildClicked(BaseQuickAdapter baseQuickAdapter, View view, int position) {
        if (view.getId() == R.id.newsVideoDurationView) {
            NewsResponse item = mAdapter.getItem(position);
            int itemViewType = mAdapter.getItemViewType(position);
            showToast("播放视频：" + item.getTitle());
        }
    }

    private void onNewsItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        NewsResponse item = mAdapter.getItem(position);
        int itemViewType = mAdapter.getItemViewType(position);
        showToast("跳转到新闻详情页：type=" + itemViewType);
    }

    private void onRefreshListener() {
        getNewsList();
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragemnt_news;
    }
}
