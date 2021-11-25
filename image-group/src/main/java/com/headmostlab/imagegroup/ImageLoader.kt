package com.headmostlab.imagegroup

import android.widget.ImageView

internal interface ImageLoader {
    fun load(url: String, iamge: ImageView, roundCorners: RoundCorners)
}