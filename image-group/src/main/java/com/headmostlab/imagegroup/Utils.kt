package com.headmostlab.imagegroup

import android.content.res.Resources
import android.util.TypedValue

fun Resources.toPx(inDp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, inDp, displayMetrics)
}