package com.example.gestionenvios

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionenvios.databinding.ActivityRecepcionBinding

class RecepcionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecepcionBinding

    companion object{
        var contador = 0
        val MENSAJE_RECEPCION = "mensaje_recepcion"
        val ASEGURADO = "paquete_asegurado"
        val ORIGEN_MENU = "1"
        val CERTIFICADO = "certificado"
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
        else if(origen == CERTIFICADO){
            val tamanyo = intent.getStringExtra(CERTIFICADO)
            mensajeCertificado(tamanyo.toString())
        }
        else mensajeEnvioPaquete(origen)

    }

    private fun mensajeCertificado(tamanyo:String) {
        binding.tvMensaje.text = "Se ha recibido un certificado de tamanyo $tamanyo"
    }

    private fun mensajeEnvioPaquete(origen: String?) {
        binding.tvMensaje.text = "Se ha recibido un paquete de tamano $origen"
    }

    private fun mensajeDesdeMenu() {
        binding.tvMensaje.text = "Se han recibido un total de ${CorreoPostalActivity.contador} correos certificados"

    }

    fun onClickVolver(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}