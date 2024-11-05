package com.jesando.zphererent.activity.defaultActivity.admin.car

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jesando.zphererent.R
import com.jesando.zphererent.config.NetworkConfig
import com.jesando.zphererent.databinding.ActivityAddNewCarBinding
import com.jesando.zphererent.databinding.ActivityAddNewFeaturesBinding
import com.jesando.zphererent.model.response.car.ResponseAddNewCarFeature
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewFeaturesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewFeaturesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddNewFeaturesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tambahData.setOnClickListener {
            uploadData()
        }

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {
        val fitur = binding.edtFeature.text.toString()
        val deskripsi = binding.edtDescription.text.toString()

        if (fitur.isEmpty() || deskripsi.isEmpty()) {
            if (fitur.isEmpty()) {
                binding.edtFeature.error = "Fitur Harus diisi"
            }
            if (deskripsi.isEmpty()) {
                binding.edtDescription.error = "Deskripsi Harus diisi"
            }
        } else {
            NetworkConfig().getServices().postCarFeature(fitur, deskripsi)
                .enqueue(object : Callback<ResponseAddNewCarFeature> {
                    override fun onResponse(
                        call: Call<ResponseAddNewCarFeature>,
                        response: Response<ResponseAddNewCarFeature>
                    ) {
                        val addNewFeatureResponse = response.body()
                        if (addNewFeatureResponse?.success == 1) {
                            showToast("Berhasil Menambahkan Fitur")
                        } else {
                            showToast(addNewFeatureResponse?.message ?: "Gagal Menambahkan Fitur")
                        }
                    }

                    override fun onFailure(call: Call<ResponseAddNewCarFeature>, t: Throwable) {
                        showToast("Terjadi kesalahan ${t.message}")
                    }

                })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}