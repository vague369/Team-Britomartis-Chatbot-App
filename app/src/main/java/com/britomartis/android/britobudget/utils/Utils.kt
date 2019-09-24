package com.britomartis.android.britobudget.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentTimeAsString(): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
}

fun getCurrentTimeAsLong(): Long {
    return Date().time
}

fun convertTimeLongToString(time: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(time))
}
