package com.jessmobilesolutions.tradelink.activities

import android.content.Intent
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: Button
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var togglePasswordVisibilityButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        observeLoginResult()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        applyWindowInsets(findViewById(R.id.main))

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        btnLogin = findViewById(R.id.btnLogin)
        email = findViewById(R.id.editTextEmailAddress)
        password = findViewById(R.id.editTextPassword)
        togglePasswordVisibilityButton = findViewById(R.id.togglePasswordVisibility)
        progressBar = findViewById(R.id.progressBar)

        val newRegister = findViewById<TextView>(R.id.textViewRegister)
        val resetPassword = findViewById<TextView>(R.id.textViewRecover)

        val loginType = intent.getStringExtra("login_type")
        loginType?.let {
            when (it) {
                "client" -> {
                    findViewById<TextView>(R.id.textViewTitle).text = getString(R.string.text_view_client)
                }
                "representative" -> {
                    findViewById<TextView>(R.id.textViewTitle).text = getString(R.string.text_view_representative)
                }
            }
        }

        newRegister.setOnClickListener {
            startActivity(Intent(this, NewClientActivity::class.java))
        }

        btnLogin.setOnClickListener {
            login()
        }

        resetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        togglePasswordVisibilityButton.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun applyWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(this) { success ->
            progressBar.visibility = View.GONE
            if (success) {
                navigateToHomePage()
            } else {
                showToast(getString(R.string.authentication_failed))
            }
        }
    }

    private fun login() {
        if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            viewModel.login(email.text.toString(), password.text.toString())
        } else {
            showToast(getString(R.string.fill_all_fields))
        }
    }

    private fun togglePasswordVisibility() {
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

    private fun navigateToHomePage() {
        val user = viewModel.getCurrentUser()
        user?.let { user ->
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userType = document.getString("userType")
                        userType?.let { type ->
                            val homeIntent = if (type == "client") {
                                Intent(this, CompanyActivity::class.java)
                            } else {
                                Intent(this, RepresentativeActivity::class.java)
                            }
                            startActivity(homeIntent)
                        }
                    }
                }
                .addOnFailureListener {
                    showToast("Failed to fetch user details")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
