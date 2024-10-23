package com.jesando.zphererent.activity.defaultActivity.admin.car

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jesando.zphererent.R
import com.jesando.zphererent.databinding.ActivityAddNewCarBinding

class AddNewCarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddNewCarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNewCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}