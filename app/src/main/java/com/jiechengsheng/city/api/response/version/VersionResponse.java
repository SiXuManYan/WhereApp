package com.jiechengsheng.city.api.response.version;

/**
 * Created by Wangsw  2021/3/6 16:18.
 */
public class VersionResponse {
/*
    {
        "new_version": "新版本号",
            "download_url": "下载url",
            "update_desc": "更新描述",
            "is_force_install": "是否为强制更新（true:是，false:否）",
            "status": "显示状态（true:显示，false:不显示）"
    }
    */


    /**
     * 新版本号
     */
    public String  new_version = "";

    /**
     * 下载url
     */
    public String  download_url = "";

    /**
     * 更新描述
     */
    public String  update_desc = "";

    /**
     * 是否为强制更新（true:是，false:否）
     */
    public boolean  is_force_install;

    /**
     * 显示状态（true:显示，false:不显示）
     */
    public boolean  status;




}
