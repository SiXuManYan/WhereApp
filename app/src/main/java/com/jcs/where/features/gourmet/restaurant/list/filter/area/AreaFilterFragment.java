package com.jcs.where.features.gourmet.restaurant.list.filter.area;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.jcs.where.R;
import com.jcs.where.api.response.area.AreaResponse;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.view.empty.EmptyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw  2021/3/29 15:55.
 * 餐厅商业区筛选
 */
public class AreaFilterFragment extends BaseMvpFragment<AreaFilterPresenter> implements AreaFilterView {


    private AreaFilterAdapter mAdapter;
    private EmptyView mEmptyView;


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
        presenter = new AreaFilterPresenter(this);
        mAdapter = new AreaFilterAdapter();
        mAdapter.setEmptyView(mEmptyView);
        contentRv.setAdapter(mAdapter);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void loadOnVisible() {
        presenter.getAreasList();
    }

    @Override
    public void bindList(List<AreaResponse> response) {
        mAdapter.setNewInstance(response);
    }
}
