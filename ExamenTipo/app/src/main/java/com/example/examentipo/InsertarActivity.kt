package com.example.examentipo

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import com.example.examentipo.databinding.ActivityInsertarBinding
import com.example.examentipo.databinding.ActivityMainBinding

class InsertarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertarBinding
    private var colorSeleccioado = null
    val spinner : Spinner = findViewById(R.id.spinnerColores)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        anyadirColores()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) return
                val editText = EditText(this)
                editText.hint = "Mensaje"
                editText.textSize = 16f

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                params.setMargins(0, 30, 0, 0)
                editText.layoutParams = params

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

    }



    private fun anyadirColores() {
        val colores = listOf<String>(
            "Seleccione",
            "Rojo",
            "Verde",
            "Azul",
            "Amarillo"
        )


        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            colores
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

    }
}