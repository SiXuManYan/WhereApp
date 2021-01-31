package com.jcs.where.mine.fragment;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.NewsChannelResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.mine.adapter.ArticleListAdapter;
import com.jcs.where.mine.model.CollectionListModel;
import com.jcs.where.news.NewsDetailActivity;
import com.jcs.where.news.fragment.NewsFragment;
import com.jcs.where.news.item_decoration.NewsListItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.jzvd.Jzvd;

/**
 * 页面-收藏文章列表
 * create by zyf on 2021/1/31 7:37 下午
 */
public class ArticleListFragment extends BaseFragment {

    private final static String K_NEW_TAB_RESPONSE = "newTabResponse";
    private final static String K_NEW_INPUT = "input";
    private final static String K_IS_FIRST = "isFirst";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private ArticleListAdapter mAdapter;
    private NewsChannelResponse mTabResponse;
    private CollectionListModel mModel;
    private PageResponse<CollectedResponse> mPage;
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
        mAdapter = new ArticleListAdapter();
        mRecyclerView.addItemDecoration(new NewsListItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new CollectionListModel();
        if (mIsFirst && !mIsLoaded) {
            getArticleList();
            mIsLoaded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirst && !mIsLoaded) {
            showLoading();
            getArticleList();
            mIsLoaded = true;
        }
    }

    private void getArticleList() {
            mModel.getCollectionArticle(new BaseObserver<PageResponse<CollectedResponse>>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                    mSwipeLayout.setRefreshing(false);
                }

                @Override
                public void onSuccess(@NotNull PageResponse<CollectedResponse> pageResponse) {
                    stopLoading();
                    mSwipeLayout.setRefreshing(false);
                    mPage = pageResponse;
                    mAdapter.getData().clear();
                    List<CollectedResponse> data = pageResponse.getData();
                    if (data.size() > 0) {
                        mAdapter.addData(data);
                    } else {
                        mAdapter.setEmptyView(R.layout.view_empty_data_brvah);
                    }
                }
            });

    }

    @Override
    protected void bindListener() {
        mSwipeLayout.setOnRefreshListener(this::onRefreshListener);
        mAdapter.setOnItemClickListener(this::onArticleItemClicked);
    }

    private void onArticleItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        NewsResponse item = mAdapter.getItem(position).getNews();
        toActivity(NewsDetailActivity.class, new IntentEntry("newsId", String.valueOf(item.getId())));
    }

    private void onRefreshListener() {
        getArticleList();
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
