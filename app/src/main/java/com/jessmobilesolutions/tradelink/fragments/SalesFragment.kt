package com.jessmobilesolutions.tradelink.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.activities.NewClientActivity
import com.jessmobilesolutions.tradelink.activities.NewSalesActivity
import com.jessmobilesolutions.tradelink.viewmodels.SalesViewModel

class SalesFragment : Fragment() {
    private val viewModel: SalesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_sales, container, false)
        var fabNewSale: FloatingActionButton = view.findViewById(R.id.fabNewSale)

        fabNewSale.setOnClickListener {
            var intent = Intent(requireContext(), NewSalesActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}