package com.jcs.where.government.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SizeUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.features.travel.detail.TravelDetailActivity;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.MapMarkerUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图页面下半部分装载 ViewPager 的 Fragment
 * create by zyf on 2020/12/31 10:04 PM
 */
public class CardViewPagerFragment extends BaseFragment {
    private ViewPager mViewPager;
    private PageAnimation mPageAnimation;
    private Adapter mAdapter;
    private OnVpPageSelectedListener mPageSelectedListener;
    private List<? extends MapMarkerUtil.IMapData> mIMapData;

    @Override
    protected void initView(View view) {
        mViewPager = view.findViewById(R.id.viewPager);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void bindListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    public void bindAllData(List<? extends MapMarkerUtil.IMapData> mapData) {
        mIMapData = mapData;
        mAdapter = new Adapter(getContext(), this::onPageClicked);
        mAdapter.addAllData(mapData);
        mPageAnimation = new PageAnimation();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageMargin(SizeUtils.dp2px(15f));
        mViewPager.setPageTransformer(false, mPageAnimation);
        mViewPager.setOffscreenPageLimit(3);
        if (mapData != null && mapData.size() > 1) {
            mViewPager.setCurrentItem(1);
        }
    }

    public void restore() {
        mViewPager.setAdapter(mAdapter);
    }

    public void bindSingleData(MapMarkerUtil.IMapData mechanismResponses) {
        Adapter adapter = new Adapter(getContext(), this::onPageClicked);
        adapter.addCardItem(mechanismResponses);
        mViewPager.setAdapter(adapter);
    }

    public void bindPageSelectedListener(OnVpPageSelectedListener onVpPageSelectedListener) {
        this.mPageSelectedListener = onVpPageSelectedListener;
    }

    public void onPageClicked(int position) {
        int mechanismId = mAdapter.mData.get(position).getId();
        TravelDetailActivity.Companion.navigation(requireContext(), mechanismId);
    }

    public void selectPosition(int selectPosition) {
        if (selectPosition > -1) {
            mViewPager.setCurrentItem(selectPosition);
        }
    }

    private class Adapter extends PagerAdapter {

        private final List<MapMarkerUtil.IMapData> mData;
        private final Context mContext;
        private OnVpPageClickedListener mOnVpPageClicked;

        public Adapter(Context context, OnVpPageClickedListener listener) {
            mData = new ArrayList<>();
            mContext = context;
            mOnVpPageClicked = listener;
        }

        public void addAllData(List<? extends MapMarkerUtil.IMapData> mechanismResponses) {
            mData.clear();
            mData.addAll(mechanismResponses);
        }

        public void addCardItem(MapMarkerUtil.IMapData item) {
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

        private void bind(MapMarkerUtil.IMapData mechanismResponse, View view) {
            view.setOnClickListener(this::onPageClicked);
            TextView nameTv = view.findViewById(R.id.mechanismTitleTv);
            nameTv.setText(mechanismResponse.getName());
            RoundedImageView photoIv = view.findViewById(R.id.mechanismIconIv);
            photoIv.setCornerRadius(SizeUtils.dp2px(4f), 0, SizeUtils.dp2px(4f), 0);
            List<String> images = mechanismResponse.getImages();
            if (images != null && !TextUtils.isEmpty(images.get(0))) {
                GlideUtil.load(mContext, images.get(0), photoIv);
            } else {
                photoIv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_test));
            }
            TextView addressTv = view.findViewById(R.id.mechanismAddressTv);
            addressTv.setText(mechanismResponse.getAddress());
            TextView distanceTv = view.findViewById(R.id.mechanismDistanceTv);
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
