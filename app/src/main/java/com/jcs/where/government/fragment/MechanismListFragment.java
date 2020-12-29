package com.jcs.where.government.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismPageResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.government.adapter.MechanismListAdapter;
import com.jcs.where.government.model.MechanismListModel;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
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
    private RadioGroup mRadioGroup;
    private MechanismListAdapter mAdapter;
    private MechanismListModel mModel;
    private CategoryResponse mCategoryResponse;
    private List<CategoryResponse> mChildCategorys;
    private static final String KEY_CATEGORY_RESPONSE = "categoryResponse";

    public static MechanismListFragment newInstance(CategoryResponse category) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_CATEGORY_RESPONSE, category);
        MechanismListFragment fragment = new MechanismListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        mSwipeLayout = view.findViewById(R.id.mechanismRefresh);
        mRecycler = view.findViewById(R.id.mechanismRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRadioGroup = view.findViewById(R.id.mechanismRadioGroup);
    }

    @Override
    protected void initData() {
        mModel = new MechanismListModel();
        mAdapter = new MechanismListAdapter();
        mRecycler.setAdapter(mAdapter);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mChildCategorys = new ArrayList<>();
            mCategoryResponse = (CategoryResponse) arguments.getSerializable(KEY_CATEGORY_RESPONSE);
            mSwipeLayout.setRefreshing(true);
            getMechanismList();
            getChildCategory();
        }
    }

    private void getChildCategory() {
        mModel.getChildCategories(mCategoryResponse.getType(),mCategoryResponse.getId(), new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull List<CategoryResponse> categoryResponses) {
                RadioButton allRadio = (RadioButton) mRadioGroup.getChildAt(0);
                mChildCategorys.clear();
                mChildCategorys.addAll(categoryResponses);
                if (categoryResponses != null && categoryResponses.size() > 0) {
                    for (int i = 0; i < categoryResponses.size(); i++) {
                        RadioButton temp = new RadioButton(getContext());
                        temp.setButtonDrawable(null);
                        temp.setBackgroundResource(R.drawable.selector_mechanism_child_radio);
                        temp.setText(categoryResponses.get(i).getName());
                        temp.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.selector_333_666));
                        temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                        temp.setLayoutParams(allRadio.getLayoutParams());
                        temp.setId(i);
                        temp.setPadding(allRadio.getPaddingLeft(), allRadio.getPaddingTop(), allRadio.getPaddingRight(), allRadio.getPaddingBottom());
                        mRadioGroup.addView(temp, mRadioGroup.getChildCount());
                    }

                }
            }
        });
    }

    private void getMechanismList() {
        mModel.getMechanismList(mCategoryResponse.getId(), new BaseObserver<MechanismPageResponse>() {
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
        getMechanismList();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mechanism_list;
    }
}
