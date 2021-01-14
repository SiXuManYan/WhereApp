package com.jcs.where.hotel.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.hotel.adapter.HotelCommentsAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.HotelCommentsResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.hotel.model.HotelCommentModel;
import com.jcs.where.utils.ImagePreviewActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.annotations.NonNull;

public class HotelCommentActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXT_ID = "id";
    private HotelCommentModel mModel;
    private int hotelId;
    private TextView mAllTv, mShowImageTv, mLowScoreTv, mNewestTv;
    private TextView mWhereTv, mAgodaTv, mBookingTv;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecycler;
    private HotelCommentsAdapter mAdapter;
    private List<TextView> mCategoryTvs;
    private List<TextView> mTagTvs;

    public static void goTo(Context context, int id) {
        Intent intent = new Intent(context, HotelCommentActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mSwipeLayout = findViewById(R.id.swipeLayout);
        mWhereTv = findViewById(R.id.whereTag);
        mAgodaTv = findViewById(R.id.agodaTag);
        mBookingTv = findViewById(R.id.bookingTag);

        mAllTv = findViewById(R.id.allTv);
        mShowImageTv = findViewById(R.id.showImageTv);
        mLowScoreTv = findViewById(R.id.lowScoreTv);
        mNewestTv = findViewById(R.id.newestTv);

        mRecycler = findViewById(R.id.commentsRecycler);
        mAdapter = new HotelCommentsAdapter(R.layout.item_commentlist);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mModel = new HotelCommentModel();
        hotelId = getIntent().getIntExtra(EXT_ID, 0);
        mCategoryTvs = new ArrayList<>();
        mCategoryTvs.add(mAllTv);
        mCategoryTvs.add(mShowImageTv);
        mCategoryTvs.add(mLowScoreTv);
        mCategoryTvs.add(mNewestTv);

        mTagTvs = new ArrayList<>();
        mTagTvs.add(mWhereTv);
        mTagTvs.add(mAgodaTv);
        mTagTvs.add(mBookingTv);

        mModel.getHotelCommentNum(hotelId, new BaseObserver<List<Integer>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onNext(@NonNull List<Integer> integers) {
                injectToSubTab(mAllTv, R.string.all_with_number, integers.get(0));
                injectToSubTab(mShowImageTv, R.string.show_icon_with_number, integers.get(1));
                injectToSubTab(mLowScoreTv, R.string.low_rating_with_number, integers.get(2));
                injectToSubTab(mNewestTv, R.string.new_with_number, integers.get(3));
            }
        });

        mAllTv.setSelected(true);
        mWhereTv.setSelected(true);

        showLoading();
        getCommentByType(0);
    }

    private void injectToSubTab(TextView tv, int strResId, int num) {
        String show = "";
        if (num > 0) {
            show = String.format(getString(strResId), num);
        } else {
            show = getString(strResId).replace("%1$d", "");
        }
        tv.setText(show);
    }

    private void getCommentByType(int type) {
        mModel.getComments(hotelId, type, new BaseObserver<HotelCommentsResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                mSwipeLayout.setRefreshing(false);
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull HotelCommentsResponse hotelCommentsResponse) {
                stopLoading();
                mSwipeLayout.setRefreshing(false);
                mAdapter.getData().clear();
                List<HotelCommentsResponse.DataBean> data = hotelCommentsResponse.getData();
                mAdapter.addData(data);
                for (int i = 0; i < data.size(); i++) {
                    Log.e("HotelCommentActivity", "----onNext---" + i + "====" + data.get(i).toString());
                }
            }
        });
    }

    @Override
    protected void bindListener() {
        mAllTv.setOnClickListener(this);
        mShowImageTv.setOnClickListener(this);
        mLowScoreTv.setOnClickListener(this);
        mNewestTv.setOnClickListener(this);

        mWhereTv.setOnClickListener(this);
        mAgodaTv.setOnClickListener(this);
        mBookingTv.setOnClickListener(this);

        mSwipeLayout.setOnRefreshListener(() -> getCommentByType(getCurrentType(mCategoryTvs)));

        mAdapter.addChildClickViewIds(R.id.fullText, R.id.commentIcon01, R.id.commentIcon02, R.id.commentIcon03, R.id.commentIcon04);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.fullText) {
                Log.e("HotelCommentActivity", "----bindListener---fullText");
                HotelCommentsResponse.DataBean bean = (HotelCommentsResponse.DataBean) adapter.getData().get(position);
                bean.is_select = !bean.is_select;
                mAdapter.notifyItemChanged(position);
            }

            if (view instanceof RoundedImageView) {
                Intent to = new Intent(this, ImagePreviewActivity.class);
                HotelCommentsResponse.DataBean dataBean = mAdapter.getData().get(position);
                ArrayList<String> images = (ArrayList<String>) dataBean.getImages();
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

        });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotelcomment;
    }

    @Override
    public void onClick(View view) {
        if (view instanceof TextView && mCategoryTvs.contains(view)) {
            TextView temp = (TextView) view;
            temp.setSelected(true);
            if (view == mAllTv) {
                getCommentByType(0);
            }

            if (view == mShowImageTv) {
                getCommentByType(1);
            }

            if (view == mLowScoreTv) {
                getCommentByType(2);
            }

            if (view == mNewestTv) {
                getCommentByType(3);
            }

            unSelectByList(temp, mCategoryTvs);
        }

        if (view instanceof TextView && mTagTvs.contains(view)) {
//            TextView temp = (TextView) view;
//            temp.setSelected(true);
//            if (view == mWhereTv) {
//            }
//
//            if (view == mAgodaTv) {
//            }
//
//            if (view == mBookingTv) {
//            }
//
//            unSelectByList(temp, mTagTvs);
            showComing();
        }
    }

    private void unSelectByList(TextView textView, List<TextView> temp) {
        if (temp != null) {
            int size = temp.size();
            for (int j = 0; j < size; j++) {
                TextView tv = temp.get(j);
                if (tv != textView) {
                    tv.setSelected(false);
                }
            }
        }
    }

    private int getCurrentType(List<TextView> temp) {
        if (temp != null) {
            int size = temp.size();
            for (int j = 0; j < size; j++) {
                TextView tv = temp.get(j);
                if (tv.isSelected()) {
                    return j;
                }
            }
        }
        return 0;
    }
}
