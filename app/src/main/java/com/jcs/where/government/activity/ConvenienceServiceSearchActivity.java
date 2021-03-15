package com.jcs.where.government.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.government.adapter.MechanismListAdapter;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.government.model.ConvenienceServiceSearchModel;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 页面-综合服务的搜索结果页
 * create by zyf on 2021/3/11 11:12 下午
 */
public class ConvenienceServiceSearchActivity extends BaseActivity {
    private ConvenienceServiceSearchModel mModel;
    public static final String K_INPUT = "input";
    public static final String K_CATEGORY_ID = "categoryId";
    private MechanismListFragment mFragment;
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mSwipeLayout;
    private MechanismListAdapter mAdapter;
    private String mCurrentCategoryId;
    private String mSearchInput;


    public static void goTo(Activity activity, String categoryId, String input) {
        Intent intent = new Intent(activity, ConvenienceServiceSearchActivity.class);
        intent.putExtra(K_INPUT, input);
        intent.putExtra(K_CATEGORY_ID, categoryId);
        activity.startActivity(intent);
    }

    @Override
    protected void initView() {
        mSearchInput = getIntent().getStringExtra(K_INPUT);
        mCurrentCategoryId = getIntent().getStringExtra(K_CATEGORY_ID);
        mJcsTitle.setMiddleTitle(mSearchInput);
        mSwipeLayout = findViewById(R.id.mechanismRefresh);

        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MechanismListAdapter();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new ConvenienceServiceSearchModel();
        getMechanismList(mCurrentCategoryId, mSearchInput);
    }

    @Override
    protected void bindListener() {

        mSwipeLayout.setOnRefreshListener(this::onSwipeRefresh);
        mAdapter.setOnItemClickListener(this::onMechanismItemClicked);
    }

    private void onMechanismItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        int mechanismId = mAdapter.getData().get(position).getId();
        toActivity(
                MechanismDetailActivity.class,
                new IntentEntry(
                        MechanismDetailActivity.K_MECHANISM_ID,
                        String.valueOf(mechanismId)
                )
        );
    }

    private void onSwipeRefresh() {
        Log.e("MechanismListFragment", "onSwipeRefresh: " + "-----");
        getMechanismList(mCurrentCategoryId, mSearchInput);
    }

    /**
     * 根据分类id获取机构信息
     *
     * @param categoryId 分类id
     */
    private void getMechanismList(String categoryId, String searchInput) {
        mSwipeLayout.setRefreshing(true);
        Log.e("MechanismListFragment", "getMechanismList: " + "id=" + categoryId);

        mModel.getMechanismList(categoryId, searchInput, new BaseObserver<PageResponse<MechanismResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                mSwipeLayout.setRefreshing(false);
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull PageResponse<MechanismResponse> mechanismPageResponse) {
                mSwipeLayout.setRefreshing(false);
                mAdapter.getData().clear();
                List<MechanismResponse> data = mechanismPageResponse.getData();
                if (data != null && data.size() > 0) {
                    mAdapter.addData(data);
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
}
