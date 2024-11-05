package com.jesando.zphererent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jesando.zphererent.databinding.ListCarsAdapterBinding
import com.jesando.zphererent.model.response.car.DataCar
import com.squareup.picasso.Picasso


class CarsAdapter(
    private val listCars: List<DataCar?>?
) : RecyclerView.Adapter<CarsAdapter.ViewHolder>() {

    inner class ViewHolder(val listCarsAdapterBinding: ListCarsAdapterBinding) :
        RecyclerView.ViewHolder(listCarsAdapterBinding.root) {
        // Define your views here
        fun onBindItem(dataCar: DataCar?) {
            // Bind your views here
            listCarsAdapterBinding.carModel.text = dataCar?.model
            listCarsAdapterBinding.carYear.text = dataCar?.year
            listCarsAdapterBinding.carSeats.text = dataCar?.seats
            listCarsAdapterBinding.carMileage.text = dataCar?.mileage
            listCarsAdapterBinding.carTransmission.text = dataCar?.transmission
            Picasso.get()
                .load("http://192.168.100.13:8000/storage/${dataCar?.image}")
                .into(listCarsAdapterBinding.carImage)
            val priceFormatted = formatRupiah(dataCar?.pricePerDay?.toDouble() ?: 0.0)
            listCarsAdapterBinding.carPrice.text = priceFormatted
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsAdapter.ViewHolder {
        val binding =
            ListCarsAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarsAdapter.ViewHolder, position: Int) {
        holder.onBindItem(listCars?.get(position))
    }

    override fun getItemCount(): Int {
        return listCars?.size ?: 0
    }

    fun formatRupiah(value: Double): String {
        val formatter = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("in", "ID"))
        return formatter.format(value).replace("Rp", "Rp ").replace(",00", "")
    }

}