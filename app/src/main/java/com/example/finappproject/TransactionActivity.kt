package com.example.finappproject

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finappproject.data.DBHelper

class TransactionActivity : AppCompatActivity() {
    private lateinit var radioButtonCredit: RadioButton
    private lateinit var radioButtonDebit: RadioButton
    private lateinit var editTextDescription: EditText
    private lateinit var editTextValue: EditText
    private lateinit var btnFinish: Button
    private lateinit var btnCancel: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaction)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        radioButtonDebit = findViewById(R.id.radioButtonDebit)
        radioButtonCredit = findViewById(R.id.radioButtonCredit)
        editTextDescription = findViewById(R.id.editTextDescription)
        editTextValue = findViewById(R.id.editTextValue)
        btnFinish = findViewById(R.id.btnFinish)
        btnCancel = findViewById(R.id.btnCancel)
        dbHelper = DBHelper(this)
    }

    fun insertTransaction(view: View) {
        val operation = if (radioButtonCredit.isChecked) "Crédito" else "Débito"
        val description = editTextDescription.text.toString()

        if (editTextValue.text.toString().isEmpty()) {
            editTextValue.error = "O campo valor não pode estar vazio"
            return
        }

        var value = editTextValue.text.toString().toDouble()

        if (value == 0.0) {
            editTextValue.error = "O valor deve ser maior que 0"
            return
        }
        val balance = dbHelper.consultBalance()
        if (radioButtonDebit.isChecked && balance < value) {
            editTextValue.error = "Saldo insuficiente"
            return
        }

        value = if (radioButtonDebit.isChecked) -value else value

        dbHelper.insertTransaction(operation, description, value)

        Toast.makeText(this, "Transação realizada com sucesso", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun cancel(view: View) {
        finish()
    }
}