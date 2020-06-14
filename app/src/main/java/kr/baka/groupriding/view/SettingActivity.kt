package kr.baka.groupriding.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.ActivitySettingBinding
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.viewmodel.MainViewModel
import kr.baka.groupriding.viewmodel.SettingViewModel

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_setting)

        val binding = DataBindingUtil.setContentView<ActivitySettingBinding>(this, R.layout.activity_setting)
        val settingVM = SettingViewModel()
        val mainVM = MainViewModel()

        binding.settingVM = settingVM
        binding.mainVM = mainVM

        settingVM.startSettingActivityEvent.observeForever(Observer {
            //createThemeSelectDialog(this)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("태마를 선택하세요")
            dialog.setItems(this.resources.getStringArray(R.array.arrayThemeName)) { dialog, which ->
                val arrayThemeColor = this.resources.getIntArray(R.array.arrayThemeColor)
                App.themeColor.postValue(arrayThemeColor[which])
            }
            dialog.show()
        })

    }

    fun createThemeSelectDialog(context:Context){
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("태마를 선택하세요")
        dialog.setItems(context.resources.getStringArray(R.array.arrayThemeName)) { dialog, which ->
            val arrayThemeColor = context.resources.getIntArray(R.array.arrayThemeColor)
            App.themeColor.postValue(arrayThemeColor[which])

        }
        dialog.show()
    }
}
