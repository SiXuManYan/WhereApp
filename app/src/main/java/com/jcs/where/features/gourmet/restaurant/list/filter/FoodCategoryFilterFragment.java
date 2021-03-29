package com.jcs.where.features.gourmet.restaurant.list.filter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.jcs.where.R;
import com.jcs.where.api.response.area.AreaResponse;
import com.jcs.where.api.response.category.Category;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.view.empty.EmptyView;

import java.util.ArrayList;

/**
 * Created by Wangsw  2021/3/29 15:55.
 * 餐厅美食分类筛选
 */
public class FoodCategoryFilterFragment extends BaseFragment {


    private final ArrayList<Category> dataList = new ArrayList<>();
    private EmptyView mEmptyView;
    private FoodCategoryFilterAdapter mAdapter;

    public FoodCategoryFilterFragment getInstance(ArrayList<Category> dataList) {
        this.dataList.addAll(dataList);
        return new FoodCategoryFilterFragment();
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
        mAdapter = new FoodCategoryFilterAdapter();
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setNewInstance(dataList);
        contentRv.setAdapter(mAdapter);

    }

    @Override
    protected void bindListener() {

    }


}
