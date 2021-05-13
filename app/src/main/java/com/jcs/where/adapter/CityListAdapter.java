package com.jcs.where.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.bean.LocateState;
import com.jcs.where.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 城市列表适配器
 */
public class CityListAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 2;

    private final Context mContext;
    private LayoutInflater inflater;
    private List<CityResponse> mCities;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;
    private String locatedCity;
    private String locatedCityId;
    private final List<CityResponse> mHotData = new ArrayList<>();
    private int locateState = LocateState.LOCATING;

    public CityListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<CityResponse> mCities) {
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
        if (mCities == null) {
            mCities = new ArrayList<>();
        }
        mCities.add(0, new CityResponse("-1", "定位", "0"));

        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];

        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(mCities.get(index).getPinyin());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(mCities.get(index - 1).getPinyin()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position < VIEW_TYPE_COUNT - 1 ? position : VIEW_TYPE_COUNT - 1;
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public CityResponse getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0:     //定位
                view = inflater.inflate(R.layout.cp_view_locate_city, parent, false);
                ViewGroup container = (ViewGroup) view.findViewById(R.id.layout_locate);
                TextView state = (TextView) view.findViewById(R.id.tv_located_city);
                switch (locateState) {
                    case LocateState.LOCATING:
                        state.setText(R.string.positioning);
                        break;
                    case LocateState.FAILED:
                        state.setText(R.string.position_failed);
                        break;
                    case LocateState.SUCCESS:
                        state.setText(locatedCity);
                        break;
                    case LocateState.INIT:
                        state.setText(R.string.position);
                        break;
                }
                container.setOnClickListener(v -> {
                    if (locateState == LocateState.FAILED) {
                        //重新定位
                        if (onCityClickListener != null) {
                            onCityClickListener.onLocateClick();
                        }
                    } else if (locateState == LocateState.SUCCESS) {
                        //返回定位城市
                        if (onCityClickListener != null) {
                            onCityClickListener.onCityClick(locatedCity, locatedCityId);
                        }
                    }
                });
                break;
            case 1:     //所有
                if (view == null) {
                    view = inflater.inflate(R.layout.cp_item_city_listview, parent, false);
                    holder = new CityViewHolder();
                    holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
                    holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
                    view.setTag(holder);
                } else {
                    holder = (CityViewHolder) view.getTag();
                }
                if (position >= 1) {
                    final String city = mCities.get(position).getName();
                    final String cityId = mCities.get(position).getId();
                    holder.name.setText(city);
                    String currentLetter = PinyinUtils.getFirstLetter(mCities.get(position).getPinyin());
                    String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(mCities.get(position - 1).getPinyin()) : "";
                    if (!TextUtils.equals(currentLetter, previousLetter)) {
                        holder.letter.setVisibility(View.VISIBLE);
                        holder.letter.setText(currentLetter);
                    } else {
                        holder.letter.setVisibility(View.GONE);
                    }
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onCityClickListener != null) {
                                onCityClickListener.onCityClick(city, cityId);
                            }
                        }
                    });
                }
                break;
        }
        return view;
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public void updateLocateState(int state, String city, String cityId) {
        this.locateState = state;
        this.locatedCity = city;
        this.locatedCityId = cityId;
        notifyDataSetChanged();
    }

    public interface OnCityClickListener {
        void onCityClick(String name, String id);

        void onLocateClick();
    }

    public static class CityViewHolder {
        TextView letter;
        TextView name;
    }
}
