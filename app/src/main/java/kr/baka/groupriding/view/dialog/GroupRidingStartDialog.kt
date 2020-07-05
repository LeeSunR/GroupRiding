package kr.baka.groupriding.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.DialogGroupRidingStartBinding
import kr.baka.groupriding.service.GroupRidingService
import kr.baka.groupriding.viewmodel.GroupRidingStartViewModel


class GroupRidingStartDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        setCancelable(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val binding: DialogGroupRidingStartBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_group_riding_start,
            null,
            false
        )
        setContentView(binding.root)
        val viewModel = GroupRidingStartViewModel()
        binding.vm = viewModel

        viewModel.eventCreateGroupRidingService.observeForever {
            val intent = Intent(context, GroupRidingService::class.java)
            intent.putExtra("RequestCreateGroup",true)
            context.startService(intent)
            dismiss()
        }

        viewModel.eventJoinGroupDialogShow.observeForever {
            GroupRidingJoinDialog(context).show()
            dismiss()
        }
    }

}