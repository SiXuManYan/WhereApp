package com.jcs.where.government.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.utils.GlideUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by zyf on 2020/12/28 9:13 PM
 */
public class MechanismListAdapter extends BaseQuickAdapter<MechanismResponse, BaseViewHolder> implements LoadMoreModule {
    public MechanismListAdapter() {
        super(R.layout.item_mechainsm_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MechanismResponse mechanismResponse) {
        baseViewHolder.setText(R.id.mechanismTitleTv, mechanismResponse.getTitle());
        baseViewHolder.setText(R.id.mechanismAddressTv, mechanismResponse.getAddress());
        Double distance = mechanismResponse.getDistance();
        if (distance == 0) {
            baseViewHolder.setVisible(R.id.mechanismDistanceTv, false);
        } else {
            baseViewHolder.setVisible(R.id.mechanismDistanceTv, true);
            baseViewHolder.setText(R.id.mechanismDistanceTv, distance + "km");
        }
        ImageView mechanismIconIv = baseViewHolder.findView(R.id.mechanismIconIv);
        List<String> images = mechanismResponse.getImages();
        if (images != null && images.size() > 0 && mechanismIconIv != null) {
            GlideUtil.load(getContext(), images.get(0), mechanismIconIv);
        }
    }
}
