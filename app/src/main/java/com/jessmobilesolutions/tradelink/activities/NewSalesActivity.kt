package com.jessmobilesolutions.tradelink.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jessmobilesolutions.tradelink.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewSalesActivity : AppCompatActivity() {
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
}