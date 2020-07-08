package kr.baka.groupriding.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.PathOverlay
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.DialogRecordRouteSetBinding
import kr.baka.groupriding.repository.room.AppDatabase
import kr.baka.groupriding.repository.room.entity.RouteEntity
import kr.baka.groupriding.repository.room.entity.RouteSubEntity
import kr.baka.groupriding.viewmodel.RecordRouteSetViewModel

class RecordRouteSetDialog(context: Context,private val routeEntity: RouteEntity) : Dialog(context),OnMapReadyCallback  {

    private val routeObserver = RouteObserver()
    private lateinit var mapView: MapView
    lateinit var naverMap: NaverMap
    var arrayListLatLng:ArrayList<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val binding: DialogRecordRouteSetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_record_route_set,
            null,
            false
        )
        setContentView(binding.root)
        val viewModel = RecordRouteSetViewModel()
        binding.vm = viewModel

        mapView = findViewById(R.id.dialogMap)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        viewModel.eventCloseDialog.observeForever {
            arrayListLatLng = null
            dismiss()
        }

        viewModel.stopRecordRouteServiceEvent.observeForever {
            dismiss()
        }


    }

    override fun onStart() {
        mapView.onStart()
        super.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun dismiss() {
        mapView.onDestroy()
        super.dismiss()
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

        AppDatabase.getInstance(context).routeSubDao().getRouteSub(routeEntity.rid).observeForever(routeObserver)
        this.naverMap = naverMap
        naverMap.uiSettings.isScaleBarEnabled=false
        naverMap.uiSettings.isZoomControlEnabled=false
        naverMap.uiSettings.isZoomGesturesEnabled=false

    }

}