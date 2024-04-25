package com.jessmobilesolutions.tradelink.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.adapters.ProductsAdapter
import com.jessmobilesolutions.tradelink.viewmodels.CompanyProductsViewModel

class CompanyProductsActivity : AppCompatActivity() {

    private lateinit var viewModel: CompanyProductsViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_company_products)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        val companyId = intent.getStringExtra("companyId")
        if (companyId != null) {
            viewModel = ViewModelProvider(this).get(CompanyProductsViewModel::class.java)
            
            viewModel.getProductsForCompany(companyId)

            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
            recyclerView.addItemDecoration(dividerItemDecoration)

            adapter = ProductsAdapter(emptyList())
            recyclerView.adapter = adapter
            
            viewModel.products.observe(this) { products ->
                adapter.products = products
                adapter.notifyDataSetChanged()
            }
        } else {
            // Tratar o caso em que não há ID da empresa
            // Talvez exibir uma mensagem de erro ou voltar para a tela anterior
        }
    }
}
