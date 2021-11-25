package com.headmostlab.testimagegroupapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.headmostlab.testimagegroupapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageGroupView.setImages(
            listOf(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
            )
        )

        binding.imageGroupView.setOnImageClickListener { idx: Int, url: String ->
            Toast.makeText(this, "$idx - $url", Toast.LENGTH_SHORT).show()
        }

        binding.imageGroupView.postDelayed({
            binding.imageGroupView.setImages(
                listOf(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97q4u3ss2kWkCN9Xr0BKMAmVWUgAZIKrTzbd2_ycyduxhKy17q1ADbH9tUF-ePLP3d8s&usqp=CAU",
                )
            )

            binding.imageGroupView.postDelayed({
                binding.imageGroupView.setImages(
                    listOf(
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97q4u3ss2kWkCN9Xr0BKMAmVWUgAZIKrTzbd2_ycyduxhKy17q1ADbH9tUF-ePLP3d8s&usqp=CAU",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnPnJkdOwQWOf9CV8-aNmwyaPcKt4ltgOwZa7nxQyi3xym1gdtJg1Ih279Aq_8ePsSROw&usqp=CAU",
                    )
                )

                binding.imageGroupView.postDelayed({
                    binding.imageGroupView.setImages(
                        listOf(
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97q4u3ss2kWkCN9Xr0BKMAmVWUgAZIKrTzbd2_ycyduxhKy17q1ADbH9tUF-ePLP3d8s&usqp=CAU",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnPnJkdOwQWOf9CV8-aNmwyaPcKt4ltgOwZa7nxQyi3xym1gdtJg1Ih279Aq_8ePsSROw&usqp=CAU",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOc1fhAqUxP46QoMXFeydklc8bLFIrji_ifyH753c7iDhGZlyg5Bkkqf9VNZgm_m3ah8w&usqp=CAU",
                        )
                    )

                    binding.imageGroupView.postDelayed({
                        binding.imageGroupView.setImages(
                            listOf(
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97q4u3ss2kWkCN9Xr0BKMAmVWUgAZIKrTzbd2_ycyduxhKy17q1ADbH9tUF-ePLP3d8s&usqp=CAU",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnPnJkdOwQWOf9CV8-aNmwyaPcKt4ltgOwZa7nxQyi3xym1gdtJg1Ih279Aq_8ePsSROw&usqp=CAU",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOc1fhAqUxP46QoMXFeydklc8bLFIrji_ifyH753c7iDhGZlyg5Bkkqf9VNZgm_m3ah8w&usqp=CAU",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1BhxP2pm7FdxjFUq6n1IO39zU56Qfwcn-AK3xx9Bf7cIVcZqJ_HqnvFJ99RSJ9RqH460&usqp=CAU",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqaLXGntqlzT6SH1xtB4XfzuZg-3NUtQYnjL3K0BNMQwWMzomaC6xzaNIw8AGvrk2vDn4&usqp=CAU",
                            )
                        )
                    }, 5000)

                }, 5000)

            }, 5000)

        }, 5000)




        binding.imageGroupView2.setImages(
            listOf(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97q4u3ss2kWkCN9Xr0BKMAmVWUgAZIKrTzbd2_ycyduxhKy17q1ADbH9tUF-ePLP3d8s&usqp=CAU",
            )
        )

        binding.imageGroupView2.setOnImageClickListener { idx: Int, url: String ->
            Toast.makeText(this, "$idx - $url", Toast.LENGTH_SHORT).show()
        }




        binding.imageGroupView3.setImages(
            listOf(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97q4u3ss2kWkCN9Xr0BKMAmVWUgAZIKrTzbd2_ycyduxhKy17q1ADbH9tUF-ePLP3d8s&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnPnJkdOwQWOf9CV8-aNmwyaPcKt4ltgOwZa7nxQyi3xym1gdtJg1Ih279Aq_8ePsSROw&usqp=CAU",
            )
        )

        binding.imageGroupView3.setOnImageClickListener { idx: Int, url: String ->
            Toast.makeText(this, "$idx - $url", Toast.LENGTH_SHORT).show()
        }

        binding.imageGroupView4.setImages(
            listOf(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97q4u3ss2kWkCN9Xr0BKMAmVWUgAZIKrTzbd2_ycyduxhKy17q1ADbH9tUF-ePLP3d8s&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnPnJkdOwQWOf9CV8-aNmwyaPcKt4ltgOwZa7nxQyi3xym1gdtJg1Ih279Aq_8ePsSROw&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOc1fhAqUxP46QoMXFeydklc8bLFIrji_ifyH753c7iDhGZlyg5Bkkqf9VNZgm_m3ah8w&usqp=CAU",
            )
        )

        binding.imageGroupView4.setOnImageClickListener { idx: Int, url: String ->
            Toast.makeText(this, "$idx - $url", Toast.LENGTH_SHORT).show()
        }

        binding.imageGroupView5.setImages(
            listOf(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtuw_NfVDAlg0SnCer8nTPLcRYsX3y9RKDl7he65wtUF-7N8tjN_jz4WY1pdZUjltRDbc&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS97q4u3ss2kWkCN9Xr0BKMAmVWUgAZIKrTzbd2_ycyduxhKy17q1ADbH9tUF-ePLP3d8s&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnPnJkdOwQWOf9CV8-aNmwyaPcKt4ltgOwZa7nxQyi3xym1gdtJg1Ih279Aq_8ePsSROw&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOc1fhAqUxP46QoMXFeydklc8bLFIrji_ifyH753c7iDhGZlyg5Bkkqf9VNZgm_m3ah8w&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1BhxP2pm7FdxjFUq6n1IO39zU56Qfwcn-AK3xx9Bf7cIVcZqJ_HqnvFJ99RSJ9RqH460&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqaLXGntqlzT6SH1xtB4XfzuZg-3NUtQYnjL3K0BNMQwWMzomaC6xzaNIw8AGvrk2vDn4&usqp=CAU",
            )
        )

        binding.imageGroupView5.setOnImageClickListener { idx: Int, url: String ->
            Toast.makeText(this, "$idx - $url", Toast.LENGTH_SHORT).show()
        }

    }
}