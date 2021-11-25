package com.headmostlab.imagegroup

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.withStyledAttributes
import java.util.*

class ImageGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.Widget_ImageGroupViewDefStyle
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val MAX_IMAGE_NUMBER = 4
    }

    private var imageRadius = 0f

    private val imageUrls = mutableListOf<String>()
    private val imageLoader: ImageLoader by lazy { AndroidImageLoader(context) }
    private var listener: ((Int, String) -> Unit)? = null

    private val styledContext: Context = ContextThemeWrapper(context,
        resources.newTheme().apply {
            setTo(context.theme)
            applyStyle(defStyleRes, true)
            context.withStyledAttributes(
                attrs,
                intArrayOf(R.styleable.ImageGroupView_imageGroupViewStyle),
                0,
                0
            ) {
                applyStyle(getResourceId(R.styleable.ImageGroupView_imageGroupViewStyle, 0), true)
            }
        }
    )

    init {
        styledContext.withStyledAttributes(attrs, R.styleable.ImageGroupView) {
            imageRadius = getDimensionOrThrow(R.styleable.ImageGroupView_imageRadius)
        }
    }

    fun setImages(images: List<String>) {
        imageUrls.clear()
        imageUrls.addAll(images)
        inflate()
        loadImages()
    }

    fun getImages(): List<String> = Collections.unmodifiableList(imageUrls)

    private fun inflate() {
        removeAllViews()

        if (imageUrls.size == 0) return

        val layout = when (imageUrls.size) {
            1 -> R.layout.one_image_layout
            2 -> R.layout.two_images_layout
            3 -> R.layout.three_images_layout
            else -> R.layout.four_images_layout
        }

        val inflator = LayoutInflater.from(styledContext)

        inflator.inflate(layout, this, true)

        findViewById<View>(R.id.image1)?.setOnClickListener { listener?.invoke(0, getImages()[0]) }
        findViewById<View>(R.id.image2)?.setOnClickListener { listener?.invoke(1, getImages()[1]) }
        findViewById<View>(R.id.image3)?.setOnClickListener { listener?.invoke(2, getImages()[2]) }
        findViewById<View>(R.id.image4)?.setOnClickListener { listener?.invoke(3, getImages()[3]) }

        if (imageUrls.size > MAX_IMAGE_NUMBER) {
            showAdditionalCount(imageUrls.size - MAX_IMAGE_NUMBER)
        }

    }

    private fun loadImages() {
        val size = imageUrls.size
        imageUrls.forEachIndexed { idx, url ->
            val number = idx + 1
            val corners = RoundCorners(
                leftTop =
                if (size == 1 || size == 2 && idx == 0 || size >= 3 && idx == 0) imageRadius else 0f,
                leftBottom =
                if (size == 1 || size == 2 && idx == 0 || size == 3 && idx == 0 || size >= 4 && idx == 2) imageRadius else 0f,
                rightTop =
                if (size == 1 || size == 2 && idx == 1 || size >= 3 && idx == 1) imageRadius else 0f,
                rightBottom =
                if (size == 1 || size == 2 && idx == 1 || size == 3 && idx == 2 || size >= 4 && idx == 3) imageRadius else 0f
            )
            if (number in 0..MAX_IMAGE_NUMBER) loadImage(number, url, corners)
        }
    }

    private fun loadImage(imageNumber: Int, url: String, roundCorners: RoundCorners) {
        val image: ImageView = when (imageNumber) {
            1 -> findViewById(R.id.image1)
            2 -> findViewById(R.id.image2)
            3 -> findViewById(R.id.image3)
            4 -> findViewById(R.id.image4)
            else -> throw IllegalArgumentException("Wrong image number")
        }
        imageLoader.load(url, image, roundCorners)
    }

    private fun showAdditionalCount(count: Int) {
        findViewById<TextView>(R.id.addition_count_text_view)?.let {
            it.visibility = View.VISIBLE
            it.text = "+$count"
        }
        findViewById<ImageView>(R.id.image4)?.setColorFilter(
            Color.argb(255, 50, 50, 50),
            PorterDuff.Mode.MULTIPLY
        )
    }

    fun setOnImageClickListener(listener: (Int, String) -> Unit) {
        this.listener = listener
    }

}