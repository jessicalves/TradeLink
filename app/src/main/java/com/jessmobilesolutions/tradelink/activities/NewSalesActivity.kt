package com.jessmobilesolutions.tradelink.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.adapters.ItemSalesAdapter
import com.jessmobilesolutions.tradelink.viewmodels.CompanyProductsViewModel
import com.jessmobilesolutions.tradelink.viewmodels.NewSalesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewSalesActivity : AppCompatActivity() {
    private lateinit var viewModel: NewSalesViewModel
    private lateinit var itemAdapter: ItemSalesAdapter
    private lateinit var totalTextView: TextView
    private lateinit var btnSave: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_sales)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupFieldDate()
        setupView()
    }

    private fun setupFieldDate() {
        val currentDate = Calendar.getInstance()
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        editTextDate.setText(dateFormatter.format(currentDate.time))
        editTextDate.setOnClickListener {
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                editTextDate.setText(dateFormatter.format(selectedDate.time))
            }, year, month, day)
            datePickerDialog.show()
        }
    }

    private fun setupView() {
        viewModel = ViewModelProvider(this)[NewSalesViewModel::class.java]
        viewModel.loadProducts()

        val recyclerViewItems: RecyclerView = findViewById(R.id.recyclerViewItems)
        recyclerViewItems.layoutManager = LinearLayoutManager(this)

        totalTextView = findViewById(R.id.textViewTotal)
        totalTextView.text = "Total R$ 0.00"

        btnSave = findViewById(R.id.btnSave)
        btnSave.setOnClickListener { saveSale() }

        itemAdapter = ItemSalesAdapter(emptyList(), totalTextView)
        recyclerViewItems.adapter = itemAdapter

        viewModel.products.observe(this) { products ->
            itemAdapter.products = products
            itemAdapter.notifyDataSetChanged()
        }
    }

    private fun saveSale() {
        val clientName = findViewById<EditText>(R.id.editTextClientName).text.toString()
        val saleDate = findViewById<EditText>(R.id.editTextDate).text.toString()
        val soldProducts = itemAdapter.soldProductsList.map { it.product to it.quantity }
        val total = totalTextView.text.toString()

        viewModel.saveSale(
            clientName,
            saleDate,
            total,
            soldProducts,
            {
                Toast.makeText(this, getString(R.string.save_sale), Toast.LENGTH_SHORT).show()
                finish()
            },
            { error ->
                Toast.makeText(this, getString(R.string.error_save_sale, error.toString()), Toast.LENGTH_SHORT).show()
                finish()
            }
        )
    }
}