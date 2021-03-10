package com.jcs.where.mine.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.mine.adapter.VideoListAdapter;
import com.jcs.where.mine.model.CollectionListModel;
import com.jcs.where.news.NewsVideoActivity;
import com.jcs.where.news.item_decoration.NewsListItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.jzvd.Jzvd;

/**
 * 页面-收藏视频列表
 * create by zyf
 */
public class VideoListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private VideoListAdapter mAdapter;
    private CollectionListModel mModel;
    private boolean mIsFirst = false;
    private boolean mIsLoaded = false;

    @Override
    protected void initView(View view) {
        mSwipeLayout = view.findViewById(R.id.swipeLayout);
        mRecyclerView = view.findViewById(R.id.newsRecycler);
        mAdapter = new VideoListAdapter();
        mRecyclerView.addItemDecoration(new NewsListItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new CollectionListModel();
        if (mIsFirst && !mIsLoaded) {
            getCollectedVideoList();
            mIsLoaded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirst && !mIsLoaded) {
            showLoading();
            getCollectedVideoList();
            mIsLoaded = true;
        }
    }

    private void getCollectedVideoList() {

        mModel.getCollectionVideo(new BaseObserver<List<CollectedResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(@NotNull List<CollectedResponse> pageResponse) {
                stopLoading();

                mSwipeLayout.setRefreshing(false);
                mAdapter.getData().clear();
                if (pageResponse != null && pageResponse.size() > 0) {
                    mAdapter.addData(pageResponse);
                } else {
                    mAdapter.setEmptyView(R.layout.view_empty_data_brvah);
                }
            }
        });
    }

    @Override
    protected void bindListener() {
        mSwipeLayout.setOnRefreshListener(this::onRefreshListener);
        mAdapter.setOnItemClickListener(this::onNewsItemClicked);
    }

    private void onNewsItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        CollectedResponse collectedResponse = mAdapter.getItem(position);
        NewsResponse item = collectedResponse.getNews();
        // 跳转到播放视频的详情页面
        toActivity(NewsVideoActivity.class, new IntentEntry("newsId", String.valueOf(item.getId())));
    }

    private void onRefreshListener() {
        getCollectedVideoList();
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collected_video_list;
    }
}
