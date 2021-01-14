package com.jcs.where.travel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.dialog.CommentSuccessDialog;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.view.GridItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TravelWriteCommentActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXT_ID = "id";
    private ImageView starIv1, starIv2, starIv3, starIv4, starIv5;
    private TextView starTv, contentLengthTv;
    private PhotoAdapter mAdapter;
    private EditText contentEt;
    private Dialog commentSuccessDialog;
    private int star = -1;


    public static void goTo(Context context, int id) {
        Intent intent = new Intent(context, TravelWriteCommentActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                int viewId = view.getId();
                int itemCount = mAdapter.getItemCount();
                if (viewId == R.id.iv_picture) {
                    if (position == itemCount - 1) {
                        if (itemCount - 1 == 4) {
                            showToast("最多4张图片");
                            return;
                        }
                        int size = itemCount - 1;
                    }
                }

                if (viewId == R.id.iv_picture_delete) {
                    mAdapter.removeAt(position);
                }
            }
        });
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.rv_photo);
        recyclerView.setLayoutManager(new GridLayoutManager(TravelWriteCommentActivity.this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.addItemDecoration(new GridItemDecoration(TravelWriteCommentActivity.this, 5, 0));
        mAdapter = new PhotoAdapter(TravelWriteCommentActivity.this);
        mAdapter.addChildClickViewIds(R.id.iv_picture, R.id.iv_picture_delete);
        mAdapter.addData("0");
        recyclerView.setAdapter(mAdapter);
        starIv1 = findViewById(R.id.iv_star1);
        starIv1.setOnClickListener(this);
        starIv2 = findViewById(R.id.iv_star2);
        starIv2.setOnClickListener(this);
        starIv3 = findViewById(R.id.iv_star3);
        starIv3.setOnClickListener(this);
        starIv4 = findViewById(R.id.iv_star4);
        starIv4.setOnClickListener(this);
        starIv5 = findViewById(R.id.iv_star5);
        starIv5.setOnClickListener(this);
        starTv = findViewById(R.id.tv_star);
        starTv.setVisibility(View.GONE);
        findViewById(R.id.tv_submit).setOnClickListener(this);
        contentLengthTv = findViewById(R.id.tv_contentlength);
        contentEt = findViewById(R.id.et_conetnt);
        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contentLengthTv.setText(charSequence.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        commentSuccessDialog = new CommentSuccessDialog(TravelWriteCommentActivity.this, R.style.dialog, new CommentSuccessDialog.OnCloseListener() {
            @Override
            public void onClose(Dialog dialog) {
                dialog.dismiss();
                finish();
            }

            @Override
            public void onConfirm(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_travelwritecomment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_star1:
                starIv1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starIv3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starIv4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starIv5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starTv.setText("一般");
                starTv.setVisibility(View.VISIBLE);
                star = 1;
                break;
            case R.id.iv_star2:
                starIv1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starIv4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starIv5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starTv.setText("不错");
                starTv.setVisibility(View.VISIBLE);
                star = 2;
                break;
            case R.id.iv_star3:
                starIv1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starIv5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starTv.setText("很好");
                starTv.setVisibility(View.VISIBLE);
                star = 3;
                break;
            case R.id.iv_star4:
                starIv1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_greystar));
                starTv.setText("非常好");
                starTv.setVisibility(View.VISIBLE);
                star = 4;
                break;
            case R.id.iv_star5:
                starIv1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starIv5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_writecomment_lightstar));
                starTv.setText("好极了");
                starTv.setVisibility(View.VISIBLE);
                star = 5;
                break;
            case R.id.tv_submit:
                if (star == -1) {
                    showToast("请选择评分");
                    return;
                }
                if (TextUtils.isEmpty(contentEt.getText().toString().trim())) {
                    showToast("请输入评论内容");
                    return;
                }
                if (mAdapter.getCirclePhoto().size() == 0) {
                    showToast("请选择需要上传的图片");
                    return;
                }
                showLoading();
                break;
            default:
                break;
        }
    }

    private void submitComment(List<String> pathList) {
        Gson g = new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("travel_id", getIntent().getIntExtra(EXT_ID, 0) + "");
        params.put("star_level", star + "");
        params.put("content", contentEt.getText().toString());
        params.put("images", g.toJson(pathList));
        //  params.put("comment_travel_type_id", typeId + "");
        HttpUtils.doHttpReqeust("POST", "travelapi/v1/comment", params, "", TokenManager.get().getToken(TravelWriteCommentActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    commentSuccessDialog.show();
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    showToast(errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                showToast(e.getMessage());
            }
        });
    }

    private class PhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public PhotoAdapter(Context context) {
            super(R.layout.item_picture);
        }

        private List<String> getCirclePhoto() {
            List<String> pathList = new ArrayList<>();
            pathList.addAll(getData());
            pathList.remove(pathList.size() - 1);
            return pathList;
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            ImageView pictureIv = baseViewHolder.findView(R.id.iv_picture);
            ImageView deleteIv = baseViewHolder.findView(R.id.iv_picture_delete);

            if (baseViewHolder.getAdapterPosition() == getItemCount() - 1 && "0".equals(getItem(getItemCount() - 1))) {
                deleteIv.setVisibility(View.GONE);
                pictureIv.setScaleType(ImageView.ScaleType.FIT_XY);
                pictureIv.setImageResource(R.drawable.ic_addphoto);
                pictureIv.setBackgroundColor(Color.TRANSPARENT);
            } else {
                deleteIv.setVisibility(View.VISIBLE);
                GlideUtil.load(getContext(), s, pictureIv);
            }
        }
    }


}
