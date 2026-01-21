package com.example.examentipo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.examentipo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickInsertar(view: View){
        val intent = Intent(this, InsertarActivity::class.java)
    }

//    fun onClickNotificacion(view: View){
//        val intent = Intent(this, )
//    }
//
//    fun onClickDibujo(view: View){
//        val intent = Intent(this,)
//    }
}