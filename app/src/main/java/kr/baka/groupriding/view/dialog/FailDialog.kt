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
import kr.baka.groupriding.databinding.DialogFailBinding
import kr.baka.groupriding.viewmodel.FailViewModel


class FailDialog(context: Context, private val title:String, private val message:String) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        setCancelable(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val binding: DialogFailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_fail,
            null,
            false
        )
        setContentView(binding.root)
        val viewModel = FailViewModel()
        binding.vm = viewModel

        viewModel.title.value = title
        viewModel.message.value = message
        viewModel.eventCloseDialog.observeForever {
            dismiss()
        }
    }

}