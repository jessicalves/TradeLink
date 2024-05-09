package com.jessmobilesolutions.tradelink.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: Button
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setupView()
        observeLoginResult()
    }

    private fun setupView() {
        val loginType = intent.getStringExtra("login_type")
        val newRegister = findViewById<TextView>(R.id.textViewRegister)
        val resetPassword = findViewById<TextView>(R.id.textViewRecover)
        var intent = Intent(this, NewClientActivity::class.java)
        auth = Firebase.auth
        btnLogin = findViewById(R.id.btnLogin)
        email = findViewById(R.id.editTextEmailAddress)
        password = findViewById(R.id.editTextPassword)
        progressBar = findViewById(R.id.progressBar)
        firestore = FirebaseFirestore.getInstance()

        loginType?.let {
            when (it) {
                "client" -> {
                    findViewById<TextView>(R.id.textViewTitle).text = getString(R.string.text_view_client)
                    intent = Intent(this, NewClientActivity::class.java)
                }

                "representative" -> {
                    findViewById<TextView>(R.id.textViewTitle).text = getString(R.string.text_view_representative)
                    intent = Intent(this, NewRepresentativeActivity::class.java)
                }

                else -> {
                }
            }
        }
        newRegister.setOnClickListener {
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            login()
        }

        resetPassword.setOnClickListener { 
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(this) { success ->
            if (success) {
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
                            Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                        }
                }

            } else {
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            progressBar.visibility = View.GONE
        }
    }

    private fun login() {
        if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            viewModel.login(email.text.toString(), password.text.toString())
        } else {
            Toast.makeText(
                baseContext,
                getString(R.string.fill_all_fields),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}
