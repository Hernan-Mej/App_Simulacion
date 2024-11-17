package com.example.simulacion.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simulacion.R
import com.example.simulacion.modelos.DiasDeObservacion
import com.example.simulacion.modelos.FlujoVehicular
import com.example.simulacion.modelos.TipoVehiculo
import com.example.simulacion.services.ColasService
import java.time.LocalTime

class AgregarVehiculo : AppCompatActivity() {

    private lateinit var spinnerTipoVehiculo: Spinner
    private lateinit var editTextPlaca: EditText
    private lateinit var buttonAgregarVehiculo: Button
    private val servicio = ColasService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fechaSeleccionada = intent.getStringExtra("fecha")
        Log.d("Registro", "Fecha seleccionada: $fechaSeleccionada")
        if (fechaSeleccionada != null) {
            val dia = DiasDeObservacion.entries.find { it.fecha.toString() == fechaSeleccionada }
            if (dia != null) {
                this.servicio.setDia(dia)
            } else {
                Log.e("Registro", "No enum constant for date: $fechaSeleccionada")
            }
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_vehiculo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo)
        editTextPlaca = findViewById(R.id.editTextPlaca)
        buttonAgregarVehiculo = findViewById(R.id.buttonAgregarVehiculo)

        // Configurar el Spinner
        val tiposVehiculo = TipoVehiculo.entries.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposVehiculo)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoVehiculo.adapter = adapter

        // Cargar la selección guardada
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val savedTipoVehiculo = sharedPreferences.getString("tipoVehiculo", null)
        if (savedTipoVehiculo != null) {
            val position = tiposVehiculo.indexOf(savedTipoVehiculo)
            if (position >= 0) {
                spinnerTipoVehiculo.setSelection(position)
            }
        }

        // Manejar el clic del botón
        buttonAgregarVehiculo.setOnClickListener {
            val tipoVehiculo = spinnerTipoVehiculo.selectedItem.toString()
            val placa = editTextPlaca.text.toString()
            val flujoVehicular = FlujoVehicular(LocalTime.now(), placa, servicio.getDia(), TipoVehiculo.valueOf(tipoVehiculo))

            // Agregar el vehículo al servicio
            servicio.agregarVehiculo(flujoVehicular)

            // Guardar la selección
            with(sharedPreferences.edit()) {
                putString("tipoVehiculo", tipoVehiculo)
                apply()
            }

            val intent = Intent(this, Registro::class.java)
            intent.putExtra("fecha", this.servicio.getDia().fecha.toString())
            startActivity(intent)
        }
    }
}