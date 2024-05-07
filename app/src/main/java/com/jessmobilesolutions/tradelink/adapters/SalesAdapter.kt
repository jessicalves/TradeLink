package com.jessmobilesolutions.tradelink.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.models.Sale

class SalesAdapter(var sales: List<Sale>) : RecyclerView.Adapter<SalesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clientNameTextView: TextView = itemView.findViewById(R.id.textViewClientName)
        val saleDateTextView: TextView = itemView.findViewById(R.id.textViewSaleDate)
        val totalTextView: TextView = itemView.findViewById(R.id.textViewTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sale, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sale = sales[position]
        holder.clientNameTextView.text = "${sale.clientName}"
        holder.saleDateTextView.text = "${sale.saleDate}"
        holder.totalTextView.text = "${sale.total}"
    }

    override fun getItemCount(): Int {
        return sales.size
    }
}
