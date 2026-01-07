package com.example.harrypotterapi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.harrypotterapi.adapter.VaritaAdapter
import com.example.harrypotterapi.data.ApiService
import com.example.harrypotterapi.data.Varita
import com.example.harrypotterapi.databinding.ActivityGestionBinding
import com.example.harrypotterapi.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbBack)
        supportActionBar?.setDisplayShowTitleEnabled(false) //Quita el titulo predeterminado (HarryPotterApi)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        inicializarLista()
        var varitas = binding.lvVaritas
        varitas.setOnItemClickListener { parent, view, position, id ->
            val varitaSeleccionada = parent.getItemAtPosition(position)
            lanzarVaritaActivity(varitaSeleccionada)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    private fun inicializarLista() {
        recuperarVaritas()
        //VaritaAdapter en vez de ArrayAdapter
    }

    private fun recuperarVaritas(){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicio = retrofit.create(ApiService::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            delay(1000)
            try{
                val respuesta = servicio.getVaritas()
                Log.e("API_ERROR", "Codigo de error ${respuesta.code()}")
                if(respuesta.isSuccessful){
                    val datos = respuesta.body()?.toMutableList() ?: mutableListOf()
                    withContext(Dispatchers.Main){
                        val adapter = VaritaAdapter(this@GestionActivity,R.layout.item_mago , datos)
                        binding.lvVaritas.adapter = adapter
                    }
                }
                else{
                    Toast.makeText(this@GestionActivity, "Error en la peticion de datos", Toast.LENGTH_SHORT).show()
                }
            }
            catch (e: Exception){
            }
        }
    }

    private fun lanzarVaritaActivity(varitaSeleccionada: Any) {
        var varita: Varita = varitaSeleccionada as Varita

        val intent = Intent(this, VaritaActivity::class.java)

        val varitaJson = obtenerJson(varita)

        intent.putExtra("varita_seleccionada", varitaJson)
        
        startActivity(intent)

    }

    private fun obtenerJson(varita: Varita): String? {
        val gson = Gson()
        return gson.toJson(varita)
    }

}


