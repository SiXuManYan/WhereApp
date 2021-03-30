package com.jcs.where.features.gourmet.restaurant.list.filter.food;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.jcs.where.R;
import com.jcs.where.api.response.category.Category;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.view.empty.EmptyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw  2021/3/29 15:55.
 * 餐厅美食分类筛选
 */
public class FoodCategoryFilterFragment extends BaseMvpFragment<FoodCategoryFilterPresenter> implements FoodCategoryFilterView {


    private final ArrayList<Category> dataList = new ArrayList<>();
    private EmptyView mEmptyView;
    private FoodCategoryFilterAdapter mAdapter;



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
        presenter = new FoodCategoryFilterPresenter(this);
        mAdapter = new FoodCategoryFilterAdapter();
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setNewInstance(dataList);
        contentRv.setAdapter(mAdapter);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void loadOnVisible() {
        presenter.getCategoriesList();
    }

    @Override
    public void bindList(List<Category> response) {
        mAdapter.setNewInstance(response);
    }
}
