package com.jesando.zphererent.activity.defaultActivity.admin.car

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.jesando.zphererent.config.NetworkConfig
import com.jesando.zphererent.databinding.ActivityAddNewCarBinding
import com.jesando.zphererent.model.response.car.ResponseAddNewCar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddNewCarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewCarBinding
    private var selectedImageFile: File? = null

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
            layoutParams.height = 100 // 100 dp
            layoutParams.width = 100
            binding.carPreview.layoutParams = layoutParams

            binding.txtUrlImage.text = file.absolutePath
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNewCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadImage.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
            )
        }

        binding.tambahData.setOnClickListener {
            uploadCarData()
        }
    }

    private fun uploadCarData() {
        val brand = binding.edtBrand.text.toString()
        val model = binding.edtModel.text.toString()
        val year = binding.edtYear.text.toString()
        val color = binding.edtColor.text.toString()
        val seats = binding.edtSeats.text.toString()
        val categoryId = "2"
        val licensePlate = binding.edtLicense.text.toString()
        val pricePerDay = binding.edtPrice.text.toString()
        val fuelType = binding.edtFuel.text.toString()
        val transmission = binding.edtTransmission.text.toString()
        val mileage = binding.edtMileage.text.toString()
        val description = binding.edtDescription.text.toString()

        if (selectedImageFile == null) {
            showToast("Gambar belum dipilih")
            return
        }

        val brandBody = RequestBody.create("text/plain".toMediaTypeOrNull(), brand)
        val modelBody = RequestBody.create("text/plain".toMediaTypeOrNull(), model)
        val yearBody = RequestBody.create("text/plain".toMediaTypeOrNull(), year)
        val colorBody = RequestBody.create("text/plain".toMediaTypeOrNull(), color)
        val seatsBody = RequestBody.create("text/plain".toMediaTypeOrNull(), seats)
        val categoryIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), categoryId)
        val licensePlateBody = RequestBody.create("text/plain".toMediaTypeOrNull(), licensePlate)
        val pricePerDayBody = RequestBody.create("text/plain".toMediaTypeOrNull(), pricePerDay)
        val fuelTypeBody = RequestBody.create("text/plain".toMediaTypeOrNull(), fuelType)
        val transmissionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), transmission)
        val mileageBody = RequestBody.create("text/plain".toMediaTypeOrNull(), mileage)
        val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), selectedImageFile!!)
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
            override fun onResponse(call: Call<ResponseAddNewCar>, response: Response<ResponseAddNewCar>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.error == false) {
                        showToast("Data mobil berhasil ditambahkan")
                    } else {
                        showToast(responseBody?.message ?: "Gagal menambahkan data mobil")
                    }
                } else {
                    // Parsing error body jika ada
                    val errorMessage = response.errorBody()?.string() ?: "Terjadi kesalahan yang tidak diketahui"
                    showToast(errorMessage)
                }
            }

            override fun onFailure(call: Call<ResponseAddNewCar>, t: Throwable) {
                showToast("Terjadi kesalahan: ${t.message}")
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
