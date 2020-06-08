package kr.baka.groupriding.model
import android.util.Log
import androidx.databinding.BaseObservable
import kr.baka.groupriding.R
import kr.baka.groupriding.etc.App

class Information(type:Int, size:Int): BaseObservable() {
    private var data:String = "-"
    private var size:Int = SIZE_SUB_FLOAT
    private var title:String = "-"
    private var type:Int = TYPE_UNKNOWN

    init {
        setType(type)
        setSize(size)
    }

    companion object {
        const val SIZE_SUB_TIME:Int = 23
        const val SIZE_SUB_FLOAT:Int = 24
        const val SIZE_MAIN_FLOAT:Int = 25
        const val TYPE_UNKNOWN:Int = -1
        const val TYPE_DISTANCE:Int = 53
        const val TYPE_SPEED:Int = 54
        const val TYPE_ACG_SPEED:Int = 55
        const val TYPE_RIDING_TIME:Int = 56
        const val TYPE_REST_TIME:Int = 57
    }

    fun getSize() = this.size

    fun setSize(value: Int){
        this.size = value
        notifyPropertyChanged(2)
    }

    fun getType() = this.type
    fun setType(value: Int){
        this.type = value
        Log.e("d",value.toString())
        when(value){
            TYPE_UNKNOWN -> setTitle("-")
            TYPE_DISTANCE -> setTitle(App.context!!.getString(R.string.stringInfoDistance))
            TYPE_SPEED -> setTitle(App.context!!.getString(R.string.stringInfoSpeed))
            TYPE_ACG_SPEED -> setTitle(App.context!!.getString(R.string.stringInfoAvgSpeed))
            TYPE_RIDING_TIME -> setTitle(App.context!!.getString(R.string.stringInfoRidingTime))
            TYPE_REST_TIME -> setTitle(App.context!!.getString(R.string.stringInfoRestTime))
        }
    }

    fun getTitle() = this.title
    private fun setTitle(value: String){
        Log.e("dddd",value)
        this.title = value
        notifyPropertyChanged(1)
    }

    fun getData() = this.data
    fun setData(value:String){
        this.data = value
        notifyPropertyChanged(0)
    }


}