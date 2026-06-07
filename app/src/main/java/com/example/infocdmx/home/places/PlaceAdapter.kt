package com.example.infocdmx.home.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.infocdmx.R
import com.example.infocdmx.core.model.Place
import com.example.infocdmx.databinding.ItemPlaceBinding

class PlaceAdapter(
    private val onItemClick: (Place) -> Unit = {}
) : ListAdapter<Place, PlaceAdapter.PlaceViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaceViewHolder(
        private val binding: ItemPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.tvNombreLugar.text = place.name
            binding.tvUbicacion.text = place.vicinity ?: ""

            Glide.with(binding.ivLugar.context)
                .load(place.icon)
                .placeholder(R.drawable.ic_place)
                .error(R.drawable.ic_place)
                .centerCrop()
                .into(binding.ivLugar)

            binding.root.setOnClickListener {
                onItemClick(place)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean =
                oldItem.placeId == newItem.placeId

            override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean =
                oldItem == newItem
        }
    }
}