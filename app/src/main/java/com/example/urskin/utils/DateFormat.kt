package com.example.urskin.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun DateFormat(date: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd | HH:mm", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    return sdf.format(calendar.time)
}