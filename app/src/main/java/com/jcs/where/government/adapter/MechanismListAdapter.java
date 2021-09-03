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
    protected void convert(@NotNull BaseViewHolder holder, MechanismResponse response) {
        holder.setText(R.id.mechanismTitleTv, response.getTitle());
        holder.setText(R.id.mechanismAddressTv, response.getAddress());
        Double distance = response.getDistance();
        if (distance == 0) {
            holder.setVisible(R.id.mechanismDistanceTv, false);
        } else {
            holder.setVisible(R.id.mechanismDistanceTv, true);
            holder.setText(R.id.mechanismDistanceTv, distance + "km");
        }
        ImageView mechanismIconIv = holder.findView(R.id.mechanismIconIv);
        List<String> images = response.getImages();
        if (images != null && images.size() > 0 && mechanismIconIv != null) {
            GlideUtil.load(getContext(), images.get(0), mechanismIconIv);
        }
    }
}
