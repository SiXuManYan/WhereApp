package com.jcs.where.features.address;

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
import com.jcs.where.base.mvp.BaseMvpActivity;
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
        mAdapter.setEmptyView(new EmptyView(this));
        mAdapter.addChildClickViewIds(R.id.edit_iv);
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(getItemDecoration());
    }

    @Override
    protected void initData() {
        presenter = new AddressPresenter(this);
        presenter.getList();
    }

    @Override
    public void bindList(List<AddressResponse> response) {
        mAdapter.setNewInstance(response);
    }

    @Override
    protected void bindListener() {
        mBackIv.setOnClickListener(v -> finish());
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        if (view.getId() == R.id.edit_iv) {
            // 编辑
        }
    }

    private RecyclerView.ItemDecoration getItemDecoration() {
        DividerDecoration itemDecoration = new DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(1), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        itemDecoration.setDrawHeaderFooter(false);
        return itemDecoration;
    }
}
