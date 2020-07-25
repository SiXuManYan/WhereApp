package com.jcs.where.hotel.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelListBean;
import com.jcs.where.hotel.HotelDetailActivity;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.view.ptr.MyPtrClassicFrameLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class HotelListFragment extends BaseFragment {
    private View view;
    private TextView testTv;
    private MyPtrClassicFrameLayout ptrFrame;
    private RecyclerView hotelListRv;
    private int page = 1;
    private HotelListAdpater hotelListAdpater;
    private List<HotelListBean.DataBean> list;


    public static HotelListFragment newInstance(String hotelTypeIds, String cityId, String price, String star, String startDate, String endDate, String startWeek, String endWeek, String allDay, String startYear, String endYear) {
        Bundle args = new Bundle();
        args.putString("hotelTypeIds", hotelTypeIds);
        args.putString("cityId", cityId);
        args.putString("price", price);
        args.putString("star", star);
        args.putString("startDate", startDate);
        args.putString("endDate", endDate);
        args.putString("startWeek", startWeek);
        args.putString("endWeek", endWeek);
        args.putString("allDay", allDay);
        args.putString("startYear", startYear);
        args.putString("endYear", endYear);
        HotelListFragment fragment = new HotelListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);
        initView();
        return view;
    }

    private void initView() {
        ptrFrame = V.f(view, R.id.ptr_frame);
        hotelListRv = V.f(view, R.id.rv_hotellist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        hotelListRv.setLayoutManager(linearLayoutManager);
        hotelListAdpater = new HotelListAdpater(getContext());
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
        String url = null;
        if (getArguments().getString("price") == null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&star_level=" + getArguments().getString("star");
        }
        if (getArguments().getString("star") == null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&price_range=" + getArguments().getString("price");
        }
        if (getArguments().getString("price") == null && getArguments().getString("star") == null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]";
        }
        if (getArguments().getString("price") != null && getArguments().getString("star") != null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&star_level=" + getArguments().getString("star") + "&price_range=" + getArguments().getString("price");
        }
        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    HotelListBean hotelListBean = new Gson().fromJson(result, HotelListBean.class);
                    list = hotelListBean.getData();
                    if (list.size() < 10) {
                        ptrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
                    } else {
                        ptrFrame.setMode(PtrFrameLayout.Mode.BOTH);
                    }
                    hotelListAdpater.setData(list);
                    hotelListRv.setAdapter(hotelListAdpater);
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
        String url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]";
        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    HotelListBean hotelListBean = new Gson().fromJson(result, HotelListBean.class);
                    if (hotelListBean.getData().size() < 10) {
                        ptrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
                    } else {
                        ptrFrame.setMode(PtrFrameLayout.Mode.BOTH);
                    }
                    list.addAll(hotelListBean.getData());
                    hotelListAdpater.setData(list);
                    hotelListRv.setAdapter(hotelListAdpater);
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


    private class HotelListAdpater extends BaseQuickAdapter<HotelListBean.DataBean> {

        public HotelListAdpater(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_hotellist;
        }

        @Override
        protected void initViews(QuickHolder holder, final HotelListBean.DataBean data, int position) {
            RoundedImageView photoIv = holder.findViewById(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImages().get(0))) {
                Picasso.with(getContext()).load(data.getImages().get(0)).into(photoIv);
            } else {
                photoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
            }
            TextView nameTv = holder.findViewById(R.id.tv_name);
            nameTv.setText(data.getName());
            TextView tagOneTv = holder.findViewById(R.id.tv_tagone);
            TextView tagTwoTv = holder.findViewById(R.id.tv_tagtwo);
            LinearLayout tagLl = holder.findViewById(R.id.ll_tag);
            if (data.getTags().size() == 0) {
                tagLl.setVisibility(View.GONE);
            } else if (data.getTags().size() == 1) {
                tagLl.setVisibility(View.VISIBLE);
                tagOneTv.setText(data.getTags().get(0).getName());
            } else {
                tagLl.setVisibility(View.VISIBLE);
                tagOneTv.setText(data.getTags().get(0).getName());
                tagTwoTv.setText(data.getTags().get(1).getName());
            }
            TextView addressTv = holder.findViewById(R.id.tv_address);
            addressTv.setText(data.getAddress());
            TextView distanceTv = holder.findViewById(R.id.tv_distance);
            distanceTv.setText("<" + data.getDistance() + "Km");
            TextView scoreTv = holder.findViewById(R.id.tv_score);
            scoreTv.setText(data.getGrade() + "");
            TextView commentNumTv = holder.findViewById(R.id.tv_commentnumber);
            commentNumTv.setText(data.getComment_counts() + "条评论");
            TextView priceTv = holder.findViewById(R.id.tv_price);
            priceTv.setText("₱" + data.getPrice() + "起");
            holder.findViewById(R.id.ll_hotel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HotelDetailActivity.goTo(getContext(), data.getId(), getArguments().getString("startDate"), getArguments().getString("endDate"), getArguments().getString("startWeek"), getArguments().getString("endWeek"), getArguments().getString("allDay"), getArguments().getString("startYear"), getArguments().getString("endYear"));
                }
            });
        }
    }

}
