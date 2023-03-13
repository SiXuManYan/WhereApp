package com.jiechengsheng.city.search.tag;

import java.io.Serializable;

/**
 * create by zyf on 2021/1/31 2:36 下午
 */

public enum SearchTag implements Serializable {
    HOTEL("hotel"), NEWS("news"), YELLOW_PAGE("yellow_page"), CONVENIENCE_SERVICE("convenience_service");
    String name;

    SearchTag(String name) {
        this.name = name;
    }
}
