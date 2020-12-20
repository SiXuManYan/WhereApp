package co.tton.android.base.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import co.tton.android.base.R;


public class CommonLayout extends FrameLayout {

    private int mEmptyLayoutId;
    private int mErrorLayoutId;
    private int mLoadingLayoutId;
    private int mContentLayoutId;
    private int mSearchEmptyLayoutId;

    private ViewStub mEmptyStub;
    private ViewStub mErrorStub;
    private ViewStub mLoadingStub;
    private ViewStub mContentStub;
    private ViewStub mSearchEmptyStub;

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mContentView;
    private View mSearchEmptyView;

    private OnClickListener mOnErrorClickListener;

    public CommonLayout(Context context) {
        super(context);
        init(context, null);
    }

    public CommonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommonLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (mEmptyStub != null) return;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonLayout);
        mEmptyLayoutId = ta.getResourceId(R.styleable.CommonLayout_layout_empty, 0);
        mErrorLayoutId = ta.getResourceId(R.styleable.CommonLayout_layout_error, 0);
        mLoadingLayoutId = ta.getResourceId(R.styleable.CommonLayout_layout_loading, 0);
        mContentLayoutId = ta.getResourceId(R.styleable.CommonLayout_layout_content, 0);
        mSearchEmptyLayoutId = ta.getResourceId(R.styleable.CommonLayout_layout_search_empty, 0);
        ta.recycle();

        View view = inflate(context, R.layout.common_layout, this);

        mEmptyStub = view.findViewById(R.id.vs_empty);
        mEmptyStub.setLayoutResource(mEmptyLayoutId);

        mErrorStub = view.findViewById(R.id.vs_error);
        mErrorStub.setLayoutResource(mErrorLayoutId);

        mLoadingStub = view.findViewById(R.id.vs_loading);
        mLoadingStub.setLayoutResource(mLoadingLayoutId);

        mContentStub = view.findViewById(R.id.vs_content);
        mContentStub.setLayoutResource(mContentLayoutId);

        mSearchEmptyStub = view.findViewById(R.id.vs_search_empty);
        mSearchEmptyStub.setLayoutResource(mSearchEmptyLayoutId);
    }

    public void showLoading() {
        if (mLoadingLayoutId == 0) return;

        setContentViewVisible(GONE);
        setErrorViewVisible(GONE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(VISIBLE);
        setSearchEmptyViewVisible(GONE);
    }

    public void showEmpty() {
        if (mEmptyLayoutId == 0) return;

        setContentViewVisible(GONE);
        setErrorViewVisible(GONE);
        setEmptyViewVisible(VISIBLE);
        setLoadingViewVisible(GONE);
        setSearchEmptyViewVisible(GONE);
    }

    public void showError() {
        if (mErrorLayoutId == 0) return;

        setContentViewVisible(GONE);
        setErrorViewVisible(VISIBLE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(GONE);
        setSearchEmptyViewVisible(GONE);
    }

    public void showContent() {
        if (mContentLayoutId == 0) return;

        setContentViewVisible(VISIBLE);
        setErrorViewVisible(GONE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(GONE);
        setSearchEmptyViewVisible(GONE);
    }

    public void showSearchEmpty() {
        if (mSearchEmptyLayoutId == 0) return;

        setContentViewVisible(GONE);
        setErrorViewVisible(GONE);
        setEmptyViewVisible(GONE);
        setLoadingViewVisible(GONE);
        setSearchEmptyViewVisible(VISIBLE);
    }

    private void setEmptyViewVisible(int visible) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mEmptyView = mEmptyStub.inflate();
            mEmptyView.setVisibility(VISIBLE);
        }
    }

    private void setErrorViewVisible(int visible) {
        if (mErrorView != null) {
            mErrorView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mErrorView = mErrorStub.inflate();
            mErrorView.setVisibility(VISIBLE);
            mErrorView.setOnClickListener(mOnErrorClickListener);
        }
    }

    private void setLoadingViewVisible(int visible) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mLoadingView = mLoadingStub.inflate();
            mLoadingView.setVisibility(VISIBLE);
        }
    }

    private void setContentViewVisible(int visible) {
        if (mContentView != null) {
            mContentView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mContentView = mContentStub.inflate();
            mContentView.setVisibility(VISIBLE);
        }
    }

    private void setSearchEmptyViewVisible(int visible) {
        if (mSearchEmptyView != null) {
            mSearchEmptyView.setVisibility(visible);
        } else if (visible == VISIBLE) {
            mSearchEmptyView = mSearchEmptyStub.inflate();
            mSearchEmptyView.setVisibility(VISIBLE);
        }
    }

    public void setEmptyLayoutId(int layoutId) {
        mEmptyLayoutId = layoutId;
        mEmptyStub.setLayoutResource(layoutId);
    }

    public void setErrorLayoutId(int layoutId) {
        mErrorLayoutId = layoutId;
        mErrorStub.setLayoutResource(layoutId);
    }

    public void setLoadingLayoutId(int layoutId) {
        mLoadingLayoutId = layoutId;
        mLoadingStub.setLayoutResource(layoutId);
    }

    public void setContentLayoutId(int layoutId) {
        mContentLayoutId = layoutId;
        mContentStub.setLayoutResource(layoutId);
    }

    public void setSearchEmptyLayoutId(int layoutId) {
        mSearchEmptyLayoutId = layoutId;
        mSearchEmptyStub.setLayoutResource(layoutId);
    }

    public View getContentView() {
        return mContentView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public View getSearchEmptyView() {
        return mSearchEmptyView;
    }

    public void setOnErrorClickListener(OnClickListener listener) {
        mOnErrorClickListener = listener;
    }
}
