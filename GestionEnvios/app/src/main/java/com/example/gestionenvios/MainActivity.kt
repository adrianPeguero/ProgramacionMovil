package com.example.gestionenvios

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionenvios.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.recepcion ->{
                navegar(RecepcionActivity::class.java, RecepcionActivity.ORIGEN_MENU)
                true
            }

            R.id.envioPaquetes -> {
                navegar(EnvioPaquetesActivity::class.java, RecepcionActivity.ORIGEN_MENU)
                true
            }

            R.id.correo -> {
                navegar(CorreoPostalActivity::class.java, RecepcionActivity.ORIGEN_MENU)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun navegar(clase: Class<*>, origenMenu: String) {
        val intent = Intent(this, clase)
        intent.putExtra(RecepcionActivity.MENSAJE_RECEPCION, origenMenu)
        startActivity(intent)
    }
}