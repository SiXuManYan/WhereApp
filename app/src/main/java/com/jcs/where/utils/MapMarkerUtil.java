package com.jcs.where.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.jcs.where.R;
import com.jcs.where.government.bean.MarkerBitmapDescriptors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

/**
 * create by zyf on 2020/12/30 10:29 PM
 */
public class MapMarkerUtil {
    public View getMarkerView(AppCompatActivity context) {
        return context.getLayoutInflater().inflate(R.layout.view_map_marker, null);
    }

    public BitmapDescriptor getSelectView(AppCompatActivity context, View view) {
        TextView tv = (TextView) view;
        tv.setTextColor(context.getColor(R.color.white));
        tv.setBackgroundResource(R.mipmap.ic_map_marker_selected);
        return fromView(context, tv);
    }

    public BitmapDescriptor getUnselectedView(AppCompatActivity context, View view) {
        TextView tv = (TextView) view;
        tv.setTextColor(context.getColor(R.color.blue_4C9EF2));
        tv.setBackgroundResource(R.mipmap.ic_map_marker_unselected);
        return fromView(context, tv);
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
     * @param marker        目标marker
     * @param mMarkersOnMap marker map集合
     */
    public void changeMarkerStatus(Marker marker, HashMap<Marker, MarkerBitmapDescriptors> mMarkersOnMap) {
        Set<Map.Entry<Marker, MarkerBitmapDescriptors>> entries = mMarkersOnMap.entrySet();
        for (Map.Entry<Marker, MarkerBitmapDescriptors> entry : entries) {
            Marker key = entry.getKey();
            MarkerBitmapDescriptors value = entry.getValue();
            Object keyTag = key.getTag();
            Object markerTag = marker.getTag();
            if (keyTag == markerTag) {
                if (value.isSelected()) {
                    value.setSelected(false);
                    marker.setIcon(value.getUnselectedBitmapDescriptor());
                } else {
                    value.setSelected(true);
                    marker.setIcon(value.getSelectedBitmapDescriptor());
                }
            } else {
                value.setSelected(false);
                key.setIcon(value.getUnselectedBitmapDescriptor());
            }
        }
    }
}
