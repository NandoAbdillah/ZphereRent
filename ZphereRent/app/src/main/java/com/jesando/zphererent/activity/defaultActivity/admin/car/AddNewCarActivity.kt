package com.jesando.zphererent.activity.defaultActivity.admin.car

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jesando.zphererent.R
import com.jesando.zphererent.config.NetworkConfig
import com.jesando.zphererent.databinding.ActivityAddNewCarBinding
import com.jesando.zphererent.model.response.car.ResponseAddNewCar
import com.jesando.zphererent.model.response.car.ResponseListCarCategory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddNewCarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewCarBinding
    private var selectedImageFile: File? = null
    private var selectedCategoryId: Int? = null


    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val file = getFileFromUri(uri)
            if (file == null) {
                showToast("File tidak ditemukan")
                return@registerForActivityResult
            }
            selectedImageFile = file
            binding.carPreview.setImageURI(uri)

            // Correct way to set height
            val layoutParams = binding.carPreview.layoutParams
            layoutParams.height = 435
            layoutParams.width = 535
            binding.carPreview.layoutParams = layoutParams

            binding.txtUrlImage.text = file.absolutePath
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNewCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets =
                insets.getInsets(WindowInsetsCompat.Type.ime()) // Mengambil insets keyboard
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                imeInsets.bottom
            ) // Menambahkan padding bawah saat keyboard muncul
            insets
        }


        binding.uploadImage.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
            )
        }

        binding.tambahData.setOnClickListener {
            uploadCarData()
        }

        binding.back.setOnClickListener {
            finish()
        }

        setUpDropdownMenu()
    }

    private fun setUpDropdownMenu() {
        val fuelTypeList = arrayOf("Bensin", "Diesel", "Listrik", "Hibrida")
        val transmissionTypeList = arrayOf("Manual", "Automatic")

        NetworkConfig().getServices().getAllCarCategories()
            .enqueue(object : Callback<ResponseListCarCategory> {
                override fun onResponse(
                    call: Call<ResponseListCarCategory>,
                    response: Response<ResponseListCarCategory>
                ) {
                    if (response.isSuccessful) {
                        val receiveDatas = response.body()?.data
                        if (!receiveDatas.isNullOrEmpty()) {

                            val categoryIds = receiveDatas.mapNotNull { it?.id }
                            val categoryNames = receiveDatas.mapNotNull { it?.name }

                            val categoryAdapter = ArrayAdapter(
                                this@AddNewCarActivity,
                                android.R.layout.simple_dropdown_item_1line,
                                categoryNames
                            )
                            binding.edtCateogries.setAdapter(categoryAdapter)

                            binding.edtCateogries.setOnItemClickListener { _, _, position, _ ->
                                selectedCategoryId = categoryIds[position]

                            }
                        } else {
                            binding.edtCateogries.error = "Belum Ada Kategori yang ditambahkan!"
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseListCarCategory>, t: Throwable) {
                    Toast.makeText(this@AddNewCarActivity, "Failed to fetch data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        val fuelAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, fuelTypeList)
        val transmissionAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, transmissionTypeList)
        binding.edtFuel.setAdapter(fuelAdapter)
        binding.edtTransmission.setAdapter(transmissionAdapter)
    }



    private fun uploadCarData() {
        val brand = binding.edtBrand.text.toString()
        val model = binding.edtModel.text.toString()
        val year = binding.edtYear.text.toString()
        val color = binding.edtColor.text.toString()
        val seats = binding.edtSeats.text.toString()
        val categoryId = selectedCategoryId?.toString() ?: ""
        val licensePlate = binding.edtLicense.text.toString()
        val pricePerDay = binding.edtPrice.text.toString()
        val fuelType = binding.edtFuel.text.toString()
        val transmission = binding.edtTransmission.text.toString()
        val mileage = binding.edtMileage.text.toString()
        val description = binding.edtDescription.text.toString()

        if (categoryId.isEmpty()) {
            showToast("Kategori belum dipilih")
            return
        }

        if (selectedImageFile == null) {
            showToast("Gambar belum dipilih")
            return
        }

        val brandBody = brand.toRequestBody("text/plain".toMediaTypeOrNull())
        val modelBody = model.toRequestBody("text/plain".toMediaTypeOrNull())
        val yearBody = year.toRequestBody("text/plain".toMediaTypeOrNull())
        val colorBody = color.toRequestBody("text/plain".toMediaTypeOrNull())
        val seatsBody = seats.toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryIdBody = categoryId.toRequestBody("text/plain".toMediaTypeOrNull())
        val licensePlateBody = licensePlate.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePerDayBody = pricePerDay.toRequestBody("text/plain".toMediaTypeOrNull())
        val fuelTypeBody = fuelType.toRequestBody("text/plain".toMediaTypeOrNull())
        val transmissionBody = transmission.toRequestBody("text/plain".toMediaTypeOrNull())
        val mileageBody = mileage.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

        val requestFile = selectedImageFile!!.asRequestBody("image/*".toMediaTypeOrNull())
        val imageBody = MultipartBody.Part.createFormData("image", selectedImageFile!!.name, requestFile)

        NetworkConfig().getServices().postCar(
            brandBody,
            modelBody,
            yearBody,
            colorBody,
            seatsBody,
            categoryIdBody,
            licensePlateBody,
            pricePerDayBody,
            fuelTypeBody,
            transmissionBody,
            mileageBody,
            descriptionBody,
            imageBody
        ).enqueue(object : Callback<ResponseAddNewCar> {
            override fun onResponse(
                call: Call<ResponseAddNewCar>,
                response: Response<ResponseAddNewCar>
            ) {
                val addNewCarResponse = response.body()
                if (addNewCarResponse?.success == 1) {
                    showToast("Berhasil Menambahkan Mobil Baru")
                } else {
                    showToast(addNewCarResponse?.message ?: "Gagal Menambahkan Mobil")
                }
            }

            override fun onFailure(call: Call<ResponseAddNewCar>, t: Throwable) {
                showToast("Berhasil Menambahkan Mobil Baru")

//                showToast("Terjadi kesalahan: ${t.message}")
            }
        })
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getFileFromUri(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return filePath?.let { File(it) }
    }
}
