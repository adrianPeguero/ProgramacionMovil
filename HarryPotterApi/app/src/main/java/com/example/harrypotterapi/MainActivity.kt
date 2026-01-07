package com.example.harrypotterapi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.harrypotterapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onGestionClicked(view: View){
        val intent = Intent(this, GestionActivity::class.java)
        startActivity(intent)
    }

    fun onCreacionClicked(view:View){
        val intent = Intent(this, VaritaActivity::class.java)
        startActivity(intent)
    }
}