package com.jcs.where.hotel.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelListBean;
import com.jcs.where.hotel.HotelDetailActivity;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.view.ptr.MyPtrClassicFrameLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class HotelListFragment extends BaseFragment {
    private MyPtrClassicFrameLayout ptrFrame;
    private RecyclerView hotelListRv;
    private int page = 1;
    private HotelListAdpater hotelListAdpater;
    private List<HotelListBean.DataBean> list;
    private String useInputText = "";
    private String mStartYear, mStartDate, mStartWeek, mEndYear, mEndData, mEndWeek, mAllDay, mRoomNum;


    public static HotelListFragment newInstance(String hotelTypeIds, String cityId, String price, String star, String startDate, String endDate, String startWeek, String endWeek, String allDay, String startYear, String endYear, String roomNumber) {
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
        args.putString("roomNumber", roomNumber);
        HotelListFragment fragment = new HotelListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void getdata() {
        showLoading();
        String url = null;
        if (getArguments().getString("price") == null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&star_level=" + getArguments().getString("star") + "&search_input=" + useInputText;
        }
        if (getArguments().getString("star") == null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&price_range=" + getArguments().getString("price") + "&search_input=" + useInputText;
        }
        if (getArguments().getString("price") == null && getArguments().getString("star") == null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&search_input=" + useInputText;
        }
        if (getArguments().getString("price") != null && getArguments().getString("star") != null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&star_level=" + getArguments().getString("star") + "&price_range=" + getArguments().getString("price") + "&search_input=" + useInputText;
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
                    hotelListAdpater.addData(list);
                    hotelListRv.setAdapter(hotelListAdpater);
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

    private void getmoredata() {
        showLoading();
        String url = null;
        if (getArguments().getString("price") == null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&star_level=" + getArguments().getString("star") + "&search_input=" + useInputText;
        }
        if (getArguments().getString("star") == null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&price_range=" + getArguments().getString("price") + "&search_input=" + useInputText;
        }
        if (getArguments().getString("price") == null && getArguments().getString("star") == null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&search_input=" + useInputText;
        }
        if (getArguments().getString("price") != null && getArguments().getString("star") != null) {
            url = "hotelapi/v1/hotels?page=" + page + "&area_id=" + getArguments().getString("cityId") + "&hotel_type_ids=[" + getArguments().getString("hotelTypeIds") + "]" + "&star_level=" + getArguments().getString("star") + "&price_range=" + getArguments().getString("price") + "&search_input=" + useInputText;
        }
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
                    hotelListAdpater.addData(list);
                    hotelListRv.setAdapter(hotelListAdpater);
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

    public void setSearchText(String inputText) {
        if (hotelListRv != null) {
            hotelListRv.removeAllViews();
        }
        if (hotelListAdpater != null) {
            hotelListAdpater.getData().clear();
            hotelListAdpater.notifyDataSetChanged();
        }
        if (list != null) {
            list.clear();
        }
        page = 1;
        useInputText = inputText;
        getdata();
    }

    public void changeData(String startData, String endDate, String startWeek, String endWeek, String allDay, String startYear, String endYear, String roomNum) {
        mStartDate = startData;
        mEndData = endDate;
        mStartWeek = startWeek;
        mEndWeek = endWeek;
        mAllDay = allDay;
        mStartYear = startYear;
        mEndYear = endYear;
        mRoomNum = roomNum;
    }

    @Override
    protected void initView(View view) {
        ptrFrame = view.findViewById(R.id.ptr_frame);
        hotelListRv = view.findViewById(R.id.rv_hotellist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        hotelListRv.setLayoutManager(linearLayoutManager);
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
    }

    @Override
    protected void initData() {
        mStartYear = getArguments().getString("startYear");
        mStartDate = getArguments().getString("startDate");
        mStartWeek = getArguments().getString("startWeek");
        mEndYear = getArguments().getString("endYear");
        mEndData = getArguments().getString("endDate");
        mEndWeek = getArguments().getString("endWeek");
        mAllDay = getArguments().getString("allDay");
        mRoomNum = getArguments().getString("roomNumber");

        hotelListAdpater = new HotelListAdpater();
        hotelListAdpater.addChildClickViewIds(R.id.ll_hotel);

        getdata();
    }

    @Override
    protected void bindListener() {
        hotelListAdpater.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.ll_hotel) {
                    HotelDetailActivity.goTo(getContext(), (int) adapter.getItemId(position), mStartDate, mEndData, mStartWeek, mEndWeek, mAllDay, mStartYear, mEndYear, mRoomNum);
                }

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    private class HotelListAdpater extends BaseQuickAdapter<HotelListBean.DataBean, BaseViewHolder> {


        public HotelListAdpater() {
            super(R.layout.item_hotellist);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelListBean.DataBean data) {

            RoundedImageView photoIv = baseViewHolder.findView(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImages().get(0))) {
                Glide.with(getContext()).load(data.getImages().get(0)).into(photoIv);
            } else {
                photoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
            }
            TextView nameTv = baseViewHolder.findView(R.id.tv_name);
            nameTv.setText(data.getName());
            if (data.getFacebook_link() == null) {
                nameTv.setCompoundDrawables(null, null, null, null);
            }
            TextView tagOneTv = baseViewHolder.findView(R.id.tv_tagone);
            TextView tagTwoTv = baseViewHolder.findView(R.id.tv_tagtwo);
            LinearLayout tagLl = baseViewHolder.findView(R.id.ll_tag);
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
            TextView addressTv = baseViewHolder.findView(R.id.tv_address);
            addressTv.setText(data.getAddress());
            TextView distanceTv = baseViewHolder.findView(R.id.tv_distance);
            String distanceText = "<" + data.getDistance() + "Km";
            distanceTv.setText(distanceText);
            TextView scoreTv = baseViewHolder.findView(R.id.tv_score);
            String scoreText = data.getGrade() + "";
            scoreTv.setText(scoreText);
            TextView commentNumTv = baseViewHolder.findView(R.id.tv_commentnumber);
            String commentNumText = data.getComment_counts() + "条评论";
            commentNumTv.setText(commentNumText);
            TextView priceTv = baseViewHolder.findView(R.id.tv_price);
            String priceText = "₱" + data.getPrice() + "起";
            priceTv.setText(priceText);
        }

        @Override
        public long getItemId(int position) {
            return getData().get(position).getId();
        }
    }
}
