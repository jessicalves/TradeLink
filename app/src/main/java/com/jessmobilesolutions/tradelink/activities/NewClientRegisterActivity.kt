package com.jessmobilesolutions.tradelink.activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.models.Client

class NewClientRegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dataBase: FirebaseFirestore
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
        setContentView(R.layout.activity_new_client_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
    }

    private fun setupView() {
        auth = FirebaseAuth.getInstance()
        dataBase = FirebaseFirestore.getInstance()
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
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val user = hashMapOf(
                        "uid" to uid,
                        "email" to email.text.toString(),
                        "name" to name.text.toString(),
                        "phone" to phone.text.toString(),
                        "city" to city.text.toString(),
                        "state" to state.text.toString(),
                        "userType" to "client"
                    )
                    uid?.let {
                        dataBase.collection("users")
                            .document(it)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot added with ID: $it")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                    }
                    Toast.makeText(this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()

                } else {
                    Toast.makeText(this, "Falha ao criar usuário: ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}