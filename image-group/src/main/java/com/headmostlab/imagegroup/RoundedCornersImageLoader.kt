package com.headmostlab.imagegroup

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

class RoundedCornersImageLoader(private val radius: Float) :
    ImageLoader {

    private val targetList = mutableSetOf<ViewTarget<ImageView, Drawable>>()

    override fun load(url: String, imageView: ImageView, index: Int, count: Int) {
        val target = Glide.with(imageView.context).asDrawable()
            .load(url)
            .placeholder(getPlaceHolder(count, index))
            .transition(
                withCrossFade(
                    DrawableCrossFadeFactory.Builder().apply { setCrossFadeEnabled(false) })
            )
            .transform(
                CenterCrop(),
                createRoundedCornersTransformation(count, index)
            )
            .into(imageView)

        targetList.add(target)
    }

    override fun clear() {
        targetList.forEach {
            Glide.with(it.view.context).clear(it.view)
        }
        targetList.clear()
    }

    private fun createRoundedCornersTransformation(count: Int, index: Int) = GranularRoundedCorners(
        if (isTopLefRounded(count, index)) radius else 0f,
        if (isTopRightRounded(count, index)) radius else 0f,
        if (isBottomRightRounded(count, index)) radius else 0f,
        if (isBottomLeftRounded(count, index)) radius else 0f
    )

    private fun isBottomLeftRounded(count: Int, index: Int) =
        count == 1 || count == 2 && index == 0 || count == 3 && index == 0 || count >= 4 && index == 2

    private fun isBottomRightRounded(count: Int, index: Int) =
        count == 1 || count == 2 && index == 1 || count == 3 && index == 2 || count >= 4 && index == 3

    private fun isTopRightRounded(count: Int, index: Int) =
        count == 1 || count == 2 && index == 1 || count >= 3 && index == 1

    private fun isTopLefRounded(count: Int, index: Int) =
        count == 1 || count == 2 && index == 0 || count >= 3 && index == 0

    @DrawableRes
    private fun getPlaceHolder(count: Int, index: Int): Int = when (count) {
        0 -> 0

        1 -> R.drawable.image_view_group_placeholder_all

        2 -> when (index) {
            0 -> R.drawable.image_view_group_placeholder_top_bottom_left
            else -> R.drawable.image_view_group_placeholder_top_bottom_right
        }

        3 -> when (index) {
            0 -> R.drawable.image_view_group_placeholder_top_bottom_left
            1 -> R.drawable.image_view_group_placeholder_top_right
            else -> R.drawable.image_view_group_placeholder_bottom_right
        }

        else -> when (index) {
            0 -> R.drawable.image_view_group_placeholder_top_left
            1 -> R.drawable.image_view_group_placeholder_top_right
            2 -> R.drawable.image_view_group_placeholder_bottom_left
            else -> R.drawable.image_view_group_placeholder_bottom_right
        }
    }
}