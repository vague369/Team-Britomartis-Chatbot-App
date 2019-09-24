package com.britomartis.android.britobudget.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentTimeAsString(): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
}