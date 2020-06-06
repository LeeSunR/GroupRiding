package kr.baka.groupriding.viewmodel

import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.model.Information
import java.util.*

class MainViewModel: ViewModel(){
    val layoutTopLeft = MutableLiveData<Information>()
    val layoutTopRight = MutableLiveData<Information>()
    val layoutMiddle = MutableLiveData<Information>()
    val layoutBottomLeft = MutableLiveData<Information>()
    val layoutBottomRight = MutableLiveData<Information>()



    init {
        layoutTopLeft.value = Information(Information.INFO_TYPE_SUB_FLOAT,"타이틀","벨류")
        layoutTopRight.value = Information(Information.INFO_TYPE_SUB_FLOAT,"타이틀","벨류")
        layoutMiddle.value = Information(Information.INFO_TYPE_MAIN_FLOAT,"타이틀","벨류")
        layoutBottomLeft.value = Information(Information.INFO_TYPE_SUB_TIME,"타이틀","벨류")
        layoutBottomRight.value = Information(Information.INFO_TYPE_SUB_TIME,"타이틀","벨류")
    }

    override fun onCleared() {
        super.onCleared()

    }
    fun click(){
    }


}