package kr.baka.groupriding.etc

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import kr.baka.groupriding.R
import kr.baka.groupriding.etc.App.Companion.context

class MySharedPreferences(context: Context){
    companion object{
        private const val PREFS_FILENAME = "group_riding"
        private const val KEY_THEME_COLOR = "key_theme_color"
    }
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

    var themeColor: Int
        get() = sharedPreferences.getInt(KEY_THEME_COLOR, ContextCompat.getColor(context,R.color.colorThemeDark))
        set(value) = sharedPreferences.edit().putInt(KEY_THEME_COLOR, value).apply()
}