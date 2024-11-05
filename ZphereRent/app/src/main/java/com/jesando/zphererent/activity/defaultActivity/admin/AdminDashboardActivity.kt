package com.jesando.zphererent.activity.defaultActivity.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jesando.zphererent.R
import com.jesando.zphererent.activity.defaultActivity.admin.car.AddNewCarActivity
import com.jesando.zphererent.activity.defaultActivity.admin.car.AddNewCategoriesActivity
import com.jesando.zphererent.activity.defaultActivity.admin.car.AddNewFeaturesActivity
import com.jesando.zphererent.activity.defaultActivity.admin.car.CarManagementActivity
import com.jesando.zphererent.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdminDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tmbhMobil.setOnClickListener {
            intentActivity(AddNewCarActivity::class.java)
        }
        binding.tmbhFitur.setOnClickListener {
            intentActivity(AddNewFeaturesActivity::class.java)
        }
        binding.tmbhKategori.setOnClickListener {
            intentActivity(AddNewCategoriesActivity::class.java)
        }
        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun intentActivity(activity: Class<out Activity>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }



}