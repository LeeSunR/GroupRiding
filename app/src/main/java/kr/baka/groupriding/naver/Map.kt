package kr.baka.groupriding.naver

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.OverlayImage
import kr.baka.groupriding.etc.App

object Map {

    private lateinit var naverMap:NaverMap

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

    }

    fun myLocationUpdate(latLng: LatLng){
        naverMap.locationOverlay.position = latLng
        naverMap.cameraPosition =  CameraPosition(latLng, 17.0)
    }
}