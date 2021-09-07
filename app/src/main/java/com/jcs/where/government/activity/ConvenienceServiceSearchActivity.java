package com.jcs.where.government.activity;

import static com.jcs.where.utils.Constant.PARAM_ID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.features.map.MechanismAdapter;
import com.jcs.where.features.mechanism.MechanismActivity;
import com.jcs.where.government.model.ConvenienceServiceSearchModel;
import com.jcs.where.utils.Constant;
import com.jcs.where.view.empty.EmptyView;
import com.jcs.where.widget.list.DividerDecoration;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 页面-综合服务的搜索结果页
 * create by zyf on 2021/3/11 11:12 下午
 */
public class ConvenienceServiceSearchActivity extends BaseActivity implements OnLoadMoreListener {
    private ConvenienceServiceSearchModel mModel;

    private RecyclerView mRecycler;
    private SwipeRefreshLayout mSwipeLayout;
    private MechanismAdapter mAdapter;
    private String mCurrentCategoryId;
    private String mSearchInput;
    private int page = Constant.DEFAULT_FIRST_PAGE;

    private EmptyView emptyView;


    public static void goTo(Activity activity, String categoryId, String input) {
        Intent intent = new Intent(activity, ConvenienceServiceSearchActivity.class);
        intent.putExtra(Constant.PARAM_NAME, input);
        intent.putExtra(Constant.PARAM_CATEGORY_ID, categoryId);
        activity.startActivity(intent);
    }

    @Override
    protected void initView() {
        mSearchInput = getIntent().getStringExtra(Constant.PARAM_NAME);
        mCurrentCategoryId = getIntent().getStringExtra(Constant.PARAM_CATEGORY_ID);
        mJcsTitle.setMiddleTitle(mSearchInput);
        mSwipeLayout = findViewById(R.id.mechanismRefresh);

        emptyView = new EmptyView(this);
        emptyView.showEmptyDefault();

        mRecycler = findViewById(R.id.recycler);
        mRecycler.addItemDecoration(new DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0));
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MechanismAdapter();
        mRecycler.setAdapter(mAdapter);

        mAdapter.setEmptyView(emptyView);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.setEmptyView(emptyView);

    }

    @Override
    protected void initData() {
        mModel = new ConvenienceServiceSearchModel();
        onSwipeRefresh();
    }

    @Override
    protected void bindListener() {

        mSwipeLayout.setOnRefreshListener(this::onSwipeRefresh);
        mAdapter.setOnItemClickListener(this::onMechanismItemClicked);
    }

    private void onMechanismItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        int mechanismId = mAdapter.getData().get(position).getId();
        Bundle b = new Bundle();
        b.putInt(PARAM_ID, mechanismId);
        startActivity(MechanismActivity.class, b);
    }

    private void onSwipeRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE;
        getMechanismList(page);
    }

    /**
     * 根据分类id获取机构信息
     */
    private void getMechanismList(int page) {

        mModel.getMechanismList(page, mCurrentCategoryId, mSearchInput, new BaseObserver<PageResponse<MechanismResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                mSwipeLayout.setRefreshing(false);
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull PageResponse<MechanismResponse> response) {
                mSwipeLayout.setRefreshing(false);
                List<MechanismResponse> data = response.getData();
                boolean isLastPage = response.getLastPage() == page;

                BaseLoadMoreModule loadMoreModule = mAdapter.getLoadMoreModule();
                if (data.isEmpty()) {
                    if (page == Constant.DEFAULT_FIRST_PAGE) {
                        mAdapter.setNewInstance(null);
                        loadMoreModule.loadMoreComplete();
                        emptyView.showEmptyDefault();
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
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_convenience_service_search_result;
    }

    @Override
    public void onLoadMore() {
        page++;
        getMechanismList(page);
    }
}
