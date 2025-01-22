package com.smorzhok.hotelsapp.remote

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smorzhok.hotelsapp.R
import com.smorzhok.hotelsapp.model.Hotel


class HotelsAdapter(private val onHotelClickListener: (Hotel) -> Unit) :
    RecyclerView.Adapter<HotelsAdapter.HotelViewHolder>() {

    private var _hotels: List<Hotel> = mutableListOf()
    private val hotels: List<Hotel> get() = _hotels

    fun setHotels(hotels: List<Hotel>) {
        this._hotels = hotels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_item, parent, false)
        return HotelViewHolder(view)
    }

    override fun getItemCount(): Int = hotels.size

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = hotels[position]

        val rating = hotel.stars
        val backgroundId = if (rating > 2.5) R.drawable.green_circle else R.drawable.red_circle
        val background = ContextCompat.getDrawable(holder.itemView.context, backgroundId)
        holder.textViewRating.background = background
        holder.textViewRating.text = String.format("%.1f", rating)
        val suitesAvailability = hotel.suitesAvailability.split(":")
        val roomCount = suitesAvailability.count { it.isNotEmpty() }
        holder.textViewSpace.text = StringBuilder()
            .append(roomCount.toString())
            .append(" мест")
            .toString()
        holder.textViewDistance.text = StringBuilder()
            .append(hotel.distance)
            .append(" метров")
            .toString()
        holder.textViewName.text = hotel.name
        holder.itemView.setOnClickListener {
            onHotelClickListener(hotel)
        }
    }

    class HotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewDistance: TextView = itemView.findViewById(R.id.textViewAddress)
        val textViewRating: TextView = itemView.findViewById(R.id.textViewRating)
        val textViewSpace: TextView = itemView.findViewById(R.id.textViewSpace)
    }

}