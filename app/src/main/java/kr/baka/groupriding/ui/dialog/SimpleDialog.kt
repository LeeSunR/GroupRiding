package kr.baka.groupriding.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.DialogSimpleBinding
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.viewmodel.SimpleDialogViewModel

class SimpleDialog : DialogFragment() {
    val viewModel = SimpleDialogViewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DialogSimpleBinding.inflate(inflater, container, false)
        binding.vm = viewModel

        return binding.root
    }

    fun setTitle(value:String) {
        viewModel.title.value = value
    }

    fun setMessage(value:String) {
        viewModel.message.value = value
    }

    fun setButtonStyle(style:Int){
        viewModel.btnColor.value = App.context.getColor(style)
    }

    fun setRightButton(value:String, event:View.OnClickListener) {
        viewModel.rightBtnText.value = value
        viewModel.rightOnClick.value = event
    }

    fun setLeftButton(value:String, event:View.OnClickListener) {
        viewModel.leftBtnText.value = value
        viewModel.leftOnClick.value = event
    }

    fun setBigMessage(value: String){
        viewModel.bigMessageVisibility.value = View.VISIBLE
        viewModel.bigMessage.value = value
    }

    fun setEditText(hint: String){
        viewModel.editTextVisibility.value = View.VISIBLE
        viewModel.editTextHint.value = hint
    }

    companion object{
        const val STYLE_DANGER = R.color.colorButtonDanger
        const val STYLE_CONFIRM = R.color.colorButtonConfirm
        const val STYLE_NORMAL = R.color.colorButtonNormal
        fun getInstance(): SimpleDialog {
            return SimpleDialog()
        }
    }
}