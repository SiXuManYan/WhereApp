package com.jcs.where.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.government.activity.ConvenienceServiceSearchActivity;
import com.jcs.where.government.activity.MechanismDetailActivity;
import com.jcs.where.hotel.watcher.AfterInputWatcher;
import com.jcs.where.news.NewsSearchResultActivity;
import com.jcs.where.search.adapter.HotSearchAdapter;
import com.jcs.where.search.adapter.RecommendAdapter;
import com.jcs.where.search.adapter.SearchHistoryAdapter;
import com.jcs.where.search.bean.ISearchResponse;
import com.jcs.where.search.model.SearchModel;
import com.jcs.where.search.tag.SearchTag;
import com.jcs.where.utils.SearchHistoryUtils;
import com.jcs.where.view.MyLayoutManager;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 酒店搜索页面
 */
public class SearchActivity extends BaseActivity {


    public static final String EXT_SELECT_SEARCH = "select_search";
    private static final String EXT_CITY_ID = "cityId";
    private static final String EXT_CATEGORY_ID = "categoryId";
    private static final String EXT_SEARCH_TAG = "searchTag";
    private TextView cancelTv;
    private EditText mSearchEt;
    private View topBg;
    private RecyclerView searchHistoryRv, searchHotRv;
    private HotSearchAdapter hotSearchAdapter;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private RecommendAdapter mSearchResultAdapter;
    private RecyclerView recommendSearchRv;
    private SearchTag mSearchTag;
    private String mAreaId;
    private SearchModel mModel;
    private String mCategoryId = null;

    public static void goTo(Activity activity, String cityId, SearchTag searchTag, int requestCode) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXT_CITY_ID, cityId);
        intent.putExtra(EXT_SEARCH_TAG, searchTag);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void goTo(Activity activity, String cityId, String categoryId, SearchTag searchTag, int requestCode) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXT_CITY_ID, cityId);
        intent.putExtra(EXT_CATEGORY_ID, categoryId);
        intent.putExtra(EXT_SEARCH_TAG, searchTag);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initView() {
        cancelTv = findViewById(R.id.tv_cancel);
        mSearchEt = findViewById(R.id.et_search);
        topBg = findViewById(R.id.topBg);
        setMarginTopForStatusBar(topBg);
        searchHistoryRv = findViewById(R.id.rv_searchhistory1);
        searchHotRv = findViewById(R.id.rv_searchhot);
        recommendSearchRv = findViewById(R.id.rv_searchrecommend);

        mSearchHistoryAdapter = new SearchHistoryAdapter();
        mSearchHistoryAdapter.addChildClickViewIds(R.id.rl_searchhistory);
        mSearchResultAdapter = new RecommendAdapter();
        mSearchResultAdapter.addChildClickViewIds(R.id.ll_search);
        recommendSearchRv.setAdapter(mSearchResultAdapter);
        hotSearchAdapter = new HotSearchAdapter();
        hotSearchAdapter.addChildClickViewIds(R.id.rl_searchhistory);
        searchHotRv.setAdapter(hotSearchAdapter);
    }

    private void initSearchHistory() {
        if (searchHistoryRv != null) {
            searchHistoryRv.removeAllViews();
        }
        if (mSearchHistoryAdapter != null) {
            mSearchHistoryAdapter.getData().clear();
            mSearchHistoryAdapter.notifyDataSetChanged();
        }
        MyLayoutManager layout = new MyLayoutManager();
        //必须，防止recyclerview高度为wrap时测量item高度0
        layout.setAutoMeasureEnabled(true);
        searchHistoryRv.setLayoutManager(layout);
        mSearchHistoryAdapter.addData(SearchHistoryUtils.getSearchHistory(SearchActivity.this, mSearchTag));
        searchHistoryRv.setAdapter(mSearchHistoryAdapter);
    }

    @Override
    protected void initData() {
        mModel = new SearchModel();
        Intent intent = getIntent();
        mSearchTag = (SearchTag) intent.getSerializableExtra(EXT_SEARCH_TAG);
        mAreaId = intent.getStringExtra(EXT_CITY_ID);
        mCategoryId = intent.getStringExtra(EXT_CATEGORY_ID);
        // 根据SearchTag，设置 EditText#hint
        deployEtHint();


        MyLayoutManager layout1 = new MyLayoutManager();
        //必须，防止recyclerview高度为wrap时测量item高度0
        layout1.setAutoMeasureEnabled(true);
        searchHotRv.setLayoutManager(layout1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this,
                LinearLayoutManager.VERTICAL, false);
        recommendSearchRv.setLayoutManager(linearLayoutManager);
        initSearchHistory();

        getHotSearchFromNet();
    }

    /**
     * 获得热门搜索
     * 目前搜索页面支持搜索酒店和新闻
     * 根据tag获得不同的热门搜索内容
     * <p>
     * 企业黄页也是这个页面搜索
     */
    private void getHotSearchFromNet() {
        if (mSearchTag == SearchTag.HOTEL) {
            showLoading();
            mModel.getHotHotelList(new BaseObserver<List<String>>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                }

                @Override
                protected void onSuccess(List<String> response) {
                    stopLoading();
                    hotSearchAdapter.getData().clear();
                    hotSearchAdapter.addData(response);
                }
            });
        }

        if (mSearchTag == SearchTag.NEWS) {
            showLoading();
            mModel.getHotNewsList(new BaseObserver<List<String>>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                }

                @Override
                protected void onSuccess(List<String> response) {
                    stopLoading();
                    hotSearchAdapter.getData().clear();
                    hotSearchAdapter.addData(response);
                }
            });
        }

        if (mSearchTag == SearchTag.CONVENIENCE_SERVICE) {
            showLoading();
            mModel.getHotConvenienceServiceListAtSearch(new BaseObserver<List<String>>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                }

                @Override
                protected void onSuccess(List<String> response) {
                    stopLoading();
                    hotSearchAdapter.getData().clear();
                    hotSearchAdapter.addData(response);
                }
            });
        }
    }

    /**
     * 根据SearchTag，设置 EditText#hint
     */
    private void deployEtHint() {
        if (mSearchTag == SearchTag.HOTEL) {
            mSearchEt.setHint(R.string.search_hotel_name);
        }
        if (mSearchTag == SearchTag.NEWS) {
            mSearchEt.setHint(R.string.search_news_keyword);
        }
        if (mSearchTag == SearchTag.YELLOW_PAGE) {
            mSearchEt.setHint(R.string.search_keyword);
        }
        if (mSearchTag == SearchTag.CONVENIENCE_SERVICE) {
            mSearchEt.setHint(R.string.please_input_merchant_or_goods);
        }
    }

    @Override
    protected void bindListener() {
        cancelTv.setOnClickListener(view -> finish());
        mSearchEt.setOnEditorActionListener(this::onEditorActionClicked);
        mSearchEt.addTextChangedListener(new AfterInputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (mSearchTag != SearchTag.CONVENIENCE_SERVICE) {
                    recommendSearch(s.toString(), s.toString().length());
                }
            }
        });

        findViewById(R.id.tv_clear).setOnClickListener(this::onClearClicked);

        hotSearchAdapter.setOnItemChildClickListener(this::onHotSearchItemClicked);
        mSearchHistoryAdapter.setOnItemChildClickListener(this::onSearchHistoryItemClicked);
        mSearchResultAdapter.setOnItemChildClickListener(this::onSearchResultItemClicked);
    }

    private void onHotSearchItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        String item = hotSearchAdapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra(EXT_SELECT_SEARCH, item);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onSearchHistoryItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        String item = mSearchHistoryAdapter.getItem(position);
        recommendSearch(item, item.length());
    }

    private void onSearchResultItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        ISearchResponse item = mSearchResultAdapter.getItem(position);
        if (mSearchTag == SearchTag.HOTEL) {
            Intent intent = new Intent();

            intent.putExtra(EXT_SELECT_SEARCH, item.getName());
            setResult(RESULT_OK, intent);
            finish();
        }

        if (mSearchTag == SearchTag.NEWS) {
            toActivity(NewsSearchResultActivity.class, new IntentEntry(NewsSearchResultActivity.K_INPUT, item.getName()));
        }

        if (mSearchTag == SearchTag.YELLOW_PAGE) {
            toActivity(MechanismDetailActivity.class, new IntentEntry(MechanismDetailActivity.K_MECHANISM_ID, String.valueOf(item.getId())));
        }

        if (mSearchTag == SearchTag.CONVENIENCE_SERVICE) {
            toActivity(MechanismDetailActivity.class, new IntentEntry(MechanismDetailActivity.K_MECHANISM_ID, String.valueOf(item.getId())));
        }
    }

    private void onClearClicked(View view) {
        SearchHistoryUtils.clear(SearchActivity.this);
        mSearchHistoryAdapter.getData().clear();
        mSearchHistoryAdapter.notifyDataSetChanged();
    }

    /**
     * 点击了搜索按钮
     */
    private boolean onEditorActionClicked(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //点击搜索的时候隐藏软键盘
            hideKeyboard(mSearchEt);
            SearchHistoryUtils.saveSearchHistory(SearchActivity.this, mSearchTag, mSearchEt.getText().toString());
            initSearchHistory();
            mSearchEt.clearFocus();

            if (mSearchTag == SearchTag.CONVENIENCE_SERVICE) {
                ConvenienceServiceSearchActivity.goTo(this,mCategoryId, mSearchEt.getText().toString());
            }
            return true;
        }

        return false;
    }

    /**
     * 根据SearchTag、输入后的内容搜索
     */
    private void recommendSearch(String text, int length) {
        if (length == 0) {
            recommendSearchRv.setVisibility(View.GONE);
        } else {
            recommendSearchRv.setVisibility(View.VISIBLE);
            mSearchResultAdapter.setInputText(text);
            if (mSearchTag == SearchTag.NEWS) {
                searchNews(text);
            }

            if (mSearchTag == SearchTag.HOTEL) {
                searchHotel(text);
            }

            if (mSearchTag == SearchTag.YELLOW_PAGE || mSearchTag == SearchTag.CONVENIENCE_SERVICE) {
                searchYellowPage(text);
            }

            SearchHistoryUtils.saveSearchHistory(SearchActivity.this, mSearchTag, text);
        }

    }

    private void searchYellowPage(String text) {
        mModel.getMechanismList(mAreaId, text, new BaseObserver<PageResponse<MechanismResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(PageResponse<MechanismResponse> response) {
                stopLoading();
                mSearchResultAdapter.getData().clear();
                mSearchResultAdapter.addData(response.getData());
            }
        });
    }

    private void searchNews(String text) {
        mModel.getNewsListByInput(text, new BaseObserver<PageResponse<NewsResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(PageResponse<NewsResponse> response) {
                stopLoading();
                mSearchResultAdapter.getData().clear();
                mSearchResultAdapter.addData(response.getData());
            }
        });
    }

    private void searchHotel(String text) {
        mModel.getHotelListByInput(mAreaId, text, new BaseObserver<PageResponse<HotelResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(PageResponse<HotelResponse> response) {
                stopLoading();
                mSearchResultAdapter.getData().clear();
                mSearchResultAdapter.addData(response.getData());
            }
        });
    }

    @Override
    protected boolean isStatusDark() {
        return true;
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
}
