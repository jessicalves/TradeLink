package com.jessmobilesolutions.tradelink.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.models.Product
import java.util.Locale

class ItemSalesAdapter(var products: List<Product>, private var totalTextView: TextView) :
    RecyclerView.Adapter<ItemSalesAdapter.ViewHolder>() {

    private var total: Double = 0.0

    data class SoldProduct(val product: Product, var quantity: Int)

    val soldProductsList = mutableListOf<SoldProduct>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val editTextQtd: EditText = itemView.findViewById(R.id.editTextQtd)
        val btnAddItem: ImageButton = itemView.findViewById(R.id.btnAddItem)
        val btnLessItem: ImageButton = itemView.findViewById(R.id.btnLessItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sales, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.productName.text = product.name
        holder.productPrice.text = "R$ ${product.price}"

        holder.btnAddItem.setOnClickListener {
            val qtd = holder.editTextQtd.text.toString().toIntOrNull() ?: 0
            holder.editTextQtd.setText((qtd + 1).toString())
            updateTotal()
            addProductToSale(product)
        }

        holder.btnLessItem.setOnClickListener {
            val qtd = holder.editTextQtd.text.toString().toIntOrNull() ?: 0
            if (qtd > 0) {
                holder.editTextQtd.setText((qtd - 1).toString())
                updateTotal()
                removeProductFromSale(product)
            }
        }
        holder.editTextQtd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                updateTotal()
            }
        })
    }

    override fun getItemCount(): Int {
        return products.size
    }

    private fun addProductToSale(product: Product) {
        val existingProduct = soldProductsList.find { it.product == product }
        if (existingProduct != null) {
            existingProduct.quantity++
        } else {
            soldProductsList.add(SoldProduct(product, 1))
        }
        updateTotal()
    }

    private fun removeProductFromSale(product: Product) {
        val existingProduct = soldProductsList.find { it.product == product }
        if (existingProduct != null) {
            if (existingProduct.quantity > 1) {
                existingProduct.quantity--
            } else {
                soldProductsList.remove(existingProduct)
            }
            updateTotal()
        }
    }

    private fun updateTotal() {
        var total = 0.0
        for (soldProduct in soldProductsList) {
            total += soldProduct.product.price * soldProduct.quantity
        }
        totalTextView.text = String.format(Locale.getDefault(), "Total R$ %.2f", total)
    }
}
