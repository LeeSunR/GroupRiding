package kr.baka.groupriding.naver

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import kr.baka.groupriding.R
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.model.Member
import kr.baka.groupriding.repository.SettingRepository

object Map {

    private lateinit var naverMap:NaverMap
    private val markerHashMap = HashMap<String, Marker>()
    private val pathArrayList = ArrayList<LatLng>()
    private val path = PathOverlay()
    private val myInfoRepository = SettingRepository()

    fun initialization(naverMap: NaverMap){
        this.naverMap = naverMap

        //맵 레이아웃 설정
        naverMap.mapType = NaverMap.MapType.Basic
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING,false)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT,false)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN,false)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE,true)
        naverMap.isNightModeEnabled = true
        naverMap.uiSettings.isScaleBarEnabled=false
        naverMap.uiSettings.isZoomControlEnabled=false

        //맵 초기 위치
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true
        locationOverlay.position = LatLng(37.5670135, 126.9783740)
        locationOverlay.bearing = 90f


        locationOverlay.icon = createIcon("M", Color.RED)
        locationOverlay.bearing = 0.0f

        App.members.observeForever {
            otherLocationUpdate(it)
        }

        App.isGroupRidingServiceRunning.observeForever {
            if(!it) initData()
        }
    }

    fun myLocationUpdate(latLng: LatLng){
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
                    marker.icon = createIcon(i.toString(), Color.BLUE)
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
            if(meter>myInfoRepository.samplingInterval) pathArrayList.add(latLng)
        }
        if(pathArrayList.size>2){
            val pointC = pathArrayList[pathArrayList.size-1]
            val pointA =pathArrayList[pathArrayList.size-2]
            val pointB = pathArrayList[pathArrayList.size-3]

            val distanceC = pointB.distanceTo(pointA)
            val distanceB = pointA.distanceTo(pointC)
            val distanceA = pointB.distanceTo(pointC)

            val radians = Math.acos( (distanceB*distanceB + distanceC*distanceC - distanceA*distanceA) / (2*distanceB*distanceC) )
            val degrees = radians * (180.0/Math.PI)

            if(degrees>176.0) pathArrayList.removeAt(pathArrayList.size-2)

            var pathList = pathArrayList.toList()
            path.coords = pathList
            path.width = 64
            path.color = Color.rgb(128,255,128)
            path.outlineWidth = 0
            path.patternImage = OverlayImage.fromResource(R.drawable.ic_arrow_up)
            path.patternInterval = 128
            path.map = naverMap
        }
    }

    private fun initData(){
        pathArrayList.clear()
        path.map = null
        val iterator = markerHashMap.values.iterator()
        while (iterator.hasNext()) {
            val marker = iterator.next()
            marker.map=null
        }
        markerHashMap.clear()
    }

    private fun createIcon(symbol:String, color:Int):OverlayImage{
        val bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().also { it.color= color }
        canvas.drawCircle(32f,32f,32f,paint)
        paint.color = Color.WHITE
        paint.textSize = 48.0f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(symbol,32.0f,48.0f,paint )
        canvas.rotate(90.0f,0f,0f)

        return OverlayImage.fromBitmap(bitmap)
    }
}