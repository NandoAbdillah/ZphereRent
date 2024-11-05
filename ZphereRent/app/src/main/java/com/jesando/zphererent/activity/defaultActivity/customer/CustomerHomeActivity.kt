package com.jesando.zphererent.activity.defaultActivity.customer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jesando.zphererent.adapter.CarsAdapter
import com.jesando.zphererent.config.NetworkConfig
import com.jesando.zphererent.databinding.ActivityCustomerHomeBinding
import com.jesando.zphererent.model.response.car.DataCar
import com.jesando.zphererent.model.response.car.ResponseListCar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerHomeActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityCustomerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCustomerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menampilkan progress indicator sebelum memanggil API
        binding.progressIndicator.visibility = View.VISIBLE
        getCars()
    }

    private fun getCars() {
        NetworkConfig().getServices().getAllCars()
            .enqueue(object : Callback<ResponseListCar> {
                override fun onResponse(
                    call: Call<ResponseListCar>,
                    response: Response<ResponseListCar>
                ) {
                    binding.progressIndicator.visibility = View.GONE
                    if (response.isSuccessful) {
                        val receiveDatas = response.body()?.data
                        // Cek apakah data diterima dan tidak kosong
                        if (!receiveDatas.isNullOrEmpty()) {
                            setToAdapter(receiveDatas)
                        } else {
                            Toast.makeText(this@CustomerHomeActivity, "No data available", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@CustomerHomeActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseListCar>, t: Throwable) {
                    binding.progressIndicator.visibility = View.GONE
                    Log.e("Retrofit onFailure", "onFailure: ${t.message}", t)
                    Toast.makeText(this@CustomerHomeActivity, "Failed to fetch data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setToAdapter(receiveDatas: List<DataCar?>) {
        val adapter = CarsAdapter(receiveDatas)
        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCars.layoutManager = lm
        binding.rvCars.itemAnimator = DefaultItemAnimator()
        binding.rvCars.adapter = adapter
    }

    override fun onRefresh() {
        getCars()
    }
}
