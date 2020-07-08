package kr.baka.groupriding.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.DialogGroupRidingJoinBinding
import kr.baka.groupriding.databinding.DialogRecordRouteSaveBinding
import kr.baka.groupriding.repository.RouteRepository
import kr.baka.groupriding.repository.room.entity.RouteEntity
import kr.baka.groupriding.repository.room.entity.RouteSubEntity
import kr.baka.groupriding.viewmodel.RecordRouteSaveViewModel
import java.util.*
import kotlin.collections.ArrayList


class RecordRouteSaveDialog(context: Context, private val latLngs: ArrayList<LatLng>) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        setCancelable(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val binding: DialogRecordRouteSaveBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_record_route_save,
            null,
            false
        )
        setContentView(binding.root)
        val viewModel = RecordRouteSaveViewModel()
        binding.vm = viewModel

        viewModel.eventCloseDialog.observeForever {
            dismiss()
        }

        viewModel.recordSaveEvent.observeForever {
            val array = ArrayList<RouteSubEntity>()
            var distance:Double = 0.0
            for (i in 0 until latLngs.size){
                val routeSubEntity = RouteSubEntity(latitude = latLngs[i].latitude, longitude = latLngs[i].longitude,timestamp = 0)
                array.add(routeSubEntity)
                if(i>0) distance+=latLngs[i-1].distanceTo(latLngs[i])
            }

            val routeEntity = RouteEntity(name = viewModel.name.value!!, date = Date().time, distance = distance.toInt())

            RouteRepository(context).insert(routeEntity,array)
            dismiss()
        }
    }

}