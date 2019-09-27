package com.britomartis.android.britobudget.utils

import android.content.Context
import com.britomartis.android.britobudget.MainActivity
import com.britomartis.android.britobudget.R


fun parseBotReply(context: Context, reply: String?): String? {
    return reply
}

fun userNameAlreadySaved(context: Context): Boolean {
    val sharedPref = (context as MainActivity).getPreferences(Context.MODE_PRIVATE)
    sharedPref?.let { sp ->
        val username = sp.getString(context.getString(R.string.username_pref_key), null)
        if (!username.isNullOrEmpty()) return true
    }
    return false
}

fun getUsernameOrNull(context: Context): String? {
    val sharedPref = (context as MainActivity).getPreferences(Context.MODE_PRIVATE)
    sharedPref?.let { sp ->
        val username = sp.getString(context.getString(R.string.username_pref_key), null)
        if (!username.isNullOrEmpty()) return username
    }
    return null
}

fun saveUsername(context: Context, username: String) {
    val sharedPref = (context as MainActivity).getPreferences(Context.MODE_PRIVATE)
    sharedPref?.let { sp ->
        with(sharedPref.edit()) {
            putString(context.getString(R.string.username_pref_key), username)
            apply()
        }
    }
}