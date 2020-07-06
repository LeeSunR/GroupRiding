package kr.baka.groupriding.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.DragStartHelper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.ActivityMainBinding
import kr.baka.groupriding.naver.Map
import kr.baka.groupriding.repository.LocationLiveData
import kr.baka.groupriding.view.dialog.*
import kr.baka.groupriding.view.fragment.MenuFragment
import kr.baka.groupriding.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val groupCreateCompletedBroadcastReceiver = GroupCreateCompletedBroadcastReceiver()
    private val joinErrorBroadcastReceiver = JoinErrorBroadcastReceiver()
    private val recordFinishBroadcastReceiver = RecordFinishBroadcastReceiver()
    private val groupFinishBroadcastReceiver = GroupFinishBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)



        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = MainViewModel()
        var count = 0
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //NAVER MAP FRAGMENT INIT

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)



        //fragment show
        supportFragmentManager.beginTransaction().add(R.id.menu_fragment, MenuFragment()).commit()

        //broadcast receivers
        val groupCreateCompletedFilter = IntentFilter()
        groupCreateCompletedFilter.addAction("GroupCreateCompletedBroadcast")
        registerReceiver(groupCreateCompletedBroadcastReceiver, groupCreateCompletedFilter)


        val joinErrorFilter = IntentFilter()
        joinErrorFilter.addAction("joinErrorBroadcast")
        registerReceiver(joinErrorBroadcastReceiver, joinErrorFilter)


        val recordFinishFilter = IntentFilter()
        recordFinishFilter.addAction("recordFinishBroadcast")
        registerReceiver(recordFinishBroadcastReceiver, recordFinishFilter)

        val groupFinishFilter = IntentFilter()
        groupFinishFilter.addAction("groupFinishBroadcast")
        registerReceiver(groupFinishBroadcastReceiver, groupFinishFilter)


        LocationLiveData.observe(this, Observer {location->
            if(location.hasSpeed())
                viewModel.speed.value=(location.speed*3600/1000).toInt().toString()
            else{
                viewModel.speed.value="??"
            }

            if(location.provider != LocationManager.GPS_PROVIDER)
                Toast.makeText(applicationContext,"GPS 수신을 기다리는 중 입니다",Toast.LENGTH_SHORT).show()

            Map.myLocationUpdate(location)
        })

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

    inner class GroupCreateCompletedBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            GroupCodeShowDialog(context).show()
        }
    }

    inner class JoinErrorBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra("message")
            FailDialog(
                context,
                "그룹 참가 실패",
                "초대 코드가 유효하지 않습니다.\ncode : $message"
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
