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

class NewClientActivity : AppCompatActivity() {
    private lateinit var viewModel: NewClientViewModel
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var name: EditText
    private lateinit var city: EditText
    private lateinit var state: EditText
    private lateinit var phone: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_client)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[NewClientViewModel::class.java]
        setupView()
    }

    private fun setupView() {
        email = findViewById(R.id.editTextEmail)
        password = findViewById(R.id.editTextPassword)
        name = findViewById(R.id.editTextName)
        city = findViewById(R.id.editTextCity)
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