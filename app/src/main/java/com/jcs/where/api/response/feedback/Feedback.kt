package com.jcs.where.api.response.feedback

/**
 * Created by Wangsw  2022/10/12 16:35.
 *
 */
class FeedbackCategoryAndQuestion {

    var id = 0
    var name = ""
    var website = ""
}


class FeedbackQuestionTab {

    /** 0已解决 1 未解决 */
    var id = 0
    var nativeCanClick = true
    var nativeSelected = false

}


class FeedbackPost {

    var images: String? = null
    var content: String? = null
    var tel: String? = null
}

class FeedbackRecord {

    var image: ArrayList<String>? = ArrayList()
    var content: String? = ""
    var tel: String? = ""
    var time = ""


}