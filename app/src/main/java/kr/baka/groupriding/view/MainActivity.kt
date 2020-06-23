package kr.baka.groupriding.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.OverlayImage
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.ActivityMainBinding
import kr.baka.groupriding.databinding.ActivityMainBinding.inflate
import kr.baka.groupriding.databinding.DialogAskGroupRidingStartBinding.inflate
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.App.Companion.context
import kr.baka.groupriding.naver.Map
import kr.baka.groupriding.service.RidingService
import kr.baka.groupriding.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

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

        viewModel.startGroupRidingService.observe(this, Observer {
            if (App.isGroupRidingServiceRunning.value==false){
                AskGroupRidingStartDialog(this).show()
            }
            else{
                //AskGroupRidingStopDialog(this).start()
            }
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

        //태마 색상 업데이트
        App.themeColor.observe(this, Observer {
            window.statusBarColor = it
            viewModel.backgroundColor.value = it
        })

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

}
