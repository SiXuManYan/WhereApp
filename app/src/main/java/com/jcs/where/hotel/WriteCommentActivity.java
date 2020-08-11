package com.jcs.where.hotel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.CommentTypeBean;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.dialog.CommentSuccessDialog;
import com.jcs.where.dialog.ResetSuccessDialog;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.presenter.UploadFilePresenter;
import com.jcs.where.utils.GlideEngine;
import com.jcs.where.view.GridItemDecoration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.manager.ImageLoader;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;
import rx.Subscriber;

public class WriteCommentActivity extends BaseActivity implements View.OnClickListener {


    private static final String EXT_ID = "id";
    private static final String EXT_NAME = "name";
    private PhotoAdapter mAdapter;
    private CommentAdapter commentAdapter;
    private RecyclerView typeRv;
    private int oldPosition = -1;
    private ImageView starIv1, starIv2, starIv3, starIv4, starIv5;
    private TextView hotelNameTv, starTv;
    private int star = -1;
    private EditText contentEt;
    private int typeId = -1;
    private UploadFilePresenter mUploadPresenter;
    private Dialog commentSuccessDialog;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        mUploadPresenter = new UploadFilePresenter(WriteCommentActivity.this);
        ininView();
    }

    private void ininView() {
        RecyclerView recyclerView = V.f(this, R.id.rv_photo);
        recyclerView.setLayoutManager(new GridLayoutManager(WriteCommentActivity.this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.addItemDecoration(new GridItemDecoration(WriteCommentActivity.this, 5, 0));
        mAdapter = new PhotoAdapter(WriteCommentActivity.this);
        mAdapter.addData("0");
        recyclerView.setAdapter(mAdapter);
        typeRv = V.f(this, R.id.rv_type);
        typeRv.setLayoutManager(new GridLayoutManager(WriteCommentActivity.this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        typeRv.addItemDecoration(new GridItemDecoration(WriteCommentActivity.this, 10, 20));
        commentAdapter = new CommentAdapter(WriteCommentActivity.this);
        starIv1 = V.f(this, R.id.iv_star1);
        starIv1.setOnClickListener(this);
        starIv2 = V.f(this, R.id.iv_star2);
        starIv2.setOnClickListener(this);
        starIv3 = V.f(this, R.id.iv_star3);
        starIv3.setOnClickListener(this);
        starIv4 = V.f(this, R.id.iv_star4);
        starIv4.setOnClickListener(this);
        starIv5 = V.f(this, R.id.iv_star5);
        starIv5.setOnClickListener(this);
        starTv = V.f(this, R.id.tv_star);
        starTv.setVisibility(View.GONE);
        hotelNameTv = V.f(this, R.id.tv_hotelname);
        hotelNameTv.setText(getIntent().getStringExtra(EXT_NAME));
        V.f(this, R.id.tv_submit).setOnClickListener(this);
        contentEt = V.f(this, R.id.et_conetnt);
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
        initType();
    }

    private void initType() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/comment/types", null, "", TokenManager.get().getToken(WriteCommentActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<CommentTypeBean>>() {
                    }.getType();
                    List<CommentTypeBean> list = gson.fromJson(result, type);
                    commentAdapter.setData(list);
                    typeRv.setAdapter(commentAdapter);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(WriteCommentActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(WriteCommentActivity.this, e.getMessage());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_writecomment;
    }

    private void refresh(List<Photo> photos) {
        List<String> selectedPaths = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            selectedPaths.add(photos.get(i).path);
        }
        mAdapter.removeData(mAdapter.getItemCount() - 1);
        mAdapter.addData(selectedPaths);
        mAdapter.addData("0");
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_star1:
                starIv1.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starIv3.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starIv4.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starIv5.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starTv.setText("一般");
                starTv.setVisibility(View.VISIBLE);
                star = 1;
                break;
            case R.id.iv_star2:
                starIv1.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv3.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starIv4.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starIv5.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starTv.setText("不错");
                starTv.setVisibility(View.VISIBLE);
                star = 2;
                break;
            case R.id.iv_star3:
                starIv1.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv3.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv4.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starIv5.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starTv.setText("很好");
                starTv.setVisibility(View.VISIBLE);
                star = 3;
                break;
            case R.id.iv_star4:
                starIv1.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv3.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv4.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv5.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_greystar));
                starTv.setText("非常好");
                starTv.setVisibility(View.VISIBLE);
                star = 4;
                break;
            case R.id.iv_star5:
                starIv1.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv2.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv3.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv4.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starIv5.setImageDrawable(getResources().getDrawable(R.drawable.ic_writecomment_lightstar));
                starTv.setText("好极了");
                starTv.setVisibility(View.VISIBLE);
                star = 5;
                break;
            case R.id.tv_submit:
                if (star == -1) {
                    ToastUtils.showLong(WriteCommentActivity.this, "请选择评分");
                    return;
                }
                if (TextUtils.isEmpty(contentEt.getText().toString().trim())) {
                    ToastUtils.showLong(WriteCommentActivity.this, "请输入评论内容");
                    return;
                }
                if (mAdapter.getCirclePhoto().size() == 0) {
                    ToastUtils.showLong(WriteCommentActivity.this, "请选择需要上传的图片");
                    return;
                }
                if (typeId == -1) {
                    ToastUtils.showLong(WriteCommentActivity.this, "请选择出行类型");
                    return;
                }
                showLoading();
                addSubscription(mUploadPresenter.uploadFiles(mAdapter.getCirclePhoto())
                        .subscribe(new Subscriber<List<String>>() {
                            @Override
                            public void onCompleted() {
                                stopLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                stopLoading();
                                e.printStackTrace();
                                ToastUtils.showLong(WriteCommentActivity.this, e.getMessage());
                            }

                            @Override
                            public void onNext(List<String> strings) {
                                Log.d("ssss", strings + "");
                                stopLoading();
                                submitComment(strings);
                            }
                        }));
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
        params.put("comment_travel_type_id", typeId + "");
        params.put("hotel_id", getIntent().getIntExtra(EXT_ID, 0) + "");
        Log.d("ssss", params + "");
        HttpUtils.doHttpReqeust("POST", "hotelapi/v1/hotel/comment", params, "", TokenManager.get().getToken(WriteCommentActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    commentSuccessDialog.show();
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(WriteCommentActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(WriteCommentActivity.this, e.getMessage());
            }
        });
    }

    private class PhotoAdapter extends BaseQuickAdapter<String> {

        public PhotoAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_picture;
        }

        @Override
        protected void initViews(QuickHolder holder, String data, final int position) {
            ImageView pictureIv = holder.findViewById(R.id.iv_picture);
            ImageView deleteIv = holder.findViewById(R.id.iv_picture_delete);
            pictureIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == getItemCount() - 1) {
                        if (getItemCount() - 1 == 4) {
                            ToastUtils.showShort(mContext, "最多4张图片");
                            return;
                        }
                        int size = getItemCount() - 1;
                        EasyPhotos.createAlbum((Activity) mContext, true, GlideEngine.getInstance())
                                .setFileProviderAuthority("com.jcs.where.fileprovider")
                                .setCount(4 - size)
                                .start(new SelectCallback() {
                                    @Override
                                    public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                                        refresh(photos);
                                    }
                                });

                    }
                }
            });
            deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeData(position);
                }
            });

            if (position == getItemCount() - 1 && "0".equals(getItem(getItemCount() - 1))) {
                deleteIv.setVisibility(View.GONE);
                pictureIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                pictureIv.setImageResource(R.drawable.ic_addphoto);
                pictureIv.setBackgroundColor(Color.TRANSPARENT);
            } else {
                deleteIv.setVisibility(View.VISIBLE);
                ImageLoader.get().load(pictureIv, data, false);
            }
        }

        private List<String> getCirclePhoto() {
            List<String> pathList = new ArrayList<>();
            pathList.addAll(getData());
            pathList.remove(pathList.size() - 1);
            return pathList;
        }
    }

    private class CommentAdapter extends BaseQuickAdapter<CommentTypeBean> {

        public CommentAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_commenttype;
        }

        @Override
        protected void initViews(QuickHolder holder, CommentTypeBean data, int position) {
            TextView typeTv = holder.findViewById(R.id.tv_type);
            typeTv.setText(data.getName());
            typeTv.setTextColor(getResources().getColor(R.color.grey_666666));
            RelativeLayout typeRl = holder.findViewById(R.id.rl_type);
            typeRl.setBackground(getResources().getDrawable(R.drawable.bg_priceunselect));
            typeRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position != oldPosition) {
                        typeRl.setBackground(getResources().getDrawable(R.drawable.bg_labelblue));
                        typeTv.setTextColor(getResources().getColor(R.color.blue_4C9EF2));
                        typeId = data.getId();
                        notifyItemChanged(oldPosition);
                        oldPosition = position;
                    }
                }
            });
        }
    }

}
