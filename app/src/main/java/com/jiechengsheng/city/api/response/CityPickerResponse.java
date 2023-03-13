package com.jiechengsheng.city.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zyf on 2021/1/12 10:22 下午
 */
public class CityPickerResponse {

    public List<CityChild> lists = new ArrayList<>();



    @SerializedName("default")
    public CityChild  defaultCity ;


    public static class CityChild {

        public String id;
        public String name;
        public Double lat;
        public Double lng;
        public boolean nativeIsSelected = false ;

    }


}
