package com.jcs.where.government.activity;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.base.BaseMapActivity;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.government.model.GovernmentMapModel;
import com.jcs.where.utils.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 政府机构地图页
 * 政府机构列表是本页的fragment
 * create by zyf on 2020/12/28 7:44 PM
 */
public class GovernmentMapActivity extends BaseMapActivity {
    protected GovernmentMapModel mModel;
    protected final String TYPE_GOVERNMENT = "3";
    protected final String ID_GOVERNMENT = "1";

    /**
     * 展示机构列表的Fragment集合
     */
    protected List<MechanismListFragment> mListFragments;

    @Override
    protected void initData() {
        super.initData();
        mModel = new GovernmentMapModel();
        mListFragments = new ArrayList<>();
    }

    @Override
    protected void bindListener() {
        super.bindListener();
        mSearchEt.requestFocus();
        mSearchEt.setOnClickListener(v -> mPopupLayout.hideWithAnim());
    }

    @Override
    protected void addFistFragmentToList(CategoryResponse categoryResponse) {
        mListFragments.clear();
        mListFragments.add(MechanismListFragment.newInstance(categoryResponse, true));
    }

    @Override
    protected void addFragmentToList(CategoryResponse categoryResponse) {
        mListFragments.add(MechanismListFragment.newInstance(categoryResponse));
    }

    @Override
    protected void getListDataByInput(String input) {
        mModel.getMechanismListForMapSearch(input, new BaseObserver<List<MechanismResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NotNull List<MechanismResponse> mechanismResponses) {
                stopLoading();
                mSearchAdapter.getData().clear();
                if (mechanismResponses.size() > 0) {
                    mSearchAdapter.addData(mechanismResponses);
                } else {
                    showEmptySearchAdapter();
                }
                if (mSearchRecycler.getVisibility() == View.GONE) {
                    mSearchRecycler.setVisibility(View.VISIBLE);
                }
                mSearchEt.requestFocus();
            }
        });
    }

    @Override
    protected String getAllCategoryId() {
        return ID_GOVERNMENT;
    }

    @Override
    protected String getParentCategoryId() {
        return ID_GOVERNMENT;
    }

    /**
     * 政府是1级分类，政府的子分类是二级分类
     *
     * @return 二级分类
     */
    @Override
    protected int getCategoryLevel() {
        return 2;
    }

    @Override
    protected List<? extends Fragment> getListFragments() {
        return mListFragments;
    }

    @Override
    protected void firstFragmentGetData() {
        if (mListFragments != null && mListFragments.size() > 0) {
            MechanismListFragment fistFragment = mListFragments.get(0);
            fistFragment.getNetData();
        }
    }

    @Override
    protected void getDataAtMapFromNet() {
        mModel.getMechanismListForMap(ID_GOVERNMENT, Constant.LAT, Constant.LNG, new BaseObserver<List<MechanismResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull List<MechanismResponse> mechanismResponses) {
                stopLoading();
                mMapMarkerUtil.clear();
                if (mechanismResponses.size() > 0) {
                    mMapMarkerUtil.addAllMechanismForMap(mechanismResponses);
                    mMapMarkerUtil.addMarkerToMap();

                    mCardFragment.bindAllData(mechanismResponses);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_government_map;
    }
}
