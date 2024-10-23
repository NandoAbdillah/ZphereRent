package com.jesando.zphererent.activity.guestActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jesando.zphererent.R
import com.jesando.zphererent.config.NetworkConfig
import com.jesando.zphererent.databinding.ActivityRegisterBinding
import com.jesando.zphererent.model.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button register
        binding.RBtn1.setOnClickListener {
            val fullname = binding.edtFullname.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfPassword.text.toString()

            var isValid = true

            if (fullname.isEmpty()) {
                binding.edtFullname.error = "Nama lengkap wajib diisi"
                isValid = false
            }
            if (email.isEmpty()) {
                binding.edtEmail.error = "Email wajib diisi"
                isValid = false
            }
            if (password.isEmpty()) {
                binding.edtPassword.error = "Password wajib diisi"
                isValid = false
            }
            if (confirmPassword.isEmpty()) {
                binding.edtConfPassword.error = "Konfirmasi password wajib diisi"
                isValid = false
            }
            if (password != confirmPassword) {
                binding.edtConfPassword.error = "Password dan konfirmasi password tidak sesuai"
                isValid = false
            }

            if (isValid) {
                performRegister(fullname, email, password, confirmPassword)
            }
        }

        binding.txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister(fullname: String, email: String, password: String, confirmPassword: String) {
        NetworkConfig().getServices().registerUser(
            name = fullname,
            email = email,
            password = password,
            passwordConfirmation = confirmPassword
        ).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null && registerResponse.success == 1) {
                        showToast("Pendaftaran berhasil")
                    } else {
                        // Tampilkan pesan kesalahan dari server jika ada
                        showToast(registerResponse?.message ?: "Pendaftaran gagal, silakan coba lagi")
                    }
                } else {
                    // Jika respons tidak sukses, coba parsing error body untuk mendapatkan pesan "message"
                    try {
                        val errorBody  = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody)
                        val errorMessage = jsonObject.getString("message")
                        showToast(errorMessage)
                    } catch (e: Exception) {
                        showToast("Terjadi kesalahan")
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showToast("Gagal terhubung ke server: ${t.message}")
            }
        })
    }


    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }
}
