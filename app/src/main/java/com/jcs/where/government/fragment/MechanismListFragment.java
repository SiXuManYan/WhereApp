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
    private List<CategoryResponse> mChildCategories;
    /**
     * 第一次展示时获取网络数据
     * 此 tag 表示是否已经获取过网络数据
     */
    private boolean mIsDataLoaded = false;
    /**
     * 当前展示的数据是属于哪个分类的
     */
    private int mCurrentCategoryId;
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

    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (!mIsDataLoaded && arguments != null) {
            mChildCategories = new ArrayList<>();
            mCategoryResponse = (CategoryResponse) arguments.getSerializable(KEY_CATEGORY_RESPONSE);
            getMechanismList(mCategoryResponse.getId());
            getChildCategory();
            mIsDataLoaded = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {

        }
    }

    /**
     * 获得当前分类下的子分类
     */
    private void getChildCategory() {
        mModel.getChildCategories(mCategoryResponse.getType(), mCategoryResponse.getId(), new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull List<CategoryResponse> categoryResponses) {
                RadioButton allRadio = (RadioButton) mRadioGroup.getChildAt(0);
                mChildCategories.clear();
                mChildCategories.addAll(categoryResponses);
                if (categoryResponses != null) {
                    int size = categoryResponses.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            RadioButton temp = new RadioButton(getContext());
                            CategoryResponse categoryResponse = categoryResponses.get(i);
                            temp.setButtonDrawable(null);
                            temp.setBackgroundResource(R.drawable.selector_mechanism_child_radio);
                            temp.setText(categoryResponse.getName());
                            temp.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.selector_333_666));
                            temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                            temp.setLayoutParams(allRadio.getLayoutParams());
                            // 使用分类id作为RadioButton的id
                            temp.setId(categoryResponse.getId());
                            temp.setPadding(allRadio.getPaddingLeft(), allRadio.getPaddingTop(), allRadio.getPaddingRight(), allRadio.getPaddingBottom());
                            mRadioGroup.addView(temp, mRadioGroup.getChildCount());
                        }

                    }
                }
            }
        });
    }

    /**
     * 根据分类id获取机构信息
     *
     * @param categoryId 分类id
     */
    private void getMechanismList(int categoryId) {
        mCurrentCategoryId = categoryId;
        mSwipeLayout.setRefreshing(true);
        mModel.getMechanismList(categoryId, new BaseObserver<MechanismPageResponse>() {
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
        mRadioGroup.setOnCheckedChangeListener(this::onRadioChecked);
    }

    /**
     * radioButton 的选择事件
     *
     * @param i radioButton 的id
     */
    private void onRadioChecked(RadioGroup radioGroup, int i) {
        // 当前不处于刷新状态才可以执行操作
        // 防止连续多次点击或者网速慢在刷新的点击
        if (!mSwipeLayout.isRefreshing()) {
            if (i == R.id.allRadio) {
                // 选择了全部
                getMechanismList(mCategoryResponse.getId());
            } else {
                // 选择了其他的子分类
                int size = mChildCategories.size();
                for (int j = 0; j < size; j++) {
                    CategoryResponse categoryResponse = mChildCategories.get(j);
                    int categoryId = categoryResponse.getId();
                    if (i == categoryId) {
                        getMechanismList(categoryId);
                    }
                }
            }
        }

    }

    private void onSwipeRefresh() {
        getMechanismList(mCurrentCategoryId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mechanism_list;
    }
}
