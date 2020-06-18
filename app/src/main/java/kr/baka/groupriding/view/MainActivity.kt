package kr.baka.groupriding.view

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.ActivityMainBinding
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.service.RidingService
import kr.baka.groupriding.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = MainViewModel()
        var count = 0
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.startRunningService.observe(this, Observer {
            if(App.isServiceRunning.value==false){
                val intent = Intent(this, RidingService::class.java)
                startService(intent)
            }
            else{
                val intent = Intent(this, RidingService::class.java)
                stopService(intent)
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

        App.isServiceRunning.observe(this, Observer {
            Log.e("service",it.toString())
            viewModel.isServiceRunning.value = it
        })
    }

    override fun onResume() {
        super.onResume()
        //TODO 브로드케스트 송신 수신 체크
    }
}
