package com.jcs.where.home.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.jcs.where.R;
import com.jcs.where.base.BaseBottomDialog;
import com.jcs.where.utils.BigDecimalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HotelStarDialog extends BaseBottomDialog implements View.OnClickListener {

    private ImageView close;
    private TextView price_start_tv;
    private TextView price_end_tv;
    private Button ensureBtn;
    private TextView price0To1, price1To2, price2To5, priceAbove5;
    private TextView starLessThan2, star3, star4, star5;
    private TextView score30, score35, score40, score45;
    private List<TextView> priceTvs;
    private List<TextView> starTvs;
    private List<TextView> scoreTvs;
    private HashMap<TextView, PriceIntervalBean> priceBeans;
    private HashMap<TextView, StarBean> starBeans;
    private HashMap<TextView, ScoreBean> scoreBeans;


    private RangeSeekBar mSeekBar;
    private DecimalFormat mDecimalFormat;
    private HotelStarCallback mCallback;

    private final String TAG_PRICE = "price";
    private final String TAG_STAR = "star";
    private final String TAG_SCORE = "score";

    /**
     * 选中的评分
     */
    private ScoreBean mScoreBean = new ScoreBean();

    /**
     * 选中的价格区间
     */
    private PriceIntervalBean mPriceBeans = new PriceIntervalBean();


    /**
     * 选中的星级
     */
    private StarBean mSelectStartBean = new StarBean();


    @Override
    protected int getLayout() {
        return R.layout.dialog_hotel_star_2;
    }

    @Override
    protected void initView(View view) {
        close = view.findViewById(R.id.close);
        price_start_tv = view.findViewById(R.id.price_start_tv);
        price_end_tv = view.findViewById(R.id.price_end_tv);
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
    public void show(FragmentManager fm) {
        super.show(fm);
        if (mSeekBar != null) {
            mSeekBar.setProgress(0, 0);
        }
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
        priceBeans.put(price0To1, new PriceIntervalBean(0, 10, getString(R.string.price_0_to_1), 0, 1000));
        priceBeans.put(price1To2, new PriceIntervalBean(10, 20, getString(R.string.price_1_to_2), 1000, 2000));
        priceBeans.put(price2To5, new PriceIntervalBean(20, 50, getString(R.string.price_2_to_5), 2000, 5000));
        priceBeans.put(priceAbove5, new PriceIntervalBean(50, 50, getString(R.string.price_above_5), 5000, 1000000));

        starTvs = new ArrayList<>();
        starTvs.add(starLessThan2);
        starTvs.add(star3);
        starTvs.add(star4);
        starTvs.add(star5);

        starBeans = new HashMap<>();
        starBeans.put(starLessThan2, new StarBean(getString(R.string.less_2_stat), 2));
        starBeans.put(star3, new StarBean(getString(R.string.star_3), 3));
        starBeans.put(star4, new StarBean(getString(R.string.star_4), 4));
        starBeans.put(star5, new StarBean(getString(R.string.star_5), 5));

        scoreTvs = new ArrayList<>();
        scoreTvs.add(score30);
        scoreTvs.add(score35);
        scoreTvs.add(score40);
        scoreTvs.add(score45);

        scoreBeans = new HashMap<>();
        scoreBeans.put(score30, new ScoreBean(3.0f, getString(R.string.score_3_0_hint)));
        scoreBeans.put(score35, new ScoreBean(3.5f, getString(R.string.score_3_5_hint)));
        scoreBeans.put(score40, new ScoreBean(4.0f, getString(R.string.score_4_0_hint)));
        scoreBeans.put(score45, new ScoreBean(4.5f, getString(R.string.score_4_5_hint)));


    }

    @Override
    protected void bindListener() {
        close.setOnClickListener(v -> dismiss());
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

                String startPrice = mDecimalFormat.format(leftProgress / 10) + "k";
                String endPrice = mDecimalFormat.format(rightProgress / 10) + "k";

                price_start_tv.setText(getString(R.string.price_unit_format, startPrice));
                price_end_tv.setText(getString(R.string.price_unit_format, endPrice));

                unSelectByList(null, priceTvs);

                //

                float leftPrice = BigDecimalUtil.mul(leftProgress, 100).floatValue();
                mPriceBeans.startPrice = Math.round(leftPrice);

                float rightPrice = BigDecimalUtil.mul(rightProgress, 100).floatValue();
                mPriceBeans.endPrice = Math.round(rightPrice);
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
        return 540;
    }

    @Override
    public void onClick(View view) {

        if (view instanceof Button) {
            //点击了确定
            StringBuilder callbackToShow = new StringBuilder(price_start_tv.getText() + "-" + price_end_tv.getText());

            if (mSelectStartBean != null) {
                callbackToShow.append("，").append(mSelectStartBean.starShow);
            }
            mCallback.selectPriceOrStar(callbackToShow.toString());
            mCallback.selectResult(mPriceBeans, mSelectStartBean, mScoreBean);
            dismiss();
            return;
        }


        if (view instanceof TextView) {
            unSelectByTag((TextView) view);
            view.setSelected(true);
            PriceIntervalBean priceIntervalBean = priceBeans.get(view);
            if (priceIntervalBean != null) {
                //说明点击的是价格

                int start = priceIntervalBean.startProgress;
                int end = priceIntervalBean.endProgress;
                mSeekBar.setProgress(start, end);

                String startPrice = mDecimalFormat.format(start / 10) + "k";
                String endPrice = mDecimalFormat.format(end / 10) + "k";


                price_start_tv.setText(getString(R.string.price_unit_format, startPrice));
                price_end_tv.setText(getString(R.string.price_unit_format, endPrice));

                mPriceBeans = priceIntervalBean;


            }

            StarBean starBean = starBeans.get(view);
            if (starBean != null) {
                // 说明点击的是星级
                mSelectStartBean = starBean;
            }

            ScoreBean scoreBean = scoreBeans.get(view);
            if (scoreBean != null) {
                // 点击评分
                mScoreBean = scoreBean;
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
        price_start_tv.setText("");
        price_end_tv.setText("");

    }

    public static class PriceIntervalBean {

        public int startProgress;
        public int endProgress;
        public String priceShow = "";
        public int startPrice;
        public int endPrice;

        public PriceIntervalBean() {

        }

        public PriceIntervalBean(int startProgress, int endProgress, String priceShow, int startPrice, int endPrice) {
            this.startProgress = startProgress;
            this.endProgress = endProgress;
            this.priceShow = priceShow;
            this.startPrice = startPrice;
            this.endPrice = endPrice;
        }


    }

    public static class StarBean {
        public String starShow = "";
        public int starValue;

        public StarBean() {
        }

        public StarBean(String starShow, int starValue) {
            this.starShow = starShow;
            this.starValue = starValue;
        }
    }

    public static class ScoreBean {
        public float score;
        public String scoreString = "";

        public ScoreBean() {
        }

        public ScoreBean(float score, String scoreString) {
            this.score = score;
            this.scoreString = scoreString;
        }
    }

    public interface HotelStarCallback {
        void selectPriceOrStar(String show);

        /**
         * 选择结果
         *
         * @param mPriceBeans      选中的价格区间
         * @param mSelectStartBean 选中的星级
         * @param mScoreBean       选中的评分
         */
        void selectResult(PriceIntervalBean mPriceBeans, StarBean mSelectStartBean, ScoreBean mScoreBean);
    }


}
