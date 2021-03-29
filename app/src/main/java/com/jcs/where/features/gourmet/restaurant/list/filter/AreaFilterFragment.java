package com.jcs.where.features.gourmet.restaurant.list.filter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.jcs.where.R;
import com.jcs.where.api.response.area.AreaResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.view.empty.EmptyView;

import java.util.ArrayList;

/**
 * Created by Wangsw  2021/3/29 15:55.
 * 餐厅商业区筛选
 */
public class AreaFilterFragment extends BaseFragment {


    private final ArrayList<AreaResponse> dataList = new ArrayList<>();
    private AreaFilterAdapter mAdapter;
    private EmptyView mEmptyView;

    public AreaFilterFragment getInstance(ArrayList<AreaResponse> dataList) {
        this.dataList.addAll(dataList);
        return new AreaFilterFragment();
    }

    private RecyclerView contentRv;

    @Override
    protected int getLayoutId() {
        return R.layout.single_recycler_view;
    }

    @Override
    protected void initView(View view) {
        contentRv = view.findViewById(R.id.content_rv);
        mEmptyView = new EmptyView(requireContext());
    }

    @Override
    protected void initData() {
        mAdapter = new AreaFilterAdapter();
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setNewInstance(dataList);
        contentRv.setAdapter(mAdapter);
    }

    @Override
    protected void bindListener() {

    }


}
