package com.headmostlab.imagegroup

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnNextLayout
import kotlin.math.min
import kotlin.math.roundToInt as rnd

class ImageGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.imageGroupViewStyle,
    defStyleRes: Int = R.style.Widget_ImageGroupViewDefStyle
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val MAX_IMAGE_VIEW_COUNT = 4
        private const val ASPECT_RATIO = 16 / 9.0
        private const val UNSPECIFIED_WIDTH_IN_DP = 250.0
        private const val UNSPECIFIED_HEIGHT_IN_DP = UNSPECIFIED_WIDTH_IN_DP / ASPECT_RATIO
    }

    private val images = mutableListOf<String>()
    private val imageViewList = mutableListOf<ImageView>()
    private var listener: ((index: Int, url: String) -> Unit)? = null
    private var imageLoader: ImageLoader? = null
    private var overflowTextView: TextView? = null

    @ColorInt
    private val dimmedImageViewColor =
        ResourcesCompat.getColor(
            resources,
            R.color.image_group_view_dimmed_image_view_color,
            context.theme
        )

    @ColorInt
    private val dimmedOverflowImageViewColor =
        ResourcesCompat.getColor(
            resources,
            R.color.image_group_view_overflow_dimmed_image_view_color,
            context.theme
        )

    @StringRes
    private var overflowTextTemplate = 0

    @StyleRes
    private val styleFromDefAttr: Int = run {
        context.withStyledAttributes(attrs = intArrayOf(defStyleAttr)) {
            return@run getResourceId(0, ResourcesCompat.ID_NULL)
        }
        ResourcesCompat.ID_NULL
    }

    private val styledContext = ContextThemeWrapper(context,
        resources.newTheme().apply {
            setTo(context.theme)
            applyStyle(defStyleRes, true)
            applyStyle(styleFromDefAttr, true)
            applyStyle(attrs?.styleAttribute ?: ResourcesCompat.ID_NULL, true)
        }
    )

    private val inflater = LayoutInflater.from(context).cloneInContext(styledContext)

    private val layoutIds = listOf(
        R.layout.image_view_group_one_image_layout,
        R.layout.image_view_group_two_images_layout,
        R.layout.image_view_group_three_images_layout,
        R.layout.image_view_group_four_images_layout
    )

    private val imageViewIds =
        listOf(R.id.image1, R.id.image2, R.id.image3, R.id.image4)

    private val isOverflow: Boolean get() = images.size > MAX_IMAGE_VIEW_COUNT

    init {
        context.withStyledAttributes(attrs, R.styleable.ImageGroupView, defStyleAttr, defStyleRes) {
            clipToOutline = getBoolean(R.styleable.ImageGroupView_clipToOutline, clipToOutline)
            overflowTextTemplate = getResourceId(
                R.styleable.ImageGroupView_overflowTextTemplate,
                R.string.image_group_view_overflow_text_template
            )
        }
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

    fun setImages(newImages: List<String>) {
        images.apply {
            clear()
            addAll(newImages)
        }
        prepareViews()
        requestLayout()
        invalidate()
        doOnNextLayout { loadImages() }
    }

    private fun prepareViews() {
        removeOldViews()
        val imageViewCount = min(images.size, MAX_IMAGE_VIEW_COUNT)
        if (imageViewCount == 0) return
        inflateLayout(imageViewCount)
        setUpImageViews()
        if (isOverflow) setUpOverflowTextView()
    }

    private fun loadImages() {
        val imageLoader = imageLoader ?: throwImageLoaderError()
        imageLoader.clear()
        imageViewList.forEachIndexed { index, imageView ->
            imageLoader.load(images[index], imageView, index, images.size)
        }
    }

    private fun removeOldViews() {
        removeAllViews()
        imageViewList.clear()
        overflowTextView = null
    }

    private fun inflateLayout(imageViewCount: Int) {
        layoutIds[imageViewCount - 1].let { layout ->
            inflater.inflate(layout, this, true)
        }
    }

    private fun throwImageLoaderError(): Nothing =
        throw IllegalArgumentException(context.getString(R.string.image_group_view_image_loader_is_not_specified))

    private fun setUpOverflowTextView() {
        overflowTextView = findViewById<TextView>(R.id.overflow_text_view)?.apply {
            visibility = VISIBLE
            text = getOverflowString()
        }
    }

    private fun getOverflowString() =
        context.getString(overflowTextTemplate, images.size - MAX_IMAGE_VIEW_COUNT)

    private fun setUpImageViews() {
        findImageViews()
        imageViewList.forEach { it.setColorFilter(dimmedImageViewColor) }
        if (isOverflow) imageViewList[3].setColorFilter(dimmedOverflowImageViewColor)
        listener?.let { setListener(it) }
    }

    private fun findImageViews() {
        imageViewIds.forEach { id ->
            findViewById<ImageView>(id)?.let { imageView ->
                imageViewList.add(imageView)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (width, height) = measureView(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width.rnd(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height.rnd(), MeasureSpec.EXACTLY),
        )
    }

    private fun measureView(widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Double, Double> {
        val specWidth = MeasureSpec.getSize(widthMeasureSpec).toDouble()
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec).toDouble()
        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        return when {
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
    }

}