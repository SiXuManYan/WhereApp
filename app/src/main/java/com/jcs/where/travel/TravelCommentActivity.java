package com.jcs.where.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.api.response.CommentResponse;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.view.ptr.MyPtrClassicFrameLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 页面-旅游评论
 */
public class TravelCommentActivity extends BaseActivity {

    private static final String EXT_ID = "id";
    private MyPtrClassicFrameLayout ptrFrame;
    private RecyclerView commentListRv;
    private int page = 1;
    private List<CommentResponse> list;
    private CommentAdapter commentAdapter;

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
        ptrFrame = findViewById(R.id.ptr_frame);
        commentListRv = findViewById(R.id.rv_commentlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TravelCommentActivity.this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        commentListRv.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter();
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
    }

    @Override
    protected void initData() {

        showLoading();
//        HttpUtils.doHttpReqeust("GET", "travelapi/v1/comment/" + 1 + "?page=" + page, null, "", TokenManager.get().getToken(TravelCommentActivity.this), new HttpUtils.StringCallback() {
//            @Override
//            public void onSuccess(int code, String result) {
//                stopLoading();
//                if (code == 200) {
//                    TouristAttractionCommentListResponse commentListBean = new Gson().fromJson(result, TouristAttractionCommentListResponse.class);
//                    list = commentListBean.getData();
//                    if (list.size() < 10) {
//                        ptrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
//                    } else {
//                        ptrFrame.setMode(PtrFrameLayout.Mode.BOTH);
//                    }
//                    commentAdapter.setNewInstance(list);
//                    commentListRv.setAdapter(commentAdapter);
//                    ptrFrame.refreshComplete();
//                } else {
//                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
//                    showToast(errorBean.message);
//                }
//            }
//
//            @Override
//            public void onFaileure(int code, Exception e) {
//                stopLoading();
//                ptrFrame.refreshComplete();
//                showToast(e.getMessage());
//            }
//        });
    }

    @Override
    protected void bindListener() {

    }

    private void initMoreData() {
        showLoading();
//        HttpUtils.doHttpReqeust("GET", "travelapi/v1/comment/" + 1 + "?page=" + page, null, "", TokenManager.get().getToken(TravelCommentActivity.this), new HttpUtils.StringCallback() {
//            @Override
//            public void onSuccess(int code, String result) {
//                stopLoading();
//                if (code == 200) {
//                    TouristAttractionCommentListResponse commentListBean = new Gson().fromJson(result, TouristAttractionCommentListResponse.class);
//                    if (commentListBean.getData().size() < 10) {
//                        ptrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
//                    } else {
//                        ptrFrame.setMode(PtrFrameLayout.Mode.BOTH);
//                    }
//                    list.addAll(commentListBean.getData());
//                    commentAdapter.addData(list);
//                    commentListRv.setAdapter(commentAdapter);
//                    ptrFrame.refreshComplete();
//
//                } else {
//                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
//                    showToast(errorBean.message);
//                }
//            }
//
//            @Override
//            public void onFaileure(int code, Exception e) {
//                stopLoading();
//                ptrFrame.refreshComplete();
//                showToast(e.getMessage());
//            }
//        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_travelcomment;
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private class CommentAdapter extends BaseQuickAdapter<CommentResponse, BaseViewHolder> {

        public CommentAdapter() {
            super(R.layout.item_comment_list);
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

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, CommentResponse data) {
            CircleImageView avaterIv = baseViewHolder.findView(R.id.circleIcon);
            if (!TextUtils.isEmpty(data.getAvatar())) {
                GlideUtil.load(getContext(), data.getAvatar(), avaterIv);
            } else {
                avaterIv.setImageResource(R.mipmap.ic_glide_default);
            }
            TextView nameTv = baseViewHolder.findView(R.id.username);
            nameTv.setText(data.getUsername());
            TextView usercontent = baseViewHolder.findView(R.id.commentContent);
            usercontent.setText(data.getContent());
            TextView timeTv = baseViewHolder.findView(R.id.tv_time);
            timeTv.setText(data.getCreatedAt());
            TextView fullText = baseViewHolder.findView(R.id.fullText);
            ImageView star1Iv = baseViewHolder.findView(R.id.iv_star1);
            ImageView star2Iv = baseViewHolder.findView(R.id.iv_star2);
            ImageView star3Iv = baseViewHolder.findView(R.id.iv_star3);
            ImageView star4Iv = baseViewHolder.findView(R.id.iv_star4);
            ImageView star5Iv = baseViewHolder.findView(R.id.iv_star5);
            if (data.getStarLevel() == 1) {
                star1Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
                star3Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
                star4Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
            } else if (data.getStarLevel() == 2) {
                star1Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
                star4Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
            } else if (data.getStarLevel() == 3) {
                star1Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
            } else if (data.getStarLevel() == 4) {
                star1Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star5Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistgreystar));
            } else if (data.getStarLevel() == 5) {
                star1Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
                star5Iv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_commentlistlightstar));
            }
            setContentLayout(usercontent, fullText);
//            if (data.is_select) {
//                fullText.setText(getString(R.string.put_up));
//                usercontent.setMaxLines(50);
//            } else {
//                fullText.setText(getString(R.string.full_text));
//                usercontent.setMaxLines(3);
//            }

            fullText.setOnClickListener(new CommentAdapter.fullTextOnclick(usercontent, fullText, baseViewHolder.getAdapterPosition()));

        }

        class fullTextOnclick implements View.OnClickListener {

            private final TextView usercontent;
            private final TextView fullText;
            private final int index;

            fullTextOnclick(TextView usercontent, TextView fullText, int index) {
                this.fullText = fullText;
                this.usercontent = usercontent;
                this.index = index;
            }

            @Override
            public void onClick(View v) {
                CommentResponse info = list.get(index);
//                if (info.is_select) {
//                    usercontent.setMaxLines(3);
//                    fullText.setText(getString(R.string.full_text));
//                    usercontent.invalidate();
//                } else {
//                    usercontent.setMaxLines(50);
//                    fullText.setText(getString(R.string.put_up));
//                    usercontent.invalidate();
//                }
//                info.is_select = !info.is_select;
                list.set(index, info);
            }
        }
    }
}
