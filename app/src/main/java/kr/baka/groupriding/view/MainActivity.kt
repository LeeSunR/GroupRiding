package kr.baka.groupriding.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.ActivityMainBinding
import kr.baka.groupriding.service.RidingService
import kr.baka.groupriding.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = MainViewModel()
        binding.viewModel = viewModel

        viewModel.tgRidingStatus.observe(this, Observer {
            if(it){
                val intent = Intent(this, RidingService::class.java)
                startService(intent)
            }
            else{
                val intent = Intent(this, RidingService::class.java)
                stopService(intent)
            }
        })

    }
}
