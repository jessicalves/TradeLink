package com.jessmobilesolutions.tradelink.activities

import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
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
    private lateinit var progressBar: ProgressBar
    private lateinit var togglePasswordVisibilityButton: ImageButton

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
        progressBar = findViewById(R.id.progressBar)
        togglePasswordVisibilityButton = findViewById(R.id.togglePasswordVisibility)

        btnRegister.setOnClickListener {
            if (validateFields()) {
                showProgressBar()
                createNewUser()
            } else {
                Toast.makeText(this,  getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }

        togglePasswordVisibilityButton.setOnClickListener {
            val isVisible = password.transformationMethod == HideReturnsTransformationMethod.getInstance()
            if (isVisible) {
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility_off)
            } else {
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility)
            }
            val position = password.text.length
            password.text?.let {
                Selection.setSelection(it as Spannable?, position)
            }
        }
    }

    private fun validateFields(): Boolean {
        return email.text.isNotBlank() &&
                password.text.isNotBlank() &&
                name.text.isNotBlank() &&
                city.text.isNotBlank() &&
                state.text.isNotBlank() &&
                phone.text.isNotBlank()
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
                hideProgressBar()
                Toast.makeText(this, getString(R.string.user_created), Toast.LENGTH_SHORT).show()
                finish()
            },
            onFailure = { message ->
                hideProgressBar()
                Toast.makeText(this, getString(R.string.failed_create_user, message), Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}

