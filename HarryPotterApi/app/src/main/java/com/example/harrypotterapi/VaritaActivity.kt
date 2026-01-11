package com.example.harrypotterapi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.harrypotterapi.adapter.VaritaAdapter
import com.example.harrypotterapi.data.ApiService
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
            binding.btnCrearVarita.visibility = View.INVISIBLE
        }

    }

    private fun rellenarCampos() {
        binding.tvMadera.setText(varitaSeleccionada?.materiales?.split(". ")?.first())
        binding.tvMago.setText(varitaSeleccionada?.mago)
        binding.tvNucleo.setText(varitaSeleccionada?.materiales?.split(". ")?.last())
        binding.tvLongitud.setText(varitaSeleccionada?.longitud.toString())
        if(varitaSeleccionada?.rota.equals("true")) binding.cbRota.isChecked = true
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
                    Toast.makeText(this@VaritaActivity, "Varita actualizada.", Toast.LENGTH_SHORT).show()
                }
            }

        }

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


