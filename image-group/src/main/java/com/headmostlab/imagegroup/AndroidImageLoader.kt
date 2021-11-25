package com.headmostlab.imagegroup

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade


internal class AndroidImageLoader(private val context: Context) : ImageLoader {
    override fun load(url: String, image: ImageView, roundCorners: RoundCorners) {
        Glide.with(context)
            .load(url)
            .transition(withCrossFade())
            .transform(
                CenterCrop(), GranularRoundedCorners(
                    roundCorners.leftTop,
                    roundCorners.rightTop,
                    roundCorners.rightBottom,
                    roundCorners.leftBottom
                )
            )
            .into(image)
    }
}