package com.jcs.where.mine.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.BaseApplication;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.mine.adapter.LanguageAdapter;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.LocalLanguageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * create by zyf on 2021/1/10 8:00 下午
 */
public class LanguageActivity extends BaseActivity {

    private RecyclerView mRecycler;
    private LanguageAdapter mAdapter;
    private List<LanguageAdapter.LanguageBean> mLanguageList;
    private String mDefaultLanguage = "";
    private boolean isChangedLanguage = false;

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mRecycler = findViewById(R.id.languageRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LanguageAdapter();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        Locale languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this);
        mDefaultLanguage = languageLocale.getLanguage();


        mLanguageList = new ArrayList<>();
        mLanguageList.add(new LanguageAdapter.LanguageBean("简体中文", "zh", "zh".equals(mDefaultLanguage)));
        mLanguageList.add(new LanguageAdapter.LanguageBean("English", "en", "en".equals(mDefaultLanguage)));
        mAdapter.addData(mLanguageList);
    }

    @Override
    protected void bindListener() {
        mAdapter.setOnItemClickListener(this::onItemClicked);
    }

    private void onItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        int size = mLanguageList.size();
        for (int i = 0; i < size; i++) {
            LanguageAdapter.LanguageBean languageBean = mLanguageList.get(i);
            if (i == position) {
                languageBean.isSelected = true;
                CacheUtil.cacheLanguage(languageBean.local);
                isChangedLanguage = !languageBean.local.equals(mDefaultLanguage);
                extracted();
            } else {
                languageBean.isSelected = false;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        extracted();
    }

    private void extracted() {
        if (isChangedLanguage) {
            // 重置app的语言环境
            BaseApplication application = (BaseApplication) getApplication();
            application.changeLanguage();

            EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_REFRESH_LANGUAGE));
        }
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {

    }

}
