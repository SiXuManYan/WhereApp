package com.jcs.where.mine.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.GeneralResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.TouristAttractionResponse;
import com.jcs.where.api.response.collection.StoreCollectionResponse;
import com.jcs.where.mine.view_type.SameCityType;
import com.jcs.where.utils.GlideUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zyf on 2021/1/31
 */
public class SameCityListAdapter extends BaseMultiItemQuickAdapter<CollectedResponse, BaseViewHolder> implements LoadMoreModule {

    public SameCityListAdapter() {
        addItemType(SameCityType.Hotel, R.layout.item_hotellist);
        addItemType(SameCityType.TouristAttraction, R.layout.item_mechainsm_list);
        addItemType(SameCityType.Mechanism, R.layout.item_mechainsm_list);
        addItemType(SameCityType.Store, R.layout.item_mechainsm_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CollectedResponse response) {
        Integer type = response.getType();
        switch (type) {
            case SameCityType.Hotel:
                dealHotel(holder, response);
                break;
            case SameCityType.TouristAttraction:
                dealTouristAttraction(holder, response);
                break;
            case SameCityType.Mechanism:
                dealMechanism(holder, response);
                break;
            case SameCityType.Store:
                setStoreItem(holder, response);
                break;
            default:

                break;
        }
    }

    private void setStoreItem(BaseViewHolder holder, CollectedResponse response) {
        StoreCollectionResponse data = response.estore;
        holder.setText(R.id.mechanismTitleTv, data.getName());
        holder.setText(R.id.mechanismAddressTv, data.getAddress());
        ImageView iv = holder.findView(R.id.mechanismIconIv);
        ArrayList<String> images = data.getImages();
        if (!images.isEmpty() && iv != null) {
            GlideUtil.load(getContext(), images.get(0), iv);
        }
    }

    private void dealMechanism(BaseViewHolder baseViewHolder, CollectedResponse collectedResponse) {
        GeneralResponse general = collectedResponse.getGeneral();
        baseViewHolder.setText(R.id.mechanismTitleTv, general.getName());
        baseViewHolder.setText(R.id.mechanismAddressTv, general.getAddress());
        ImageView mechanismIconIv = baseViewHolder.findView(R.id.mechanismIconIv);
        List<String> images = general.getImages();
        if (images != null && images.size() > 0 && mechanismIconIv != null) {
            GlideUtil.load(getContext(), images.get(0), mechanismIconIv);
        }
    }

    private void dealTouristAttraction(BaseViewHolder baseViewHolder, CollectedResponse collectedResponse) {
        TouristAttractionResponse travel = collectedResponse.getTravel();
        baseViewHolder.setText(R.id.mechanismTitleTv, travel.getName());
        baseViewHolder.setText(R.id.mechanismAddressTv, travel.getAddress());
        ImageView mechanismIconIv = baseViewHolder.findView(R.id.mechanismIconIv);
        List<String> images = travel.getImage();
        if (images != null && images.size() > 0 && mechanismIconIv != null) {
            GlideUtil.load(getContext(), images.get(0), mechanismIconIv);
        }
    }

    private void dealHotel(BaseViewHolder baseViewHolder, CollectedResponse collectedResponse) {
        HotelResponse hotel = collectedResponse.getHotel();
        RoundedImageView photoIv = baseViewHolder.findView(R.id.iv_photo);

        if (photoIv != null) {

            ViewGroup.LayoutParams layoutParams = photoIv.getLayoutParams();
            layoutParams.height = SizeUtils.dp2px(70);
            layoutParams.width = SizeUtils.dp2px(70);
            photoIv.setLayoutParams(layoutParams);

            if (!TextUtils.isEmpty(hotel.getImages().get(0))) {
                GlideUtil.load(getContext(), hotel.getImages().get(0), photoIv);
            } else {
                photoIv.setImageResource(R.mipmap.ic_glide_default);
            }
        }
        TextView nameTv = baseViewHolder.findView(R.id.tv_name);
        if (nameTv != null) {
            nameTv.setText(hotel.getName());
            if (hotel.getFacebook_link() == null) {
                nameTv.setCompoundDrawables(null, null, null, null);
            }
        }

        LinearLayout tagLl = baseViewHolder.findView(R.id.ll_tag);
        if (tagLl != null) {
            tagLl.setVisibility(View.GONE);
        }
        TextView addressTv = baseViewHolder.findView(R.id.tv_address);
        if (addressTv != null) {
            addressTv.setText(hotel.getAddress());
        }
        TextView distanceTv = baseViewHolder.findView(R.id.tv_distance);
        if (distanceTv != null) {
            distanceTv.setVisibility(View.GONE);
        }
        TextView scoreTv = baseViewHolder.findView(R.id.tv_score);
        if (scoreTv != null) {
            scoreTv.setText(String.valueOf(hotel.getGrade()));
        }
        TextView commentNumTv = baseViewHolder.findView(R.id.tv_commentnumber);
        String commentNumberText = String.format(getContext().getString(R.string.comment_num_prompt), hotel.getComment_counts());
        if (commentNumTv != null) {
            commentNumTv.setText(commentNumberText);
        }
        TextView priceTv = baseViewHolder.findView(R.id.tv_price);
        if (priceTv != null) {
            priceTv.setVisibility(View.GONE);
        }
    }
}
