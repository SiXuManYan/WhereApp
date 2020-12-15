package com.jcs.where.category;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.adapter.CategoryGroupAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.ParentCategoryResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.model.CategoryModel;
import com.jcs.where.widget.JcsTitle;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.annotations.NonNull;


public class CategoryFragment extends BaseFragment {

    private JcsTitle mJcsTitle;
    private TabLayout mTabLayout;
    private RecyclerView mRecycler;
    private CategoryModel mModel;
    private CategoryGroupAdapter mAdapter;
    private List<CategoryResponse> mTabCategories;
    private List<ParentCategoryResponse> mData;

    @Override

    protected void initView(View view) {
        mJcsTitle = view.findViewById(R.id.jcsTitle);
        setMargins(mJcsTitle, 0, getStatusBarHeight(), 0, 0);

        mTabLayout = view.findViewById(R.id.categoryTabLayout);
        mRecycler = view.findViewById(R.id.categoryGroupRecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        mRecycler.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        mModel = new CategoryModel();
        mAdapter = new CategoryGroupAdapter();
        mRecycler.setAdapter(mAdapter);
        mTabCategories = new ArrayList<>();
        showLoading();
        mModel.getCategories(new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull List<CategoryResponse> categoryResponses) {
                stopLoading();
                mTabCategories.clear();
                mTabCategories.addAll(categoryResponses);
                int size = categoryResponses.size();
                mTabLayout.removeAllTabs();
                for (int i = 0; i < size; i++) {
                    TabLayout.Tab tab = mTabLayout.newTab();
                    CategoryResponse categoryResponse = mTabCategories.get(i);
                    tab.setCustomView(makeTabView(categoryResponse.getName()));
                    mTabLayout.addTab(tab);
                }
            }
        });

        mModel.getParentCategory(new BaseObserver<List<ParentCategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull List<ParentCategoryResponse> parentCategoryResponses) {
                mData = parentCategoryResponses;
                mAdapter.getData().clear();
                mAdapter.addData(parentCategoryResponses);
            }
        });
    }

    @Override
    protected void bindListener() {
        mRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@androidx.annotation.NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager linearManager = (GridLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    BaseNode baseNode = mAdapter.getData().get(firstItemPosition);
                    if (baseNode instanceof ParentCategoryResponse) {
                        firstItemPosition = mData.indexOf(baseNode);
                    } else {
                        firstItemPosition = mData.indexOf(mAdapter.getItem(mAdapter.findParentNode(baseNode)));
                    }
                    mTabLayout.selectTab(mTabLayout.getTabAt(firstItemPosition));
                }
            }

            @Override
            public void onScrolled(@androidx.annotation.NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private View makeTabView(String title) {
        View tabView = LayoutInflater.from(getContext()).inflate(R.layout.tab_normal_only_text, null);
        TextView tabTitle = tabView.findViewById(R.id.tabTitle);
        tabTitle.setText(title);
        return tabView;
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }
}
