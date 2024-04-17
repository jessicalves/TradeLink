package com.jessmobilesolutions.tradelink.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.models.Representative
import java.util.*

class RepresentativeAdapter(private var representatives: List<Representative>) :
    RecyclerView.Adapter<RepresentativeAdapter.RepresentativeViewHolder>() {

    private var filteredRepresentatives: List<Representative> = representatives

    inner class RepresentativeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val companyTextView: TextView = itemView.findViewById(R.id.companyTextView)
        private val nicheTextView: TextView = itemView.findViewById(R.id.nicheTextView)
        private val representativeTextView: TextView = itemView.findViewById(R.id.representativeTextView)
        private val cityTextView: TextView = itemView.findViewById(R.id.cityTextView)

        fun bind(representative: Representative) {
            companyTextView.text = representative.representedCompany
            nicheTextView.text = representative.niche
            representativeTextView.text = representative.name
            cityTextView.text = representative.city
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_representative, parent, false)
        return RepresentativeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        holder.bind(filteredRepresentatives[position])
    }

    override fun getItemCount(): Int {
        return filteredRepresentatives.size
    }

    fun filter(text: String) {
        filteredRepresentatives = if (text.isEmpty()) {
            representatives
        } else {
            representatives.filter { representative ->
                representative.representedCompany.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) ||
                        representative.niche.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))
            }
        }
        notifyDataSetChanged()
    }
}
