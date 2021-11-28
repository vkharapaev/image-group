package com.headmostlab.testimagegroupapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.headmostlab.imagegroup.ImageGroupView
import com.headmostlab.imagegroup.ImageLoaderImpl
import com.headmostlab.testimagegroupapp.databinding.ActivityMainBinding
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private var count = 1

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val images = listOf(
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97q4u3ss2kWkCN9Xr0BKMAmVWUgAZIKrTzbd2_ycyduxhKy17q1ADbH9tUF-ePLP3d8s&usqp=CAU",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnPnJkdOwQWOf9CV8-aNmwyaPcKt4ltgOwZa7nxQyi3xym1gdtJg1Ih279Aq_8ePsSROw&usqp=CAU",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOc1fhAqUxP46QoMXFeydklc8bLFIrji_ifyH753c7iDhGZlyg5Bkkqf9VNZgm_m3ah8w&usqp=CAU",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1BhxP2pm7FdxjFUq6n1IO39zU56Qfwcn-AK3xx9Bf7cIVcZqJ_HqnvFJ99RSJ9RqH460&usqp=CAU",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqaLXGntqlzT6SH1xtB4XfzuZg-3NUtQYnjL3K0BNMQwWMzomaC6xzaNIw8AGvrk2vDn4&usqp=CAU",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        with(binding) {

            prevBtn.setOnClickListener {
                count = max(1, count - 1)
                imageGroupView.setImages(count)
            }

            nextBtn.setOnClickListener {
                count = min(images.size, count + 1)
                imageGroupView.setImages(count)
            }

            imageGroupView.setImageLoader(ImageLoaderImpl(R.drawable.image_view_group_image_placeholder))
            imageGroupView.setListener { pos, url ->
                Toast.makeText(this@MainActivity, "$pos", Toast.LENGTH_SHORT).show()
            }

            imageGroupView.setImages(1)

            imageGroupView2.setImageLoader(ImageLoaderImpl(R.drawable.image_view_group_image_placeholder))
        }

        setImagesWithDelay(
            1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1, 0,
            1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1, 0,
            1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1, 0,
            1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1, 0,
            1, 2, 3, 4, 5, 6,
        )
    }

    private fun ImageGroupView.setImages(count: Int) {
        setImages(images.subList(0, max(min(images.size, count), 0)))
    }

    private fun setImagesWithDelay(vararg counts: Int) {
        setImagesInRecursion(0, counts)
    }

    private fun setImagesInRecursion(index: Int, counts: IntArray) {
        if (counts.size - 1 < index) {
            return
        } else {
            with(binding.imageGroupView2) {
                setImages(counts[index])
                postDelayed({ if (!isDestroyed) setImagesInRecursion(index + 1, counts) }, 2000)
            }
        }
    }
}