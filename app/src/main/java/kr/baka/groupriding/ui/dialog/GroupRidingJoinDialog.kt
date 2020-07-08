package kr.baka.groupriding.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.DialogGroupRidingJoinBinding
import kr.baka.groupriding.repository.SettingRepository
import kr.baka.groupriding.service.GroupRidingService
import kr.baka.groupriding.viewmodel.GroupRidingJoinViewModel


class GroupRidingJoinDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        setCancelable(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val binding: DialogGroupRidingJoinBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_group_riding_join,
            null,
            false
        )
        setContentView(binding.root)
        val viewModel = GroupRidingJoinViewModel()
        binding.vm = viewModel

        viewModel.eventCloseDialog.observeForever {
            dismiss()
        }

        viewModel.eventJoinGroupRidingService.observeForever {
            val intent = Intent(context, GroupRidingService::class.java)
            intent.putExtra("RequestCreateGroup",false)
            SettingRepository.inviteCode.value = viewModel.inviteCode.value
            context.startService(intent)
            dismiss()
        }
    }

}