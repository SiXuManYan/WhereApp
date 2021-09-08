package com.jcs.where.features.map

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.jcs.where.R
import com.jcs.where.features.map.government.GovernmentActivity

/**
 * Created by Wangsw  2021/9/1 16:16.
 * 自定义 google map 中 Marker 点击后的信息栏 ,
 * 自定义全部内容（窗口+窗口内信息）时 覆盖 getInfoWindow ，将 getInfoContents 返回 null ，
 * 仅替换窗口内信息，覆盖 getInfoContents ， 将 getInfoWindow 返回null
 *
 * 参考：
 * @see <a href="https://github.com/SiXuManYan/android-samples/blob/main/ApiDemos/kotlin/app/src/gms/java/com/example/kotlindemos/MarkerDemoActivity.kt">MarkerDemoActivity.kt</a>
 * @see <a href="https://github.com/SiXuManYan/android-samples/blob/main/ApiDemos/java/app/src/gms/java/com/example/mapdemo/MarkerDemoActivity.java">MarkerDemoActivity.java</a>
 *
 * 文档：
 * @see <a href="https://developers.google.com/maps/documentation/android-sdk/reference/com/google/android/libraries/maps/GoogleMap.InfoWindowAdapter">InfoWindowAdapter</a>
 *
 *
 * 谷歌地图由于回收机制，默认只能显示一个info窗口，
 * 使用 layout转bitmap ，setmaker 替代
 */
class CustomInfoWindowAdapter(activity: AppCompatActivity) : GoogleMap.InfoWindowAdapter {

    private val contents: View = activity.layoutInflater.inflate(R.layout.custom_info_contents, null)

    /**
     * 仅替换默认信息窗口框内的信息窗口的内容（标注气泡）
     */
    override fun getInfoContents(marker: Marker): View? = null

    /**
     * 自定义全部内容
     */
    override fun getInfoWindow(marker: Marker): View {

        render(marker , contents)
        return contents
    }

    private fun render(marker: Marker, view: View) {
        val title_tv = view.findViewById<TextView>(R.id.title_tv)

        if (marker.title != null) {
            title_tv.text = marker.title
        }



    }


}