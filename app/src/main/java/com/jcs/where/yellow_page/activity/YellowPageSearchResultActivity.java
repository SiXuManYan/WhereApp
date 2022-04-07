package com.jcs.where.yellow_page.activity;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.adapter.MechanismListAdapter;
import com.jcs.where.news.fragment.NewsFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 页面—企业黄页搜索结果页
 * create by zyf on 2021/3/1 20:24 下午
 */
public class YellowPageSearchResultActivity extends BaseActivity {

    public static final String K_INPUT = "input";
    private String mInput;
    private MechanismListAdapter mAdapter;
    private RecyclerView mRecycler;


    @Override
    protected void initView() {
        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MechanismListAdapter();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mInput = getIntent().getStringExtra(K_INPUT);
        mJcsTitle.setMiddleTitle(mInput);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_search_result;
    }
}
