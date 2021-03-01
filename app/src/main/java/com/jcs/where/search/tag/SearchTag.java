package com.jcs.where.search.tag;

import java.io.Serializable;

/**
 * create by zyf on 2021/1/31 2:36 下午
 */

public enum SearchTag implements Serializable {
    HOTEL("hotel"), NEWS("news"), YELLOW_PAGE("news");;
    String name;

    SearchTag(String name) {
        this.name = name;
    }
}
