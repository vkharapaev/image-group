package com.headmostlab.imagegroup

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnNextLayout
import androidx.core.widget.TextViewCompat
import kotlin.math.min
import kotlin.math.roundToInt as rnd

class ImageGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.imageGroupViewStyle,
    defStyleRes: Int = R.style.Widget_ImageGroupViewDefStyle
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val MAX_IMAGE_VIEW_COUNT = 4
        private const val ASPECT_RATIO = 16 / 9.0
        private const val UNSPECIFIED_WIDTH_IN_DP = 250.0
        private const val UNSPECIFIED_HEIGHT_IN_DP = UNSPECIFIED_WIDTH_IN_DP / ASPECT_RATIO
    }

    @StyleRes
    private var textAppearanceStyle = 0
    private val images = mutableListOf<String>()
    private var gridSize = 0f
    private var listener: ((index: Int, url: String) -> Unit)? = null
    private var imageLoader: ImageLoader? = null
    private val imageViewList = mutableListOf<ImageView>()
    private var overflowTextView: TextView? = null
    private var rippleColor = Color.TRANSPARENT
    private var clippingDrawable: Drawable? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.ImageGroupView, defStyleAttr, defStyleRes) {
            gridSize = getDimensionOrThrow(R.styleable.ImageGroupView_gridSize)
            textAppearanceStyle = getResourceId(R.styleable.ImageGroupView_overflowTexAppearance, 0)
            rippleColor = getColor(R.styleable.ImageGroupView_rippleColor, 0)
            clippingDrawable = getDrawable(R.styleable.ImageGroupView_clippingDrawable)
        }

        clippingDrawable?.let {
            clipToOutline = true
            background = it
        }
    }

    fun setImages(images: List<String>) {
        this.images.clear()
        this.images.addAll(images)
        prepareViews()
        doOnNextLayout { loadImages() }
        requestLayout()
        invalidate()
    }

    fun setListener(listener: ((index: Int, url: String) -> Unit)) {
        this.listener = listener
        imageViewList.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                listener.invoke(index, images[index])
            }
        }
    }

    fun setImageLoader(imageLoader: ImageLoader) {
        this.imageLoader = imageLoader
    }

    private fun loadImages() {
        val imageLoader =
            this.imageLoader ?: throw IllegalArgumentException("ImageLoader is not specified")
        imageLoader.clear()
        imageViewList.forEachIndexed { index, imageView ->
            imageLoader.load(images[index], imageView, index, images.size)
        }
    }

    private fun prepareViews() {
        removeViews()

        repeat(min(images.size, MAX_IMAGE_VIEW_COUNT)) {
            RippleImageView(context, rippleColor).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                imageViewList.add(this)
                addView(this)
            }
        }

        if (images.size > MAX_IMAGE_VIEW_COUNT) {
            dimLastImageView()
            overflowTextView = TextView(context).apply {
                TextViewCompat.setTextAppearance(this, textAppearanceStyle)
                text = "+${images.size - MAX_IMAGE_VIEW_COUNT}"
                gravity = Gravity.CENTER
                includeFontPadding = false
                addView(this)
            }
        }

        listener?.let { setListener(it) }
    }

    private fun removeViews() {
        removeAllViews()
        imageViewList.clear()
        overflowTextView = null
    }

    private fun dimLastImageView() {
        imageViewList[3].setColorFilter(Color.argb(255, 50, 50, 50), PorterDuff.Mode.MULTIPLY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val specWidth = MeasureSpec.getSize(widthMeasureSpec).toDouble()
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec).toDouble()
        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        val (width, height) = when {
            specWidthMode == MeasureSpec.UNSPECIFIED && specHeightMode == MeasureSpec.UNSPECIFIED -> {
                val density = resources.displayMetrics.density
                UNSPECIFIED_WIDTH_IN_DP * density to UNSPECIFIED_HEIGHT_IN_DP * density
            }
            specWidthMode == MeasureSpec.UNSPECIFIED -> {
                specHeight * ASPECT_RATIO to specHeight
            }
            specHeightMode == MeasureSpec.UNSPECIFIED -> {
                specWidth to specWidth / ASPECT_RATIO
            }
            else -> {
                specWidth to specHeight
            }
        }

        setMeasuredDimension(width.rnd(), height.rnd())

        val parentHalfWidth = measuredWidth * 0.5
        val parentHalfHeight = measuredHeight * 0.5
        val gridHalfSize = gridSize * 0.5

        when (images.size) {
            0 -> {}
            1 -> measure1()
            2 -> measure2(parentHalfWidth, gridHalfSize)
            3 -> measure3(parentHalfWidth, parentHalfHeight, gridHalfSize)
            4 -> measure4(parentHalfWidth, parentHalfHeight, gridHalfSize)
            else -> {
                measure4(parentHalfWidth, parentHalfHeight, gridHalfSize)
                measureText()
            }
        }
    }

    private fun measure1() {
        imageViewList[0].measure(
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
        )
    }

    private fun measure2(w: Double, g: Double) {
        val width = MeasureSpec.makeMeasureSpec((w - g).rnd(), MeasureSpec.EXACTLY)
        val height = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
        imageViewList.forEach { it.measure(width, height) }
    }

    private fun measure3(w: Double, h: Double, g: Double) {
        val width = MeasureSpec.makeMeasureSpec((w - g).rnd(), MeasureSpec.EXACTLY)
        val height1 = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
        val heightOther = MeasureSpec.makeMeasureSpec((h - g).rnd(), MeasureSpec.EXACTLY)
        imageViewList[0].measure(width, height1)
        (1..2).forEach { imageViewList[it].measure(width, heightOther) }
    }

    private fun measure4(w: Double, h: Double, g: Double) {
        val width = MeasureSpec.makeMeasureSpec((w - g).rnd(), MeasureSpec.EXACTLY)
        val height = MeasureSpec.makeMeasureSpec((h - g).rnd(), MeasureSpec.EXACTLY)
        imageViewList.forEach { it.measure(width, height) }
    }

    private fun measureText() {
        overflowTextView?.measure(
            MeasureSpec.makeMeasureSpec(imageViewList[3].measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(imageViewList[3].measuredHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val parentHalfWidth = measuredWidth * 0.5
        val parentHalfHeight = measuredHeight * 0.5
        val gridHalfSize = gridSize * 0.5

        when (images.size) {
            0 -> {}
            1 -> layout1()
            2 -> layout2(parentHalfWidth, gridHalfSize)
            3 -> layout3(parentHalfWidth, parentHalfHeight, gridHalfSize)
            4 -> layout4(parentHalfWidth, parentHalfHeight, gridHalfSize)
            else -> {
                layout4(parentHalfWidth, parentHalfHeight, gridHalfSize)
                layoutText()
            }
        }
    }

    private fun layout1() {
        imageViewList[0].apply { layout(0, 0, measuredWidth, measuredHeight) }
    }

    private fun layout2(w: Double, g: Double) {
        imageViewList[0].apply {
            layout(0, 0, measuredWidth, measuredHeight)
        }
        imageViewList[1].apply {
            layout((w + g).rnd(), 0, (w + g).rnd() + measuredWidth, measuredHeight)
        }
    }

    private fun layout3(w: Double, h: Double, g: Double) {
        imageViewList[0].apply {
            layout(0, 0, measuredWidth, measuredHeight)
        }
        imageViewList[1].apply {
            layout((w + g).rnd(), 0, (w + g + measuredWidth).rnd(), measuredHeight)
        }
        imageViewList[2].apply {
            layout(
                (w + g).rnd(), (h + g).rnd(),
                (w + g + measuredWidth).rnd(), (h + g + measuredHeight).rnd()
            )
        }
    }

    private fun layout4(w: Double, h: Double, g: Double) {
        imageViewList[0].apply {
            layout(0, 0, measuredWidth, measuredHeight)
        }
        imageViewList[1].apply {
            layout((w + g).rnd(), 0, (w + g + measuredWidth).rnd(), measuredHeight)
        }
        imageViewList[2].apply {
            layout(0, (h + g).rnd(), measuredWidth, (h + g + measuredHeight).rnd())
        }
        imageViewList[3].apply {
            layout(
                (w + g).rnd(), (h + g).rnd(),
                (w + g + measuredWidth).rnd(), (h + g + measuredHeight).rnd()
            )
        }
    }

    private fun layoutText() {
        imageViewList[3].run { overflowTextView?.layout(left, top, right, bottom) }
    }

    private class RippleImageView(context: Context, @ColorInt private val color: Int) :
        AppCompatImageView(context) {

        override fun setImageDrawable(drawable: Drawable?) {
            super.setImageDrawable(
                if (color == Color.TRANSPARENT) drawable else wrapWithRipple(drawable)
            )
        }

        private fun wrapWithRipple(original: Drawable?): Drawable =
            RippleDrawable(ColorStateList.valueOf(color), original, null)
    }
}