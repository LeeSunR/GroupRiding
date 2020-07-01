package kr.baka.groupriding.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.ActivityMainBinding
import kr.baka.groupriding.naver.Map
import kr.baka.groupriding.repository.LocationLiveData
import kr.baka.groupriding.service.RidingService
import kr.baka.groupriding.view.dialog.AskGroupRidingStartDialog
import kr.baka.groupriding.view.dialog.AskGroupRidingStopDialog
import kr.baka.groupriding.view.dialog.FailDialog
import kr.baka.groupriding.view.dialog.GroupCodeShowDialog
import kr.baka.groupriding.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var groupCreateCompletedBroadcastReceiver: GroupCreateCompletedBroadcastReceiver? = null
    private var joinErrorBroadcastReceiver: JoinErrorBroadcastReceiver? = null

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
                    R.id.menu_invite->{

                    }
                    R.id.menu_route_recoding_start->{

                    }
                    R.id.menu_start_setting_activity->{
                        val intent = Intent(this,
                            SettingActivity::class.java)
                        startActivity(intent)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        })

        //broadcast receivers
        val groupCreateCompletedFilter = IntentFilter()
        groupCreateCompletedFilter.addAction("GroupCreateCompletedBroadcast")
        groupCreateCompletedBroadcastReceiver = GroupCreateCompletedBroadcastReceiver()
        registerReceiver(groupCreateCompletedBroadcastReceiver, groupCreateCompletedFilter)


        val joinErrorFilter = IntentFilter()
        joinErrorFilter.addAction("joinErrorBroadcast")
        joinErrorBroadcastReceiver = JoinErrorBroadcastReceiver()
        registerReceiver(joinErrorBroadcastReceiver, joinErrorFilter)

        LocationLiveData.observe(this, Observer {
            Map.myLocationUpdate(LatLng(it))
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        Map.initialization(naverMap)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(groupCreateCompletedBroadcastReceiver)
        unregisterReceiver(joinErrorBroadcastReceiver)
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
}
