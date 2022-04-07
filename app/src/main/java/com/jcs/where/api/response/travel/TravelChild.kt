package com.jcs.where.api.response.travel

import java.util.*

/**
 * Created by Wangsw  2021/10/18 10:07.
 * https://api.jcstest.com/travelapi/v2/travels?category_id=205&search_input=&lat=14.6631685&lng=120.588784
 */
class TravelChild {

    var id = 0
    var image = ArrayList<String>()
    var name = ""
    var address = ""
    var lat = 0.0
    var lng = 0.0
    var grade = 0.0
    var tags = ArrayList<String>()
    var comments_count = 0
    var distance = ""


}