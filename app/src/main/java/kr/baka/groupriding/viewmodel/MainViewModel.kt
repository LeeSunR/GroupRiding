package kr.baka.groupriding.viewmodel

import android.os.Parcelable
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ScrollView
import androidx.core.view.marginTop
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize
import kr.baka.groupriding.R
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData
import kr.baka.groupriding.model.Information

@Parcelize
class MainViewModel: ViewModel(), Parcelable {

    val tag = MainViewModel::class.java.simpleName



    val speed = MutableLiveData<String>()
    val groupRidingStartVisibility = MutableLiveData<Int>()
    val groupRidingStopVisibility = MutableLiveData<Int>()

    val showMenuFragmentEvent = SingleLiveData<Any>()
    val fragmentOnDragEvent = SingleLiveData<Any>()

    val translateY = MutableLiveData<Int>()


    fun menuOnScroll(v:View, scrollX:Int, scrollY:Int, oldScrollX:Int, oldScrollY:Int){
        val view = v as ScrollView
        val maxScroller = view.getChildAt(0).height - view.height

        var rate = scrollY.toFloat()/maxScroller.toFloat()

        val imBtn = view.findViewById<ImageButton>(R.id.menu_down_btn)
        imBtn.alpha=rate
    }

    fun menuOnTouch(v:View, event:MotionEvent):Boolean{

        val view = v as ScrollView
        val maxY = view.getChildAt(0).height - view.height
        val nowY = view.scrollY

        if (event.action == MotionEvent.ACTION_UP){
            if(nowY<maxY/2)
                view.scrollTo(0,0)
            else
                view.scrollTo(0,maxY)
            return true
        }
        return false
    }

    fun showMenuFragment(){
        showMenuFragmentEvent.call()
    }
}