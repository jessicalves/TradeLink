package com.jessmobilesolutions.tradelink.fragments

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.firebase.storage.FirebaseStorage
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.activities.CompanyProductsActivity
import com.jessmobilesolutions.tradelink.models.Product
import com.jessmobilesolutions.tradelink.viewmodels.ProductsCatalogViewModel
import java.util.UUID

class AddProductDialog : DialogFragment() {
    private val viewModel: ProductsCatalogViewModel by viewModels()
    private lateinit var etProductName: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var ivProductImage: ImageView
    private var productImageUri: Uri? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_product, null)
            etProductName = view.findViewById(R.id.etProductName)
            etProductPrice = view.findViewById(R.id.etProductPrice)
            ivProductImage = view.findViewById(R.id.ivProductImage)
            ivProductImage.setOnClickListener { openImageSelector() }

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

    private fun openImageSelector() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            productImageUri = data.data
            ivProductImage.setImageURI(productImageUri)
        }
    }

    private fun saveProduct() {
        val productName = etProductName.text.toString()
        val productPrice = etProductPrice.text.toString().toDoubleOrNull()

        if (productName.isNotEmpty() && productPrice != null && productImageUri != null) {
            val productId = UUID.randomUUID().toString()
            val product = Product(id = productId, name = productName, price = productPrice)
            val productsCatalogFragment = parentFragmentManager.fragments.firstOrNull { it is ProductsCatalogFragment } as? ProductsCatalogFragment
            productsCatalogFragment?.uploadImageAndAddProduct(productId, product, productImageUri!!)
            Toast.makeText(context, getString(R.string.msg_save), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, getString(R.string.msg_fill_all_fields), Toast.LENGTH_SHORT).show()
        }
    }
}
