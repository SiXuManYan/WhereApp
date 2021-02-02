package com.jcs.where.api.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.jcs.where.mine.view_type.SameCityType;

/**
 * create by zyf on 2021/1/31 4:39 下午
 */
public class CollectedResponse implements MultiItemEntity {

    /**
     * type : 3
     * news : {"id":64,"content_type":2,"title":"Wonderland of long ago: and how she would keep, through all her coaxing.","cover_images":["https://whereoss.oss-accelerate.aliyuncs.com/news/covers/311055f76cd9466e91aab421ad9d11d1.jpg"],"video_time":"06:58","video_link":"https://whereoss.oss-cn-beijing.aliyuncs.com/videos/218679048-1-208.mp4","publisher":{"id":11,"nickname":"Where"},"created_at":"5个月前"}
     */

    @SerializedName("type")
    private Integer type;
    @SerializedName("news")
    private NewsResponse news;
    @SerializedName("hotel")
    private HotelResponse hotel;
    @SerializedName("travel")
    private TouristAttractionResponse travel;
    @SerializedName("restaurant")
    private RestaurantResponse restaurant;
    @SerializedName("general")
    private GeneralResponse general;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public NewsResponse getNews() {
        return news;
    }

    public void setNews(NewsResponse news) {
        this.news = news;
    }

    public HotelResponse getHotel() {
        return hotel;
    }

    public void setHotel(HotelResponse hotel) {
        this.hotel = hotel;
    }

    public TouristAttractionResponse getTravel() {
        return travel;
    }

    public void setTravel(TouristAttractionResponse travel) {
        this.travel = travel;
    }

    public RestaurantResponse getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantResponse restaurant) {
        this.restaurant = restaurant;
    }

    public GeneralResponse getGeneral() {
        return general;
    }

    public void setGeneral(GeneralResponse general) {
        this.general = general;
    }

    @Override
    public int getItemType() {
        // type 为 3
        // 表示当前是在收藏页面 CollectionListActivity
        //          -新闻模块 ArticleListFragment 和 VideoListFragment中使用的
        if (type == 3) {
            return news.getItemType();
        }

        // 收藏页面 CollectionListActivity
        //      -同城模块 SameCityListFragment 中不展示新闻所以判断下面三个
        if (hotel != null) {
            return SameCityType.Hotel;
        }
        if (travel != null) {
            return SameCityType.TouristAttraction;
        }
        if (general != null) {
            return SameCityType.Mechanism;
        }
        return 0;
    }
}
