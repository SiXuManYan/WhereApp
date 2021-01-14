package com.jcs.where.travel.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.TravelListBean;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.travel.TravelDetailActivity;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.ImagePreviewActivity;
import com.jcs.where.view.ptr.MyPtrClassicFrameLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class TravelListFragment extends BaseFragment {

    private View view;
    private MyPtrClassicFrameLayout ptrFrame;
    private RecyclerView travelListRv;
    private int page = 1;
    private TravelAdapter travelAdapter;
    private List<TravelListBean.DataBean> useList;

    public static TravelListFragment newInstance(String travelId, String lat, String lng) {
        Bundle args = new Bundle();
        args.putString("travelId", travelId);
        args.putString("lat", lat);
        args.putString("lng", lng);
        TravelListFragment fragment = new TravelListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        ptrFrame = view.findViewById(R.id.ptr_frame);
        travelListRv = view.findViewById(R.id.rv_travellist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        travelListRv.setLayoutManager(linearLayoutManager);
        travelAdapter = new TravelAdapter();
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
        String url = "travelapi/v1/travel?category_id=" + getArguments().getString("travelId") + "&lat=" + getArguments().getString("lat") + "&lng=" + getArguments().getString("lng") + "&page=" + page;
        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    TravelListBean travelListBean = new Gson().fromJson(result, TravelListBean.class);
                    useList = travelListBean.getData();
                    if (useList.size() < 10) {
                        ptrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
                    } else {
                        ptrFrame.setMode(PtrFrameLayout.Mode.BOTH);
                    }
                    travelAdapter.setNewInstance(useList);
                    travelListRv.setAdapter(travelAdapter);
                    ptrFrame.refreshComplete();
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

    private void getmoredata() {
        showLoading();
        String url = "travelapi/v1/travel?category_id=" + getArguments().getString("travelId") + "&lat=" + getArguments().getString("lat") + "&lng=" + getArguments().getString("lng") + "&page=" + page;
        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    TravelListBean travelListBean = new Gson().fromJson(result, TravelListBean.class);
                    if (travelListBean.getData().size() < 10) {
                        ptrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
                    } else {
                        ptrFrame.setMode(PtrFrameLayout.Mode.BOTH);
                    }
                    useList.addAll(travelListBean.getData());
                    travelAdapter.setNewInstance(useList);
                    travelListRv.setAdapter(travelAdapter);
                    ptrFrame.refreshComplete();

                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    showToast(errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ptrFrame.refreshComplete();
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_travellist;
    }

    private static class TravelAdapter extends BaseQuickAdapter<TravelListBean.DataBean, BaseViewHolder> {

        public TravelAdapter() {
            super(R.layout.item_travellist);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, TravelListBean.DataBean data) {
            RoundedImageView photoIv = baseViewHolder.findView(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImage())) {
                GlideUtil.load(getContext(), data.getImage(), photoIv);
            } else {
                photoIv.setImageResource(R.mipmap.ic_glide_default);
            }
            TextView nameTv = baseViewHolder.findView(R.id.tv_name);
            nameTv.setText(data.getName());

            TextView tagOneTv = baseViewHolder.findView(R.id.tv_tagone);
            TextView tagTwoTv = baseViewHolder.findView(R.id.tv_tagtwo);
            LinearLayout tagLl = baseViewHolder.findView(R.id.ll_tag);
            if (data.getTags().size() == 0) {
                tagLl.setVisibility(View.GONE);
            } else if (data.getTags().size() == 1) {
                tagLl.setVisibility(View.VISIBLE);
                tagOneTv.setText(data.getTags().get(0));
            } else {
                tagLl.setVisibility(View.VISIBLE);
                tagOneTv.setText(data.getTags().get(0));
                tagTwoTv.setText(data.getTags().get(1));
            }
            TextView addressTv = baseViewHolder.findView(R.id.tv_address);
            addressTv.setText(data.getAddress());
            TextView distanceTv = baseViewHolder.findView(R.id.tv_distance);
            distanceTv.setText("<" + data.getKm() + "Km");
            TextView scoreTv = baseViewHolder.findView(R.id.tv_score);
            scoreTv.setText(data.getGrade() + "");
            TextView commentNumTv = baseViewHolder.findView(R.id.tv_commentnumber);
            commentNumTv.setText(data.getComments_count() + "条评论");
            baseViewHolder.findView(R.id.ll_travel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TravelDetailActivity.goTo(getContext(), data.getId());
                }
            });
        }
    }
}
