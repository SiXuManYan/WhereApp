package com.jcs.where.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.GeneralResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.TouristAttractionResponse;
import com.jcs.where.mine.view_type.SameCityType;
import com.jcs.where.utils.GlideUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by zyf on 2021/1/31
 */
public class SameCityListAdapter extends BaseMultiItemQuickAdapter<CollectedResponse, BaseViewHolder> {

    public SameCityListAdapter() {
        addItemType(SameCityType.Hotel, R.layout.item_hotellist);
        addItemType(SameCityType.TouristAttraction, R.layout.item_mechainsm_list);
        addItemType(SameCityType.Mechanism, R.layout.item_mechainsm_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, CollectedResponse collectedResponse) {
        Integer type = collectedResponse.getType();
        Log.e("SameCityListAdapter", "convert: " + "type=" + type);
        switch (type) {
            case SameCityType.Hotel:
                dealHotel(baseViewHolder, collectedResponse);
                break;
            case SameCityType.TouristAttraction:
                dealTouristAttraction(baseViewHolder, collectedResponse);
                break;
            case SameCityType.Mechanism:
                dealMechanism(baseViewHolder, collectedResponse);
                break;
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
            layoutParams.height = getDp(70);
            layoutParams.width = getDp(70);
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

    protected int getDp(int height) {
        Context context = getContext();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
    }
}
