package kr.baka.groupriding.repository.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class MySharedPreferences(context: Context){
    companion object{
        private const val PREFS_FILENAME = "group_riding"
        private const val KEY_MAX_SPEED = "key_max_speed"
        private const val KEY_NICKNAME = "key_nickname"
        private const val KEY_SAMPLING_INTERVAL = "key_Sampling_Interval"
    }
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_FILENAME, MODE_PRIVATE)

    var maxSpeed: Int
        get() = sharedPreferences.getInt(KEY_MAX_SPEED, 40)
        set(value) = sharedPreferences.edit().putInt(KEY_MAX_SPEED, value).apply()

    var nickname: String?
        get() = sharedPreferences.getString(KEY_NICKNAME, "유저")
        set(value) = sharedPreferences.edit().putString(KEY_NICKNAME, value).apply()

    var samplingInterval: Int
        get() = sharedPreferences.getInt(KEY_SAMPLING_INTERVAL, 5)
        set(value) = sharedPreferences.edit().putInt(KEY_SAMPLING_INTERVAL, value).apply()
}