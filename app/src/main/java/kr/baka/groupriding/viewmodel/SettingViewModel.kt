package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.R
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData

class SettingViewModel :ViewModel(){

    val tag = SettingViewModel::class.java.simpleName
    val themeName = MutableLiveData<String>()
    val startSettingActivityEvent = SingleLiveData<Any>()

    init {
        themeName.value = "오류"

        App.themeColor.observeForever { getThemeName() }
    }


    private fun getThemeName(){
        val themeColorArray = App.context.resources.getIntArray(R.array.arrayThemeColor)
        val themeNameArray = App.context.resources.getStringArray(R.array.arrayThemeName)
        for (i in 0 until themeColorArray.size-1){


            Log.e(tag,themeColorArray[i].toString() +"  /  "+App.themeColor.value.toString())

            if (themeColorArray[i]==App.themeColor.value) {
                themeName.value = themeNameArray[i]
                break
            }
        }
    }

    fun showThemeSelectDialog(){
        startSettingActivityEvent.call()
    }
}