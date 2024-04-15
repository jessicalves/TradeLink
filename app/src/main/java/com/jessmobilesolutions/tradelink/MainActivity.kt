package com.jessmobilesolutions.tradelink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAnimation()
        setupView()
    }

    private fun setupAnimation() {
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
        val animationView = findViewById<LottieAnimationView>(R.id.animation_view)
        val animationRes = if (isNightMode) R.raw.animation_night else R.raw.animation_light
        animationView.setAnimation(animationRes)
    }

    private fun setupView() {
        val btnClient = findViewById<Button>(R.id.btnClient)
        val btnRepresentative = findViewById<Button>(R.id.btnRepresentative)
        val loginIntent = Intent(this, LoginActivity::class.java)
        
        btnClient.setOnClickListener {
            loginIntent.putExtra("login_type", "client")
            startActivity(loginIntent)
        }

        btnRepresentative.setOnClickListener {
            loginIntent.putExtra("login_type", "representative")
            startActivity(loginIntent)
        }
    }
}