package com.jcs.where.home.dialog;

import android.view.View;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

public class HotelStarDialog extends BaseDialog implements View.OnClickListener {

    private TextView price0To1, price1To2, price2To5, priceAbove5;
    private TextView starLessThan2, star3, star4, star5;
    private TextView score30, score35, score40, score45;
    private List<TextView> priceTvs;
    private List<TextView> starTvs;
    private List<TextView> scoreTvs;
    private final String TAG_PRICE = "price";
    private final String TAG_STAR = "star";
    private final String TAG_SCORE = "score";

    @Override
    protected int getLayout() {
        return R.layout.dialog_hotel_star;
    }

    @Override
    protected void initView(View view) {
        price0To1 = view.findViewById(R.id.price0To1);
        price1To2 = view.findViewById(R.id.price1To2);
        price2To5 = view.findViewById(R.id.price2To5);
        priceAbove5 = view.findViewById(R.id.priceAbove5);

        starLessThan2 = view.findViewById(R.id.starLessThan2);
        star3 = view.findViewById(R.id.star3);
        star4 = view.findViewById(R.id.star4);
        star5 = view.findViewById(R.id.star5);

        score30 = view.findViewById(R.id.score_30);
        score35 = view.findViewById(R.id.score_35);
        score40 = view.findViewById(R.id.score_40);
        score45 = view.findViewById(R.id.score_45);
    }

    @Override
    protected void initData() {
        priceTvs = new ArrayList<>();
        priceTvs.add(price0To1);
        priceTvs.add(price1To2);
        priceTvs.add(price2To5);
        priceTvs.add(priceAbove5);

        starTvs = new ArrayList<>();
        starTvs.add(starLessThan2);
        starTvs.add(star3);
        starTvs.add(star4);
        starTvs.add(star5);

        scoreTvs = new ArrayList<>();
        scoreTvs.add(score30);
        scoreTvs.add(score35);
        scoreTvs.add(score40);
        scoreTvs.add(score45);
    }

    @Override
    protected void bindListener() {
        int priceSize = priceTvs.size();
        for (int i = 0; i < priceSize; i++) {
            TextView textView = priceTvs.get(i);
            textView.setTag(TAG_PRICE);
            textView.setOnClickListener(this);

        }

        int starSize = starTvs.size();
        for (int i = 0; i < starSize; i++) {
            TextView textView = starTvs.get(i);
            textView.setTag(TAG_STAR);
            textView.setOnClickListener(this);
        }

        int scoreSize = scoreTvs.size();
        for (int i = 0; i < scoreSize; i++) {
            TextView textView = scoreTvs.get(i);
            textView.setTag(TAG_SCORE);
            textView.setOnClickListener(this);
        }
    }

    private void unSelectByTag(TextView textView) {
        String tag = (String) textView.getTag();
        List<TextView> temp = null;
        switch (tag) {
            case TAG_PRICE:
                temp = priceTvs;
                break;
            case TAG_STAR:
                temp = starTvs;
                break;
            case TAG_SCORE:
                temp = scoreTvs;
                break;
        }
        if (temp != null) {
            int size = temp.size();
            for (int j = 0; j < size; j++) {

                TextView tv = temp.get(j);
                if (tv != textView) {
                    tv.setSelected(false);
                }
            }
        }
    }

    @Override
    protected int getHeight() {
        return 388;
    }

    @Override
    public void onClick(View view) {
        if (view instanceof TextView) {
            unSelectByTag((TextView) view);
            view.setSelected(true);
        }
    }
}
