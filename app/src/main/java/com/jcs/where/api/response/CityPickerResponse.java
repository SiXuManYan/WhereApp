package com.jcs.where.api.response;

import java.util.List;

/**
 * create by zyf on 2021/1/12 10:22 下午
 */
public class CityPickerResponse {

    public List<CityChild> lists;


    public static class CityChild {

        public String id;
        public String name;
        public Double lat;
        public Double lng;

    }
}
