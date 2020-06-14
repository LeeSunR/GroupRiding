package kr.baka.groupriding.adapter

import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.model.Information.Companion.SIZE_MAIN_FLOAT
import kr.baka.groupriding.model.Information.Companion.SIZE_SUB_FLOAT
import kr.baka.groupriding.model.Information.Companion.SIZE_SUB_TIME

object BindingAdapter{
    @BindingAdapter("variableTextSize")
    @JvmStatic
    fun setVariableTextSize(view: TextView, type: Int) {
        when(type){
            SIZE_MAIN_FLOAT->
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 150f)
            SIZE_SUB_FLOAT->
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 64f)
            SIZE_SUB_TIME->
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 48f)
        }
    }

    @BindingAdapter("variableBackgroundColor")
    @JvmStatic
    fun setVariableBackgroundColor(view: View, color: Int) {
        Log.e("tag","call")
        view.setBackgroundColor(color)
    }
}