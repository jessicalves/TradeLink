package com.jessmobilesolutions.tradelink.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.adapters.ProductsAdapter
import com.jessmobilesolutions.tradelink.viewmodels.CompanyProductsViewModel

class CompanyProductsActivity : AppCompatActivity() {

    private lateinit var viewModel: CompanyProductsViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupViewModel()
        setupRecyclerView()
        setupFloatingActionButton()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        setContentView(R.layout.activity_company_products)
        applyWindowInsets(findViewById(R.id.main))
    }

    private fun applyWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupViewModel() {
        val companyId = intent.getStringExtra("companyId")
        val companyName = intent.getStringExtra("companyName")

        if (companyId != null) {
            viewModel = ViewModelProvider(this).get(CompanyProductsViewModel::class.java)
            viewModel.getProductsForCompany(companyId)

            val titleCompanyTextView = findViewById<TextView>(R.id.titleCompanyTextView)
            titleCompanyTextView.text = getString(R.string.title_catalog_company, companyName)
        } else {
            //error
            finish()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL))

        adapter = ProductsAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.products.observe(this) { products ->
            adapter.products = products
            adapter.notifyDataSetChanged()

            val emptyTextView: TextView = findViewById(R.id.emptyTextView)
            emptyTextView.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupFloatingActionButton() {
        findViewById<FloatingActionButton>(R.id.fabVisit).setOnClickListener {
            val companyId = intent.getStringExtra("companyId")
            val intent = Intent(this, ClientRequestVisit::class.java)
            intent.putExtra("companyId", companyId)
            startActivity(intent)
        }
    }
}
