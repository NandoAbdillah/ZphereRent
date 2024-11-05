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
import com.jesando.zphererent.databinding.ActivityAddNewCategoriesBinding
import com.jesando.zphererent.model.response.car.ResponseAddNewCarCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewCategoriesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddNewCategoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNewCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tambahData.setOnClickListener {
            uploadData()
        }
        binding.back.setOnClickListener {
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    private fun uploadData() {
        val category = binding.edtCategoy.text.toString()

        if (category.isEmpty()) {
            binding.edtCategoy.error = "Kategori wajib diisi"
        } else {
            NetworkConfig().getServices().postCarCategory(category)
                .enqueue(object : Callback<ResponseAddNewCarCategory> {
                    override fun onResponse(
                        call: Call<ResponseAddNewCarCategory>,
                        response: Response<ResponseAddNewCarCategory>
                    ) {
                        val addNewCarCategory = response.body()
                        if (addNewCarCategory?.success == 1) {
                            showToast("Berhasil Menambahkan Kategori Baru")
                        } else {
                            showToast(addNewCarCategory?.message?: "Gagal Menambahkan Kategori")
                        }
                    }

                    override fun onFailure(call: Call<ResponseAddNewCarCategory>, t: Throwable) {
                        showToast("Terjadi kesalahan : ${t.message}")
                    }

                })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}