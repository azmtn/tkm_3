package com.example.homework_2

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

fun View.measuredHeightWithMargins() : Int{
    return this.measuredHeight + this.marginTop + this.marginBottom
}

fun View.measuredWidthWithMargins() : Int{
    return this.measuredWidth + this.marginRight + this.marginLeft
}