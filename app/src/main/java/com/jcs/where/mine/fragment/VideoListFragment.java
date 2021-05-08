package com.jcs.where.mine.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.mine.adapter.VideoListAdapter;
import com.jcs.where.mine.model.CollectionListModel;
import com.jcs.where.news.NewsVideoActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.view.empty.EmptyView;
import com.jcs.where.widget.list.DividerDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.jzvd.Jzvd;

/**
 * 页面-收藏视频列表
 * create by zyf
 */
public class VideoListFragment extends BaseFragment implements OnLoadMoreListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private VideoListAdapter mAdapter;
    private CollectionListModel mModel;
    private boolean mIsFirst = false;
    private int page = Constant.DEFAULT_FIRST_PAGE;
    private boolean mIsLoaded = false;

    @Override
    protected void initView(View view) {
        mSwipeLayout = view.findViewById(R.id.swipeLayout);
        mRecyclerView = view.findViewById(R.id.newsRecycler);
        EmptyView emptyView = new EmptyView(getActivity());
        emptyView.showEmptyDefault();
        mAdapter = new VideoListAdapter();
        mAdapter.setEmptyView(emptyView);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mRecyclerView.addItemDecoration(getItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private RecyclerView.ItemDecoration getItemDecoration() {
        DividerDecoration itemDecoration = new DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(1), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        itemDecoration.setDrawHeaderFooter(false);
        return itemDecoration;
    }

    @Override
    protected void initData() {
        mModel = new CollectionListModel();
        if (mIsFirst && !mIsLoaded) {
            getCollectedVideoList(page);
            mIsLoaded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirst && !mIsLoaded) {
            showLoading();
            getCollectedVideoList(page);
            mIsLoaded = true;
        }
    }

    private void getCollectedVideoList(int page) {

        mModel.getCollectionVideo(page, new BaseObserver<PageResponse<CollectedResponse>>() {
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


                // ##

                boolean isLastPage = response.getLastPage() == page;
                List<CollectedResponse> data = response.getData();

//                List<CollectedResponse> newData = new ArrayList<>();
//                for (int i = 0; i < data.size(); i++) {
//
//                    Integer type = data.get(i).getType();
//
//                    // 只处理现有的三种类型
//                    if (type == 1 || type == 2 || type == 11) {
//                        newData.add(data.get(i));
//                    }
//                }


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
        mAdapter.setOnItemClickListener(this::onNewsItemClicked);
    }

    private void onNewsItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        CollectedResponse collectedResponse = mAdapter.getItem(position);
        NewsResponse item = collectedResponse.getNews();
        // 跳转到播放视频的详情页面
        toActivity(NewsVideoActivity.class, new IntentEntry("newsId", String.valueOf(item.getId())));
    }

    private void onRefreshListener() {
        page = Constant.DEFAULT_FIRST_PAGE;
        getCollectedVideoList(page);
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

    @Override
    public void onLoadMore() {
        page++;
        getCollectedVideoList(page);
    }
}
