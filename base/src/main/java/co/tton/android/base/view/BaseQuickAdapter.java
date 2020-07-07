package co.tton.android.base.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.R;
import co.tton.android.base.utils.V;

public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<BaseQuickAdapter.QuickHolder> {

    public static final int NORMAL_VIEW = 0;
    public static final int HEADER_VIEW = 11;
    public static final int LOADING_VIEW = 12;
    public static final int FOOTER_VIEW = 13;

    protected View mHeaderView;
    protected View mFooterView;

    protected boolean mLoadNextEnable;
    protected View mLoadingView;

    protected Context mContext;
    protected List<T> mData;

    public BaseQuickAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public BaseQuickAdapter(Context context, ArrayList<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getItemCount() {
        int headSize = getHeaderViewCount();
        int loadingSize = mLoadNextEnable && mLoadingView != null ? 1 : 0;
        int footSize = loadingSize == 0 && mFooterView != null ? 1 : 0;

        if (mData.isEmpty()) {
            return headSize + footSize;
        } else {
            return mData.size() + headSize + loadingSize + footSize;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return HEADER_VIEW;
        } else if (position == mData.size() + getHeaderViewCount()) {
            if (mLoadNextEnable && mLoadingView != null) {
                return LOADING_VIEW;
            } else if (mFooterView != null) {
                return FOOTER_VIEW;
            }
        }
        return NORMAL_VIEW;
    }

    public int getHeaderViewCount() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterViewCount() {
        return mFooterView == null ? 0 : 1;
    }

    @Override
    public QuickHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QuickHolder holder;
        switch (viewType) {
            case HEADER_VIEW:
                holder = new QuickHolder(mHeaderView);
                break;
            case FOOTER_VIEW:
                holder = new QuickHolder(mFooterView);
                break;
            case LOADING_VIEW:
                holder = new QuickHolder(mLoadingView);
                break;
            default:
                View view = LayoutInflater.from(mContext).inflate(getLayoutId(viewType), parent, false);
                holder = new QuickHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(QuickHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER_VIEW:
            case FOOTER_VIEW:
            case LOADING_VIEW:
                break;
            default:
                initViews(holder, getItem(position), position);
                break;
        }
    }

    protected abstract int getLayoutId(int viewType);

    protected abstract void initViews(QuickHolder holder, T data, int position);

    public T getItem(int position) {
        return mData.get(position - getHeaderViewCount());
    }

    public void addData(int position, T data) {
        mData.add(position - getHeaderViewCount(), data);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public void addData(T data) {
        mData.add(data);
        notifyItemRangeChanged(getItemCount() - 1, 1);
    }

    // TODO: 使用notifyItemRangeChanged替代notifyDataSetChanged
    public void addData(List<T> data) {
        if (data != null && !data.isEmpty()) {
//            int oldSize = getItemCount() - getFooterViewCount();
            mData.addAll(data);
            notifyDataSetChanged();
//            notifyItemRangeChanged(oldSize, data.size() + getFooterViewCount());
        }
    }

    public void setData(List<T> data) {
        if (!mData.isEmpty()) {
            mData.clear();
        }
        if (data != null && !data.isEmpty()) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setDataDirectly(List<T> data) {
        mData = data;
        if (mData == null) {
            mData = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        if (mData != null && !mData.isEmpty()) {
            mData.remove(position - getHeaderViewCount());
            notifyItemRemoved(position);
            // 保证移除后，position也会跟着改变
            notifyItemRangeChanged(position, getItemCount() - getFooterViewCount() - position);
        }
    }

    public void clearData() {
        if (mData != null && !mData.isEmpty()) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    public void removeData(T t) {
        mData.remove(t);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public static class QuickHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mContentView;

        public QuickHolder(View view) {
            super(view);
            mContentView = view;
            mViews = new SparseArray<>();
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T findViewById(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = V.f(mContentView, viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }
    }

    public void setHeaderView(View view) {
        mHeaderView = view;
    }

    public void setFooterView(View view) {
        mFooterView = view;
    }

    public void setLoadingView(View view) {
        mLoadingView = view;
    }

    public static class LoadMoreComponent<T> extends RecyclerView.OnScrollListener {
        private RecyclerView mRecyclerView;
        private BaseQuickAdapter<T> mAdapter;
        private int mLoadMoreLayoutId;

        private View mLoadingView;
        private View mLoadFailView;

        private boolean mIsLoading;
        private boolean mLoadFailed;
        private boolean mIsScrollDown;
        private OnLoadMoreListener mOnLoadNextListener;

        public void install(RecyclerView recyclerView, final BaseQuickAdapter<T> adapter, int loadMoreLayoutId) {
            mAdapter = adapter;
            mRecyclerView = recyclerView;
            mLoadMoreLayoutId = loadMoreLayoutId;

            // 确保加载View是独立的一行
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int itemViewType = adapter.getItemViewType(position);
                        if (itemViewType == LOADING_VIEW
                                || itemViewType == HEADER_VIEW
                                || itemViewType == FOOTER_VIEW) {
                            return gridLayoutManager.getSpanCount();
                        }
                        return 1;
                    }
                });
            }
            // 设置加载view
            View loadingView = LayoutInflater.from(recyclerView.getContext()).inflate(loadMoreLayoutId, (ViewGroup) recyclerView.getParent(), false);
            mLoadFailView = V.f(loadingView, R.id.load_fail);
            mLoadingView = V.f(loadingView, R.id.load_ing);
            mLoadFailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoadNextStart();
                }
            });
            mAdapter.setLoadingView(loadingView);
            mAdapter.mLoadNextEnable = true;

            mRecyclerView.addOnScrollListener(this);
        }

        public void uninstall() {
            if (mRecyclerView != null) {
                mRecyclerView.removeOnScrollListener(this);
                mRecyclerView = null;
            }
            mAdapter = null;
        }

        public void reset() {
            mRecyclerView.removeOnScrollListener(this);
            install(mRecyclerView, mAdapter, mLoadMoreLayoutId);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mAdapter.mLoadNextEnable) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();

                        if (lastVisibleItemPosition + 5 >= totalItemCount // 提前5项开始加载下一页
                                || (totalItemCount - lastVisibleItemPosition) == 0 && totalItemCount > visibleItemCount) {
                            // 没有正在加载 或 加载失败,但是还在往下滚动
                            if (!mIsLoading || (mLoadFailed && mIsScrollDown)) {
                                onLoadNextStart();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mIsScrollDown = dy > 0;
        }

        // 开始加载
        private void onLoadNextStart() {
            mIsLoading = true;
            if (mLoadFailed) {
                mLoadFailed = false;
                mLoadingView.setVisibility(View.VISIBLE);
                mLoadFailView.setVisibility(View.GONE);
            }
            if (mOnLoadNextListener != null) {
                mOnLoadNextListener.onLoadMore();
            }
        }

        // 加载成功
        public void onLoadMoreSuccess() {
            mIsLoading = false;
        }

        // 加载失败
        public void onLoadMoreFailed() {
            mLoadFailed = true;
            mIsLoading = false;
            mLoadingView.setVisibility(View.GONE);
            mLoadFailView.setVisibility(View.VISIBLE);
        }

        // 没有下一页
        public void onNoMore() {
            if (mAdapter == null) return;

            int lastPosition = mAdapter.getItemCount() - 1;
            int viewType = mAdapter.getItemViewType(lastPosition);
            if (viewType == LOADING_VIEW) {
                mAdapter.notifyItemRemoved(lastPosition);
                mAdapter.mLoadNextEnable = false;
                mAdapter.mLoadingView = null;
            }
        }

        public void setOnLoadNextListener(OnLoadMoreListener listener) {
            mOnLoadNextListener = listener;
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
