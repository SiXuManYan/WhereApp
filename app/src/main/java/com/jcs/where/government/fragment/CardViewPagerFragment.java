package com.jcs.where.government.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcs.where.R;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.government.activity.MechanismDetailActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 地图页面下半部分装载 ViewPager 的 Fragment
 * create by zyf on 2020/12/31 10:04 PM
 */
public class CardViewPagerFragment extends BaseFragment {
    private ViewPager mViewPager;
    private PageAnimation mPageAnimation;
    private Adapter mAdapter;
    private OnVpPageSelectedListener mPageSelectedListener;

    @Override
    protected void initView(View view) {
        mViewPager = view.findViewById(R.id.viewPager);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void bindListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mPageSelectedListener != null) {
                    mPageSelectedListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_card_viewpager;
    }

    public void bindAllData(List<MechanismResponse> mechanismResponses) {
        mAdapter = new Adapter(getContext(), this::onPageClicked);
        mAdapter.addAllData(mechanismResponses);
        mPageAnimation = new PageAnimation();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageMargin(getPxFromDp(15));
        mViewPager.setPageTransformer(false, mPageAnimation);
        mViewPager.setOffscreenPageLimit(3);
    }

    public void bindPageSelectedListener(OnVpPageSelectedListener onVpPageSelectedListener) {
        this.mPageSelectedListener = onVpPageSelectedListener;
    }

    public void onPageClicked(int position) {
        int mechanismId = mAdapter.mData.get(position).getId();
        toActivity(MechanismDetailActivity.class,new IntentEntry(MechanismDetailActivity.K_MECHANISM_ID,String.valueOf(mechanismId)));
    }

    private class Adapter extends PagerAdapter {

        private final List<MechanismResponse> mData;
        private final Context mContext;
        private OnVpPageClickedListener mOnVpPageClicked;

        public Adapter(Context context, OnVpPageClickedListener listener) {
            mData = new ArrayList<>();
            mContext = context;
            mOnVpPageClicked = listener;
        }

        public void addAllData(List<MechanismResponse> mechanismResponses) {
            mData.clear();
            mData.addAll(mechanismResponses);
        }

        public void addCardItem(MechanismResponse item) {
            mData.add(item);
        }

        public void clear() {
            mData.clear();
            notifyDataSetChanged();
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
                    .inflate(R.layout.page_mechainsm, container, false);
            view.setTag(position);
            container.addView(view);
            bind(mData.get(position), view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void bind(MechanismResponse mechanismResponse, View view) {
            view.setOnClickListener(this::onPageClicked);
            TextView nameTv = (TextView) view.findViewById(R.id.mechanismTitleTv);
            nameTv.setText(mechanismResponse.getTitle());
            RoundedImageView photoIv = (RoundedImageView) view.findViewById(R.id.mechanismIconIv);
            photoIv.setCornerRadius(getPxFromDp(4), 0, getPxFromDp(4), 0);
            if (!TextUtils.isEmpty(mechanismResponse.getImages().get(0))) {
                Glide.with(mContext).load(mechanismResponse.getImages().get(0)).into(photoIv);
            } else {
                photoIv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_test));
            }
            TextView addressTv = (TextView) view.findViewById(R.id.mechanismAddressTv);
            addressTv.setText(mechanismResponse.getAddress());
            TextView distanceTv = (TextView) view.findViewById(R.id.mechanismDistanceTv);
            String distance = mechanismResponse.getDistance() + "Km";
            distanceTv.setText(distance);
        }

        public void onPageClicked(View view) {
            mOnVpPageClicked.onPageClicked((int) view.getTag());
        }
    }

    public interface OnVpPageSelectedListener {
        void onPageSelected(int position);
    }

    public interface OnVpPageClickedListener {
        void onPageClicked(int position);
    }
}
