package com.jesando.zphererent.activity.defaultActivity.admin.car

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jesando.zphererent.R
import com.jesando.zphererent.databinding.ActivityCarManagementBinding

class CarManagementActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCarManagementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCarManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddCar.setOnClickListener {
            val intent = Intent(this, AddNewCarActivity::class.java)
            startActivity(intent)  // Pindah ke AddNewCarActivity
        }

    }
}