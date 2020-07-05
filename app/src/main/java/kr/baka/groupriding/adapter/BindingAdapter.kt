package kr.baka.groupriding.adapter

import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.DragEvent
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ListenerUtil
import androidx.databinding.adapters.ViewBindingAdapter
import kr.baka.groupriding.R
import kr.baka.groupriding.model.Information.Companion.SIZE_MAIN_FLOAT
import kr.baka.groupriding.model.Information.Companion.SIZE_SUB_FLOAT
import kr.baka.groupriding.model.Information.Companion.SIZE_SUB_TIME

object BindingAdapter{
    @BindingAdapter("variableTextSize")
    @JvmStatic
    fun setVariableTextSize(view: TextView, type: Int) {
        when(type){
            SIZE_MAIN_FLOAT->
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 100f)
            SIZE_SUB_FLOAT->
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 56f)
            SIZE_SUB_TIME->
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32f)
        }
    }

    @BindingAdapter("variableBackgroundColor")
    @JvmStatic
    fun setVariableBackgroundColor(view: View, color: Int) {
        Log.e("tag","call")
        view.setBackgroundColor(color)
    }

    @BindingAdapter("android:onScrollChange")
    @JvmStatic
    fun setOnScrollChangeListener(view: View, listener: View.OnScrollChangeListener?) {
        view.setOnScrollChangeListener(listener)
    }

    @BindingAdapter("android:onTouch")
    @JvmStatic
    fun setOnTouchListener(view: View, listener: View.OnTouchListener?) {
        view.setOnTouchListener(listener)
    }
}