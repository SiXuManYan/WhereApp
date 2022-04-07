package com.jcs.where.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.FootprintResponse;
import com.jcs.where.utils.GlideUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by zyf on 2021/1/31
 */
public class FootprintListAdapter extends BaseQuickAdapter<FootprintResponse, BaseViewHolder> {

    public FootprintListAdapter() {
        super(R.layout.item_footprint_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, FootprintResponse footprintResponse) {
        FootprintResponse.ModuleDataDTO dto = footprintResponse.getModuleData();
        // 所有 ViewType 都有的信息
        baseViewHolder.setText(R.id.footprintTitleTv, dto.getTitle());
        baseViewHolder.setText(R.id.footprintDateTv, footprintResponse.getCreatedAt());
        RoundedImageView riv = baseViewHolder.findView(R.id.footprintIconIv);
        List<String> coverImages = dto.getImages();
        if (coverImages.size() > 0) {
            GlideUtil.load(getContext(), coverImages.get(0), riv);
        } else {
            GlideUtil.load(getContext(), "", riv);
        }
    }
}
