package com.jessmobilesolutions.tradelink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.activities.CompanyActivity
import com.jessmobilesolutions.tradelink.activities.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
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

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            redirectToHome()
        }
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
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        
        btnClient.setOnClickListener {
            loginIntent.putExtra("login_type", "client")
            startActivity(loginIntent)
        }

        btnRepresentative.setOnClickListener {
            loginIntent.putExtra("login_type", "representative")
            startActivity(loginIntent)
        }
    }

    private fun redirectToHome() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userType = document.getString("userType")
                        userType?.let { type ->
                            val homeIntent = if (type == "client") {
                                Intent(this, CompanyActivity::class.java)
                            } else {
                                Intent(this, CompanyActivity::class.java)
                            }
                            startActivity(homeIntent)
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                }
        }
    }
}