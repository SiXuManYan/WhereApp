package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelListBean;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.utils.SearchHistoryUtils;
import com.jcs.where.view.MyLayoutManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;

public class HotelSearchActivity extends BaseActivity {


    private static final String EXT_CITYID = "cityId";
    public static final String EXT_SELECTSEARCH = "select_search";
    private TextView cancelTv;
    private EditText searchEt;
    private LinearLayout searchHistoryLl;
    private RecyclerView searchHistoryRv, searchHotRv;
    private SearchHistoryAdapter searchHistoryAdapter;
    private HotSearchAdapter hotSearchAdapter;
    private RecyclerView recommendSearchRv;
    private String useText;
    private RecommendAdapter recommendAdapter;

    public static void goTo(Context context, String cityId) {
        Intent intent = new Intent(context, HotelSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXT_CITYID, cityId);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void goTo(Activity activity, String cityId, int requestCode) {
        Intent intent = new Intent(activity, HotelSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXT_CITYID, cityId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        initView();
    }

    private void initView() {
        cancelTv = V.f(this, R.id.tv_cancel);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchEt = V.f(this, R.id.et_search);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    hideKeyboard(searchEt);
                    SearchHistoryUtils.saveSearchHistory(HotelSearchActivity.this, searchEt.getText().toString());
                    initSearshHistory();
                    //  EventBus.getDefault().post(MessageEvent.SEARCHHISTORY);
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
        searchHistoryLl = V.f(this, R.id.ll_searchhistory);
        searchHistoryRv = V.f(this, R.id.rv_searchhistory1);
        searchHistoryAdapter = new SearchHistoryAdapter(HotelSearchActivity.this);
        V.f(this, R.id.tv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchHistoryUtils.clear(HotelSearchActivity.this);
                searchHistoryLl.setVisibility(View.GONE);
            }
        });
        searchHotRv = V.f(this, R.id.rv_searchhot);
        MyLayoutManager layout1 = new MyLayoutManager();
        //必须，防止recyclerview高度为wrap时测量item高度0
        layout1.setAutoMeasureEnabled(true);
        searchHotRv.setLayoutManager(layout1);
        hotSearchAdapter = new HotSearchAdapter(HotelSearchActivity.this);
        recommendSearchRv = V.f(this, R.id.rv_searchrecommend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelSearchActivity.this,
                LinearLayoutManager.VERTICAL, false);
        recommendSearchRv.setLayoutManager(linearLayoutManager);
        recommendAdapter = new RecommendAdapter(HotelSearchActivity.this);
        initSearshHistory();
        initData();
    }

    private void initSearshHistory() {
        if (SearchHistoryUtils.getSearchHistory(HotelSearchActivity.this).size() == 0) {
            searchHistoryLl.setVisibility(View.GONE);
        } else {
            searchHistoryLl.setVisibility(View.VISIBLE);
        }
        if (searchHistoryRv != null) {
            searchHistoryRv.removeAllViews();
        }
        if (searchHistoryAdapter != null) {
            searchHistoryAdapter.clearData();
        }
        MyLayoutManager layout = new MyLayoutManager();
        //必须，防止recyclerview高度为wrap时测量item高度0
        layout.setAutoMeasureEnabled(true);
        searchHistoryRv.setLayoutManager(layout);
        searchHistoryAdapter.setData(SearchHistoryUtils.getSearchHistory(HotelSearchActivity.this));
        searchHistoryRv.setAdapter(searchHistoryAdapter);
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hot/searches?area_id=" + getIntent().getStringExtra(EXT_CITYID), null, "", TokenManager.get().getToken(HotelSearchActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> list = gson.fromJson(result, type);
                    hotSearchAdapter.setData(list);
                    searchHotRv.setAdapter(hotSearchAdapter);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelSearchActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelSearchActivity.this, e.getMessage());
            }
        });
    }

    private void recommendSearch(String text, int length) {
        if (length == 0) {
            recommendSearchRv.setVisibility(View.GONE);
        } else {
            recommendSearchRv.setVisibility(View.VISIBLE);
            useText = text;
            HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotels?area_id=" + getIntent().getStringExtra(EXT_CITYID) + "&search_input=" + text, null, "", TokenManager.get().getToken(HotelSearchActivity.this), new HttpUtils.StringCallback() {
                @Override
                public void onSuccess(int code, String result) {
                    stopLoading();
                    if (code == 200) {
                        HotelListBean hotelListBean = new Gson().fromJson(result, HotelListBean.class);
                        recommendAdapter.setData(hotelListBean.getData());
                        recommendSearchRv.setAdapter(recommendAdapter);
                    } else {
                        ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                        ToastUtils.showLong(HotelSearchActivity.this, errorBean.message);
                    }
                }

                @Override
                public void onFaileure(int code, Exception e) {
                    stopLoading();
                    ToastUtils.showLong(HotelSearchActivity.this, e.getMessage());
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

    private class SearchHistoryAdapter extends BaseQuickAdapter<String> {

        public SearchHistoryAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_searchhistory;
        }

        @Override
        protected void initViews(QuickHolder holder, String data, int position) {
            TextView searchHistoryTv = holder.findViewById(R.id.tv_searchhistory);
            searchHistoryTv.setText(data);
            holder.findViewById(R.id.rl_searchhistory).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(EXT_SELECTSEARCH, data);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    private class HotSearchAdapter extends BaseQuickAdapter<String> {

        public HotSearchAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_searchhistory;
        }

        @Override
        protected void initViews(QuickHolder holder, String data, int position) {
            TextView searchHistoryTv = holder.findViewById(R.id.tv_searchhistory);
            searchHistoryTv.setText(data);
            holder.findViewById(R.id.rl_searchhistory).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchHistoryUtils.saveSearchHistory(HotelSearchActivity.this, data);
                    Intent intent = new Intent();
                    intent.putExtra(EXT_SELECTSEARCH, data);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    private class RecommendAdapter extends BaseQuickAdapter<HotelListBean.DataBean> {

        public RecommendAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_recommendsearch;
        }

        @Override
        protected void initViews(QuickHolder holder, HotelListBean.DataBean data, int position) {
            TextView nameTv = holder.findViewById(R.id.tv_name);
            nameTv.setText(matcherSearchText(data.getName(), useText));
            holder.findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SearchHistoryUtils.saveSearchHistory(HotelSearchActivity.this, data.getName());
                    Intent intent = new Intent();
                    intent.putExtra(EXT_SELECTSEARCH, data.getName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
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

}
