package com.jcs.where.features.search;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jcs.where.R;
import com.jcs.where.api.response.search.SearchResultResponse;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.government.activity.MechanismDetailActivity;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.hotel.watcher.AfterInputWatcher;
import com.jcs.where.travel.TouristAttractionDetailActivity;
import com.jcs.where.widget.calendar.JcsCalendarDialog;

import java.util.List;

import static com.jcs.where.api.response.search.SearchResultResponse.TYPE_1_HOTEL;
import static com.jcs.where.api.response.search.SearchResultResponse.TYPE_2_TRAVEL;
import static com.jcs.where.api.response.search.SearchResultResponse.TYPE_3_SERVICE;
import static com.jcs.where.api.response.search.SearchResultResponse.TYPE_4_RESTAURANT;

/**
 * Created by Wangsw  2021/2/25 10:25.
 * 搜索
 */
public class SearchAllActivity extends BaseMvpActivity<SearchAllPresenter> implements SearchAllView, OnItemClickListener {


    private AppCompatEditText search_aet;
    private TextView
            cancel_tv;
    private SearchAllAdapter mAdapter;
    private RecyclerView recycler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_all;
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(findViewById(R.id.parent_ll));
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white));
        recycler = findViewById(R.id.recycler);
        search_aet = findViewById(R.id.search_aet);
        cancel_tv = findViewById(R.id.cancel_tv);

    }

    @Override
    protected void initData() {
        presenter = new SearchAllPresenter(this);
        mAdapter = new SearchAllAdapter();
        recycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_empty_data_brvah_default);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void bindListener() {
        cancel_tv.setOnClickListener(v -> finish());

        search_aet.addTextChangedListener(new AfterInputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String finalInput = s.toString().trim();
                handleSearch(finalInput);
            }
        });
    }

    private void handleSearch(String finalInput) {
        if (TextUtils.isEmpty(finalInput)) {
            mAdapter.setNewInstance(null);
            return;
        }
        mAdapter.keyWord = finalInput;
        presenter.search(finalInput);
    }

    @Override
    public void bindSearchResult(List<SearchResultResponse> response) {
        if (response == null || response.isEmpty()) {
            mAdapter.setNewInstance(null);
            return;
        }
        mAdapter.setNewInstance(response);
    }


    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        SearchResultResponse data = mAdapter.getData().get(position);

        switch (data.type) {
            case TYPE_1_HOTEL:
                JcsCalendarDialog dialog = new JcsCalendarDialog();
                dialog.initCalendar(this);
                HotelDetailActivity.goTo(this, data.id, dialog.getStartBean(), dialog.getEndBean(), 1, "", "", 1);
                break;
            case TYPE_2_TRAVEL:
                TouristAttractionDetailActivity.goTo(this, data.id);
                break;
            case TYPE_3_SERVICE:
                toActivity(MechanismDetailActivity.class, new IntentEntry(MechanismDetailActivity.K_MECHANISM_ID, String.valueOf(data.id)));
                break;
            case TYPE_4_RESTAURANT:
                // todo 跳转至餐厅页
                ToastUtils.showShort(R.string.coming_soon);
                break;
            default:
                break;
        }

    }


}
