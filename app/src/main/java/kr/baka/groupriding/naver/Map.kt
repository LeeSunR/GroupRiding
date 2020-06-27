package kr.baka.groupriding.naver

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.alpha
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import kr.baka.groupriding.R
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.model.Member

object Map {

    private lateinit var naverMap:NaverMap
    private val markerHashMap = HashMap<String, Marker>()
    private val pathArrayList = ArrayList<LatLng>()

    fun initialization(naverMap: NaverMap){
        this.naverMap = naverMap
        naverMap.mapType = NaverMap.MapType.Basic
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING,false)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT,false)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN,false)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE,true)
        naverMap.isNightModeEnabled = true
        naverMap.uiSettings.isScaleBarEnabled=false
        naverMap.uiSettings.isZoomControlEnabled=false


        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true
        locationOverlay.position = LatLng(37.5670135, 126.9783740)
        locationOverlay.bearing = 90f

        val bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().also { it.color= Color.RED }
        canvas.drawCircle(16f,16f,16f,paint)

        locationOverlay.icon = OverlayImage.fromBitmap(bitmap)
        locationOverlay.position = LatLng(37.5700000, 127.9783740)

        //live data binging
        App.location.observeForever {
            myLocationUpdate(LatLng(it))
        }

        App.members.observeForever {
            otherLocationUpdate(it)
        }
    }

    private fun myLocationUpdate(latLng: LatLng){
        naverMap.locationOverlay.position = latLng
        naverMap.cameraPosition =  CameraPosition(latLng, 17.0)
    }

    private fun otherLocationUpdate(members: ArrayList<Member>){
        val keys = ArrayList<String>()
        val iterator = markerHashMap.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            keys.add(key)
        }

        for (i in 0 until members.size){
            if(members[i].me==false){
                var marker = markerHashMap.getOrDefault(members[i].id,null)
                keys.remove(members[i].id)
                if (marker==null){
                    marker = Marker()
                    marker.position = LatLng(members[i].latitude!!, members[i].longitude!!)
                    val bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    val paint = Paint().also { it.color= Color.BLUE }
                    canvas.drawCircle(16f,16f,16f,paint)
                    marker.icon = OverlayImage.fromBitmap(bitmap)
                    marker.map = naverMap
                }
                else{
                    marker.position = LatLng(members[i].latitude!!, members[i].longitude!!)
                }
                markerHashMap[members[i].id!!] = marker

            }

            if(members[i].owner==true){
                addPath(LatLng(members[i].latitude!!,members[i].longitude!!))
            }
        }

        for (i in 0 until keys.size){
            val marker = markerHashMap[keys[i]]
            marker!!.map = null
            markerHashMap.remove(keys[i])
        }
    }


    private fun addPath(latLng: LatLng){
        if(pathArrayList.size==0) pathArrayList.add(latLng)
        else {
            val meter = latLng.distanceTo(pathArrayList[pathArrayList.size-1])
            if(meter>6.0f) pathArrayList.add(latLng)
        }

        if(pathArrayList.size>2){
            var pathList = pathArrayList.toList()
            val path = PathOverlay()
            path.coords = pathList
            path.width = 64
            path.color = Color.rgb(180,255,180)
            path.outlineWidth = 0
            path.patternImage = OverlayImage.fromResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
            path.patternInterval = 64
            path.map = naverMap
        }
    }
}