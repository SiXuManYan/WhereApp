package com.jcs.where.view.XBanner;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abby on 12/3/2017.
 */
public class XBPagerAdapter extends PagerAdapter {

    private List<View> mData;
    private XBanner.BannerPageListener mBannerPageListner;
    private int mImageCount;
    private View view;

    XBPagerAdapter(XBanner.BannerPageListener listener, int imagecount){
        mData=new ArrayList<>();
        mBannerPageListner=listener;
        mImageCount=imagecount;
    }

    XBPagerAdapter(XBanner.BannerPageListener listener,List<View> data,int count){
        mData=new ArrayList<>();
        mData.addAll(data);
        mBannerPageListner=listener;
        mImageCount=count;
    }
    /**
     * Add data,will not clear the data already exists
     * @param data the ImageViews to be added
     */
    public void addData(List<View> data){
        if(mData!=null){
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * Reset the data
     */
    public void setData(List<View> data){

        if(mData!=null){
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }

    }



    @Override
    public boolean isViewFromObject(View view,Object object){
        return view==object;
    }


    @Override
    public int getCount(){
        return mData.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        view=mData.get(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBannerPageListner!=null){
                    mBannerPageListner.onBannerClick(getTruePos(position));
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }


    private  int getTruePos(int pos){
        //get the position of the indicator
        int truepos=(pos-1)%mImageCount;
        if(truepos<0){
            truepos=mImageCount-1;
        }
        return truepos;
    }

    public void releaseAdapter(){

        mBannerPageListner=null;
        if(mData!=null&&mData.size()>0){
            for(int i=0;i<mData.size();i++){
                mData.get(i).setOnClickListener(null);
            }
            mData.clear();
        }
    }



}
