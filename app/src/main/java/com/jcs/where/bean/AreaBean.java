package com.jcs.where.bean;

import java.util.List;

public class AreaBean {

    public List<ListsBean> lists;
    public List<HotsBean> hots;


    public class ListsBean {
        /**
         * letter : a
         * areas : [{"id":"8","name":"阿布凯","lat":14.7128091,"lng":120.4933624}]
         */

        public String letter;
        public List<AreasBean> areas;

        public class AreasBean {
            /**
             * id : 8
             * name : 阿布凯
             * lat : 14.7128091
             * lng : 120.4933624
             */

            public String id;
            public String name;
            public double lat;
            public double lng;

        }
    }

    public class HotsBean {
        public String id;
        public String name;
    }
}
