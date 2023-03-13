package com.jiechengsheng.city.api.response;

import com.jiechengsheng.city.search.bean.ISearchResponse;
import com.jiechengsheng.city.utils.MapMarkerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 机构信息
 * create by zyf on 2020/12/28 9:17 PM
 */
public class MechanismResponse implements ISearchResponse, MapMarkerUtil.IMapData {

    /**
     * id : 1
     * images : ["图片"]
     * title : 标题
     * address : 地址
     * lat : 纬度
     * lng : 经度
     * distance : 距离
     * tags : ["标签"]
     */

    public Integer id;
    public String title;
    public String address;
    public double lat;
    public double lng;
    public Double distance;
    public List<String> images;
    public ArrayList<String> tags = new ArrayList();

    /**
     * 是否选中
     */
    public boolean native_select = false;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }




    @Override
    public String getName() {
        return title;
    }
}
