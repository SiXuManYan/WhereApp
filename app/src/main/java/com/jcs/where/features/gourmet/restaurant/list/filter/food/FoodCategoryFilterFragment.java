package com.jcs.where.features.gourmet.restaurant.list.filter.food;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jcs.where.R;
import com.jcs.where.api.response.category.Category;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.view.empty.EmptyView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw  2021/3/29 15:55.
 * 餐厅美食分类筛选
 */
public class FoodCategoryFilterFragment extends BaseMvpFragment<FoodCategoryFilterPresenter> implements FoodCategoryFilterView, OnItemClickListener {


    private final ArrayList<Category> dataList = new ArrayList<>();
    private EmptyView mEmptyView;
    private FoodCategoryFilterAdapter mAdapter;
    private RecyclerView contentRv;
    public int pid = 0;
    public String pidName = "";


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
        mAdapter.setOnItemClickListener(this);
        contentRv.setAdapter(mAdapter);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void loadOnVisible() {
        presenter.getCategoriesList(pid, pidName);
    }

    @Override
    public void bindList(List<Category> response) {
        mAdapter.setNewInstance(response);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        List<Category> list = mAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            Category data = list.get(i);
            if (position == i) {
                data.nativeIsSelected = true;
            } else {
                data.nativeIsSelected = false;
            }
        }
        mAdapter.notifyDataSetChanged();
        Category category = mAdapter.getData().get(position);
        EventBus.getDefault().post(new BaseEvent<>(category));
    }
}
