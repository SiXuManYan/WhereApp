package com.jcs.where.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.TravelCommentListBean;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.mango.ImageSelectListener;
import com.jcs.where.mango.Mango;
import com.jcs.where.mango.MultiplexImage;
import com.jcs.where.utils.ImageLoaders;
import com.jcs.where.view.ptr.MyPtrClassicFrameLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class TravelCommentActivity extends BaseActivity {

    private static final String EXT_ID = "id";
    private MyPtrClassicFrameLayout ptrFrame;
    private RecyclerView commentListRv;
    private int page = 1;
    private List<TravelCommentListBean.DataBean> list;
    private CommentAdapter commentAdapter;
    // 屏幕宽度
    public float Width;
    // 屏幕高度
    public float Height;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Width = dm.widthPixels;
        Height = dm.heightPixels;
        initView();
    }

    private void initView() {
        ptrFrame = V.f(this, R.id.ptr_frame);
        commentListRv = V.f(this, R.id.rv_commentlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TravelCommentActivity.this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        commentListRv.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(TravelCommentActivity.this);
        ptrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                initMoreData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                initData();
            }
        });
        initData();
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "travelapi/v1/comment/" + 1 + "?page=" + page, null, "", TokenManager.get().getToken(TravelCommentActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    TravelCommentListBean commentListBean = new Gson().fromJson(result, TravelCommentListBean.class);
                    list = commentListBean.getData();
                    if (list.size() < 10) {
                        ptrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
                    } else {
                        ptrFrame.setMode(PtrFrameLayout.Mode.BOTH);
                    }
                    commentAdapter.setData(list);
                    commentListRv.setAdapter(commentAdapter);
                    ptrFrame.refreshComplete();
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(TravelCommentActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ptrFrame.refreshComplete();
                ToastUtils.showLong(TravelCommentActivity.this, e.getMessage());
            }
        });
    }

    private void initMoreData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "travelapi/v1/comment/" + 1 + "?page=" + page, null, "", TokenManager.get().getToken(TravelCommentActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    TravelCommentListBean commentListBean = new Gson().fromJson(result, TravelCommentListBean.class);
                    if (commentListBean.getData().size() < 10) {
                        ptrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
                    } else {
                        ptrFrame.setMode(PtrFrameLayout.Mode.BOTH);
                    }
                    list.addAll(commentListBean.getData());
                    commentAdapter.setData(list);
                    commentListRv.setAdapter(commentAdapter);
                    ptrFrame.refreshComplete();

                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(TravelCommentActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ptrFrame.refreshComplete();
                ToastUtils.showLong(TravelCommentActivity.this, e.getMessage());
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_travelcomment;
    }


    private class CommentAdapter extends BaseQuickAdapter<TravelCommentListBean.DataBean> {

        private int ImagaId[] = {R.id.img_0, R.id.img_1, R.id.img_2, R.id.img_3, R.id.img_4, R.id.img_5, R.id.img_6, R.id.img_7, R.id.img_8};

        public CommentAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_commentlist;
        }

        @Override
        protected void initViews(QuickHolder holder, TravelCommentListBean.DataBean data, int position) {
            CircleImageView avaterIv = holder.findViewById(R.id.listuserimg);
            if (!TextUtils.isEmpty(data.getAvatar())) {
                Picasso.with(TravelCommentActivity.this).load(data.getAvatar()).into(avaterIv);
            } else {
                avaterIv.setImageDrawable(TravelCommentActivity.this.getResources().getDrawable(R.drawable.ic_noheader));
            }
            TextView nameTv = holder.findViewById(R.id.username);
            nameTv.setText(data.getUsername());
            TextView usercontent = holder.findViewById(R.id.usercontent);
            usercontent.setText(data.getContent());
            TextView timeTv = holder.findViewById(R.id.tv_time);
            timeTv.setText(data.getCreated_at());
            TextView fullText = holder.findViewById(R.id.fullText);
            ImageView star1Iv = holder.findViewById(R.id.iv_star1);
            ImageView star2Iv = holder.findViewById(R.id.iv_star2);
            ImageView star3Iv = holder.findViewById(R.id.iv_star3);
            ImageView star4Iv = holder.findViewById(R.id.iv_star4);
            ImageView star5Iv = holder.findViewById(R.id.iv_star5);
            if (data.getStar_level() == 1) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar_level() == 2) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar_level() == 3) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar_level() == 4) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar_level() == 5) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
            }
            GridLayout gridview = (GridLayout) holder.findViewById(R.id.gridview);
            ImageView showimage = (ImageView) holder.findViewById(R.id.showimage);
            RoundedImageView imgview[] = new RoundedImageView[9];
            for (int i = 0; i < 9; i++) {
                imgview[i] = (RoundedImageView) holder.findViewById(ImagaId[i]);
            }
            if (data.getImages().size() == 0) {
                showimage.setVisibility(View.GONE);
                gridview.setVisibility(View.GONE);
            } else if (data.getImages().size() > 0) {
                showimage.setVisibility(View.GONE);
                gridview.setVisibility(View.VISIBLE);
                int a = data.getImages().size() / 4;
                int b = data.getImages().size() % 4;
                if (b > 0) {
                    a++;
                }
                float width = (Width - dip2px(60) - dip2px(2)) / 4;
                gridview.getLayoutParams().height = (int) (a * width);

                for (int i = 0; i < 9; i++) {
                    imgview[i].setVisibility(View.GONE);
                }

                List<MultiplexImage> images = new ArrayList<>();
                images.clear();
                for (int i = 0; i < data.getImages().size(); i++) {
                    imgview[i].setVisibility(View.VISIBLE);
                    imgview[i].getLayoutParams().width = (int) width;
                    imgview[i].getLayoutParams().height = (int) width;
                    ImageLoaders.setsendimg(data.getImages().get(i), imgview[i]);
                    images.add(new MultiplexImage(data.getImages().get(i), MultiplexImage.ImageType.NORMAL));
                    int finalI = i;
                    imgview[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Mango.setImages(images);
                            Mango.setPosition(finalI);
                            Mango.setImageSelectListener(new ImageSelectListener() {
                                @Override
                                public void select(int index) {

                                }
                            });
                            try {
                                Mango.open(TravelCommentActivity.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            setContentLayout(usercontent, fullText);
            if (data.is_select) {
                fullText.setText("收起");
                usercontent.setMaxLines(50);
            } else {
                fullText.setText("全文");
                usercontent.setMaxLines(3);
            }

            fullText.setOnClickListener(new CommentAdapter.fullTextOnclick(usercontent, fullText, position));

        }

        private void setContentLayout(final TextView usercontent,
                                      final TextView fullText) {
            ViewTreeObserver vto = usercontent.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                private boolean isInit;

                @Override
                public boolean onPreDraw() {
                    if (isInit) {
                        return true;
                    }
                    Layout layout = usercontent.getLayout();
                    if (layout != null) {
                        int maxline = layout.getLineCount();
                        if (maxline > 3) {
                            fullText.setVisibility(View.VISIBLE);
                        } else {
                            fullText.setVisibility(View.GONE);
                        }
                        isInit = true;
                    }
                    return true;
                }
            });
        }

        class fullTextOnclick implements View.OnClickListener {

            private TextView usercontent;
            private TextView fullText;
            private int index;

            fullTextOnclick(TextView usercontent, TextView fullText, int index) {
                this.fullText = fullText;
                this.usercontent = usercontent;
                this.index = index;
            }

            @Override
            public void onClick(View v) {
                TravelCommentListBean.DataBean info = list.get(index);
                if (info.is_select) {
                    usercontent.setMaxLines(3);
                    fullText.setText("全文");
                    usercontent.invalidate();
                } else {
                    usercontent.setMaxLines(50);
                    fullText.setText("收起");
                    usercontent.invalidate();
                }
                info.is_select = !info.is_select;
                list.set(index, info);
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
