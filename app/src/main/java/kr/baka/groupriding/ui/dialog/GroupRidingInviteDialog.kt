package kr.baka.groupriding.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.DialogGroupRidingInviteBinding
import kr.baka.groupriding.viewmodel.GroupRidingInviteViewModel


class GroupRidingInviteDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        setCancelable(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val binding: DialogGroupRidingInviteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_group_riding_invite,
            null,
            false
        )
        setContentView(binding.root)
        val viewModel = GroupRidingInviteViewModel()
        binding.vm = viewModel

        viewModel.eventCloseDialog.observeForever {
            dismiss()
        }
    }

}