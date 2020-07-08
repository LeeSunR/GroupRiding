package kr.baka.groupriding.viewmodel

import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ScrollView
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import kr.baka.groupriding.R
import kr.baka.groupriding.etc.SingleLiveData
import kr.baka.groupriding.model.Member
import kr.baka.groupriding.repository.LocationLiveData


class MainViewModel: ViewModel() {

    val tag = MainViewModel::class.java.simpleName
    val route = MediatorLiveData<ArrayList<LatLng>?>().also { it.value = null }
    var members = MutableLiveData<ArrayList<Member>>()

    val speed = MediatorLiveData<String>().also {liveData->
        liveData.addSource(LocationLiveData, Observer {
            if(it.hasSpeed()) liveData.value=(it.speed*3600/1000).toInt().toString()
            else liveData.value="--"
        })
    }

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
}