package kr.baka.groupriding.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.DialogRecordRouteStopBinding
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.service.GroupRidingService
import kr.baka.groupriding.service.RecordRouteService
import kr.baka.groupriding.viewmodel.GroupRidingJoinViewModel
import kr.baka.groupriding.viewmodel.RecordRouteStopViewModel
import kr.baka.groupriding.viewmodel.RecordRouteViewModel


class RecordRouteStopDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        setCancelable(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val binding: DialogRecordRouteStopBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_record_route_stop,
            null,
            false
        )
        setContentView(binding.root)
        val viewModel = RecordRouteStopViewModel()
        binding.vm = viewModel

        viewModel.eventCloseDialog.observeForever {
            dismiss()
        }

        viewModel.stopRecordRouteServiceEvent.observeForever {
            val intent = Intent(context, RecordRouteService::class.java)
            context.stopService(intent)
            dismiss()
        }
    }

}