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
import com.jcs.where.api.response.NewsChannelResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.news.NewsDetailActivity;
import com.jcs.where.news.adapter.NewsFragmentAdapter;
import com.jcs.where.news.item_decoration.NewsListItemDecoration;
import com.jcs.where.news.model.NewsFragModel;
import com.jcs.where.news.view_type.NewsType;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 新闻列表
 * author : hwd
 * date   : 2021/1/6-23:19
 */
public class NewsFragment extends BaseFragment {

    private final static String K_NEW_TAB_RESPONSE = "newTabResponse";
    private final static String K_NEW_INPUT = "input";
    private final static String K_IS_FIRST = "isFirst";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private NewsFragmentAdapter mAdapter;
    private NewsChannelResponse mTabResponse;
    private NewsFragModel mModel;
    private PageResponse<NewsResponse> mPageNews;
    private boolean mIsFirst = false;
    private boolean mIsLoaded = false;
    private String mInput;

    public static NewsFragment newInstance(NewsChannelResponse tabResponse, boolean isFirst) {
        Bundle args = new Bundle();
        args.putSerializable(K_NEW_TAB_RESPONSE, tabResponse);
        args.putBoolean(K_IS_FIRST, isFirst);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static NewsFragment newInstance(String input, boolean isFirst) {
        Bundle args = new Bundle();
        args.putSerializable(K_NEW_INPUT, input);
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
            mTabResponse = (NewsChannelResponse) arguments.getSerializable(K_NEW_TAB_RESPONSE);
            mInput = arguments.getString(K_NEW_INPUT);
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
                public void onSuccess(@NotNull PageResponse<NewsResponse> newsResponsePageResponse) {
                    stopLoading();
                    mSwipeLayout.setRefreshing(false);
                    mPageNews = newsResponsePageResponse;
                    mAdapter.getData().clear();
                    List<NewsResponse> data = newsResponsePageResponse.getData();
                    if (data.size() > 0) {
                        mAdapter.addData(data);
                    } else {
                        mAdapter.setEmptyView(R.layout.view_empty_data_brvah);
                    }
                }
            });
        }

        if (mInput != null){
            mModel.getNews(mInput, new BaseObserver<PageResponse<NewsResponse>>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                    mSwipeLayout.setRefreshing(false);
                }

                @Override
                public void onSuccess(@NotNull PageResponse<NewsResponse> newsResponsePageResponse) {
                    stopLoading();
                    mSwipeLayout.setRefreshing(false);
                    mPageNews = newsResponsePageResponse;
                    mAdapter.getData().clear();
                    List<NewsResponse> data = newsResponsePageResponse.getData();
                    if (data.size() > 0) {
                        mAdapter.addData(data);
                    } else {
                        mAdapter.setEmptyView(R.layout.view_empty_data_brvah);
                    }
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

    private void onNewsItemChildClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        if (view.getId() == R.id.newsVideoDurationView) {
            NewsResponse item = mAdapter.getItem(position);
            int itemViewType = mAdapter.getItemViewType(position);
            showToast("播放视频：" + item.getTitle());
        }
    }

    private void onNewsItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        NewsResponse item = mAdapter.getItem(position);
        int itemViewType = mAdapter.getItemViewType(position);
        if (itemViewType != NewsType.VIDEO) {
            toActivity(NewsDetailActivity.class, new IntentEntry("newsId", String.valueOf(item.getId())));
        }
    }

    private void onRefreshListener() {
        getNewsList();
    }

    public void injectSearch(String input){
        mInput = input;
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
