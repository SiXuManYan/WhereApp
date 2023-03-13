package com.jiechengsheng.city.api.response.message;

/**
 * Created by Wangsw  2021/2/22 10:09.
 */
public class SystemMessageResponse {
/*	        "id": -6673147685166376,
            "is_read": 3371227702246084,
            "title": "W&%0sBi",
            "message": "h!%%Xo",
            "created_at": "M)#m",
            "link": "K#c",
            "detail_type": -4422777697144884*/


    public String id = "";

    /**
     * 0 未读
     * 1 已读
     */
    public int is_read = 0;

    public String title = "";
    public String message = "";
    public String created_at = "";
    public String link = "";

    /**
     * 详情类型（1：webview，2：详情页）
     */
    public int detail_type = 0;





}
