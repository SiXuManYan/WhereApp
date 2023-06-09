package com.jiechengsheng.city.mine.activity.merchant_settled;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.ErrorResponse;
import com.jiechengsheng.city.api.response.MerchantTypeResponse;
import com.jiechengsheng.city.base.BaseActivity;
import com.jiechengsheng.city.mine.model.merchant_settled.SettledTypeModel;
import com.jiechengsheng.city.utils.RequestResultCode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面-入驻类型
 * create by zyf on 2021/2/25 8:46 下午
 */
public class SettledTypeActivity extends BaseActivity {
    private SettledTypeModel mModel;
    private RecyclerView mRecycler;
    private SettledTypeAdapter mAdapter;
    private int mTypeLevel = 1;
    private List<Integer> mParentIds;
    private String[] mMerchantTypeLevels = {"first", "second", "third"};

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
        mParentIds.add(0);
        getCategories(mMerchantTypeLevels[mTypeLevel - 1], mParentIds.get(mTypeLevel - 1));
    }

    private void getCategories(String typeLevel, int parentId) {
        mModel.getMerchantType(typeLevel, parentId, new BaseObserver<List<MerchantTypeResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
            }

            @Override
            protected void onSuccess(List<MerchantTypeResponse> response) {
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
            getCategories(mMerchantTypeLevels[mTypeLevel], mParentIds.get(mParentIds.size() - 1));

        }
    }

    private void onItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        MerchantTypeResponse item = mAdapter.getItem(position);
        if (item.getHasChildren() == 2) {
            // 有下级，则获取下级数据
            mTypeLevel++;
            mParentIds.add(item.getId());

            if (mTypeLevel - 1 < mMerchantTypeLevels.length) {
                getCategories(mMerchantTypeLevels[mTypeLevel - 1], item.getId());
            } else {
                Intent result = new Intent();
                result.putExtra("typeName", item.getName());
                result.putExtra("typeId", item.getId());
                setResult(RequestResultCode.RESULT_SETTLED_TYPE_TO_MERCHANT_SETTLED, result);

                // 没有下级，则选中此分类
                finish();
            }

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

    static class SettledTypeAdapter extends BaseQuickAdapter<MerchantTypeResponse, BaseViewHolder> {

        public SettledTypeAdapter() {
            super(R.layout.item_settled_type);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, MerchantTypeResponse typeResponse) {
            baseViewHolder.setText(R.id.settledTypeNameTv, typeResponse.getName() + "");
            Log.e("SettledTypeAdapter", "convert: " + typeResponse.getId());
            if (typeResponse.getHasChildren() == 1) {
                // 1 表示没有下级
                baseViewHolder.setGone(R.id.arrowIv, true);
            } else {
                baseViewHolder.setGone(R.id.arrowIv, false);
            }
        }
    }
}
