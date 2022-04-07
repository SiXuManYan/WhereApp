package com.jcs.where.features.gourmet.restaurant.list.filter.area;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jcs.where.R;
import com.jcs.where.api.response.area.AreaResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.mvp.BaseMvpFragment;
import com.jcs.where.view.empty.EmptyView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Wangsw  2021/3/29 15:55.
 * 餐厅商业区筛选
 */
public class AreaFilterFragment extends BaseMvpFragment<AreaFilterPresenter> implements AreaFilterView, OnItemClickListener {


    private AreaFilterAdapter mAdapter;
    private EmptyView mEmptyView;


    private RecyclerView contentRv;

    @Override
    protected int getLayoutId() {
        return R.layout.single_recycler_view;
    }

    @Override
    protected void initView(View view) {
        contentRv = view.findViewById(R.id.content_rv);
        contentRv.setVerticalScrollBarEnabled(true);
        mEmptyView = new EmptyView(requireContext());
    }

    @Override
    protected void initData() {
        presenter = new AreaFilterPresenter(this);
        mAdapter = new AreaFilterAdapter();
        mAdapter.setEmptyView(mEmptyView);
        contentRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void loadOnVisible() {
        presenter.getAreasList();
    }

    @Override
    public void bindList(List<AreaResponse> response) {
        mAdapter.setNewInstance(response);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        List<AreaResponse> list = mAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            AreaResponse data = list.get(i);
            if (position == i) {
                data.nativeIsSelected = true;
            } else {
                data.nativeIsSelected = false;
            }
        }
        mAdapter.notifyDataSetChanged();
        AreaResponse areaData = mAdapter.getData().get(position);
        EventBus.getDefault().post(new BaseEvent<>(areaData));
    }
}
