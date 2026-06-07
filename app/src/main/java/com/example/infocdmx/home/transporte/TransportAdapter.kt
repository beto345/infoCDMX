package com.example.infocdmx.home.transporte

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infocdmx.databinding.ItemTransportBinding

class TransportAdapter : ListAdapter<TransportLine, TransportAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemTransportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransportLine) {
            binding.tvLineName.text = item.name
            binding.tvLineType.text = item.type
            
            // Mostrar estaciones
            if (item.stations.isNotEmpty()) {
                val stationsText = "Estaciones: ${item.stations.joinToString(", ")}"
                binding.tvStations.text = stationsText
            } else {
                binding.tvStations.text = ""
            }

            try {
                binding.viewLineColor.setBackgroundColor(Color.parseColor(item.color))
            } catch (e: Exception) {
                binding.viewLineColor.setBackgroundColor(Color.GRAY)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TransportLine>() {
        override fun areItemsTheSame(oldItem: TransportLine, newItem: TransportLine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TransportLine, newItem: TransportLine): Boolean {
            return oldItem == newItem
        }
    }
}
