package com.jessmobilesolutions.tradelink.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.R
import com.jessmobilesolutions.tradelink.models.Visit

class VisitsAdapter(var visits: List<Visit>) : RecyclerView.Adapter<VisitsAdapter.ViewHolder>() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxVisit)
        val clientName: TextView = itemView.findViewById(R.id.clientName)
        val clientPhone: TextView = itemView.findViewById(R.id.clientPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visit, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return visits.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val visit = visits[position]
        holder.clientName.text = visit.clientName
        holder.clientPhone.text = visit.clientPhone

        val visitRef = db.collection("users")
            .document(currentUser!!.uid)
            .collection("visits")
            .document(visit.uid)

        visitRef.get().addOnSuccessListener { documentSnapshot ->
            val visited = documentSnapshot.getBoolean("visited") ?: false
            holder.checkBox.isChecked = visited
        }.addOnFailureListener { exception ->
            Toast.makeText(holder.itemView.context, "Erro ao obter dados: ${exception.message}", Toast.LENGTH_SHORT)
                .show()
        }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            visitRef.update("visited", isChecked)
                .addOnSuccessListener {
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        holder.itemView.context,
                        "Erro ao salvar dados: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}