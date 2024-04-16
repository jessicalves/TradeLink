package com.jessmobilesolutions.tradelink.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.jessmobilesolutions.tradelink.R

class LoginActivity : AppCompatActivity() {
    private lateinit var newRegister: TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupView()
    }

    private fun setupView() {
        val loginType = intent.getStringExtra("login_type")
        val newRegister = findViewById<TextView>(R.id.textViewRegister)
        var intent = Intent(this, NewClientActivity::class.java)

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
    }

}