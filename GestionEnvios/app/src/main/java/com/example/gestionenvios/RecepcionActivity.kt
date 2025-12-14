package com.example.gestionenvios

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.gestionenvios.databinding.ActivityRecepcionBinding

class RecepcionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecepcionBinding

    companion object{
        var contador = 0
        val MENSAJE_RECEPCION = "mensaje_recepcion"
        val ASEGURADO = "paquete_asegurado"
        val ORIGEN_MENU = "1"
        var ORIGEN_NOTIFICACION = "2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecepcionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actualizarMensaje()
        notificarAsegurado()
    }

    private fun notificarAsegurado() {
        val intent = intent
        val asegurado = intent.getBooleanExtra(ASEGURADO, false)
        if(asegurado) mostrarDialogo()
    }

    private fun mostrarDialogo() {
        AlertDialog.Builder(this)
            .setTitle("Paquete asegurado")
            .setMessage("El paquete se encuentra asegurado")
            .setPositiveButton("Aceptar", {dialog, which ->
                dialog.dismiss()
            }).show()
    }

    private fun actualizarMensaje() {
        val intent = intent
        val origen = intent.getStringExtra(MENSAJE_RECEPCION)
        if(origen == ORIGEN_MENU) mensajeDesdeMenu()
        else mensajeDesdeNotificacion(origen)

    }

    private fun mensajeDesdeNotificacion(origen: String?) {
        binding.tvMensaje.text = "Se ha recibido un paquete certificado de tamano $origen"
    }

    private fun mensajeDesdeMenu() {
        binding.tvMensaje.text = "Se han recibido un total de ${CorreoPostalActivity.contador} correos certificados"

    }

    fun onClickVolver(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}