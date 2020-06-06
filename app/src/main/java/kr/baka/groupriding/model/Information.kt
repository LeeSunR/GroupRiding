package kr.baka.groupriding.model

import android.util.TypedValue
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*

data class Information(
    var type:Int,
    var title:String,
    var value:String
) {
    companion object {
        val INFO_TYPE_SUB_TIME:Int = 0x0ee2
        val INFO_TYPE_SUB_FLOAT:Int = 0x023a
        val INFO_TYPE_MAIN_FLOAT:Int = 0x0232
    }


}