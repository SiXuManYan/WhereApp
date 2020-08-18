package com.jcs.where.travel;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.jcs.where.R;
import com.jcs.where.bean.TravelMapListBean;
import com.jcs.where.hotel.card.CardAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.view.ToastUtils;

public class TravelCardAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<TravelMapListBean.DataBean> mData;
    private Context useContext;
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
            Picasso.with(useContext).load(bean.getImage()).into(photoIv);
        } else {
            photoIv.setImageDrawable(useContext.getResources().getDrawable(R.drawable.ic_test));
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
                ToastUtils.showLong(useContext,bean.getId()+"");
            }
        });
    }

}
