package com.jessmobilesolutions.tradelink.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.models.Product
import com.jessmobilesolutions.tradelink.models.Visit
import com.jessmobilesolutions.tradelink.utils.PhoneNumberFormattingTextWatcher
import com.jessmobilesolutions.tradelink.viewmodels.ClientRequestVisitViewModel
import java.util.UUID

class ClientRequestVisit : AppCompatActivity() {

    private lateinit var btnBackPage: ImageButton
    private lateinit var btnRequestVisit: Button
    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var clientRequestVisitViewModel: ClientRequestVisitViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_request_visit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
    }

    private fun setupView() {
        btnBackPage = findViewById(R.id.btnBack)
        btnBackPage.setOnClickListener {
            finish()
        }

        btnRequestVisit = findViewById(R.id.btnRequestVisit)
        editTextName = findViewById(R.id.editTextName)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextAddress = findViewById(R.id.editTextAddress)
        clientRequestVisitViewModel = ViewModelProvider(this)[ClientRequestVisitViewModel::class.java]

        btnRequestVisit.setOnClickListener {
            if (editTextName.text.isNullOrEmpty() || editTextPhone.text.isNullOrEmpty() || editTextAddress.text.isNullOrEmpty())
                Toast.makeText(this, getString(R.string.msg_fill_fields), Toast.LENGTH_SHORT).show()
            else {
                val companyId = intent.getStringExtra("companyId")
                val visitId = UUID.randomUUID().toString()
                val visit = Visit(
                    visitId,
                    companyId.toString(),
                    editTextName.text.toString(),
                    editTextPhone.text.toString(),
                    editTextAddress.text.toString(),
                    false
                )
                clientRequestVisitViewModel.createNewVisit(visit)

                Toast.makeText(this, getString(R.string.msg_confirm_request_visit), Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        val editText = findViewById<EditText>(R.id.editTextPhone)
        editText.addTextChangedListener(PhoneNumberFormattingTextWatcher(editText))
    }
}