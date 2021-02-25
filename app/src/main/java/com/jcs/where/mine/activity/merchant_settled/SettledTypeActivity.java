package com.jcs.where.mine.activity.merchant_settled;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.mine.model.merchant_settled.SettledTypeModel;
import com.jcs.where.utils.RequestResultCode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 页面-入驻类型
 * create by zyf on 2021/2/25 8:46 下午
 */
public class SettledTypeActivity extends BaseActivity {
    private SettledTypeModel mModel;
    private RecyclerView mRecycler;
    private SettledTypeAdapter mAdapter;
    private int mTypeLevel = 1;
    private List<String> mParentIds;

    @Override
    protected void initView() {
        mRecycler = findViewById(R.id.settledTypeRecycler);
        mAdapter = new SettledTypeAdapter();

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new SettledTypeModel();
        mParentIds = new ArrayList<>();
        mParentIds.add("0");
        mModel.getCategories(mTypeLevel, mParentIds.get(mParentIds.size() - 1), new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(List<CategoryResponse> response) {
                mAdapter.getData().clear();
                mAdapter.addData(response);
            }
        });
    }

    private void getCategories(int typeLevel, String parentId) {
        mModel.getCategories(typeLevel, parentId, new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(List<CategoryResponse> response) {
                mAdapter.getData().clear();
                mAdapter.addData(response);
            }
        });
    }

    @Override
    protected void bindListener() {
        mAdapter.setOnItemClickListener(this::onItemClicked);
        mJcsTitle.setBackIvClickListener(this::onBackIvClicked);
    }

    private void onBackIvClicked(View view) {
        if (mTypeLevel == 1) {
            finish();
        } else {
            mParentIds.remove(mParentIds.size() - 1);
            mTypeLevel--;
            getCategories(mTypeLevel, mParentIds.get(mParentIds.size() - 1));

        }
    }

    private void onItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        CategoryResponse item = mAdapter.getItem(position);
        if (item.getHas_children() == 2) {
            // 有下级，则获取下级数据
            mTypeLevel++;
            mParentIds.add(item.getId());
            getCategories(mTypeLevel, item.getId());
        } else {
            Intent result = new Intent();
            result.putExtra("typeName", item.getName());
            result.putExtra("typeId", item.getId());
            setResult(RequestResultCode.RESULT_SETTLED_TYPE_TO_MERCHANT_SETTLED, result);

            // 没有下级，则选中此分类
            finish();
        }
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settled_type;
    }

    static class SettledTypeAdapter extends BaseQuickAdapter<CategoryResponse, BaseViewHolder> {

        public SettledTypeAdapter() {
            super(R.layout.item_settled_type);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, CategoryResponse categoryResponse) {
            baseViewHolder.setText(R.id.settledTypeNameTv, categoryResponse.getName() + "");
            if (categoryResponse.getHas_children() == 1) {
                // 1 表示没有下级
                baseViewHolder.setGone(R.id.arrowIv, true);
            } else {
                baseViewHolder.setGone(R.id.arrowIv, false);
            }
        }
    }
}
