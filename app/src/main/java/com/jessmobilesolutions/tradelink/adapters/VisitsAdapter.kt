package com.jessmobilesolutions.tradelink.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.models.Visit

class VisitsAdapter(var visits: List<Visit>) : RecyclerView.Adapter<VisitsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clientName: TextView = itemView.findViewById(R.id.clientName)
        val clientPhone: TextView = itemView.findViewById(R.id.clientPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visit, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return visits.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val visit = visits[position]
        holder.clientName.text = visit.clientName
        holder.clientPhone.text = visit.clientPhone
    }
}