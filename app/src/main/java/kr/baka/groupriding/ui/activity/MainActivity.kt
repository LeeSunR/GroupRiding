package kr.baka.groupriding.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
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
import kr.baka.groupriding.ui.dialog.*
import kr.baka.groupriding.ui.fragment.MenuFragment
import kr.baka.groupriding.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val groupCreateCompletedBroadcastReceiver = GroupCreateCompletedBroadcastReceiver()
    private val joinErrorBroadcastReceiver = JoinErrorBroadcastReceiver()
    private val recordFinishBroadcastReceiver = RecordFinishBroadcastReceiver()
    private val groupFinishBroadcastReceiver = GroupFinishBroadcastReceiver()
    private val groupCreateCompletedFilter = IntentFilter().also { it.addAction("GroupCreateCompletedBroadcast") }
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
        val menuFragment = MenuFragment.newInstance().also {
            supportFragmentManager.beginTransaction().add(R.id.menu_fragment, it).commit()
        }

        LocationLiveData.observe(this, Observer {
            if(it.provider != LocationManager.GPS_PROVIDER)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toastMessageGPSreception),
                    Toast.LENGTH_SHORT).show()
            Map.myLocationUpdate(it)
        })

        viewModel.route.observe(this, Observer { Map.setRoute(it) })
        viewModel.members.observe(this, Observer{ Map.otherLocationUpdate(it) })

        registerReceiver(groupCreateCompletedBroadcastReceiver, groupCreateCompletedFilter)
        registerReceiver(joinErrorBroadcastReceiver, joinErrorFilter)
        registerReceiver(recordFinishBroadcastReceiver, recordFinishFilter)
        registerReceiver(groupFinishBroadcastReceiver, groupFinishFilter)
    }

    override fun onMapReady(naverMap: NaverMap) {
        Map.initialization(naverMap)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(groupCreateCompletedBroadcastReceiver)
        unregisterReceiver(joinErrorBroadcastReceiver)
        unregisterReceiver(recordFinishBroadcastReceiver)
        unregisterReceiver(groupFinishBroadcastReceiver)
    }

    //BroadcastReceiver Class
    inner class GroupCreateCompletedBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            GroupRidingInviteDialog(context).show()
        }
    }

    inner class JoinErrorBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            FailDialog(
                context,
                getString(R.string.failDialogTitle_groupJoinFail),
                getString(R.string.failDialogMessage_groupJoinFail),
            ).show()
        }
    }

    inner class RecordFinishBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val list = intent.getParcelableArrayListExtra<LatLng>("list")
            RecordRouteSaveDialog(context,list).show()
        }
    }

    inner class GroupFinishBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val list = intent.getParcelableArrayListExtra<LatLng>("list")
            GroupRidingFinishDialog(context,list).show()
        }
    }
}
