package com.jesando.zphererent.activity.guestActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.jesando.zphererent.activity.defaultActivity.DashboardActivity
import com.jesando.zphererent.config.NetworkConfig
import com.jesando.zphererent.databinding.ActivityLoginBinding
import com.jesando.zphererent.model.response.auth.LoginResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        Menangani loggin button

        binding.LBtn1.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            var isValid = true

            if (email.isEmpty()) {
                binding.edtEmail.error = "Email wajib diisi"
                isValid = false
            }
            if (password.isEmpty()) {
                binding.edtPassword.error = "Password wajib diisi"
                isValid = false
            }

            if (isValid)
            {
                performLogin(email, password)
            }
        }

        binding.toForgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin(email: String, password: String) {
        val api = NetworkConfig().getServices()

        api.login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.success == 1) {
                        Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()

                        // Menyimpan token untuk menavigasi ke aktivitas selanjutnya
                        val token = "Bearer ${loginResponse.token}"
                        val user = loginResponse.user
                        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()

                        val gson = Gson()
                        val userJson = gson.toJson(user)

                        editor.putString("token", token)
                        editor.putString("user_detail", userJson)
                        editor.apply()

                        // Menavigasi ke DashboardActivity
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()

                    } else {
                        showToast(loginResponse?.message ?: "Login Failed")
                    }
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody)
                        val errorMessage = jsonObject.optString("message", "Login Failed")
                        showToast(errorMessage)
                    } catch (e: Exception) {
                        showToast("Login Failed")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }


    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }


}