package com.jacob.portfolio.utilities

import android.content.Context
import android.util.DisplayMetrics
import java.util.*


class CommonUtilities {
    fun getMinutesDifference(currentTime: Date, savedTime: Date) : Long {
        val diff = currentTime.time - savedTime.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        return minutes
    }
    fun calculateNoOfColumns(context: Context, columnWidthDp: Int): Int {
        val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
}