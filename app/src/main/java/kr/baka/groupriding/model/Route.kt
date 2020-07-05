package kr.baka.groupriding.model

import com.naver.maps.geometry.LatLng
import java.util.*
import kotlin.collections.ArrayList

class Route {
    var name:String = "unknown"
    var regdate:Date? = null
    var routeArrayList:ArrayList<LatLng>? = null

}