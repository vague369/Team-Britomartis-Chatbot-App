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

fun convertTimeLongToDateString(time: Long): String {
    // Check if the time was yesterday
    val c1 = Calendar.getInstance()
    c1.add(Calendar.DAY_OF_YEAR, -1)
    val c2 = Calendar.getInstance()
    c2.timeInMillis = time

    if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
        return "Yesterday"
    }

    // Else just return the formatted date
    return SimpleDateFormat("MMMM d, YYYY", Locale.getDefault()).format(time)
}