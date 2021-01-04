package com.jcs.where.yellow_page;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 选择分类的列表
 * create by zyf on 2021/1/3 8:06 PM
 */
public class CategoryToSelectedListFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private OnItemClickListener mListener;
    /**
     * index = 0：一级分类选中的item
     * index = 1：二级分类选中的item
     * index = 2：三级分类选中的item
     */
    private List<Integer> mSelectPosition;
    private List<CategoryResponse> mTotalCategories;
    private String mFirstLevelTotalIds = "";
    public static final int LEVEL_FIRST = 1;
    public static final int LEVEL_SECOND = 2;
    public static final int LEVEL_THIRD = 3;
    /**
     * 默认分类等级是一级分类
     * 注意，这个分类等级只是对应企业黄页的选项
     * 不是对应分类API的level参数
     */
    private int mLevel = -1;

    @Override
    protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void initData() {
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);

        mSelectPosition = new ArrayList<>();
        mSelectPosition.add(null);
        mSelectPosition.add(null);
        mSelectPosition.add(null);
    }

    @Override
    protected void bindListener() {
        mAdapter.setOnItemClickListener(this::onItemClicked);
    }

    private void onItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        // 当前展示的数据是什么level
        switch (mLevel) {
            case LEVEL_FIRST:
                // 存储一级分类选中状态，选择了哪个item，并重置下级分类的选择状态
                mSelectPosition.set(0, position);
                mSelectPosition.set(1, null);
                mSelectPosition.set(2, null);
                break;
            case LEVEL_SECOND:
                // 存储二级分类选中状态，选择了哪个item，并重置下级分类的选择状态
                mSelectPosition.set(1, position);
                mSelectPosition.set(2, null);
                break;
            case LEVEL_THIRD:
                // 存储三级分类选中状态，选择了哪个item
                mSelectPosition.set(2, position);
                break;
        }
        CategoryResponse categoryResponse = mAdapter.getData().get(position);
        mListener.onItemClicked(mLevel, categoryResponse);
    }

    public void setData(List<CategoryResponse> categoryList, int level) {
        mAdapter.getData().clear();
        mAdapter.addData(categoryList);
        mLevel = level;
    }

    public void setTotalCategories(List<CategoryResponse> totalCategories) {
        this.mTotalCategories = totalCategories;
    }

    public void setFirstLevelTotalIds(String firstLevelTotalIds) {
        this.mFirstLevelTotalIds = firstLevelTotalIds;
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public boolean isFirstLevel(){
        return mLevel == LEVEL_FIRST;
    }

    public boolean isSecondLevel(){
        return mLevel == LEVEL_SECOND;
    }

    public boolean isThirdLevel(){
        return mLevel == LEVEL_THIRD;
    }

    /**
     * level 为 -1，说明还没有点击选择子分类，应该展示全部数据
     * @return
     */
    public boolean isNoLevel(){
        return mLevel == -1;
    }

    public int getSelectFirstPosition(){
        return mSelectPosition.get(0);
    }
    
    public int getSelectSecondPosition(){
        return mSelectPosition.get(1);
    }
    
    public int getSelectThirdPosition(){
        return mSelectPosition.get(2);
    }

    public CategoryResponse getSelectFirstCate(){
        return mTotalCategories.get(getSelectFirstPosition());
    }

    public CategoryResponse getSelectSecondCate(){
        return getSelectFirstCate().getChild_categories().get(getSelectSecondPosition());
    }

    public CategoryResponse getSelectThirdCate(){
        return getSelectSecondCate().getChild_categories().get(getSelectThirdPosition());
    }

    /**
     * 获得当前分类id
     *
     * @return 分类id字符串，可能是 "1" "[1,2,3]"
     */
    public String getCurrentCategoryId(){
        switch (mLevel) {
            case LEVEL_FIRST:
                return String.valueOf(getSelectFirstCate().getId());
            case LEVEL_SECOND:
                return String.valueOf(getSelectSecondCate().getId());
            case LEVEL_THIRD:
                return String.valueOf(getSelectThirdCate().getId());
        }

        return mFirstLevelTotalIds;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmeng_category_list;
    }

    public void notifyAdapter() {
        mAdapter.notifyDataSetChanged();
    }

    public void unSelectSecond() {
        if (mSelectPosition != null) {
            mSelectPosition.set(1,null);
        }
    }

    public void unSelectThird() {
        if (mSelectPosition != null) {
            mSelectPosition.set(2,null);
        }
    }

    class Adapter extends BaseQuickAdapter<CategoryResponse, BaseViewHolder> {

        public Adapter() {
            super(R.layout.item_category_to_selected_list);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, CategoryResponse categoryResponse) {
            baseViewHolder.setText(R.id.categoryTitleTv, categoryResponse.getName());
            Integer selectedPosition = null;
            switch (mLevel) {
                case LEVEL_FIRST:
                    // 获取一级分类选中的item索引
                    selectedPosition = mSelectPosition.get(0);
                    break;
                case LEVEL_SECOND:
                    // 获取二级分类选中的item索引
                    selectedPosition = mSelectPosition.get(1);
                    break;
                case LEVEL_THIRD:
                    // 获取三级分类选中的item索引
                    selectedPosition = mSelectPosition.get(2);
                    break;
            }

            // 选中状态的item
            if (selectedPosition != null && baseViewHolder.getAdapterPosition() == selectedPosition) {
                baseViewHolder.setTextColor(R.id.categoryTitleTv,getContext().getColor(R.color.blue_4D9FF2));
                baseViewHolder.setGone(R.id.categoryCheckedIcon, false);
            } else {
                baseViewHolder.setGone(R.id.categoryCheckedIcon, true);
                baseViewHolder.setTextColor(R.id.categoryTitleTv,getContext().getColor(R.color.black_333333));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int level, CategoryResponse categoryResponse);
    }
}
