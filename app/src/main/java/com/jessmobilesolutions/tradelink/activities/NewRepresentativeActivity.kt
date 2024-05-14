package com.jessmobilesolutions.tradelink.activities

import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.Spannable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.ViewGroup
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
import com.jessmobilesolutions.tradelink.viewmodels.NewRepresentativeViewModel

class NewRepresentativeActivity : AppCompatActivity() {

    private lateinit var viewModel: NewRepresentativeViewModel
    private lateinit var email: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var name: EditText
    private lateinit var company: EditText
    private lateinit var niche: EditText
    private lateinit var city: EditText
    private lateinit var state: EditText
    private lateinit var phone: EditText
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var togglePasswordVisibilityButton: ImageButton
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

    private fun validateFields(): Boolean {
        val password = editTextPassword.text.trim()
        return email.text.isNotBlank() &&
                password.length >= 6 &&
                password.matches(Regex(".*\\d.*")) &&
                name.text.isNotBlank() &&
                city.text.isNotBlank() &&
                company.text.isNotBlank() &&
                niche.text.isNotBlank() &&
                state.text.isNotBlank() &&
                phone.text.isNotBlank()
    }


    private fun setupView() {
        email = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        name = findViewById(R.id.editTextName)
        city = findViewById(R.id.editTextCity)
        company = findViewById(R.id.editTextRepresentedCompany)
        niche = findViewById(R.id.editTextNiche)
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
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }
        togglePasswordVisibilityButton.setOnClickListener {
            val isVisible = editTextPassword.transformationMethod == HideReturnsTransformationMethod.getInstance()
            if (isVisible) {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility_off)
            } else {
                editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility)
            }
            val position = editTextPassword.text.length
            editTextPassword.text?.let {
                Selection.setSelection(it as Spannable?, position)
            }
        }

        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString().trim()
                if (password.length < 6 || !password.matches(Regex(".*\\d.*"))) {
                    editTextPassword.error = getString(R.string.password_requirements)
                    val marginEndPixels = (35 * resources.displayMetrics.density).toInt()
                    val params = togglePasswordVisibilityButton.layoutParams as ViewGroup.MarginLayoutParams
                    params.marginEnd = marginEndPixels
                    togglePasswordVisibilityButton.layoutParams = params
                } else {
                    editTextPassword.error = null
                    val marginEndPixels = (16 * resources.displayMetrics.density).toInt()
                    val params = togglePasswordVisibilityButton.layoutParams as ViewGroup.MarginLayoutParams
                    params.marginEnd = marginEndPixels
                    togglePasswordVisibilityButton.layoutParams = params

                }
            }
        })
    }

    private fun createNewUser() {
        viewModel.createNewUser(
            email.text.toString(),
            editTextPassword.text.toString(),
            name.text.toString(),
            company.text.toString(),
            niche.text.toString(),
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