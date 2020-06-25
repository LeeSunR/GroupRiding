package kr.baka.groupriding.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.DialogAskGroupRidingStartBinding
import kr.baka.groupriding.databinding.DialogAskGroupRidingStopBinding
import kr.baka.groupriding.service.GroupRidingService
import kr.baka.groupriding.service.RidingService
import kr.baka.groupriding.viewmodel.AskGroupRidingStartViewModel
import kr.baka.groupriding.viewmodel.AskGroupRidingStopViewModel


class AskGroupRidingStopDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        setCancelable(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val binding: DialogAskGroupRidingStopBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_ask_group_riding_stop,
            null,
            false
        )
        setContentView(binding.root)
        val viewModel = AskGroupRidingStopViewModel()
        binding.vm = viewModel

        viewModel.eventStopGroupRidingService.observeForever {
            //STOP SERVICE
            val intent = Intent(context, GroupRidingService::class.java)
            context.stopService(intent)
            dismiss()
        }

        viewModel.eventCloseDialog.observeForever {
            dismiss()
        }
    }

}