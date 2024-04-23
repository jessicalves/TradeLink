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

    inner class RepresentativeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val companyTextView: TextView = itemView.findViewById(R.id.companyTextView)
        private val nicheTextView: TextView = itemView.findViewById(R.id.nicheTextView)
        private val representativeTextView: TextView = itemView.findViewById(R.id.representativeTextView)
        private val cityTextView: TextView = itemView.findViewById(R.id.cityTextView)
        private val stateTextView: TextView = itemView.findViewById(R.id.stateTextView)

        fun bind(representative: Representative) {
            companyTextView.text = representative.representedCompany
            nicheTextView.text = representative.niche
            representativeTextView.text = representative.name
            cityTextView.text = representative.city
            stateTextView.text = representative.state
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_representative, parent, false)
        return RepresentativeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        holder.bind(representatives[position])
    }

    override fun getItemCount(): Int {
        return representatives.size
    }

    fun updateList(newList: List<Representative>) {
        representatives = newList
        notifyDataSetChanged()
    }
}
