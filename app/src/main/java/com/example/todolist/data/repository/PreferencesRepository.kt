package com.example.todolist.data.repository

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private var editor = sharedPreferences.edit()

    fun getStringPreferences(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun setStringPreferences(key: String, value: String?) {
        value?.let {
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun getIntPreferences(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun setIntPreferences(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun getBooleanPreferences(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun setBooleanPreferences(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun deletePreferences(key: String) {
        editor.remove(key).apply()
    }
}