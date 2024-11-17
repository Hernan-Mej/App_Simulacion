package com.example.simulacion.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simulacion.R
import com.example.simulacion.modelos.DiasDeObservacion
import com.example.simulacion.services.ColasService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.format.DateTimeFormatter

class Registro : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private val servicio = ColasService.getInstance()
    private lateinit var buttonAgregar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Recuperar la fecha enviada como extra del Intent
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

        tableLayout = findViewById(R.id.tableLayout)
        buttonAgregar = findViewById(R.id.buttonAgregar)
        buttonAgregar.setOnClickListener {
            val intent = Intent(this, AgregarVehiculo::class.java)
            intent.putExtra("fecha", this.servicio.getDia().fecha.toString())
            startActivity(intent)
        }

        populateTable()
    }

    private fun populateTable() {
        for (flujo in servicio.getVehiculos()) {
            if (flujo.isAtendido()) {
                val tableRow =
                    LayoutInflater.from(this).inflate(R.layout.item_atendido, null) as TableRow
                val textViewTipoVehiculo =
                    tableRow.findViewById<TextView>(R.id.textViewTipoVehiculo)
                val textViewPlaca = tableRow.findViewById<TextView>(R.id.textViewPlaca)
                val textViewHoraEntrada = tableRow.findViewById<TextView>(R.id.textViewHoraEntrada)
                val buttonLiberar = tableRow.findViewById<TextView>(R.id.liberar)
                val buttonNoEspero = tableRow.findViewById<TextView>(R.id.noEspero)

                textViewTipoVehiculo.text = flujo.getTipoVehiculo().name
                textViewPlaca.text = flujo.getPlaca() // Assuming getPlaca() method exists
                textViewHoraEntrada.text = flujo.getHoraEntrada()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"))// Assuming getHoraEntrada() returns a LocalTime

                buttonLiberar.setOnClickListener {
                    val index = servicio.getIndexVehiculo(flujo)
                    servicio.liberarVehiculo(index)
                    tableLayout.removeView(tableRow)
                }

                buttonNoEspero.setOnClickListener {
                    val index = servicio.getIndexVehiculo(flujo)
                    servicio.noEspero(index)
                    tableLayout.removeView(tableRow)
                }

                tableLayout.addView(tableRow)

            } else {

                val tableRow =
                    LayoutInflater.from(this).inflate(R.layout.item_no_atendido, null) as TableRow

                val textViewTipoVehiculo =
                    tableRow.findViewById<TextView>(R.id.textViewTipoVehiculo)
                val textViewPlaca = tableRow.findViewById<TextView>(R.id.textViewPlaca)
                val textViewHoraEntrada = tableRow.findViewById<TextView>(R.id.textViewHoraEntrada)
                val buttonLiberar = tableRow.findViewById<TextView>(R.id.liberar)
                val buttonNoEspero = tableRow.findViewById<TextView>(R.id.noEspero)
                val buttonAtender = tableRow.findViewById<TextView>(R.id.atender)

                textViewTipoVehiculo.text = flujo.getTipoVehiculo().name
                textViewPlaca.text = flujo.getPlaca() // Assuming getPlaca() method exists
                textViewHoraEntrada.text = flujo.getHoraEntrada()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"))// Assuming getHoraEntrada() returns a LocalTime

                buttonLiberar.setOnClickListener {
                    if (flujo.isAtendido()){
                        val index = servicio.getIndexVehiculo(flujo)
                        servicio.liberarVehiculo(index)
                        tableLayout.removeView(tableRow)
                    }else {
                        Toast.makeText(this, "El vehiculo no ha sido atendido", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                buttonNoEspero.setOnClickListener {
                    if (flujo.isAtendido()) {
                        val index = servicio.getIndexVehiculo(flujo)
                        servicio.noEspero(index)
                        tableLayout.removeView(tableRow)
                    }else {
                        Toast.makeText(this, "El vehiculo no ha sido atendido", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                buttonAtender.setOnClickListener {
                    val index = servicio.getIndexVehiculo(flujo)
                    servicio.atenderVehiculo(index)
                    buttonAtender.visibility = TextView.GONE
                    buttonAtender.isEnabled = false
                }

                tableLayout.addView(tableRow)
            }
        }
    }
}