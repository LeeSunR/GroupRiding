package kr.baka.groupriding.etc

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kr.baka.groupriding.R

class MySharedPreferences(context: Context){
    companion object{
        private const val PREFS_FILENAME = "group_riding"
        private const val KEY_THEME_COLOR = "key_theme_color"
    }
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

    var getThemeColor: Int
        get() = sharedPreferences.getInt(KEY_THEME_COLOR, R.color.colorDefaultBackGround)
        set(value) = sharedPreferences.edit().putInt(KEY_THEME_COLOR, value).apply()
}