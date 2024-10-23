package com.jesando.zphererent.activity.defaultActivity;

import android.content.Intent
import android.content.SharedPreferences
import com.jesando.zphererent.databinding.ActivityDashboardBinding;
import android.os.Bundle;
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson
import com.jesando.zphererent.R
import com.jesando.zphererent.activity.defaultActivity.customer.CustomerHomeActivity
import com.jesando.zphererent.activity.mainActivity.MainActivity
import com.jesando.zphererent.config.NetworkConfig
import com.jesando.zphererent.model.User
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


public class DashboardActivity : AppCompatActivity(){

    private  lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil username dari SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val userJson = sharedPreferences.getString("user_detail", null);

        if (userJson != null)
        {
            val gson = Gson()

            val user = gson.fromJson(userJson, User::class.java)
            val userName = user.name

            Toast.makeText(this, "User Name: $userName", Toast.LENGTH_SHORT).show()

            // Ubah teks pada TextView
            binding.username.text = "Hai, $userName" // Menetapkan teks yang diinginkan
        }

        binding.cardCustomer.setOnClickListener {
            // Pindah ke CustomerActivity
            val intent = Intent(this, CustomerHomeActivity::class.java)
            startActivity(intent)
        }

        binding.profile.setOnClickListener {
            showProfileMenu(it)
        }


    }

    private fun showProfileMenu(view: android.view.View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.profile_menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    // Logout user
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_logout -> {
                    performLogout()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun performLogout() {
        // Ambil token dari SharedPreferences
        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val bearerToken = sharedPreferences.getString("token", null)

        if (bearerToken == null) {
            logoutUserLocally()
            return
        }

        val api = NetworkConfig().getServices()

        // Lakukan request logout ke server
        api.logoutUser(bearerToken).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Logout berhasil di server
                    showToast("Logout berhasil")
                    logoutUserLocally()
                } else {
                    // Gagal logout di server
                    try {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody)
                        val errorMessage = jsonObject.optString("message", "Gagal logout")
                        showToast(errorMessage)
                    } catch (e: Exception) {
                        showToast("Gagal logout")
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun logoutUserLocally() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
