package com.jcs.where.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CommentResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.hotel.adapter.CommentListAdapter;
import com.jcs.where.travel.model.TravelCommentModel;
import com.jcs.where.utils.ImagePreviewActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.annotations.NonNull;

/**
 * 页面-旅游评论
 */
public class TravelCommentActivity extends BaseActivity {

    private static final String EXT_ID = "id";
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mSwipeLayout;
    private List<CommentResponse> list;
    private CommentListAdapter mAdapter;
    private TravelCommentModel mModel;
    private int mTouristAttractionId;

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
        mSwipeLayout = findViewById(R.id.swipeLayout);
        mAdapter = new CommentListAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new TravelCommentModel();
        mTouristAttractionId = getIntent().getIntExtra(EXT_ID, 0);

        showLoading();
        getComments();
    }

    private void getComments() {
        mModel.getTouristAttractionCommentList(mTouristAttractionId, new BaseObserver<PageResponse<CommentResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                mSwipeLayout.setRefreshing(false);
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull PageResponse<CommentResponse> pageResponse) {
                stopLoading();
                mSwipeLayout.setRefreshing(false);
                mAdapter.getData().clear();
                List<CommentResponse> data = pageResponse.getData();
                mAdapter.addData(data);
            }
        });
    }

    @Override
    protected void bindListener() {
        mSwipeLayout.setOnRefreshListener(this::onRefreshListener);
        mAdapter.addChildClickViewIds(R.id.fullText, R.id.commentIcon01, R.id.commentIcon02, R.id.commentIcon03, R.id.commentIcon04);
        mAdapter.setOnItemChildClickListener(this::onItemChildClicked);
    }

    private void onItemChildClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        int id = view.getId();
        CommentResponse item = mAdapter.getData().get(position);
        if (id == R.id.fullText) {
            Log.e("HotelCommentActivity", "----bindListener---fullText");
            item.is_select = !item.is_select;
            mAdapter.notifyItemChanged(position);
        }

        if (view instanceof RoundedImageView) {
            Intent to = new Intent(this, ImagePreviewActivity.class);
            ArrayList<String> images = (ArrayList<String>) item.getImages();
            to.putStringArrayListExtra(ImagePreviewActivity.IMAGES_URL_KEY, images);
            ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "commentIcon");

            int imgPosition = -1;
            if (id == R.id.commentIcon01) {
                imgPosition = 0;
            }
            if (id == R.id.commentIcon02) {
                imgPosition = 1;
            }
            if (id == R.id.commentIcon03) {
                imgPosition = 2;
            }
            if (id == R.id.commentIcon04) {
                imgPosition = 3;
            }
            to.putExtra(ImagePreviewActivity.IMAGE_POSITION, imgPosition);


            startActivity(to);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }
    }

    private void onRefreshListener() {
        getComments();
    }

    private void initMoreData() {
        showLoading();
    }

    @Override
    protected boolean isStatusDark() {
        return true;
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
