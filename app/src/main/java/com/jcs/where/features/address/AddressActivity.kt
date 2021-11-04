package com.jcs.where.features.address;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jcs.where.R;
import com.jcs.where.api.response.address.AddressResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.address.edit.AddressEditActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.view.empty.EmptyView;
import com.jcs.where.widget.list.DividerDecoration;

import java.util.List;

/**
 * Created by Wangsw  2021/3/22 10:33.
 * 收货地址
 */
public class AddressActivity extends BaseMvpActivity<AddressPresenter> implements AddressView, OnItemChildClickListener {

    private ImageView mBackIv;
    private TextView mAddTv;
    private RecyclerView mRecyclerView;
    private AddressAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.back_iv);
        mAddTv = findViewById(R.id.add_tv);
        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new AddressAdapter();
        EmptyView emptyView = new EmptyView(this);
        emptyView.showEmptyDefault();
        mAdapter.setEmptyView(emptyView);
        mAdapter.addChildClickViewIds(R.id.edit_iv);
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(getItemDecoration());
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        presenter = new AddressPresenter(this);
        presenter.getList();
    }

    @Override
    public void bindList(List<AddressResponse> response) {
        if (response == null || response.isEmpty()) {
            mAdapter.setNewInstance(null);
        } else {
            mAdapter.setNewInstance(response);
        }
    }

    @Override
    protected void bindListener() {
        mBackIv.setOnClickListener(v -> finish());
        mAddTv.setOnClickListener(v -> startActivity(AddressEditActivity.class));
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        AddressResponse data = mAdapter.getData().get(position);
        if (view.getId() == R.id.edit_iv) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.PARAM_ADDRESS_ID, data.id);
            bundle.putInt(Constant.PARAM_SEX, data.sex);
            bundle.putString(Constant.PARAM_ADDRESS, data.address);
            bundle.putString(Constant.PARAM_RECIPIENT, data.contact_name);
            bundle.putString(Constant.PARAM_PHONE, data.contact_number);
            startActivity(AddressEditActivity.class, bundle);
        }
    }

    private RecyclerView.ItemDecoration getItemDecoration() {
        DividerDecoration itemDecoration = new DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(1), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        itemDecoration.setDrawHeaderFooter(false);
        return itemDecoration;
    }

    @Override
    public void onEventReceived(BaseEvent<?> baseEvent) {
        super.onEventReceived(baseEvent);
        if (baseEvent.code == EventCode.EVENT_ADDRESS) {
            presenter.getList();
        }

    }
}
