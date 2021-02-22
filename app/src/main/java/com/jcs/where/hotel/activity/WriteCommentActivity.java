package com.jcs.where.hotel.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LanguageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.api.request.WriteHotelCommentRequest;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.api.response.UploadFileResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.CommentTypeBean;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.home.watcher.EmptyTextWatcher;
import com.jcs.where.hotel.model.WriteCommentModel;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.EmptyWatcher;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.RequestResultCode;
import com.jcs.where.widget.SelectStarView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 页面-酒店评论
 */
public class WriteCommentActivity extends BaseActivity {


    private static final String EXT_ORDER_ID = "orderId";
    private static final String EXT_HOTEL_ID = "hotelId";
    private static final String EXT_HOTEL_NAME = "hotelId";
    private int oldPosition = -1;
    private SelectStarView mSelectStarView;
    private TextView mSatisfiedTv;
    private TextView mCommitTv;
    private EditText mContentEt;
    private RecyclerView mPictureRecycler;
    private AddPictureAdapter mAdapter;
    private final String ADD_PIC_ACTION = "action";

    private WriteCommentModel mModel;
    private int mUploadCompleted = 0;
    private int mToUploadCount = 0;
    // 接受上传图片得到的链接
    private String[] mUploadLinks;

    public static void goTo(Context context, int orderId, int hotelId, String name) {
        Intent intent = new Intent(context, WriteCommentActivity.class);
        intent.putExtra(EXT_ORDER_ID, orderId);
        intent.putExtra(EXT_HOTEL_ID, hotelId);
        intent.putExtra(EXT_HOTEL_NAME, hotelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mSelectStarView = findViewById(R.id.selectStarView);
        mSelectStarView.setListener(this::onStarSelected);

        mSatisfiedTv = findViewById(R.id.satisfiedTv);
        mContentEt = findViewById(R.id.contentEt);
        mCommitTv = findViewById(R.id.commitTv);

        mPictureRecycler = findViewById(R.id.pictureRecycler);
        mAdapter = new AddPictureAdapter();
        mPictureRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        mPictureRecycler.setAdapter(mAdapter);
    }

    private void onStarSelected(int score, String satisfaction) {
        mSatisfiedTv.setText(satisfaction);
    }

    @Override
    protected void initData() {
        mModel = new WriteCommentModel();

        mAdapter.getData().clear();
        mAdapter.addData(ADD_PIC_ACTION);
    }

    @Override
    protected void bindListener() {
        mContentEt.addTextChangedListener(new EmptyTextWatcher() {
            @Override
            protected void onEtEmpty() {
                mCommitTv.setBackgroundResource(R.drawable.shape_cccccc_radius_2);
            }

            @Override
            protected void onEtNotEmpty() {
                mCommitTv.setBackgroundResource(R.drawable.shape_4c9ef2_radius_2);
            }
        });

        mCommitTv.setOnClickListener(this::onCommitClicked);
        mAdapter.setOnItemClickListener(this::onAddPicClicked);
        mAdapter.addChildClickViewIds(R.id.delPicIv);
        mAdapter.setOnItemChildClickListener(this::onDelPicClicked);
    }

    private void onCommitClicked(View view) {
        if (mContentEt.getText().toString().equals("")) {
            showToast(getString(R.string.comment_content_not_empty));
            return;
        }

        if (mAdapter.getItemCount() == 1) {
            //没选择图片，直接提交评论即可
            showLoading();
            commitComment();
        } else {
            //先上传图片
            List<String> data = mAdapter.getData();
            int size = data.size();
            // 包含 上传图片 item，则待上传数量为 size - 1
            mToUploadCount = data.contains(ADD_PIC_ACTION) ? size - 1 : size;
            // 为了有序的保存上传链接
            mUploadLinks = new String[mToUploadCount];
            showLoading();
            for (int i = 0; i < size; i++) {
                String filePath = data.get(i);
                if (!filePath.equals(ADD_PIC_ACTION)) {
                    mModel.postUploadFile(filePath, new BaseObserver<UploadFileResponse>() {
                        @Override
                        protected void onError(ErrorResponse errorResponse) {
                            stopLoading();
                            showNetError(errorResponse);
                        }

                        @Override
                        protected void onSuccess(UploadFileResponse response) {
                            mUploadCompleted++;
                            int index = mAdapter.getData().indexOf(filePath);
                            mUploadLinks[index] = response.link;
                            if (mUploadCompleted == mToUploadCount) {
                                // 都上传完了，提交评论
                                commitComment();
                            }
                        }
                    });

                }
            }
        }
    }

    private void commitComment() {
        mModel.postHotelComment(getRequest(), new BaseObserver<SuccessResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(SuccessResponse response) {
                showToast(getString(R.string.commit_success));
                finish();
            }
        });
    }

    private WriteHotelCommentRequest getRequest() {
        WriteHotelCommentRequest request = new WriteHotelCommentRequest();
        if (mUploadLinks != null) {
            request.setImages(mUploadLinks);
        }
        request.setContent(mContentEt.getText().toString());
        request.setStar(mSelectStarView.getScore());
        return request;
    }

    private void onDelPicClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        mAdapter.removeAt(position);
        if (!mAdapter.getData().contains(ADD_PIC_ACTION)) {
            mAdapter.addData(ADD_PIC_ACTION);
        }
    }

    private void onAddPicClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        String s = mAdapter.getData().get(position);
        if (s.equals(ADD_PIC_ACTION)) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RequestResultCode.REQUEST_WRITE_COMMENT_TO_NATIVE_PHOTOS);
        }
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case RequestResultCode.REQUEST_WRITE_COMMENT_TO_NATIVE_PHOTOS:
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        int itemCount = mAdapter.getItemCount();
                        int index = itemCount - 1;
                        mAdapter.addData(index, path);
                        if (index == 3) {
                            mAdapter.removeAt(4);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_write_comment;
    }

    static class AddPictureAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public AddPictureAdapter() {
            super(R.layout.item_write_comment_pics);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            if (s.equals("action")) {
                if (CacheUtil.getLanguageFromCache().equals("en")) {
                    baseViewHolder.setImageResource(R.id.picIv, R.mipmap.ic_add_comment_pic_en);
                } else {
                    baseViewHolder.setImageResource(R.id.picIv, R.mipmap.ic_add_comment_pic_zh);
                }
                baseViewHolder.setGone(R.id.delPicIv, true);
            } else {
                baseViewHolder.setGone(R.id.delPicIv, false);
                ImageView picIv = baseViewHolder.findView(R.id.picIv);
                GlideUtil.load(getContext(), s, picIv);
            }
        }
    }

}
