package com.example.gestionenvios

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestionenvios.databinding.ActivityEnvioPaquetesBinding
import com.example.gestionenvios.databinding.ActivityMainBinding

class EnvioPaquetesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnvioPaquetesBinding
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                sendNotification()
            } else {
                // Permiso DENEGADO.
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
            }
        }
    val CHANNEL_ID = "mi_canal_principal"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEnvioPaquetesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

    }

    fun onVolverClick(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun valoresValidos(): Boolean {
        val peso = binding.editPeso.text.toString().toIntOrNull()

        // 1. Comprobación de que es un número válido.
        if (peso == null) {
            // Si es null, no es un número válido.
            return false
        }

        // 2. Comprobación del rango.
        return peso < 500
    }

    fun onEnviarClick(view: View){
        var campos = camposNulos()

        if(campos.any()){
            mostrarDialogoNulos(campos)

        } else if((!valoresValidos())){
            mostrarDialogosValores()

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Paso 1: Pidiendo permiso...", Toast.LENGTH_SHORT).show()
                askForNotificationPermission()
            } else {
                Toast.makeText(this, "Paso 2: Enviando Notificación...", Toast.LENGTH_SHORT).show()
                sendNotification()
            }
        }
    }

    private fun mostrarDialogosValores() {
        val mensaje = "El peso del paquete no puede superar los 500KG."
        AlertDialog.Builder(this).
                setTitle("Valores erroenos").
                setMessage(mensaje).
                setPositiveButton("Aceptar", {dialog, which ->
                    dialog.dismiss()
                }).show()
    }

    private fun mostrarDialogoNulos(camposNulos: List<String> ) {
        val listaFormateada = camposNulos.joinToString(separator = "\n-")
        val mensaje = "Debes rellenar los siguientes apartados: -$listaFormateada"

        AlertDialog.Builder(this).
                setTitle("Campos incompletos").
                setMessage(mensaje).
                setPositiveButton("Aceptar", { dialog, _ ->
                    dialog.dismiss()
                }).show()
    }
    private fun camposNulos(): List<String>{
        var camposVacios = mutableListOf<String>()

        var camposMap = mapOf(
            "Remitente" to binding.editRemitente,
            "Peso" to binding.editPeso,
            "Destino" to binding.editDestino,
            "Dimensiones" to binding.editDimensiones
        )

        for((nombre, editText) in camposMap){
            if(editText.text.isNullOrBlank()){
                camposVacios.add(nombre)
          }
      }

        return camposVacios

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal Principal"
            val descriptionText = "Notificaciones generales de la app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            // 1. Define el canal
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // 2. Registra el canal en el sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun sendNotification() {
        // --- 1. Primero, comprueba permisos (Android 13+) --
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            // Si no tenemos permiso, pedimos y salimos de la función
            askForNotificationPermission()
            return // No podemos continuar sin permiso
        }
        // --- 2. Define el Intent de navegación --
        // Este Intent dice "Quiero abrir DetailActivity"
        val intent = Intent(this, RecepcionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(RecepcionActivity.MENSAJE_RECEPCION, binding.editDimensiones.text.toString())
            putExtra(RecepcionActivity.ASEGURADO, binding.cbAsegurado.isChecked)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            10,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


            // --- 4. Construye la Notificación --
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_paquete) // ¡Obligatorio! Un icono pequeño
            .setContentTitle("¡Nuevo Mensaje!")
            .setContentText("Has recibido una nueva actualización.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // ¡La acción al pulsarla!
            .setAutoCancel(true) // Cierra la notificación al pulsarla
        // --- 5. Muestra la Notificación --
        Toast.makeText(this, "Paso 3: Notificación CREADA Y LLAMADA A NOTIFY.", Toast.LENGTH_LONG).show()
        with(NotificationManagerCompat.from(this)) {
            // notificationId es un ID único para esta notificación
            val notificationId = 1
            notify(notificationId, builder.build())


        }
    }
}