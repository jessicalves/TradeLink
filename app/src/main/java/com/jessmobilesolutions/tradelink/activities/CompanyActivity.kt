package com.jessmobilesolutions.tradelink.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jessmobilesolutions.tradelink.R
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jessmobilesolutions.tradelink.adapters.RepresentativeAdapter
import com.jessmobilesolutions.tradelink.viewmodels.CompanyViewModel

class CompanyActivity : AppCompatActivity() {

    private lateinit var viewModel: CompanyViewModel
    private lateinit var adapter: RepresentativeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_company)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(CompanyViewModel::class.java)
        viewModel.loadRepresentatives()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RepresentativeAdapter(emptyList()) 
        recyclerView.adapter = adapter

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterRepresentatives(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.representatives.observe(this) { representatives ->
            adapter.updateList(representatives)
        }
    }
}
