package com.jcs.where.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/2/22 12:38 上午
 */
public class WriteHotelCommentRequest {

    /**
     * star :
     * content :
     * images :
     * comment_travel_type_id :
     * hotel_id :
     * order_id :
     */

    @SerializedName("star")
    private int star;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private String[] images;
    @SerializedName("comment_travel_type_id")
    private String commentTravelTypeId;
    @SerializedName("hotel_id")
    private String hotelId;
    @SerializedName("order_id")
    private String orderId;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getCommentTravelTypeId() {
        return commentTravelTypeId;
    }

    public void setCommentTravelTypeId(String commentTravelTypeId) {
        this.commentTravelTypeId = commentTravelTypeId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
