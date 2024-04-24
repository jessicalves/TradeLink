package com.jessmobilesolutions.tradelink.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.adapters.ProductsAdapter
import com.jessmobilesolutions.tradelink.models.Product
import com.jessmobilesolutions.tradelink.viewmodels.ProductsCatalogViewModel

class ProductsCatalogFragment : Fragment() {
    private val viewModel: ProductsCatalogViewModel by viewModels()
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_products_catalog, container, false)
        
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        
        productsAdapter = ProductsAdapter(emptyList())
        recyclerView.adapter = productsAdapter
        
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productsAdapter.products = products
            productsAdapter.notifyDataSetChanged()
        }
        
        view.findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            val product = Product( 
                id = viewModel.products.value?.size ?: 0 + 1,
                name = "New Product",
                price = 0.0
            )
            viewModel.addProduct(product)
        }

        return view
    }
}