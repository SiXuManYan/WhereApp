package com.jiechengsheng.city.features.city;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiechengsheng.city.R;
import com.jiechengsheng.city.bean.CityResponse;
import com.jiechengsheng.city.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 城市列表适配器
 */
public class CityListAdapter extends BaseAdapter {

    private final Context mContext;
    private LayoutInflater inflater;
    private List<CityResponse> mCities = new ArrayList<>();
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;

    public CityListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<CityResponse> mCities) {
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);

        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];

        for (int index = 0; index < size; index++) {
            // 当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(mCities.get(index).getPinyin());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(mCities.get(index - 1).getPinyin()) : "A";
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
        if (view == null) {
            view = inflater.inflate(R.layout.cp_item_city_listview, parent, false);
            holder = new CityViewHolder();
            holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
            holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
            view.setTag(holder);
        } else {
            holder = (CityViewHolder) view.getTag();
        }

        CityResponse response = mCities.get(position);
        final String city = response.getName();
        final String cityId = response.getId();
        holder.name.setText(city);

        String currentLetter = PinyinUtils.getFirstLetter(response.getPinyin());
        String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(mCities.get(position - 1).getPinyin()) : "A";
        holder.letter.setText(currentLetter);

        if (!TextUtils.equals(currentLetter, previousLetter) || position == 0) {
            holder.letter.setVisibility(View.VISIBLE);
        } else {
            holder.letter.setVisibility(View.GONE);
        }
        holder.name.setOnClickListener(v -> {
            if (onCityClickListener != null) {
                onCityClickListener.onCityClick(city, cityId , response.lat,response.lng);
            }
        });
        return view;
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(String name, String id, double lat, double lng);

    }

    public static class CityViewHolder {
        TextView letter;
        TextView name;
    }
}
