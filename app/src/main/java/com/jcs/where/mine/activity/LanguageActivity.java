package com.jcs.where.mine.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.BaseApplication;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.features.main.MainActivity;
import com.jcs.where.mine.adapter.LanguageAdapter;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.LocalLanguageUtil;

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
        mRecycler = findViewById(R.id.languageRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LanguageAdapter();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        Locale languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this);
        mDefaultLanguage = languageLocale.getLanguage();
        Log.e("LanguageActivity", "initData: " + "mDefaultLanguage=" + mDefaultLanguage);

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
            } else {
                languageBean.isSelected = false;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isChangedLanguage) {
            // 重置app的语言环境
            BaseApplication application = (BaseApplication) getApplication();
            application.changeLanguage();
            // 修改了语言，则要重新启动 HomeActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
}
