package com.jessmobilesolutions.tradelink.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.viewmodels.NewClientViewModel
import com.jessmobilesolutions.tradelink.viewmodels.NewRepresentativeViewModel

class NewRepresentativeActivity : AppCompatActivity() {

    private lateinit var viewModel: NewRepresentativeViewModel
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var name: EditText
    private lateinit var company: EditText
    private lateinit var niche: EditText
    private lateinit var city: EditText
    private lateinit var state: EditText
    private lateinit var phone: EditText
    private lateinit var btnRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_representative)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[NewRepresentativeViewModel::class.java]
        setupView()
    }

    private fun setupView() {
        email = findViewById(R.id.editTextEmail)
        password = findViewById(R.id.editTextPassword)
        name = findViewById(R.id.editTextName)
        city = findViewById(R.id.editTextCity)
        company = findViewById(R.id.editTextRepresentedCompany)
        niche = findViewById(R.id.editTextNiche)
        state = findViewById(R.id.editTextState)
        phone = findViewById(R.id.editTextPhone)
        btnRegister = findViewById(R.id.btnNewRegister)
        btnRegister.setOnClickListener {
            createNewUser()
        }
    }

    private fun createNewUser() {
        viewModel.createNewUser(
            email.text.toString(),
            password.text.toString(),
            name.text.toString(),
            company.text.toString(),
            niche.text.toString(),
            city.text.toString(),
            state.text.toString(),
            phone.text.toString(),
            onSuccess = {
                Toast.makeText(this, getString(R.string.user_created), Toast.LENGTH_SHORT).show()
                finish()
            },
            onFailure = { message ->
                Toast.makeText(this, getString(R.string.failed_create_user, message), Toast.LENGTH_SHORT).show()
            }
        )
    }
}