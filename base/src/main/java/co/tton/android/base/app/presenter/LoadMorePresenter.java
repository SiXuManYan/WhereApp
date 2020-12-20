package co.tton.android.base.app.presenter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.tton.android.base.R;
import co.tton.android.base.manager.CompositeSubscriptionHelper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import co.tton.android.base.view.CommonLayout;
import rx.Subscription;
import rx.functions.Action1;

public abstract class LoadMorePresenter<T> {

    protected int mFirstPage;
    protected int mPage;
    protected int mPageSize;

    private CommonLayout mCommonLayout;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter<T> mAdapter;
    private BaseQuickAdapter.LoadMoreComponent<T> mLoadMoreComponent;
    private PullRefreshListener mListener;

    private CompositeSubscriptionHelper mCompositeSubscriptionHelper;

    private boolean mInit;

    // 搜索关键字为空则显示数据空，不为空则显示搜索结果为空。
    private String mKeyword;

    public LoadMorePresenter() {
        this(1, 10); // 默认第一页从1开始，每页有10项。
    }

    public LoadMorePresenter(int firstPage, int pageSize) {
        mFirstPage = firstPage;
        mPage = firstPage;
        mPageSize = pageSize;
    }

    public void init(CommonLayout commonLayout, RecyclerView recyclerView, BaseQuickAdapter<T> adapter) {
        mCommonLayout = commonLayout;
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mCompositeSubscriptionHelper = CompositeSubscriptionHelper.newInstance();

        mLoadMoreComponent = new BaseQuickAdapter.LoadMoreComponent<>();
        mLoadMoreComponent.install(recyclerView, mAdapter, R.layout.common_load_next);
        mLoadMoreComponent.setOnLoadNextListener(new BaseQuickAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                addSubscription(requestNextPage(mSuccessAction, mFailedAction));
            }
        });

        mCommonLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommonLayout.showLoading();
                addSubscription(requestNextPage(mSuccessAction, mFailedAction));
            }
        });

        mInit = true;
        reload(null);
    }

    public Subscription reload(String keyword) {
        mKeyword = keyword;
        mPage = mFirstPage;
        mCommonLayout.showLoading();

        Subscription subscription = requestNextPage(mSuccessAction, mFailedAction);
        addSubscription(subscription);
        return subscription;
    }


    public Subscription reloadDown() {
        mPage = mFirstPage;

        Subscription subscription = requestNextPage(mSuccessAction, mFailedAction);
        addSubscription(subscription);
        return subscription;
    }

    public void destroy() {
        if (mCompositeSubscriptionHelper != null) {
            mCompositeSubscriptionHelper.unsubscribe();
        }
        if (mLoadMoreComponent != null) {
            mLoadMoreComponent.uninstall();
        }
    }

    private void addSubscription(Subscription subscription) {
        mCompositeSubscriptionHelper.addSubscription(subscription);
    }

    public boolean isInit() {
        return mInit;
    }

    protected Action1<List<T>> mSuccessAction = new Action1<List<T>>() {
        @Override
        public void call(List<T> list) {
            mInit = true;
            mListener.onPullRefresh();
            if (mPage == mFirstPage) {
                if (list != null && !list.isEmpty()) {
                    mAdapter.setDataDirectly(list);
                    if (list.size() < mPageSize) {
                        mLoadMoreComponent.onNoMore();
                    } else {
                        mLoadMoreComponent.onLoadMoreSuccess();
                    }
                    mCommonLayout.showContent();
                } else {
                    if (TextUtils.isEmpty(mKeyword)) {
                        if (mAdapter.getHeaderViewCount() > 0) {
                            mCommonLayout.showContent();
                            mAdapter.setDataDirectly(null);
                        } else {
                            mCommonLayout.showEmpty();
                            setEmptyLayout(mCommonLayout.getEmptyView());
                        }
                    } else {
                        mCommonLayout.showSearchEmpty();
                        View view = mCommonLayout.getSearchEmptyView();
                        TextView textView = view.findViewById(R.id.tv_search_empty);
                        textView.setText(mRecyclerView.getContext().getString(R.string.common_search_empty, mKeyword));
                    }
                }
            } else {
                mAdapter.addData(list);
                if (list == null || list.size() < mPageSize) {
                    mLoadMoreComponent.onNoMore();
                } else {
                    mLoadMoreComponent.onLoadMoreSuccess();
                }
            }
            mPage++;
        }
    };

    protected Action1<Throwable> mFailedAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable t) {
            mInit = true;
            mListener.onPullRefresh();
            if (mPage == mFirstPage) {
                mCommonLayout.showError();
                setErrorLayout(mCommonLayout.getErrorView());
            } else {
                mLoadMoreComponent.onLoadMoreFailed();
            }
        }
    };

    protected void setEmptyLayout(View emptyLayout) {

    }

    protected void setErrorLayout(View errorLayout) {

    }

    protected abstract Subscription requestNextPage(Action1<List<T>> success, Action1<Throwable> failed);

    public void setPullRefreshListener(PullRefreshListener listener) {
        mListener = listener;
    }

    public static interface PullRefreshListener {
        public void onPullRefresh();
    }
}
