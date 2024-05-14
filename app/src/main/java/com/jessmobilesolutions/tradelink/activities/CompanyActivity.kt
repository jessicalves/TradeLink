package com.jessmobilesolutions.tradelink.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.adapters.RepresentativeAdapter
import com.jessmobilesolutions.tradelink.viewmodels.CompanyViewModel

class CompanyActivity : AppCompatActivity() {

    private lateinit var viewModel: CompanyViewModel
    private lateinit var adapter: RepresentativeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupViewModel()
        setupRecyclerView()
        setupSearchFunctionality()
        setupLogoutButton()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        setContentView(R.layout.activity_company)
        applyWindowInsets(findViewById(R.id.main))
    }

    private fun applyWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(CompanyViewModel::class.java)
        viewModel.loadRepresentatives()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL))

        adapter = RepresentativeAdapter(emptyList()) { openProductsForCompany(it.uid, it.representedCompany) }
        recyclerView.adapter = adapter
    }

    private fun setupSearchFunctionality() {
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterRepresentatives(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.representatives.observe(this) { representatives ->
            adapter.updateList(representatives)
        }
    }

    private fun setupLogoutButton() {
        val logoutButton: ImageButton = findViewById(R.id.btnLogout)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun openProductsForCompany(companyId: String, companyName: String) {
        val intent = Intent(this, CompanyProductsActivity::class.java)
        intent.putExtra("companyId", companyId)
        intent.putExtra("companyName", companyName)
        startActivity(intent)
    }
}
