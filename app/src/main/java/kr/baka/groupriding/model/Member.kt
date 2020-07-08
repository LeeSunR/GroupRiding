package kr.baka.groupriding.model
import java.util.*

data class Member(
    var nickname:String? = null,
    var latitude:Double? = null,
    var longitude:Double? = null,
    var lastDate: Date? = null,
    var id: String? = null,
    var owner: Boolean? = null,
    var me: Boolean? = null
)