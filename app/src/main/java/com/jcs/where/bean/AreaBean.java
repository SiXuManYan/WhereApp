package com.jcs.where.bean;

import java.util.List;

public class AreaBean {
    public List<ListBean> lists;
    public class ListBean{
        public String area_id;
        public String name;
        public String letter;
    }
    public List<HotBean> hots;
    public class HotBean{
        public String area_id;
        public String name;
    }
}
