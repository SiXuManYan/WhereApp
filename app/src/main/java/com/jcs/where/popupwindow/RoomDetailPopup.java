package com.jcs.where.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcs.where.R;
import com.jcs.where.bean.RoomDetailBean;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;

import pl.droidsonroids.gif.GifImageView;


public class RoomDetailPopup extends PopupWindow implements View.OnClickListener {

    private Activity activity;
    private View rootView;
    private SubscribeOnClickListener mOnClickListener = null;
    private ImageView closeIv;
    private int useId;
    private RoomDetailBean useRoomDetailBean;
    private XBanner banner;
    private TextView roomNameTv, bedTv, squareTv, windowTv, floorTv, wifiTv, peopleTv, policyTv, bathroomTv, amenitiesTv, mediaTv, foodTv, outdoorTv, otherTv, priceTv, nowSubscribeTv;

    private RoomDetailPopup(Builder builder) {
        this.activity = builder.context;
        this.mOnClickListener = builder.mOnClickListener;
        this.useId = builder.id;
        this.useRoomDetailBean = builder.roomDetailBean;
        LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.popupwindow_roomdetail, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setAnimationStyle(com.atuan.datepickerlibrary.R.style.dialogWindowAnim);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(new ShareDismissListener());
        backgroundAlpha(activity, 0.5f);
        initView();
        create(builder.parentView);
    }

    private void initView() {
        closeIv = (ImageView) rootView.findViewById(R.id.btn_close);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomDetailPopup.this.dismiss();
            }
        });
        banner = (XBanner) rootView.findViewById(R.id.banner3);
        banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
                .setImageUrls(useRoomDetailBean.getImages())
                .setImageLoader(new AbstractUrlLoader() {
                    @Override
                    public void loadImages(Context context, String url, ImageView image) {
                        Glide.with(context)
                                .load(url)
                                .into(image);
                    }

                    @Override
                    public void loadGifs(Context context, String url, GifImageView gifImageView, ImageView.ScaleType scaleType) {
                        Glide.with(context).asGif().load(url).into(gifImageView);
                    }
                })
                .setTitleHeight(50)
                .isAutoPlay(true)
                .setDelay(5000)
                .setUpIndicators(R.drawable.ic_roomselected, R.drawable.ic_roomunselected)
                .setUpIndicatorSize(20, 6)
                .setIndicatorGravity(XBanner.INDICATOR_CENTER)
                .setBannerPageListener(new XBanner.BannerPageListener() {
                    @Override
                    public void onBannerClick(int item) {

                    }

                    @Override
                    public void onBannerDragging(int item) {

                    }

                    @Override
                    public void onBannerIdle(int item) {

                    }
                })
                .start();
        roomNameTv = (TextView) rootView.findViewById(R.id.tv_roomname);
        roomNameTv.setText(useRoomDetailBean.getName());
        bedTv = (TextView) rootView.findViewById(R.id.tv_bed);
        bedTv.setText(useRoomDetailBean.getHotel_room_type());
        squareTv = (TextView) rootView.findViewById(R.id.tv_square);
        squareTv.setText(useRoomDetailBean.getRoom_area());
        windowTv = (TextView) rootView.findViewById(R.id.tv_window);
        if (useRoomDetailBean.getWindow_type() == 1) {
            windowTv.setText("有");
        } else {
            windowTv.setText("无");
        }
        floorTv = (TextView) rootView.findViewById(R.id.tv_floor);
        floorTv.setText(useRoomDetailBean.getFloor());
        wifiTv = (TextView) rootView.findViewById(R.id.tv_wifi);
        if (useRoomDetailBean.getWifi_type() == 1) {
            wifiTv.setText("有");
        } else {
            wifiTv.setText("无");
        }
        peopleTv = (TextView) rootView.findViewById(R.id.tv_people);
        peopleTv.setText(useRoomDetailBean.getPeople() + "");
        policyTv = (TextView) rootView.findViewById(R.id.tv_policy);
        policyTv.setText(useRoomDetailBean.getPolicy());
        bathroomTv = (TextView) rootView.findViewById(R.id.tv_bathroom);
        bathroomTv.setText(useRoomDetailBean.getShower_room());
        amenitiesTv = (TextView) rootView.findViewById(R.id.tv_amenities);
        amenitiesTv.setText(useRoomDetailBean.getFacility());
        mediaTv = (TextView) rootView.findViewById(R.id.tv_media);
        mediaTv.setText(useRoomDetailBean.getMedia());
        foodTv = (TextView) rootView.findViewById(R.id.tv_food);
        foodTv.setText(useRoomDetailBean.getFood());
        outdoorTv = (TextView) rootView.findViewById(R.id.tv_outdoor);
        outdoorTv.setText(useRoomDetailBean.getScene());
        otherTv = (TextView) rootView.findViewById(R.id.tv_other);
        otherTv.setText(useRoomDetailBean.getOther());
        priceTv = (TextView) rootView.findViewById(R.id.tv_price);
        priceTv.setText("₱" + useRoomDetailBean.getPrice());
        nowSubscribeTv = (TextView) rootView.findViewById(R.id.tv_nowsubscribe);
        nowSubscribeTv.setOnClickListener(this);


    }

    private void create(View view) {
        this.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nowsubscribe:
                if (mOnClickListener != null) {
                    mOnClickListener.getDate(useId, useRoomDetailBean.getName(), useRoomDetailBean.getHotel_room_type(), useRoomDetailBean.getWindow_type(), useRoomDetailBean.getWifi_type(), useRoomDetailBean.getPeople(), useRoomDetailBean.getIs_cancel());
                }
                RoomDetailPopup.this.dismiss();
                break;
        }
    }

    private class ShareDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(activity, 1f);
        }
    }


    public static class Builder {
        private Activity context;
        private View parentView;
        private SubscribeOnClickListener mOnClickListener = null;
        private int id;
        private RoomDetailBean roomDetailBean;

        public Builder(Activity context, View parentView, int id, RoomDetailBean roomDetailBean) {
            this.context = context;
            this.parentView = parentView;
            this.id = id;
            this.roomDetailBean = roomDetailBean;
        }

        public RoomDetailPopup builder() {
            return new RoomDetailPopup(this);
        }

        public RoomDetailPopup.Builder setPriceOnClickListener(SubscribeOnClickListener mlListener) {
            mOnClickListener = mlListener;
            return this;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        banner.releaseBanner();
    }

    public interface SubscribeOnClickListener {
        void getDate(int id, String name, String bed, int window, int wifi, int people, int cancel);
    }

}
