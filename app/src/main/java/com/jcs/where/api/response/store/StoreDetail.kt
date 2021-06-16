package com.jcs.where.api.response.store

/**
 * Created by Wangsw  2021/6/15 17:12.
 *
 */
data class StoreDetail(

/*
        "id": 1,
        "uuid": "6d714564-7705-310f-8b0e-9be2421b4a5f",
        "title": "Marquardt, Rempel and Maggio",
        "images": ["https://whereoss.oss-cn-beijing.aliyuncs.com/shops/webp.webp", "https://whereoss.oss-cn-beijing.aliyuncs.com/shops/webp%20%286%29.webp", "https://whereoss.oss-cn-beijing.aliyuncs.com/shops/webp%20%2823%29.webp"],
        "week_start": 1,
        "week_end": 5,
        "start_time": "09:00",
        "end_time": "18:00",
        "address": "1368 Martina Tunnel Suite 900\nNorth Chase, WA 68026",
        "lat": 14.524188,
        "lng": 120.432452,
        "tel": "13130033471",
        "abstract": "Very soon the Rabbit began. Alice thought to herself. (Alice had no idea how to spell 'stupid,' and that in the act of crawling away: besides all this, there was no longer to be two people. 'But it's no use in waiting by the fire, and at once took up the fan and gloves--that is, if I can listen all day to day.' This was quite tired of swimming about here, O Mouse!' (Alice thought this must be growing small again.' She got up very sulkily and crossed over to herself, 'in my going out.",
        "collect_status": 1,
        "extension_tel": "1-361-604-7374 x56438",
        "web_site": "https://www.google.com",
        "email": "amcglynn@lynch.com",
        "facebook": "https://www.facebook.com",
        "mer_uuid": "",
        "mer_name": "",
        "im_status": 1*/

        var id: Int,
        var collect_status: Int,
        var im_status: Int,

        var uuid: String = "",
        var title: String = "",
        var images: ArrayList<String> = ArrayList(),

        var week_start: Int,
        var week_end: Int,
        var start_time:  String = "",
        var end_time:  String = "",
        var address:  String = "",
        var abstract:  String = "",
        var extension_tel:  String = "",
        var web_site:  String = "",
        var email:  String = "",
        var facebook:  String = "",
        var mer_uuid:  String = "",
        var mer_name:  String = "",
        var tel:  String = "",
        var lat:  Float = 0f,
        var lng:  Float = 0f



        )
