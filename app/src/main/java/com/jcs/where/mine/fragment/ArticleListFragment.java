package com.jcs.where.mine.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
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
import com.jcs.where.utils.Constant;
import com.jcs.where.view.empty.EmptyView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

/**
 * 页面-收藏文章列表
 * create by zyf on 2021/1/31 7:37 下午
 */
public class ArticleListFragment extends BaseFragment implements OnLoadMoreListener {

    private final static String K_NEW_TAB_RESPONSE = "newTabResponse";
    private final static String K_NEW_INPUT = "input";
    private final static String K_IS_FIRST = "isFirst";

    private int page = Constant.DEFAULT_FIRST_PAGE;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private ArticleListAdapter mAdapter;
    private NewsChannelResponse mTabResponse;
    private CollectionListModel mModel;
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

        EmptyView emptyView = new EmptyView(getActivity());
        emptyView.showEmptyDefault();
        mAdapter = new ArticleListAdapter();

        mAdapter.setEmptyView(emptyView);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mRecyclerView.addItemDecoration(new NewsListItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new CollectionListModel();
        if (mIsFirst && !mIsLoaded) {
            getArticleList(page);
            mIsLoaded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirst && !mIsLoaded) {
            showLoading();
            getArticleList(page);
            mIsLoaded = true;
        }
    }

    private void getArticleList(int page) {
        mModel.getCollectionArticle(page, new BaseObserver<PageResponse<CollectedResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(@NotNull PageResponse<CollectedResponse> response) {
                stopLoading();
                mSwipeLayout.setRefreshing(false);

                boolean isLastPage = response.getLastPage() == page;
                List<CollectedResponse> data = response.getData();

                List<CollectedResponse> newData = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {

                    Integer type = data.get(i).getType();

                    // 只处理现有的三种类型
                    if (type == 1 || type == 2 || type == 3) {
                        newData.add(data.get(i));
                    }
                }

                BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
                if (newData.isEmpty()) {
                    if (page == Constant.DEFAULT_FIRST_PAGE) {
                        loadMoreModule.loadMoreComplete();
                    } else {
                        loadMoreModule.loadMoreEnd();
                    }
                    return;
                }
                if (page == Constant.DEFAULT_FIRST_PAGE) {
                    mAdapter.setNewInstance(newData);
                    loadMoreModule.checkDisableLoadMoreIfNotFullPage();
                } else {
                    mAdapter.addData(newData);
                    if (isLastPage) {
                        loadMoreModule.loadMoreEnd();
                    } else {
                        loadMoreModule.loadMoreComplete();
                    }
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
        page = Constant.DEFAULT_FIRST_PAGE;
        getArticleList(page);
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

    @Override
    public void onLoadMore() {
        page++;
        getArticleList(page);
    }
}
