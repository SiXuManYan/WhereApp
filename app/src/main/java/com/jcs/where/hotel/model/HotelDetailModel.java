package com.jcs.where.hotel.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.JcsResponse;
import com.jcs.where.api.response.HotelCommentsResponse;
import com.jcs.where.api.response.HotelDetailResponse;
import com.jcs.where.api.response.HotelRoomDetailResponse;
import com.jcs.where.api.response.HotelRoomListResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.api.response.hotel.HotelComment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class HotelDetailModel extends BaseModel {

    public void postCollectHotel(int hotelId, BaseObserver<Object> observer) {
        dealResponse(mRetrofit.postCollectHotel(hotelId), observer);
    }

    public void delCollectHotel(int hotelId, BaseObserver<Object> observer) {
        dealResponse(mRetrofit.delCollectHotel(hotelId), observer);
    }

    public void getHotelDetail(int hotelId, BaseObserver<HotelDetailResponse> observer) {
        dealResponse(mRetrofit.getHotelDetail(hotelId), observer);
    }

    public void getHotelRooms(int hotelId, String startDate, String endDate, int roomNum, BaseObserver<ArrayList<HotelRoomListResponse>> observer) {
        dealResponse(mRetrofit.getHotelRooms(hotelId, startDate, endDate, roomNum), observer);
    }

    public void getHotelRoomById(int roomId, String startDate, String endDate, int roomNum, BaseObserver<HotelRoomDetailResponse> observer) {
        dealResponse(mRetrofit.getHotelRoomById(roomId, startDate, endDate, roomNum), observer);
    }

    public void getComments(int hotelId, BaseObserver<HotelCommentsResponse> observer) {
        dealResponse(mRetrofit.getHotelComments(hotelId), observer);
    }

    public void getHotelCommentList(int hotelId, BaseObserver<PageResponse<HotelComment>> observer) {
        dealResponse(mRetrofit.getHotelCommentList(hotelId,0,1), observer);
    }



}
