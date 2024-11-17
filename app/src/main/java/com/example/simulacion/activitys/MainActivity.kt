package com.example.simulacion.activitys

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.simulacion.R
import com.example.simulacion.modelos.DiasDeObservacion

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Solicitar permisos de almacenamiento
        checkStoragePermissions(this)

        // Referencia a los elementos de la interfaz
        val spinnerDiasDeObservacion: Spinner = findViewById(R.id.spinnerDiasDeObservacion)
        val buttonRegistrar: Button = findViewById(R.id.buttonRegistrar)

        // Configurar el Spinner con un adaptador
        val fechas = DiasDeObservacion.entries.map { it.fecha.toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fechas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDiasDeObservacion.adapter = adapter

        // Configurar el botón
        buttonRegistrar.setOnClickListener {
            val fechaSeleccionada = spinnerDiasDeObservacion.selectedItem.toString()
            val intent = Intent(this, Registro::class.java)
            intent.putExtra("fecha", fechaSeleccionada) // Agregar la fecha seleccionada como extra
            startActivity(intent)
        }
    }

    // Solicita permisos en tiempo de ejecución
    private fun checkStoragePermissions(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissions = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val permissionsToRequest = permissions.filter {
                ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
            }
            if (permissionsToRequest.isNotEmpty()) {
                ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), 100)
                false
            } else {
                true
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100
                )
                false
            } else {
                true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if ((grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED })) {
                Log.d("MainActivity", "Permisos concedidos")
            } else {
                Log.d("MainActivity", "Permisos denegados")
            }
        }
    }
}