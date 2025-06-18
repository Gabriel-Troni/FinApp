package com.example.finappproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finappproject.data.Transaction
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.text.format

class TransactionAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewOperation: ImageView = itemView.findViewById(R.id.imageViewOperation)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewValue: TextView = itemView.findViewById(R.id.textViewValue)
        val textViewOperation: TextView = itemView.findViewById(R.id.textViewOperation)
        val textViewTimestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return TransactionViewHolder(itemView)
    }

    // Substitui o conteúdo de uma view (invocado pelo layout manager)
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentTransaction = transactions[position]
        val instant = Instant.ofEpochMilli(currentTransaction.timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        if (currentTransaction.operation == "Crédito") {
            holder.imageViewOperation.setImageResource(R.drawable.credito)
        } else {
            holder.imageViewOperation.setImageResource(R.drawable.debito)
        }
        holder.textViewDescription.text = currentTransaction.description
        holder.textViewValue.text = String.format("R$ %.2f", currentTransaction.value)
        holder.textViewOperation.text = currentTransaction.operation
        holder.textViewTimestamp.text = localDateTime.format(formatter)
}

    override fun getItemCount() = transactions.size

    fun updateData(newTransactions: List<Transaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}