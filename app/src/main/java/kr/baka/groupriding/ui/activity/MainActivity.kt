package kr.baka.groupriding.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.ActivityMainBinding
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.ViewModelFactory
import kr.baka.groupriding.lib.Map
import kr.baka.groupriding.repository.LocationLiveData
import kr.baka.groupriding.repository.RouteRepository
import kr.baka.groupriding.repository.room.entity.RouteEntity
import kr.baka.groupriding.repository.room.entity.RouteSubEntity
import kr.baka.groupriding.ui.dialog.*
import kr.baka.groupriding.ui.fragment.MenuFragment
import kr.baka.groupriding.ui.dialog.SimpleDialog
import kr.baka.groupriding.viewmodel.MainViewModel
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val joinErrorBroadcastReceiver = JoinErrorBroadcastReceiver()
    private val recordFinishBroadcastReceiver = RecordFinishBroadcastReceiver()
    private val groupFinishBroadcastReceiver = GroupFinishBroadcastReceiver()
    private val joinErrorFilter = IntentFilter().also { it.addAction("joinErrorBroadcast") }
    private val recordFinishFilter = IntentFilter().also { it.addAction("recordFinishBroadcast") }
    private val groupFinishFilter = IntentFilter().also { it.addAction("groupFinishBroadcast") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel =  ViewModelProvider(App(), ViewModelFactory()).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        //NAVER MAP FRAGMENT INIT
        val mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as MapFragment?) ?:
        MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

        //fragment show
        MenuFragment.newInstance().also {
            supportFragmentManager.beginTransaction().add(R.id.menu_fragment, it).commit()
        }

        LocationLiveData.observe(this, Observer {
            if(it.provider != LocationManager.GPS_PROVIDER)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toastMessageGPSReception),
                    Toast.LENGTH_SHORT).show()
            Map.myLocationUpdate(it)
        })

        viewModel.route.observe(this, Observer { Map.setRoute(it) })
        viewModel.members.observe(this, Observer{ Map.otherLocationUpdate(it) })

        registerReceiver(joinErrorBroadcastReceiver, joinErrorFilter)
        registerReceiver(recordFinishBroadcastReceiver, recordFinishFilter)
        registerReceiver(groupFinishBroadcastReceiver, groupFinishFilter)
    }

    override fun onMapReady(naverMap: NaverMap) {
        Map.initialization(naverMap)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(joinErrorBroadcastReceiver)
        unregisterReceiver(recordFinishBroadcastReceiver)
        unregisterReceiver(groupFinishBroadcastReceiver)
    }

    private fun saveRoute(latLngs:ArrayList<LatLng>, name:String){
        val array = ArrayList<RouteSubEntity>()
        var distance:Double = 0.0
        for (i in 0 until latLngs.size){
            val routeSubEntity = RouteSubEntity(
                latitude = latLngs[i].latitude,
                longitude = latLngs[i].longitude,
                timestamp = 0)
            array.add(routeSubEntity)
            if(i>0) distance+=latLngs[i-1].distanceTo(latLngs[i])
        }

        val routeEntity = RouteEntity(
            name = name,
            date = Date().time,
            distance = distance.toInt())

        RouteRepository(this).insert(routeEntity,array)
    }

    private fun createRouteSaveDialog(title:String, message:String,latLngs:ArrayList<LatLng>){
        SimpleDialog.getInstance().also { dialog->
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setEditText("경로명")
            dialog.setLeftButton(getString(R.string.stringBtnClose), View.OnClickListener {
                dialog.dismiss()
            })
            dialog.setRightButton(getString(R.string.stringBtnSave), View.OnClickListener {
                saveRoute(latLngs, dialog.viewModel.editText.value!!)
                dialog.dismiss()
            })
            dialog.show(supportFragmentManager,"dialog")
        }
    }

    //BroadcastReceiver Class
    inner class JoinErrorBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            FailDialog(context, getString(R.string.failDialogTitle_groupJoinFail), getString(R.string.failDialogMessage_groupJoinFail)).show()
        }
    }

    inner class RecordFinishBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val list = intent.getParcelableArrayListExtra<LatLng>("list")
            createRouteSaveDialog(getString(R.string.dialogTitle_routeRecordFinish),getString(R.string.dialogMessage_routeRecordFinish),list)
        }
    }

    inner class GroupFinishBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val list = intent.getParcelableArrayListExtra<LatLng>("list")
            if(list.size>0) createRouteSaveDialog(getString(R.string.dialogTitle_groupRidingFinish),getString(R.string.dialogMessage_groupRidingFinish),list)
        }
    }
}
