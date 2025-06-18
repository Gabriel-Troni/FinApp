package com.example.finappproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finappproject.data.DBHelper
import com.example.finappproject.data.Transaction

class StatementActivity : AppCompatActivity() {
    private lateinit var spinnerFilter: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewNoData: TextView
    private lateinit var textViewBalance: TextView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_statement)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        spinnerFilter = findViewById(R.id.spinnerFilter)
        recyclerView = findViewById(R.id.recyclerViewStatement)
        textViewNoData = findViewById(R.id.textViewNoData)
        textViewBalance = findViewById(R.id.textViewBalance)
        transactionAdapter = TransactionAdapter(emptyList())
        recyclerView.adapter = transactionAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        dbHelper = DBHelper(this)

        val filtros = listOf("Todos", "Crédito", "Débito")

        // Configurando os spinners
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filtros)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = adapter

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedFilter = parent.getItemAtPosition(position).toString()
                listTransactions(selectedFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("Spinner", "Nada selecionado")
            }
        }

        val balance: Double = dbHelper.consultBalance()
        textViewBalance.text = String.format("R$ %.2f", balance)
        listTransactions("Todos")
    }

    private fun listTransactions(operation: String) {
        val transactions = dbHelper.consultStatement(operation)
        Log.d("Transactions", transactions.toString())
        if (transactions.isEmpty()) {
            recyclerView.visibility = View.GONE
            textViewNoData.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            textViewNoData.visibility = View.GONE
            transactionAdapter.updateData(transactions)
        }
    }

    fun finish(view: View) {
        finish()
    }
}