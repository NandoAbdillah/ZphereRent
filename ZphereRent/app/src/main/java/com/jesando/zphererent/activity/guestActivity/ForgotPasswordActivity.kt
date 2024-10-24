package com.jesando.zphererent.activity.guestActivity

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jesando.zphererent.R
import com.jesando.zphererent.config.NetworkConfig
import com.jesando.zphererent.databinding.ActivityForgotPasswordBinding
import com.jesando.zphererent.model.response.auth.ForgotPasswordResponse
import com.jesando.zphererent.model.response.auth.VerifyTokenResponse
import com.jesando.zphererent.model.response.auth.ResetPasswordResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private var email: String? = null // Variabel untuk menyimpan email pengguna

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewFlipper = binding.viewFlipper
        viewFlipper.inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        viewFlipper.outAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)

        binding.LBtnSendOtp.setOnClickListener {
            val enteredEmail = binding.edtEmailForgot.text.toString()
            if (enteredEmail.isNotEmpty()) {
                performSendOtp(enteredEmail) { success ->
                    if (success) {
                        email = enteredEmail // Simpan email jika OTP berhasil dikirim
                        viewFlipper.showNext()
                    }
                }
            } else {
                showToast("Email wajib diisi")
            }
        }

        binding.LBtnConfirmOtp.setOnClickListener {
            val otp = binding.edtOtp.text.toString()
            if (otp.isNotEmpty()) {
                performConfirmOtp(otp) { success ->
                    if (success) {
                        viewFlipper.showNext()
                    }
                }
            } else {
                showToast("Kode OTP wajib diisi")
            }
        }

        binding.LBtnConfirmNewPassword.setOnClickListener {
            val newPassword = binding.edtNewPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()
            if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (newPassword == confirmPassword) {
                    performConfirmNewPassword(newPassword, confirmPassword) { success ->
                        if (success) {
                            showToast("Password berhasil direset")
                            finish()
                        }
                    }
                } else {
                    showToast("Password dan konfirmasi password tidak cocok")
                }
            } else {
                showToast("Password baru dan konfirmasi password wajib diisi")
            }
        }
    }

    private fun performSendOtp(email: String, callback: (Boolean) -> Unit) {
        val api = NetworkConfig().getServices()

        api.forgotPassword(email).enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(
                call: Call<ForgotPasswordResponse>,
                response: Response<ForgotPasswordResponse>
            ) {
                if (response.isSuccessful) {
                    val forgotResponse = response.body()
                    if (forgotResponse?.success == 1) {
                        showToast("Kode OTP berhasil dikirim, cek email anda")
                        callback(true)
                    } else {
                        showToast(forgotResponse?.message ?: "Tidak dapat mengirim OTP")
                        callback(false)
                    }
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody)
                        val errorMessage = jsonObject.getString("message")
                        showToast(errorMessage)
                    } catch (e: Exception) {
                        showToast("Gagal mengirim OTP")
                    }
                    callback(false)
                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
                callback(false)
            }
        })
    }

    private fun performConfirmOtp(otp: String, callback: (Boolean) -> Unit) {
        val api = NetworkConfig().getServices()
        val userEmail = email ?: return callback(false) // Gunakan email yang disimpan

        api.verifyToken(userEmail, otp).enqueue(object : Callback<VerifyTokenResponse> {
            override fun onResponse(
                call: Call<VerifyTokenResponse>,
                response: Response<VerifyTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val verifyResponse = response.body()
                    if (verifyResponse?.success == 1) {
                        showToast("OTP valid, silakan reset password")
                        callback(true)
                    } else {
                        showToast(verifyResponse?.message ?: "OTP tidak valid")
                        callback(false)
                    }
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody)
                        val errorMessage = jsonObject.getString("message")
                        showToast(errorMessage)
                    } catch (e: Exception) {
                        showToast("Gagal mengonfirmasi OTP")
                    }
                    callback(false)
                }
            }

            override fun onFailure(call: Call<VerifyTokenResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
                callback(false)
            }
        })
    }

    private fun performConfirmNewPassword(newPassword: String,confirmPassword : String, callback: (Boolean) -> Unit) {
        val api = NetworkConfig().getServices()
        val userEmail = email ?: return callback(false) // Gunakan email yang disimpan

        api.resetPassword(userEmail, newPassword, confirmPassword).enqueue(object : Callback<ResetPasswordResponse> {
            override fun onResponse(
                call: Call<ResetPasswordResponse>,
                response: Response<ResetPasswordResponse>
            ) {
                if (response.isSuccessful) {
                    val resetResponse = response.body()
                    if (resetResponse?.success == 1) {
                        showToast("Password berhasil direset")
                        callback(true)
                    } else {
                        showToast(resetResponse?.message ?: "Gagal mereset password")
                        callback(false)
                    }
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody)
                        val errorMessage = jsonObject.getString("message")
                        showToast(errorMessage)
                    } catch (e: Exception) {
                        showToast("Gagal mereset password")
                    }
                    callback(false)
                }
            }

            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
                callback(false)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@ForgotPasswordActivity, message, Toast.LENGTH_SHORT).show()
    }
}
