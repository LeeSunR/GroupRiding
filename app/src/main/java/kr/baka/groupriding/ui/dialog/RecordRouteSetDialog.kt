package kr.baka.groupriding.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.PathOverlay
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.DialogRecordRouteSetBinding
import kr.baka.groupriding.databinding.DialogSimpleBinding
import kr.baka.groupriding.repository.room.AppDatabase
import kr.baka.groupriding.repository.room.entity.RouteEntity
import kr.baka.groupriding.repository.room.entity.RouteSubEntity
import kr.baka.groupriding.viewmodel.RecordRouteSetViewModel

class RecordRouteSetDialog() : DialogFragment(),OnMapReadyCallback  {

    private var routeEntity: RouteEntity? = null
    private val routeObserver = RouteObserver()
    private lateinit var mapView: MapView
    lateinit var naverMap: NaverMap
    var arrayListLatLng:ArrayList<LatLng>? = null
    val viewModel = RecordRouteSetViewModel()
        companion object{
        fun getInstance(routeEntity:RouteEntity):RecordRouteSetDialog{
            val dialog = RecordRouteSetDialog()
            dialog.routeEntity = routeEntity
            return dialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DialogRecordRouteSetBinding.inflate(inflater, container, false)
        binding.vm = viewModel

        mapView = binding.root.findViewById(R.id.dialogMap)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setRightButton(event:View.OnClickListener) {
        viewModel.rightOnClick.value = event
    }

    fun setLeftButton(event:View.OnClickListener) {
        viewModel.leftOnClick.value = event
    }

    override fun onStart() {
        mapView.onStart()
        super.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    inner class RouteObserver:Observer<List<RouteSubEntity>>{
        override fun onChanged(it: List<RouteSubEntity>) {
            var minLat:Double = 360.0
            var maxLat:Double = 0.0
            var minLng:Double = 360.0
            var maxLng:Double = 0.0
            arrayListLatLng = ArrayList<LatLng>()
            for (i in it.indices){
                arrayListLatLng!!.add(LatLng(it[i].latitude,it[i].longitude))
                if(minLat>it[i].latitude) minLat = it[i].latitude
                if(maxLat<it[i].latitude) maxLat = it[i].latitude
                if(minLng>it[i].longitude) minLng = it[i].longitude
                if(maxLng<it[i].longitude) maxLng = it[i].longitude
            }

            val path = PathOverlay()
            path.coords = arrayListLatLng!!.toList()
            path.width = 24
            path.color = Color.rgb(128,128,255)
            path.outlineWidth = 0
            path.map = naverMap

            val latLngBounds = LatLngBounds(LatLng(minLat,minLng),LatLng(maxLat,maxLng))
            val cameraUpdate = CameraUpdate.fitBounds(latLngBounds,160)
            naverMap.moveCamera(cameraUpdate)
        }

    }

    override fun onMapReady(naverMap: NaverMap) {
        AppDatabase.getInstance(context!!).routeSubDao().getRouteSub(routeEntity!!.rid).observeForever(routeObserver)
        this.naverMap = naverMap
        naverMap.uiSettings.isScaleBarEnabled=false
        naverMap.uiSettings.isZoomControlEnabled=false
        naverMap.uiSettings.isZoomGesturesEnabled=false

    }

}