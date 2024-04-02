package com.panevrn.employmentseekershub

import android.content.Context
import android.content.SharedPreferences

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    //Методы, чтобы сохранить токены
    fun saveAccessToken(accessToken: String?) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun saveRefreshToken(refreshToken: String?) {
        val editor = prefs.edit()
        editor.putString(REFRESH_TOKEN, refreshToken)
        editor.apply()

    }

    // Методы, чтобы достать токены
    fun fetchAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun fetchRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }
}