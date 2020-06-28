package kr.baka.groupriding.model
import android.graphics.Color
import java.util.*

class Member(
    var nickname:String? = null,
    var latitude:Double? = null,
    var longitude:Double? = null,
    var lastDate: Date? = null,
    var id: String? = null,
    var owner: Boolean? = null,
    var me: Boolean? = null
) {

    override fun toString(): String {
        var text:String = ""
        text += "id : $id\n"
        text += "nickname : $nickname\n"
        text += "longitude : $longitude\n"
        text += "lastDate : $lastDate\n"
        text += "latitude : $latitude\n"

        return text
    }
}