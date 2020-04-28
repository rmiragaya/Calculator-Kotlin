package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

private const val OPERATION = "operationPending"
private const val VALUE = "value"
private const val OPERAND_STORED = "Operand1_stored"


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
//    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }


    //variables that hold the operand and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            binding.newNumber.append(b.text)
        }

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()

            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                binding.newNumber.setText("")
            }

            pendingOperation = op
            binding.operation.text = pendingOperation
        }

        binding.apply {
            button0.setOnClickListener(listener)
            button1.setOnClickListener(listener)
            button2.setOnClickListener(listener)
            button3.setOnClickListener(listener)
            button4.setOnClickListener(listener)
            button5.setOnClickListener(listener)
            button6.setOnClickListener(listener)
            button7.setOnClickListener(listener)
            button8.setOnClickListener(listener)
            button9.setOnClickListener(listener)
            buttonDot.setOnClickListener(listener)

            buttonPlus.setOnClickListener(opListener)
            buttonMinus.setOnClickListener(opListener)
            buttonMult.setOnClickListener(opListener)
            buttonDiv.setOnClickListener(opListener)
            buttonEqual.setOnClickListener(opListener)

            buttonNeg.setOnClickListener(View.OnClickListener {
                val value = newNumber.text.toString()
                if (value.isEmpty()) {
                    newNumber.setText("-")
                } else {
                    try {
                        var doubleValue = value.toDouble()
                        doubleValue *= -1
                        newNumber.setText(doubleValue.toString())
                    } catch (e: NumberFormatException){
                        //number was "-"or ".", so clear it
                        newNumber.setText("")
                    }
                }
            })
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OPERATION, pendingOperation)
        if (operand1 != null) {
            outState.putDouble(VALUE, operand1!!)
            outState.putBoolean(OPERAND_STORED, true)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(OPERAND_STORED, false)) {
            savedInstanceState.getDouble(VALUE)
        } else {
            null
        }
        pendingOperation = savedInstanceState.getString(OPERATION, "").toString()
        binding.operation.text = pendingOperation

    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {

            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN // Handle attempt to divide by 0
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        binding.resultado.setText(operand1.toString())
        binding.newNumber.setText("")
    }

}
