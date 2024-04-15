package com.jessmobilesolutions.tradelink

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView

class LoginActivity : AppCompatActivity() {
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
    
    private fun setupView(){
        val loginType = intent.getStringExtra("login_type")
        if (loginType != null) {
            val textViewTitle = findViewById<TextView>(R.id.textViewTitle)
            if (loginType == "client") {
                textViewTitle.text = getString(R.string.text_view_client)
            } else if (loginType == "representative") {
                textViewTitle.text = getString(R.string.text_view_representative)
            }
        }
    }
}