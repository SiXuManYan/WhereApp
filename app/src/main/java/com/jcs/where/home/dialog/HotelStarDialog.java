package com.jcs.where.home.dialog;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.jcs.where.R;
import com.jcs.where.base.BaseBottomDialog;
import com.jcs.where.base.BaseDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HotelStarDialog extends BaseBottomDialog implements View.OnClickListener {

    private TextView priceTv;
    private Button ensureBtn;
    private TextView price0To1, price1To2, price2To5, priceAbove5;
    private TextView starLessThan2, star3, star4, star5;
    private TextView score30, score35, score40, score45;
    private List<TextView> priceTvs;
    private List<TextView> starTvs;
    private List<TextView> scoreTvs;
    private HashMap<TextView, PriceIntervalBean> priceBeans;
    private HashMap<TextView, StarBean> starBeans;
    private StarBean mSelectStartBean;
    private RangeSeekBar mSeekBar;
    private DecimalFormat mDecimalFormat;
    private HotelStarCallback mCallback;

    private final String TAG_PRICE = "price";
    private final String TAG_STAR = "star";
    private final String TAG_SCORE = "score";

    @Override
    protected int getLayout() {
        return R.layout.dialog_hotel_star;
    }

    @Override
    protected void initView(View view) {
        priceTv = view.findViewById(R.id.priceTv);
        ensureBtn = view.findViewById(R.id.ensureBtn);

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

        mSeekBar = view.findViewById(R.id.seekBar);
        mSeekBar.setIndicatorTextDecimalFormat("0");
    }

    @Override
    protected void initData() {
        mDecimalFormat = new DecimalFormat("#0.0");

        priceTvs = new ArrayList<>();
        priceTvs.add(price0To1);
        priceTvs.add(price1To2);
        priceTvs.add(price2To5);
        priceTvs.add(priceAbove5);

        priceBeans = new HashMap<>();
        priceBeans.put(price0To1, new PriceIntervalBean(0, 10, getString(R.string.price_0_to_1)));
        priceBeans.put(price1To2, new PriceIntervalBean(1, 20, getString(R.string.price_1_to_2)));
        priceBeans.put(price2To5, new PriceIntervalBean(20, 50, getString(R.string.price_2_to_5)));
        priceBeans.put(priceAbove5, new PriceIntervalBean(50, 50, getString(R.string.price_above_5)));

        starTvs = new ArrayList<>();
        starTvs.add(starLessThan2);
        starTvs.add(star3);
        starTvs.add(star4);
        starTvs.add(star5);

        starBeans = new HashMap<>();
        starBeans.put(starLessThan2, new StarBean("二星及以下"));
        starBeans.put(star3, new StarBean("三星"));
        starBeans.put(star4, new StarBean("四星"));
        starBeans.put(star5, new StarBean("五星"));

        scoreTvs = new ArrayList<>();
        scoreTvs.add(score30);
        scoreTvs.add(score35);
        scoreTvs.add(score40);
        scoreTvs.add(score45);
    }

    @Override
    protected void bindListener() {
        ensureBtn.setOnClickListener(this);

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

        mSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                float leftProgress = mSeekBar.getLeftSeekBar().getProgress();
                float rightProgress = mSeekBar.getRightSeekBar().getProgress();

                StringBuilder priceShow = new StringBuilder(getString(R.string.price_unit));
                priceShow
                        .append(" ")
                        .append(mDecimalFormat.format(leftProgress / 10))
                        .append("-")
                        .append(mDecimalFormat.format(rightProgress / 10))
                        .append("k");
                priceTv.setText(priceShow);
                unSelectByList(null, priceTvs);
            }
        });
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
        unSelectByList(textView, temp);
    }

    private void unSelectByList(TextView textView, List<TextView> temp) {
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
        if (view instanceof Button) {
            //点击了确定
            StringBuilder callbackToShow = new StringBuilder(priceTv.getText().toString());
            if (mSelectStartBean != null) {
                callbackToShow.append("，").append(mSelectStartBean.starShow);
            }
            mCallback.selectPriceOrStar(callbackToShow.toString());
            dismiss();
            return;
        }

        if (view instanceof TextView) {
            unSelectByTag((TextView) view);
            view.setSelected(true);
            PriceIntervalBean priceIntervalBean = priceBeans.get(view);
            if (priceIntervalBean != null) {
                Log.e("HotelStarDialog", "----onClick---" + priceIntervalBean.toString());
                //说明点击的是价格
                priceTv.setText(priceIntervalBean.priceShow);
                mSeekBar.setProgress(priceIntervalBean.start, priceIntervalBean.end);
            }

            StarBean starBean = starBeans.get(view);
            if (starBean != null) {
                //说明点击的是星级
                mSelectStartBean = starBean;
            }
        }
    }

    public void setCallback(HotelStarCallback callback) {
        this.mCallback = callback;
    }

    public void clear() {
        mSelectStartBean = null;
        int priceSize = priceTvs.size();
        for (int i = 0; i < priceSize; i++) {
            priceTvs.get(i).setSelected(false);
        }

        int starSize = starTvs.size();
        for (int i = 0; i < starSize; i++) {
            starTvs.get(i).setSelected(false);
        }

        int scoreSize = scoreTvs.size();
        for (int i = 0; i < scoreSize; i++) {
            scoreTvs.get(i).setSelected(false);
        }
        priceTv.setText("");
    }

    static class PriceIntervalBean {
        int start;
        int end;
        String priceShow;

        public PriceIntervalBean(int start, int end, String priceShow) {
            this.start = start;
            this.end = end;
            this.priceShow = priceShow;
        }
    }

    static class StarBean {
        String starShow;

        public StarBean(String starShow) {
            this.starShow = starShow;
        }
    }


    public interface HotelStarCallback {
        void selectPriceOrStar(String show);
    }
}
