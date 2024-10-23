package com.jesando.zphererent.activity.defaultActivity.customer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jesando.zphererent.databinding.ActivityCustomerHomeBinding

class CustomerHomeActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityCustomerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}