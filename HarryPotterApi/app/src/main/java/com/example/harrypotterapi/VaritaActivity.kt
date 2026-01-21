package com.example.harrypotterapi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.harrypotterapi.adapter.VaritaAdapter
import com.example.harrypotterapi.data.ApiService
import com.example.harrypotterapi.data.CrearVarita
import com.example.harrypotterapi.data.Varita
import com.example.harrypotterapi.databinding.ActivityVaritaBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VaritaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVaritaBinding
    private var varitaSeleccionada: Varita? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaritaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tbPrincipal.setNavigationOnClickListener { view ->
            mostrarMenuNavegacion(view)
        }

        recogerVarita()
        if(varitaSeleccionada !=  null){
            rellenarCampos()
            binding.cbRota.isEnabled = false
            binding.btnCrearVarita.visibility = View.INVISIBLE
        }

    }

    private fun rellenarCampos() {
        binding.tvMadera.setText(varitaSeleccionada?.materiales?.split(". ")?.first())
        binding.tvMago.setText(varitaSeleccionada?.mago)
        binding.tvNucleo.setText(varitaSeleccionada?.materiales?.split(". ")?.last())
        binding.tvLongitud.setText(varitaSeleccionada?.longitud.toString())
        if(varitaSeleccionada?.rota.equals("Si")) binding.cbRota.isChecked = true
    }

    fun recogerVarita(){
        var varitaJson = intent.getStringExtra("varita_seleccionada")
        varitaSeleccionada = Gson().fromJson(varitaJson, Varita::class.java)
        println(varitaSeleccionada)
    }

    private fun getRetrofit(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicio = retrofit.create(ApiService::class.java)
        return servicio
    }

    fun onClickRomper(view:View){
        if(binding.cbRota.isChecked){ //Hacer cardView o aglo asi
            Toast.makeText(this@VaritaActivity, "La varita ya esta rota", Toast.LENGTH_SHORT).show()
            return
        }
        val servicio = getRetrofit()

        val id = varitaSeleccionada?.id ?:return
        lifecycleScope.launch (Dispatchers.IO) {
            try{
                val respuesta = servicio.romperVarita(id)
                if(respuesta.isSuccessful){
                    val varitaActualizada = respuesta.body()
                    withContext(Dispatchers.Main){
                        varitaSeleccionada = varitaActualizada
                        binding.cbRota.isChecked = true
                        Toast.makeText(this@VaritaActivity, "Varita actualizada.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@VaritaActivity, "Error al romper.", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    fun onClickCrear(view:View){
        var campos = getCampos()
        if(HayCamposNulos(campos).isNotEmpty()){
            Toast.makeText(this@VaritaActivity, "Debes darle valor a todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        val madera = binding.tvMadera.text.toString()
        val nucelo = binding.tvNucleo.text.toString()
        val longitud = binding.tvLongitud.text.toString().toDouble()
        val mago = binding.tvMago.text.toString()
        val rota = binding.cbRota.isChecked

        var varita = CrearVarita(madera, nucelo, longitud, rota, mago)
        val servicio = getRetrofit()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("API_TEST", "Iniciando llamada a la API...")
                val respuesta = servicio.crearVarita(varita)

                withContext(Dispatchers.Main) {
                    if (respuesta.isSuccessful) {
                        Log.d("API_TEST", "¡Éxito! Limpiando campos...")
                        limpiarInterfaz()
                        Toast.makeText(this@VaritaActivity, "Varita creada!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("API_TEST", "Excepción capturada: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@VaritaActivity, "Fallo total: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun limpiarInterfaz() {
        binding.apply {
            tvMadera.text.clear()
            tvNucleo.text.clear()
            tvLongitud.text.clear()
            tvMago.text.clear()
            cbRota.isChecked = false
        }
    }


    private fun HayCamposNulos(campos: List<String>): List<String> {
        var camposNulos = mutableListOf<String>()
        for(c in campos){
            if(c.isEmpty()){
                camposNulos.add(c)
            }
        }

        return camposNulos
    }
    private fun getCampos(): List<String> {
        return listOf(
            binding.tvMadera.text.toString(),
            binding.tvNucleo.text.toString(),
            binding.tvLongitud.toString(),
            binding.tvMago.toString()
        )
    }

    private fun mostrarMenuNavegacion(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_varita, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_main -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.item_gestion -> {
                    startActivity(Intent(this, GestionActivity::class.java))
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}


