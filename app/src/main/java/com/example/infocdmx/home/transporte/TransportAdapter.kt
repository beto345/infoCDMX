package com.example.infocdmx.home.transporte

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infocdmx.databinding.ItemTransportBinding

class TransportAdapter(
    private val onClick: (TransportLine) -> Unit = {}
) : ListAdapter<TransportLine, TransportAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemTransportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemTransportBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(line: TransportLine) {
            b.tvLineName.text = line.name
            val km = if (line.lengthKm > 0) " · ${"%.1f".format(line.lengthKm)} km" else ""
            b.tvSystem.text = "${line.type}$km"
            val color = runCatching { Color.parseColor(line.colorHex) }.getOrDefault(Color.GRAY)
            b.viewColorStripe.setBackgroundColor(color)
            b.root.setOnClickListener { onClick(line) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<TransportLine>() {
            override fun areItemsTheSame(o: TransportLine, n: TransportLine) = o.id == n.id
            override fun areContentsTheSame(o: TransportLine, n: TransportLine) = o == n
        }
    }
}