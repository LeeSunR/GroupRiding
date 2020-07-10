package kr.baka.groupriding.adapter

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.DragEvent
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
object BindingAdapter{

    @BindingAdapter("variableButtonColor")
    @JvmStatic
    fun setVariableBackgroundColor(view: View, color: Int) {
        if(color== Color.WHITE) (view as TextView).setTextColor(Color.BLACK)
        else (view as TextView).setTextColor(Color.WHITE)
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