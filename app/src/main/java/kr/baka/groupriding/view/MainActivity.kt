package kr.baka.groupriding.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.OverlayImage
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.ActivityMainBinding
import kr.baka.groupriding.databinding.ActivityMainBinding.inflate
import kr.baka.groupriding.databinding.DialogAskGroupRidingStartBinding.inflate
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.App.Companion.context
import kr.baka.groupriding.naver.Map
import kr.baka.groupriding.service.RidingService
import kr.baka.groupriding.viewmodel.MainViewModel
import java.net.URISyntaxException


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var groupCreateCompletedBroadcastReceiver:GroupCreateCompletedBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = MainViewModel()
        var count = 0
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //NAVER MAP FRAGMENT INIT
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

        //view model event
        viewModel.startGroupRidingServiceEvent.observe(this, Observer {
            AskGroupRidingStartDialog(this).show()
        })

        viewModel.stopGroupRidingServiceEvent.observe(this, Observer {
            AskGroupRidingStopDialog(this).show()
        })

        viewModel.inviteCodeDialogShowEvent.observe(this, Observer {
            GroupCodeShowDialog(this).show()
        })

        viewModel.startPopupMenuEvent.observe(this, Observer {
            val wrapper = ContextThemeWrapper(this, R.style.PopupMenuTheme)
            val popupMenu = PopupMenu(wrapper,it)
            popupMenu.inflate(R.menu.menu_more_content)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menu_destination->{

                    }
                    R.id.menu_create_group_riding->{

                    }
                    R.id.menu_join_group_riding->{

                    }
                    R.id.menu_start_setting_activity->{
                        val intent = Intent(this,SettingActivity::class.java)
                        startActivity(intent)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        })

        //broadcast receivers
        val filter = IntentFilter()
        filter.addAction("GroupCreateCompletedBroadcast")
        groupCreateCompletedBroadcastReceiver = GroupCreateCompletedBroadcastReceiver()
        registerReceiver(groupCreateCompletedBroadcastReceiver, filter);
    }

    override fun onMapReady(naverMap: NaverMap) {
        Map.initialization(naverMap)
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, RidingService::class.java)
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        val intent = Intent(this, RidingService::class.java)
        stopService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(groupCreateCompletedBroadcastReceiver)
    }

    inner class GroupCreateCompletedBroadcastReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            GroupCodeShowDialog(context).show()
        }
    }

}
