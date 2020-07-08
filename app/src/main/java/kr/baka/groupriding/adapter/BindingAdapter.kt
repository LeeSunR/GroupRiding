package kr.baka.groupriding.adapter

import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.DragEvent
import android.view.View
import androidx.databinding.BindingAdapter
object BindingAdapter{

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