package com.jcs.where.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jcs.where.R;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.bean.MarkerBitmapDescriptors;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * create by zyf on 2020/12/30 10:29 PM
 */
public class MapMarkerUtil {
    private Marker mCurrentMarker;
    private int mCurrentPosition;
    private GoogleMap mMap;
    /**
     * 存储展示在地图上的数据
     */
    private List<MechanismResponse> mMechanismsForMap;
    /**
     * 存储展示在Map上的marker
     */
    private List<Marker> mMarkersOnMap;
    /**
     * 与 mMarkersOnMap 的position是一一对应关系
     * 存储 marker 不同选择状态的 icon
     */
    private List<MarkerBitmapDescriptors> mDescriptors;

    private LatLngBounds mLatLngBounds;
    private LatLngBounds.Builder mLatLngBoundsBuilder;
    private BaseActivity mContext;
    /**
     * map是否刚被clear过
     */
    private boolean isCleared = false;

    public MapMarkerUtil(BaseActivity context) {
        mMechanismsForMap = new ArrayList<>();
        mDescriptors = new ArrayList<>();
        mMarkersOnMap = new ArrayList<>();
        mContext = context;
        mLatLngBoundsBuilder = new LatLngBounds.Builder();
    }

    public void setMap(GoogleMap map) {
        this.mMap = map;
    }

    public View getMarkerView() {
        return mContext.getLayoutInflater().inflate(R.layout.view_map_marker, null);
    }

    public BitmapDescriptor getSelectView(View view) {
        TextView tv = (TextView) view;
        tv.setTextColor(mContext.getColor(R.color.white));
        tv.setBackgroundResource(R.mipmap.ic_map_marker_selected);
        return fromView(mContext, tv);
    }

    public BitmapDescriptor getUnselectedView(View view) {
        TextView tv = (TextView) view;
        tv.setTextColor(mContext.getColor(R.color.blue_4C9EF2));
        tv.setBackgroundResource(R.mipmap.ic_map_marker_unselected);
        return fromView(mContext, tv);
    }

    public BitmapDescriptor getUnSelectMarker(AppCompatActivity context, String title) {
        TextView tv = (TextView) context.getLayoutInflater().inflate(R.layout.view_map_marker, null);
        tv.setText(title);
        tv.setTextColor(context.getColor(R.color.blue_4C9EF2));
        tv.setBackgroundResource(R.mipmap.ic_map_marker_unselected);
        return fromView(context, tv);
    }

    public BitmapDescriptor getSelectedMarker(AppCompatActivity context, String title) {
        TextView tv = (TextView) context.getLayoutInflater().inflate(R.layout.view_map_marker, null);
        tv.setText(title);
        tv.setTextColor(context.getColor(R.color.white));
        tv.setBackgroundResource(R.mipmap.ic_map_marker_selected);
        return fromView(context, tv);
    }

    /**
     * GoogleMap：根据传入的 view，创建 BitmapDescriptor 对象
     */
    public BitmapDescriptor fromView(Context context, View view) {
        FrameLayout frameLayout = new FrameLayout(context);
        ViewParent parent = view.getParent();
        if (parent instanceof FrameLayout) {
            FrameLayout temp = (FrameLayout) parent;
            temp.removeView(view);
        }
        frameLayout.addView(view);
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = getBitmapFromView(frameLayout);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        bitmap.recycle();
        return bitmapDescriptor;
    }

    /**
     * Convert a view to bitmap
     */
    public Bitmap getBitmapFromView(View view) {
        try {
            banTextViewHorizontallyScrolling(view);
            view.destroyDrawingCache();
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            Bitmap bitmap = view.getDrawingCache();
            return bitmap != null ? bitmap.copy(Bitmap.Config.ARGB_8888, false) : null;
        } catch (Throwable ex) {
            return null;
        }
    }

    /**
     * 禁止 TextView 水平滚动
     */
    private void banTextViewHorizontallyScrolling(View view) {
        if (view instanceof ViewGroup) {
            for (int index = 0; index < ((ViewGroup) view).getChildCount(); ++index) {
                banTextViewHorizontallyScrolling(((ViewGroup) view).getChildAt(index));
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setHorizontallyScrolling(false);
        }
    }

    /**
     * 遍历 map，若key 是当前点击的 marker
     * 则改变当前点击 marker 的 icon 为选择状态
     * 否则设置为非选择状态
     *
     * @param marker 目标marker
     * @return 数据的索引位置
     */
    public int changeMarkerStatus(Marker marker) {
        if (mCurrentMarker == null) {
            mCurrentMarker = marker;
        }
        Object currentTag = mCurrentMarker.getTag();
        Object markerTag = marker.getTag();

        // 此索引  是 marker，BitmapDescriptor，MechanismResponse 的索引
        if (markerTag instanceof MechanismResponse) {
            int position = mMechanismsForMap.indexOf(markerTag);
            MarkerBitmapDescriptors descriptor = null;
            if (position > -1) {
                mCurrentPosition = position;
                descriptor = mDescriptors.get(position);
            }

            // 当前点击的是已经选中的marker
            if (currentTag == markerTag) {
                if (descriptor != null) {
                    if (descriptor.isSelected()) {
                        descriptor.setSelected(false);
                        marker.setIcon(descriptor.getUnselectedBitmapDescriptor());
                    } else {
                        descriptor.setSelected(true);
                        marker.setIcon(descriptor.getSelectedBitmapDescriptor());
                    }
                }
            } else {
                // 点击的是未选中的marker
                if (descriptor != null) {
                    descriptor.setSelected(true);
                    marker.setIcon(descriptor.getSelectedBitmapDescriptor());

                    // 将原有的 marker 设置成未选择状态
                    MarkerBitmapDescriptors lastDescriptor = mDescriptors.get(mCurrentPosition);
                    lastDescriptor.setSelected(false);
                    mCurrentMarker.setIcon(lastDescriptor.getUnselectedBitmapDescriptor());

                    // 存储marker
                    mCurrentMarker = marker;
                }

            }
        }

        return mCurrentPosition;
    }

    /**
     * 向map上添加marker
     */
    public void addMarkerToMap() {
        int size = mMechanismsForMap.size();
        for (int i = 0; i < size; i++) {
            MechanismResponse mechanismResponse = mMechanismsForMap.get(i);
            if (mechanismResponse != null) {
                // 向map上添加marker
                LatLng latLng = new LatLng(mechanismResponse.getLat(), mechanismResponse.getLng());

                // 将坐标点保存到 bounders 中，用于设置最佳的 map zoom
                mLatLngBoundsBuilder.include(latLng);
                TextView markerView = (TextView) getMarkerView();
                markerView.setText(mechanismResponse.getTitle());
                MarkerBitmapDescriptors markerBitmapDescriptors = new MarkerBitmapDescriptors();
                markerBitmapDescriptors.setSelectedBitmapDescriptor(getSelectView(markerView));
                markerBitmapDescriptors.setUnselectedBitmapDescriptor(getUnselectedView(markerView));


                MarkerOptions option = new MarkerOptions()
                        .position(latLng)
                        .icon(markerBitmapDescriptors.getUnselectedBitmapDescriptor())
                        .zIndex(4)
                        .draggable(false);//是否可以拖动

                // 将第一个设置为选中状态
                if (i == mCurrentPosition) {
                    markerBitmapDescriptors.setSelected(true);
                    option.icon(markerBitmapDescriptors.getSelectedBitmapDescriptor());
                }

                // 在地图上绘制
                Marker marker = mMap.addMarker(option);
                marker.setTag(mechanismResponse);

                // 初始化 mCurrentMarker
                if (i == mCurrentPosition) {
                    mCurrentMarker = marker;
                    mCurrentPosition = i;
                }
                mMarkersOnMap.add(marker);
                mDescriptors.add(markerBitmapDescriptors);
            }
        }

        mLatLngBounds = mLatLngBoundsBuilder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 100));
    }

    /**
     * 清空缓存
     */
    public void clear() {
        mMarkersOnMap.clear();
        mDescriptors.clear();
        mMechanismsForMap.clear();
        mCurrentMarker = null;
        mCurrentPosition = 0;
    }

    public int getMarkerPosition(Marker marker) {
        int index = -1;
        Object tag = marker.getTag();
        if (tag != null) {
            index = mMechanismsForMap.indexOf(tag);
        }
        return index;
    }

    public void addAllMechanismForMap(List<MechanismResponse> mechanismResponses) {
        mMechanismsForMap.addAll(mechanismResponses);
    }

    public void selectMarker(int position) {
        if (mMarkersOnMap != null && mMarkersOnMap.size() >= position) {
            Marker marker = mMarkersOnMap.get(position);
            MechanismResponse currentMarkerTag = (MechanismResponse) mCurrentMarker.getTag();
            MechanismResponse markerTag = (MechanismResponse) marker.getTag();
            if (mCurrentMarker != null && mCurrentMarker.getTag() == marker.getTag()) {
                // 已经被选中了，什么都不需要执行
            } else {
                // 对应 position 位置的marker未被选中，则应该选中
                MarkerBitmapDescriptors markerBitmapDescriptors = mDescriptors.get(position);
                markerBitmapDescriptors.setSelected(true);
                marker.setIcon(markerBitmapDescriptors.getSelectedBitmapDescriptor());
                if (mMap != null) {
                    // 将点击的 marker 展示在屏幕中心
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                }

                // 重置上一次选择的marker的状态
                if (mCurrentMarker != null) {
                    MarkerBitmapDescriptors currentDescriptor = mDescriptors.get(mCurrentPosition);
                    currentDescriptor.setSelected(false);
                    mCurrentMarker.setIcon(currentDescriptor.getUnselectedBitmapDescriptor());
                }

                mCurrentMarker = marker;
                mCurrentPosition = position;
            }
        }
    }

    /**
     * 在map clear 后，恢复原来的状态
     */
    public void restoreMap() {
        if (isCleared) {
            addMarkerToMap();
            isCleared = false;
        }
    }

    public void clearMap() {
        mMap.clear();
        mMarkersOnMap.clear();
        mCurrentMarker = null;
        isCleared = true;
    }
}
