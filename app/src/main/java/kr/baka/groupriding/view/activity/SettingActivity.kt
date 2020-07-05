package kr.baka.groupriding.view.activity

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.ActivitySettingBinding
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.viewmodel.MainViewModel
import kr.baka.groupriding.viewmodel.SettingViewModel
import java.lang.Exception

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_setting)
        window.statusBarColor = getColor(R.color.colorPrimary)
        val binding = DataBindingUtil.setContentView<ActivitySettingBinding>(this, R.layout.activity_setting)
        val settingVM = SettingViewModel()

        binding.settingVM = settingVM
        binding.lifecycleOwner = this


        //닉네임 변경
        settingVM.eventShowNicknameChangeDialog.observe(this, Observer {
            createNicknameChangeDialog(this, settingVM)
        })

        settingVM.eventShowMaxSpeedChangeDialog.observe(this, Observer {
            createMaxSpeedChangeDialog(this,settingVM)
        })

        settingVM.eventShowSamplingIntervalChangeDialog.observe(this, Observer {
            createSamplingIntervalChangeDialog(this,settingVM)
        })
    }

    private fun createNicknameChangeDialog(context:Context, settingVM:SettingViewModel){
        AlertDialog.Builder(this).also {dialog->
            dialog.setTitle("닉네임 변경")
            dialog.setMessage("변경할 닉네임을 입력하세요")
            val et = EditText(this)
            dialog.setView(et)
            dialog.setPositiveButton(getString(R.string.stringBtnYes)) { dialogInterface: DialogInterface, i: Int ->
                settingVM.nicknameChange(et.text.toString())
            }
            dialog.setNegativeButton(getString(R.string.stringBtnNo)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            dialog.show()
        }
    }

    private fun createMaxSpeedChangeDialog(context:Context, settingVM:SettingViewModel){
        AlertDialog.Builder(this).also {dialog->
            dialog.setTitle("최대 속도")
            dialog.setMessage("최대 속도를 입력하세요")
            val et = EditText(this)
            dialog.setView(et)
            dialog.setPositiveButton(getString(R.string.stringBtnYes)) { dialogInterface: DialogInterface, i: Int ->
                var speed:Int = 0
                try {
                    speed = et.text.toString().toInt()
                }catch (e:Exception){
                    Toast.makeText(this,"잘못된 입력 형식입니다",Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }finally {
                    settingVM.maxSpeedChange(speed)
                }
            }
            dialog.setNegativeButton(getString(R.string.stringBtnNo)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            dialog.show()
        }
    }

    private fun createSamplingIntervalChangeDialog(context:Context, settingVM:SettingViewModel){
        AlertDialog.Builder(this).also {dialog->
            dialog.setTitle("샘플링 간격 변경")
            dialog.setMessage("샘플링 간격을 입력하세요")
            val et = EditText(this)
            dialog.setView(et)
            dialog.setPositiveButton(getString(R.string.stringBtnYes)) { dialogInterface: DialogInterface, i: Int ->
                var samplingInterval:Int = 0
                try {
                    samplingInterval = et.text.toString().toInt()
                }catch (e:Exception){
                    Toast.makeText(this,"잘못된 입력 형식입니다",Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }finally {
                    settingVM.samplingIntervalChange(samplingInterval)
                }
            }
            dialog.setNegativeButton(getString(R.string.stringBtnNo)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            dialog.show()
        }
    }
}
