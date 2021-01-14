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
import com.jcs.where.hotel.activity.HotelOrderDetailActivity;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelListBean;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.hotel.helper.HotelSelectDateHelper;
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
    private String mStartYear, mEndYear;
    private int mTotalDay, mRoomNum;
    private JcsCalendarAdapter.CalendarBean mStartDateBean;
    private JcsCalendarAdapter.CalendarBean mEndDateBean;

    public static HotelListFragment newInstance(String hotelTypeIds, String cityId, String price, String star, JcsCalendarAdapter.CalendarBean startBean, JcsCalendarAdapter.CalendarBean endBean, int totalDay, int roomNumber) {
        Bundle args = new Bundle();
        args.putString("hotelTypeIds", hotelTypeIds);
        args.putString(HotelSelectDateHelper.EXT_CITY_ID, cityId);
        args.putString(HotelSelectDateHelper.EXT_PRICE, price);
        args.putString(HotelSelectDateHelper.EXT_STAR, star);
        args.putInt(HotelSelectDateHelper.EXT_TOTAL_DAY, totalDay);
        args.putInt(HotelSelectDateHelper.EXT_ROOM_NUMBER, roomNumber);
        args.putSerializable(HotelSelectDateHelper.EXT_START_DATE_BEAN, startBean);
        args.putSerializable(HotelSelectDateHelper.EXT_END_DATE_BEAN, endBean);
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

    public void changeData(String startData, String endDate, String startWeek, String endWeek, int totalDay, String startYear, String endYear, int roomNum) {
        mTotalDay = totalDay;
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            mStartDateBean = (JcsCalendarAdapter.CalendarBean) arguments.getSerializable(HotelSelectDateHelper.EXT_START_DATE_BEAN);
            mEndDateBean = (JcsCalendarAdapter.CalendarBean) arguments.getSerializable(HotelSelectDateHelper.EXT_END_DATE_BEAN);
            mStartYear = arguments.getString(HotelSelectDateHelper.EXT_START_YEAR);
            mEndYear = arguments.getString(HotelSelectDateHelper.EXT_END_YEAR);
            mTotalDay = arguments.getInt(HotelSelectDateHelper.EXT_TOTAL_DAY);
            mRoomNum = arguments.getInt(HotelSelectDateHelper.EXT_ROOM_NUMBER);
        }

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
                    HotelDetailActivity.goTo(getContext(), (int) adapter.getItemId(position), mStartDateBean, mEndDateBean, mTotalDay, mStartYear, mEndYear, mRoomNum);
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
                GlideUtil.load(getContext(), data.getImages().get(0), photoIv);
            } else {
                photoIv.setImageResource(R.mipmap.ic_glide_default);
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
            String commentNumberText = String.format(getContext().getString(R.string.comment_num_prompt), data.getComment_counts());
            commentNumTv.setText(commentNumberText);
            TextView priceTv = baseViewHolder.findView(R.id.tv_price);
            String priceText = String.format(getContext().getString(R.string.price_above_number), data.getPrice());
            priceTv.setText(priceText);
        }

        @Override
        public long getItemId(int position) {
            return getData().get(position).getId();
        }
    }
}
