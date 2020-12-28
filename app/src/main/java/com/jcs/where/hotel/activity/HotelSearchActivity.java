package com.jcs.where.hotel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelListBean;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.utils.SearchHistoryUtils;
import com.jcs.where.view.MyLayoutManager;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 酒店搜索页面
 */
public class HotelSearchActivity extends BaseActivity implements OnItemChildClickListener {


    public static final String EXT_SELECT_SEARCH = "select_search";
    private static final String EXT_CITY_ID = "cityId";
    private TextView cancelTv;
    private EditText searchEt;
    private View topBg;
    private RecyclerView searchHistoryRv, searchHotRv;
    private HotSearchAdapter hotSearchAdapter;
    private SearchHistoryAdapter searchHistoryAdapter;
    private RecommendAdapter recommendAdapter;
    private RecyclerView recommendSearchRv;
    private String useText;

    public static void goTo(Activity activity, String cityId, int requestCode) {
        Intent intent = new Intent(activity, HotelSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXT_CITY_ID, cityId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initView() {
        cancelTv = findViewById(R.id.tv_cancel);
        searchEt = findViewById(R.id.et_search);
        topBg = findViewById(R.id.topBg);
        setMarginTopForStatusBar(topBg);
        searchHistoryRv = findViewById(R.id.rv_searchhistory1);
        searchHotRv = findViewById(R.id.rv_searchhot);
        recommendSearchRv = findViewById(R.id.rv_searchrecommend);

        searchHistoryAdapter = new SearchHistoryAdapter();
        searchHistoryAdapter.addChildClickViewIds(R.id.rl_searchhistory);
        recommendAdapter = new RecommendAdapter();
        recommendAdapter.addChildClickViewIds(R.id.ll_search);
        recommendSearchRv.setAdapter(recommendAdapter);
        hotSearchAdapter = new HotSearchAdapter();
        hotSearchAdapter.addChildClickViewIds(R.id.rl_searchhistory);
        searchHotRv.setAdapter(hotSearchAdapter);
    }

    private void initSearchHistory() {
        if (searchHistoryRv != null) {
            searchHistoryRv.removeAllViews();
        }
        if (searchHistoryAdapter != null) {
            searchHistoryAdapter.getData().clear();
            searchHistoryAdapter.notifyDataSetChanged();
        }
        MyLayoutManager layout = new MyLayoutManager();
        //必须，防止recyclerview高度为wrap时测量item高度0
        layout.setAutoMeasureEnabled(true);
        searchHistoryRv.setLayoutManager(layout);
        searchHistoryAdapter.addData(SearchHistoryUtils.getSearchHistory(HotelSearchActivity.this));
        searchHistoryRv.setAdapter(searchHistoryAdapter);
    }

    @Override
    protected void initData() {
        showLoading();

        MyLayoutManager layout1 = new MyLayoutManager();
        //必须，防止recyclerview高度为wrap时测量item高度0
        layout1.setAutoMeasureEnabled(true);
        searchHotRv.setLayoutManager(layout1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelSearchActivity.this,
                LinearLayoutManager.VERTICAL, false);
        recommendSearchRv.setLayoutManager(linearLayoutManager);
        initSearchHistory();

        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hot/searches?area_id=" + getIntent().getStringExtra(EXT_CITY_ID), null, "", TokenManager.get().getToken(HotelSearchActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> list = gson.fromJson(result, type);
                    hotSearchAdapter.getData().clear();
                    hotSearchAdapter.addData(list);

                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    showToast(errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void bindListener() {
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    hideKeyboard(searchEt);
                    SearchHistoryUtils.saveSearchHistory(HotelSearchActivity.this, searchEt.getText().toString());
                    initSearchHistory();
                    searchEt.clearFocus();
                    return true;
                }

                return false;
            }
        });
        searchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                recommendSearch(s.toString(), s.toString().length());
            }
        });

        findViewById(R.id.tv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchHistoryUtils.clear(HotelSearchActivity.this);
            }
        });

        hotSearchAdapter.setOnItemChildClickListener(this);
        searchHistoryAdapter.setOnItemChildClickListener(this);
        recommendAdapter.setOnItemChildClickListener(this);
    }

    private void recommendSearch(String text, int length) {
        if (length == 0) {
            recommendSearchRv.setVisibility(View.GONE);
        } else {
            recommendSearchRv.setVisibility(View.VISIBLE);
            useText = text;
            HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotels?area_id=" + getIntent().getStringExtra(EXT_CITY_ID) + "&search_input=" + text, null, "", TokenManager.get().getToken(HotelSearchActivity.this), new HttpUtils.StringCallback() {
                @Override
                public void onSuccess(int code, String result) {
                    stopLoading();
                    if (code == 200) {
                        HotelListBean hotelListBean = new Gson().fromJson(result, HotelListBean.class);
                        recommendAdapter.getData().clear();
                        recommendAdapter.addData(hotelListBean.getData());
                    } else {
                        ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                        showToast(errorBean.message);
                    }
                }

                @Override
                public void onFaileure(int code, Exception e) {
                    stopLoading();
                    showToast(e.getMessage());
                }
            });
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    public SpannableString matcherSearchText(String text, String keyword) {
        SpannableString ss = new SpannableString(text);
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new TextAppearanceSpan(this, R.style.style_grey_999999), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//new ForegroundColorSpan(color)
        }
        return ss;
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        if (adapter == hotSearchAdapter && view.getId() == R.id.rl_searchhistory) {
            String item = hotSearchAdapter.getItem(position);
            SearchHistoryUtils.saveSearchHistory(HotelSearchActivity.this, item);
            Intent intent = new Intent();
            intent.putExtra(EXT_SELECT_SEARCH, item);
            setResult(RESULT_OK, intent);
            finish();
        }

        if (adapter == searchHistoryAdapter && view.getId() == R.id.rl_searchhistory) {
            Intent intent = new Intent();
            intent.putExtra(EXT_SELECT_SEARCH, searchHistoryAdapter.getItem(position));
            setResult(RESULT_OK, intent);
            finish();
        }

        if (adapter == recommendAdapter && view.getId() == R.id.ll_search) {
            HotelListBean.DataBean item = recommendAdapter.getItem(position);
            SearchHistoryUtils.saveSearchHistory(HotelSearchActivity.this, item.getName());
            Intent intent = new Intent();
            intent.putExtra(EXT_SELECT_SEARCH, item.getName());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private static class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public SearchHistoryAdapter() {
            super(R.layout.item_searchhistory);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            TextView searchHistoryTv = baseViewHolder.findView(R.id.tv_searchhistory);
            searchHistoryTv.setText(s);
        }
    }

    private static class HotSearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public HotSearchAdapter() {
            super(R.layout.item_searchhistory);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            TextView searchHistoryTv = baseViewHolder.findView(R.id.tv_searchhistory);
            searchHistoryTv.setText(s);
        }
    }

    private class RecommendAdapter extends BaseQuickAdapter<HotelListBean.DataBean, BaseViewHolder> {

        public RecommendAdapter() {
            super(R.layout.item_recommendsearch);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelListBean.DataBean data) {
            TextView nameTv = baseViewHolder.findView(R.id.tv_name);
            nameTv.setText(matcherSearchText(data.getName(), useText));
        }
    }

}
