package com.example.harrypotterapi

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.harrypotterapi.data.ApiService
import com.example.harrypotterapi.data.Varita
import com.example.harrypotterapi.databinding.ActivityMainBinding
import com.example.harrypotterapi.databinding.ActivityVaritaBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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
        binding.tvMadera.setText(varitaSeleccionada?.madera)
        binding.tvMago.setText(varitaSeleccionada?.mago)
        binding.tvNucleo.setText(varitaSeleccionada?.nucleo)
        binding.tvLongitud.setText(varitaSeleccionada?.longitud.toString())
        binding.cbRota.isChecked = varitaSeleccionada?.rota ?:false
    }

    fun recogerVarita(){
        var varitaJson = intent.getStringExtra("varita_seleccionada")
        varitaSeleccionada = Gson().fromJson(varitaJson, Varita::class.java)
    }

    private fun getRetrofit(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicio = retrofit.create(ApiService::class.java)
        return servicio
    }

//    fun onClickRomper(view:View){
//        val servicio = getRetrofit()
//
//        lifecycleScope.launch (Dispatchers.IO) {
//            try{
//                val respuesta = servicio.romperVarita(varitaSeleccionada.)
//            }
//
//        }
//
//    }

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


