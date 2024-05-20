package com.jessmobilesolutions.tradelink.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.activities.LoginActivity
import com.jessmobilesolutions.tradelink.adapters.ProductsAdapter
import com.jessmobilesolutions.tradelink.models.Product
import com.jessmobilesolutions.tradelink.viewmodels.ProductsCatalogViewModel

class ProductsCatalogFragment : Fragment() {
    private val viewModel: ProductsCatalogViewModel by viewModels()
    private lateinit var productsAdapter: ProductsAdapter
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_products_catalog, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        productsAdapter = ProductsAdapter(emptyList())
        recyclerView.adapter = productsAdapter

        viewModel.products.observe(viewLifecycleOwner) { products ->
            productsAdapter.products = products
            productsAdapter.notifyDataSetChanged()
        }

        view.findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            val dialog = AddProductDialog()
            dialog.show(requireFragmentManager(), "AddProductDialog")
        }

        view.findViewById<ImageButton>(R.id.btnLogout).setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    fun uploadImageAndAddProduct(productId: String, product: Product, productImageUri: Uri) {
        val ref = FirebaseStorage.getInstance().getReference("/products/$productId")
        ref.putFile(productImageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    product.image = it.toString()
                    viewModel.addProduct(product)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }
}