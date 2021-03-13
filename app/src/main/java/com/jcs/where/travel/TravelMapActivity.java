package com.jcs.where.travel;

import android.view.View;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.TouristAttractionResponse;
import com.jcs.where.base.BaseMapActivity;
import com.jcs.where.travel.fragment.TouristAttractionFragment;
import com.jcs.where.travel.model.TravelMapModel;
import com.jcs.where.utils.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import io.reactivex.annotations.NonNull;


public class TravelMapActivity extends BaseMapActivity {
    private TravelMapModel mModel;
    private final String TYPE_TOURIST_ATTRACTION = "2";
    private final String ID_TOURIST_ATTRACTION = "106";

    /**
     * 展示旅游景点列表的Fragment集合
     */
    protected List<TouristAttractionFragment> mListFragments;

    @Override
    protected void initData() {
        super.initData();
        mModel = new TravelMapModel();
        mListFragments = new ArrayList<>();
    }

    @Override
    protected void addFistFragmentToList(CategoryResponse categoryResponse) {
        mListFragments.clear();
        mListFragments.add(TouristAttractionFragment.newInstance(categoryResponse, true));
    }

    @Override
    protected void addFragmentToList(CategoryResponse categoryResponse) {
        mListFragments.add(TouristAttractionFragment.newInstance(categoryResponse));
    }

    @Override
    protected void getListDataByInput(String input) {
        mModel.getTouristAttractionListForMap(input, new BaseObserver<List<TouristAttractionResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NotNull List<TouristAttractionResponse> mapData) {
                stopLoading();
                mSearchAdapter.getData().clear();
                if (mapData.size() > 0) {
                    mSearchAdapter.addData(mapData);
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
        return TYPE_TOURIST_ATTRACTION;
    }

    @Override
    protected String getParentCategoryId() {
        return ID_TOURIST_ATTRACTION;
    }

    /**
     * 旅游景点是二级分类，旅游景点的子分类是三级分类
     *
     * @return 三级分类
     */
    @Override
    protected int getCategoryLevel() {
        return 3;
    }

    @Override
    protected List<? extends Fragment> getListFragments() {
        return mListFragments;
    }

    @Override
    protected void firstFragmentGetData() {
        if (mListFragments != null && mListFragments.size() > 0) {
            TouristAttractionFragment fistFragment = mListFragments.get(0);
            fistFragment.getNetData();
        }
    }

    @Override
    protected void getDataAtMapFromNet() {
        mModel.getTouristAttractionListForMap(TYPE_TOURIST_ATTRACTION, Constant.LAT, Constant.LNG, new BaseObserver<List<TouristAttractionResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
//                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull List<TouristAttractionResponse> touristAttractionResponses) {
                stopLoading();
                mMapMarkerUtil.clear();
                if (touristAttractionResponses.size() > 0) {
                    mMapMarkerUtil.addAllMechanismForMap(touristAttractionResponses);
                    mMapMarkerUtil.addMarkerToMap();

                    mCardFragment.bindAllData(touristAttractionResponses);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_government_map;
    }
}
