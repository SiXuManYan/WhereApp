package com.jiechengsheng.city.news.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.ErrorResponse;
import com.jiechengsheng.city.api.response.NewsChannelResponse;
import com.jiechengsheng.city.api.response.NewsResponse;
import com.jiechengsheng.city.api.response.PageResponse;
import com.jiechengsheng.city.base.BaseFragment;
import com.jiechengsheng.city.base.IntentEntry;
import com.jiechengsheng.city.features.news.NewsAdapter;
import com.jiechengsheng.city.news.NewsDetailActivity;
import com.jiechengsheng.city.news.NewsVideoActivity;
import com.jiechengsheng.city.news.item_decoration.NewsListItemDecoration;
import com.jiechengsheng.city.news.model.NewsFragModel;
import com.jiechengsheng.city.news.view_type.NewsType;
import com.jiechengsheng.city.utils.Constant;
import com.jiechengsheng.city.view.empty.EmptyView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.jzvd.Jzvd;

/**
 * 页面-新闻列表
 * create by zyf
 */
public class NewsFragment extends BaseFragment implements OnLoadMoreListener {

    private final static String K_NEW_TAB_RESPONSE = "newTabResponse";
    private final static String K_NEW_INPUT = "input";
    private final static String K_IS_FIRST = "isFirst";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private NewsAdapter mAdapter;
    private NewsChannelResponse mTabResponse;
    private NewsFragModel mModel;
    private PageResponse<NewsResponse> mPageNews;
    private boolean mIsFirst = false;
    private boolean mIsLoaded = false;
    private String mInput;
    private int page = Constant.DEFAULT_FIRST_PAGE;

    private EmptyView emptyView ;

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


        emptyView = new EmptyView(getContext());
        emptyView.showEmptyDefault();

        mAdapter = new NewsAdapter();
        mAdapter.setEmptyView(emptyView);
        BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
        loadMoreModule.setAutoLoadMore(true);
        loadMoreModule.setEnableLoadMoreIfNotFullPage(false);
        loadMoreModule.setOnLoadMoreListener(this);

        mRecyclerView.addItemDecoration(new NewsListItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new NewsFragModel();
        if (mIsFirst && !mIsLoaded) {
            getNewsList(page);
            mIsLoaded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirst && !mIsLoaded) {
            getNewsList(page);
            mIsLoaded = true;
        }
    }

    private void getNewsList(int page) {
        if (mTabResponse != null) {
            mModel.getNews(mTabResponse.getId(), null, page, new BaseObserver<PageResponse<NewsResponse>>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    mSwipeLayout.setRefreshing(false);
                }

                @Override
                public void onSuccess(@NotNull PageResponse<NewsResponse> response) {
                    List<NewsResponse> data = response.getData();
                    boolean lastPage = response.getLastPage() == page;
                    mSwipeLayout.setRefreshing(false);
                    BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
                    if (data.isEmpty()) {
                        if (page == Constant.DEFAULT_FIRST_PAGE) {
                            loadMoreModule.loadMoreComplete();
                            emptyView.showEmptyContainer();
                        } else {
                            loadMoreModule.loadMoreEnd();
                        }
                        return;
                    }
                    if (page == Constant.DEFAULT_FIRST_PAGE) {
                        mAdapter.setNewInstance(data);
                        loadMoreModule.checkDisableLoadMoreIfNotFullPage();
                    } else {
                        mAdapter.addData(data);
                        if (lastPage) {
                            loadMoreModule.loadMoreEnd();
                        } else {
                            loadMoreModule.loadMoreComplete();
                        }
                    }

                }
            });
        }

        if (mInput != null) {
            mModel.getNews(null, mInput, page, new BaseObserver<PageResponse<NewsResponse>>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    mSwipeLayout.setRefreshing(false);
                }

                @Override
                public void onSuccess(@NotNull PageResponse<NewsResponse> response) {
                    List<NewsResponse> data = response.getData();
                    boolean lastPage = response.getLastPage() == page;
                    mSwipeLayout.setRefreshing(false);
                    BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
                    if (data.isEmpty()) {
                        if (page == Constant.DEFAULT_FIRST_PAGE) {
                            loadMoreModule.loadMoreComplete();
                        } else {
                            loadMoreModule.loadMoreEnd();
                        }
                        return;
                    }
                    if (page == Constant.DEFAULT_FIRST_PAGE) {
                        mAdapter.setNewInstance(data);
                        loadMoreModule.checkDisableLoadMoreIfNotFullPage();
                    } else {
                        mAdapter.addData(data);
                        if (lastPage) {
                            loadMoreModule.loadMoreEnd();
                        } else {
                            loadMoreModule.loadMoreComplete();
                        }
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
        // 这部分是适配蓝湖上的UI，但是不咋好写，时间原因先搁置
        if (view.getId() == R.id.newsVideoDurationView) {
            NewsResponse item = mAdapter.getItem(position);
            int itemViewType = mAdapter.getItemViewType(position);
        }
    }

    private void onNewsItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        NewsResponse item = mAdapter.getItem(position);
        int itemViewType = mAdapter.getItemViewType(position);
        if (itemViewType != NewsType.VIDEO) {
            toActivity(NewsDetailActivity.class, new IntentEntry(Constant.PARAM_NEWS_ID, String.valueOf(item.getId())));
        } else {
            // 跳转到播放视频的详情页面
            toActivity(NewsVideoActivity.class, new IntentEntry(Constant.PARAM_NEWS_ID, String.valueOf(item.getId())));
        }
    }

    private void onRefreshListener() {
        page = Constant.DEFAULT_FIRST_PAGE;
        getNewsList(page);
    }

    public void injectSearch(String input) {
        mInput = input;
        page = 0;
        getNewsList(page);
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
        getNewsList(page);
    }
}
