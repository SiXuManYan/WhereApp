package co.tton.android.base.app.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import co.tton.android.base.R;
import co.tton.android.base.app.presenter.LoadMorePresenter;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.CommonLayout;

public abstract class BaseListActivity<T> extends BaseActivity implements LoadMorePresenter.PullRefreshListener {

    protected CommonLayout mCommonLayout;
    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected BaseQuickAdapter<T> mAdapter;
    protected LoadMorePresenter<T> mLoadMoreComponent;

    protected String mKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化通用布局
        mCommonLayout = V.f(this, R.id.common_layout);
        mCommonLayout.setContentLayoutId(getContentLayoutId());
        mCommonLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadMoreComponent.reload(mKeyword);
            }
        });
        // 初始化分页加载列表
        mCommonLayout.showContent();
        View view = mCommonLayout.getContentView();
        mRecyclerView = V.f(view, R.id.common_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = initAdapter();
        setRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = V.f(view, R.id.swipe_layout);
        mLoadMoreComponent = initLoadMoreComponent();
        mLoadMoreComponent.setPullRefreshListener(this);
        mLoadMoreComponent.init(mCommonLayout, mRecyclerView, mAdapter);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_image_placeholder);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadMoreComponent.reloadDown();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    protected int getContentLayoutId() {
        return R.layout.common_refresh_list;
    }


    @Override
    protected void onDestroy() {
        mLoadMoreComponent.destroy();
        super.onDestroy();
    }

    protected void setRecyclerView(RecyclerView recyclerView) {

    }

    protected abstract BaseQuickAdapter<T> initAdapter();

    protected abstract LoadMorePresenter<T> initLoadMoreComponent();

    @Override
    public void onPullRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
