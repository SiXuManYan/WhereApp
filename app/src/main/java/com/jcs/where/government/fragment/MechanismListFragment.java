package com.jcs.where.government.fragment;

import android.os.Bundle;
import android.view.View;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.MechanismPageResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.government.adapter.MechanismListAdapter;
import com.jcs.where.government.model.MechanismListModel;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.annotations.NonNull;

/**
 * 机构列表
 * create by zyf on 2020/12/28 9:07 PM
 */
public class MechanismListFragment extends BaseFragment {
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecycler;
    private MechanismListAdapter mAdapter;
    private MechanismListModel mModel;
    private int mCategoryId;
    private static final String KEY_CATEGORY_ID = "categoryId";

    public static MechanismListFragment newInstance(int categoryId) {

        Bundle args = new Bundle();
        args.putInt(KEY_CATEGORY_ID, categoryId);
        MechanismListFragment fragment = new MechanismListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        mSwipeLayout = view.findViewById(R.id.mechanismRefresh);
        mRecycler = view.findViewById(R.id.mechanismRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void initData() {
        mModel = new MechanismListModel();
        mAdapter = new MechanismListAdapter();
        mRecycler.setAdapter(mAdapter);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCategoryId = arguments.getInt(KEY_CATEGORY_ID);
            mSwipeLayout.setRefreshing(true);
            getNetData();
        }
    }

    private void getNetData() {
        mModel.getMechanismList(mCategoryId, new BaseObserver<MechanismPageResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                mSwipeLayout.setRefreshing(false);
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull MechanismPageResponse mechanismPageResponse) {
                mSwipeLayout.setRefreshing(false);
                mAdapter.getData().clear();
                List<MechanismResponse> data = mechanismPageResponse.getData();
                if (data != null && data.size() > 0) {
                    mAdapter.addData(data);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void bindListener() {
        mSwipeLayout.setOnRefreshListener(this::onSwipeRefresh);
    }

    private void onSwipeRefresh() {
        getNetData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mechanism_list;
    }
}
