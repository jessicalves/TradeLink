package com.jessmobilesolutions.tradelink.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.adapters.SalesAdapter
import com.jessmobilesolutions.tradelink.viewmodels.SalesViewModel

class SalesFragment : Fragment() {

    private lateinit var viewModel: SalesViewModel
    private lateinit var salesAdapter: SalesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SalesViewModel::class.java]
        viewModel.fetchSalesFromFirebase()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sales, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewSales)
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        salesAdapter = SalesAdapter(emptyList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = salesAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.sales.observe(viewLifecycleOwner, Observer { sales ->
            salesAdapter.sales = sales
            salesAdapter.notifyDataSetChanged()
        })
    }
}

