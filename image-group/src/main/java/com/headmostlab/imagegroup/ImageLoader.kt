package com.headmostlab.imagegroup

import android.widget.ImageView

interface ImageLoader {
    fun load(url: String, imageView: ImageView, index: Int, count: Int)
    fun clear()
}