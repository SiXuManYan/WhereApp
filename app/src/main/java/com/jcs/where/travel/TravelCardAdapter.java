package com.jcs.where.travel;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcs.where.R;
import com.jcs.where.bean.TravelMapListBean;
import com.jcs.where.hotel.card.CardAdapter;
import com.jcs.where.travel.event.TravelEvent;
import com.jcs.where.utils.GlideUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

public class TravelCardAdapter extends PagerAdapter implements CardAdapter {

    private final List<CardView> mViews;
    private final List<TravelMapListBean.DataBean> mData;
    private final Context useContext;
    private float mBaseElevation;

    public TravelCardAdapter(Context context) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        useContext = context;
    }

    public void addCardItem(TravelMapListBean.DataBean item) {
        mViews.add(null);
        mData.add(item);
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.card_travel, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(TravelMapListBean.DataBean bean, View view) {
        TextView nameTv = (TextView) view.findViewById(R.id.tv_name);
        nameTv.setText(bean.getName());
        RoundedImageView photoIv = (RoundedImageView) view.findViewById(R.id.iv_photo);
        if (!TextUtils.isEmpty(bean.getImage())) {
            GlideUtil.load(useContext, bean.getImage(), photoIv);
        } else {
            photoIv.setImageResource(R.mipmap.ic_glide_default);
        }
        TextView addressTv = (TextView) view.findViewById(R.id.tv_address);
        addressTv.setText(bean.getAddress());
        TextView distanceTv = (TextView) view.findViewById(R.id.tv_distance);
        distanceTv.setText("<" + bean.getKm() + "Km");
        TextView scoreTv = (TextView) view.findViewById(R.id.tv_score);
        scoreTv.setText(bean.getGrade() + "分");
        TextView commentNumTv = (TextView) view.findViewById(R.id.tv_commentnumber);
        commentNumTv.setText(bean.getComments_count() + "条评论");
        LinearLayout cardLl = (LinearLayout) view.findViewById(R.id.ll_card);
        cardLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new TravelEvent(bean.getId()));
            }
        });
    }

}
