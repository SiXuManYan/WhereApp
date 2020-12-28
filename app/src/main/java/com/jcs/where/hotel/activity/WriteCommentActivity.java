package com.jcs.where.hotel.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.CommentTypeBean;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.dialog.CommentSuccessDialog;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.view.GridItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WriteCommentActivity extends BaseActivity implements View.OnClickListener {


    private static final String EXT_ID = "id";
    private static final String EXT_NAME = "name";
    private CommentAdapter mCommentAdapter;
    private PhotoAdapter mAdapter;
    private RecyclerView typeRv;
    private int oldPosition = -1;
    private ImageView starIv1, starIv2, starIv3, starIv4, starIv5;
    private TextView hotelNameTv, starTv;
    private int star = -1;
    private EditText contentEt;
    private Dialog commentSuccessDialog;
    private int typeId = -1;

    public static void goTo(Context context, int id, String name) {
        Intent intent = new Intent(context, WriteCommentActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.putExtra(EXT_NAME, name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.rv_photo);
        recyclerView.setLayoutManager(new GridLayoutManager(WriteCommentActivity.this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.addItemDecoration(new GridItemDecoration(WriteCommentActivity.this, 5, 0));
        mAdapter = new PhotoAdapter(R.layout.item_picture);
        mAdapter.addChildClickViewIds(R.id.iv_picture_delete);
        mAdapter.addData("0");
        recyclerView.setAdapter(mAdapter);
        typeRv = findViewById(R.id.rv_type);
        typeRv.setLayoutManager(new GridLayoutManager(WriteCommentActivity.this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        typeRv.addItemDecoration(new GridItemDecoration(WriteCommentActivity.this, 10, 20));
        mCommentAdapter = new CommentAdapter();
        starIv1 = findViewById(R.id.iv_star1);
        starIv2 = findViewById(R.id.iv_star2);
        starIv3 = findViewById(R.id.iv_star3);
        starIv4 = findViewById(R.id.iv_star4);
        starIv5 = findViewById(R.id.iv_star5);
        starTv = findViewById(R.id.tv_star);
        starTv.setVisibility(View.GONE);
        hotelNameTv = findViewById(R.id.tv_hotelname);
        hotelNameTv.setText(getIntent().getStringExtra(EXT_NAME));
        findViewById(R.id.tv_submit).setOnClickListener(this);
        contentEt = findViewById(R.id.et_conetnt);
        commentSuccessDialog = new CommentSuccessDialog(WriteCommentActivity.this, R.style.dialog, new CommentSuccessDialog.OnCloseListener() {
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
    protected void initData() {

    }

    @Override
    protected void bindListener() {
        starIv1.setOnClickListener(this);
        starIv2.setOnClickListener(this);
        starIv3.setOnClickListener(this);
        starIv4.setOnClickListener(this);
        starIv5.setOnClickListener(this);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_picture_delete) {
                mAdapter.removeAt(position);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_writecomment;
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
//                if (typeId == -1) {
//                    ToastUtils.showLong(WriteCommentActivity.this, "请选择出行类型");
//                    return;
//                }
                showLoading();
                //TODO 上传图片
                break;
            default:
                break;
        }
    }

    private void submitComment(List<String> pathList) {
        Gson g = new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("star", star + "");
        params.put("content", contentEt.getText().toString().trim());
        params.put("images", g.toJson(pathList));
        //  params.put("comment_travel_type_id", typeId + "");
        params.put("hotel_id", getIntent().getIntExtra(EXT_ID, 0) + "");
        // params.put("order_id", "1");
        HttpUtils.doHttpReqeust("POST", "hotelapi/v1/hotel/comment", params, "", TokenManager.get().getToken(WriteCommentActivity.this), new HttpUtils.StringCallback() {
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


        public PhotoAdapter(int layoutResId) {
            super(layoutResId);
        }

        private List<String> getCirclePhoto() {
            List<String> pathList = getData();
            pathList.remove(pathList.size() - 1);
            return pathList;
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            ImageView pictureIv = baseViewHolder.findView(R.id.iv_picture);
            ImageView deleteIv = baseViewHolder.findView(R.id.iv_picture_delete);
            pictureIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (baseViewHolder.getAdapterPosition() == getItemCount() - 1) {
                        if (getItemCount() - 1 == 4) {
                            showToast("最多4张图片");
                            return;
                        }
                        int size = getItemCount() - 1;

                    }
                }
            });

            if (baseViewHolder.getAdapterPosition() == getItemCount() - 1 && "0".equals(getItem(getItemCount() - 1))) {
                deleteIv.setVisibility(View.GONE);
                pictureIv.setScaleType(ImageView.ScaleType.FIT_XY);
                pictureIv.setImageResource(R.drawable.ic_addphoto);
                pictureIv.setBackgroundColor(Color.TRANSPARENT);
            } else {
                deleteIv.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(s).into(pictureIv);
            }
        }
    }

    private class CommentAdapter extends BaseQuickAdapter<CommentTypeBean, BaseViewHolder> {


        public CommentAdapter() {
            super(R.layout.item_commenttype);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, CommentTypeBean data) {

            TextView typeTv = baseViewHolder.findView(R.id.tv_type);
            typeTv.setText(data.getName());
            typeTv.setTextColor(getResources().getColor(R.color.grey_666666));
            RelativeLayout typeRl = baseViewHolder.findView(R.id.rl_type);
            typeRl.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_priceunselect));
            typeRl.setOnClickListener(view -> {
                if (baseViewHolder.getAdapterPosition() != oldPosition) {
                    typeRl.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_labelblue));
                    typeTv.setTextColor(getResources().getColor(R.color.blue_4C9EF2));
                    typeId = data.getId();
                    notifyItemChanged(oldPosition);
                    oldPosition = baseViewHolder.getAdapterPosition();
                }
            });

        }
    }

}
