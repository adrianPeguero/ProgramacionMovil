package com.example.gestionenvios

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.gestionenvios.databinding.ActivityCorreoPostalBinding

class CorreoPostalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCorreoPostalBinding
    private lateinit var spinnerTamanyo: Spinner
    private lateinit var cardViewResultado: CardView
    private lateinit var tvOpcionSeleccionada: TextView

    companion object{
        var contador = 0
    }

    private var opcionesTamanyo = listOf<String>(
                "Pequeno: 10*17",
                "Mediano: 15*25",
                "Grande: 25*45",
                )

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
        binding = ActivityCorreoPostalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerTamanyo = binding.spinnerTamanyo
        cardViewResultado = binding.cvTamanyo
        tvOpcionSeleccionada = binding.tvOpcionSeleccionada
        var tamanyosPosibles = getTamanyos()

        initializeSpinner(tamanyosPosibles, spinnerTamanyo)
        setUpSpinner()

    }

    private fun setUpSpinner() {
        spinnerTamanyo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemSeleccionado = opcionesTamanyo[position]
                tvOpcionSeleccionada.text = "Tamanyo: $itemSeleccionado"
                cardViewResultado.visibility = View.VISIBLE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                cardViewResultado.visibility = View.GONE
            }
        }
    }

    private fun initializeSpinner(tamanyos:List<String>, spinner: Spinner) {
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            tamanyos
        )
       spinner.adapter = adaptador
    }

    private fun getTamanyos(): List<String> {
        return listOf<String>(
            "Pequeno: 10*17",
            "Mediano: 15*25",
            "Grande: 25*45",
            )
    }

    fun onClickVolver(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickEnviar(view: View){
        var campos = camposNulos()

        if(campos.any()){
            mostrarDialogoNulos(campos)

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Paso 1: Pidiendo permiso...", Toast.LENGTH_SHORT).show()
                askForNotificationPermission()
            } else {
                Toast.makeText(this, "Paso 2: Enviando Notificación...", Toast.LENGTH_SHORT).show()
                contador++
                sendNotification()

            }
        }
    }

    private fun mostrarDialogoNulos(campos: List<String>) {
        val listaCamposVacios = campos.joinToString(separator = "\n-")
        val mensaje = "Debes rellenar los siguientes campos: $listaCamposVacios"

        AlertDialog.Builder(this)
            .setTitle("Campos vacios")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", { dialog, which ->
                dialog.dismiss()
            }).show()

    }

    private fun camposNulos(): List<String> {
        var camposVacios = mutableListOf<String>()

        var camposTexto = mapOf(
            "Destino" to binding.editDestino,
            "Remitente" to binding.editRemitente
        )

        for((nombre, editText) in camposTexto){
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
            putExtra(RecepcionActivity.MENSAJE_RECEPCION, tvOpcionSeleccionada.text.toString().split(": ").last())
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


