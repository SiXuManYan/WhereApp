package com.jcs.where.news.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcs.where.R;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.news.adapter.NewFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author : hwd
 * date   : 2021/1/6-23:19
 */
public class NewFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private NewFragmentAdapter newFragmentAdapter;

    public static NewFragment newInstance() {
        Bundle args = new Bundle();
//        args.putSerializable(KEY_CATEGORY_RESPONSE, category);
        NewFragment fragment = new NewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler);
    }

    @Override
    protected void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }
        newFragmentAdapter = new NewFragmentAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(newFragmentAdapter);

    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragemnt_news;
    }


}
