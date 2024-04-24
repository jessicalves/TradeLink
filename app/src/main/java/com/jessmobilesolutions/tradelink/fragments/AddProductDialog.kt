package com.jessmobilesolutions.tradelink.fragments

import android.app.Dialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.models.Product
import com.jessmobilesolutions.tradelink.viewmodels.ProductsCatalogViewModel

class AddProductDialog : DialogFragment() {
    private val viewModel: ProductsCatalogViewModel by viewModels()
    private lateinit var etProductName: EditText
    private lateinit var etProductPrice: EditText
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_product, null)
            etProductName = view.findViewById(R.id.etProductName)
            etProductPrice = view.findViewById(R.id.etProductPrice)

            builder.setView(view)
                .setTitle(getString(R.string.add_product))
                .setPositiveButton(getString(R.string.btn_save)) { dialog, id ->
                    saveProduct()
                }
                .setNegativeButton(getString(R.string.btn_cancel)) { dialog, id ->
                    dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun saveProduct() {
        val productName = etProductName.text.toString()
        val productPrice = etProductPrice.text.toString().toDoubleOrNull()

        if (productName.isNotEmpty() && productPrice != null) {
            val product = Product(name = productName, price = productPrice)
            viewModel.addProduct(product)
            Toast.makeText(context, getString(R.string.msg_save), Toast.LENGTH_SHORT).show()
            dismiss()
        } else {
            Toast.makeText(context, getString(R.string.msg_fill_all_fields), Toast.LENGTH_SHORT).show()
        }
    }
}
