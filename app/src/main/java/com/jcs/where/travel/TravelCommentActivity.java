package com.jcs.where.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jcs.where.R;
import com.jcs.where.api.response.CommentResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.hotel.adapter.CommentListAdapter;
import com.jcs.where.travel.model.TravelCommentModel;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 页面-旅游评论
 */
public class TravelCommentActivity extends BaseActivity {

    private static final String EXT_ID = "id";
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mRefreshLayout;
    private List<CommentResponse> list;
    private CommentListAdapter mAdapter;
    private TravelCommentModel mModel;

    public static void goTo(Context context, int id) {
        Intent intent = new Intent(context, TravelCommentActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        mRecycler = findViewById(R.id.commentsRecycler);
        mRefreshLayout = findViewById(R.id.swipeLayout);
        mAdapter = new CommentListAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new TravelCommentModel();
        showLoading();
    }

    @Override
    protected void bindListener() {
        mRefreshLayout.setOnRefreshListener(this::onRefreshListener);
    }

    private void onRefreshListener() {
        showToast("刷新");
    }

    private void initMoreData() {
        showLoading();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_travelcomment;
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
