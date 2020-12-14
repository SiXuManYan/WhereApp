package com.jcs.where.category;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.ParentCategoryResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.model.CategoryModel;
import com.jcs.where.widget.JcsTitle;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;


public class CategoryFragment extends BaseFragment {

    private JcsTitle mJcsTitle;
    private TabLayout mTabLayout;
    private CategoryModel mModel;
    private List<CategoryResponse> mTabCategories;

    @Override
    protected void initView(View view) {
        mJcsTitle = view.findViewById(R.id.jcsTitle);
        mTabLayout = view.findViewById(R.id.categoryTabLayout);
        setMargins(mJcsTitle, 0, getStatusBarHeight(), 0, 0);
    }

    @Override
    protected void initData() {
        mModel = new CategoryModel();
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

            }
        });
    }

    @Override
    protected void bindListener() {

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
