package kr.baka.groupriding.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
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
        viewModel.tgRidingStatus.observe(this, Observer {
            if(it){
                val intent = Intent(this, RidingService::class.java)
                //intent.putExtra("viewModel",viewModel.layoutMiddle)
                startService(intent)
            }
            else{
                val intent = Intent(this, RidingService::class.java)
                //intent.putExtra("viewModel",viewModel)
                stopService(intent)
            }
        })

        viewModel.startSettingActivityEvent.observe(this, Observer {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
            //TODO call activity
        })

        //태마 색상 업데이트
        App.themeColor.observe(this, Observer {
            window.statusBarColor = it
            viewModel.backgroundColor.value = it
        })
    }
}
