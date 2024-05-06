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
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.adapters.VisitsAdapter
import com.jessmobilesolutions.tradelink.viewmodels.VisitsRequestsViewModel

class VisitsRequestsFragment : Fragment() {

    private val viewModel: VisitsRequestsViewModel by viewModels()
    private lateinit var visitsAdapter: VisitsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_visits_requests, container, false)

        val recyclerViewVisits = view.findViewById<RecyclerView>(R.id.recyclerViewVisits)
        recyclerViewVisits.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(recyclerViewVisits.context, LinearLayoutManager.VERTICAL)
        recyclerViewVisits.addItemDecoration(dividerItemDecoration)

        visitsAdapter = VisitsAdapter(emptyList())
        recyclerViewVisits.adapter = visitsAdapter
        
        viewModel.visits.observe(viewLifecycleOwner){ visits ->
            visitsAdapter.visits = visits
            visitsAdapter.notifyDataSetChanged()
        }
        
        return view
    }

}