package com.jcs.where.hotel.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.CommentListBean;
import com.jcs.where.bean.ErrorBean;
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

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class CommentListFragment extends BaseFragment {

    // 屏幕宽度
    public float Width;
    // 屏幕高度
    public float Height;
    private View view;
    private MyPtrClassicFrameLayout ptrFrame;
    private RecyclerView commentListRv;
    private int page = 1;
    private List<CommentListBean.DataBean> list;
    private CommentListBean useCommentListBean;
    private CommentAdapter commentAdapter;

    public static CommentListFragment newInstance(String id, String type) {
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("type", type);
        CommentListFragment fragment = new CommentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        Width = dm.widthPixels;
        Height = dm.heightPixels;
    }

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commentlist, container, false);
        initView();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && isVisible()) {
            if (useCommentListBean == null) {
                page = 1;
                getdata();
            }
        }
    }

    private void initView() {
        ptrFrame = V.f(view, R.id.ptr_frame);
        commentListRv = V.f(view, R.id.rv_commentlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        commentListRv.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(getContext());
        ptrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                getmoredata();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getdata();
            }
        });
        getdata();
    }

    private void getdata() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotel/" + getArguments().getString("id") + "/comments" + "?type=" + getArguments().getString("type") + "&page=" + page, null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    CommentListBean commentListBean = new Gson().fromJson(result, CommentListBean.class);
                    Log.d("ssss", commentListBean.getData().size() + "");
                    useCommentListBean = commentListBean;
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
                    ToastUtils.showLong(getContext(), errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ptrFrame.refreshComplete();
                ToastUtils.showLong(getContext(), e.getMessage());
            }
        });
    }

    private void getmoredata() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotel/" + getArguments().getString("id") + "/comments" + "?type=" + getArguments().getString("type") + "&page=" + page, null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    CommentListBean commentListBean = new Gson().fromJson(result, CommentListBean.class);
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
                    ToastUtils.showLong(getContext(), errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ptrFrame.refreshComplete();
                ToastUtils.showLong(getContext(), e.getMessage());
            }
        });
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private class CommentAdapter extends BaseQuickAdapter<CommentListBean.DataBean> {

        private final int[] ImagaId = {R.id.img_0, R.id.img_1, R.id.img_2, R.id.img_3, R.id.img_4, R.id.img_5, R.id.img_6, R.id.img_7, R.id.img_8};

        public CommentAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_commentlist;
        }

        @Override
        protected void initViews(QuickHolder holder, CommentListBean.DataBean data, int position) {
            CircleImageView avaterIv = holder.findViewById(R.id.listuserimg);
            if (!TextUtils.isEmpty(data.getAvatar())) {
                Picasso.with(getContext()).load(data.getAvatar()).into(avaterIv);
            } else {
                avaterIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_test));
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
            if (data.getStar() == 1) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar() == 2) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar() == 3) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar() == 4) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar() == 5) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
            }
            GridLayout gridview = (GridLayout) holder.findViewById(R.id.gridview);
            ImageView showimage = (ImageView) holder.findViewById(R.id.showimage);
            RoundedImageView[] imgview = new RoundedImageView[9];
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
                                Mango.open(getContext());
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

            fullText.setOnClickListener(new fullTextOnclick(usercontent, fullText, position));

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
                CommentListBean.DataBean info = list.get(index);
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


}
