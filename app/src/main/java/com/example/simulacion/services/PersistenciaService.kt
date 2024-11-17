package com.example.simulacion.services

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDate

class PersistenciaService() {

    private lateinit var fileName: String
    private lateinit var file: File

    fun setDoc(fecha: LocalDate) {
        fileName = "datos_${fecha}.txt"

        // Ruta a almacenamiento externo accesible en "Mi teléfono"
        val storageDir = File(Environment.getExternalStorageDirectory(), "datos_simulacion")
        if (!storageDir.exists()) {
            val dirCreated = storageDir.mkdirs() // Crea la carpeta si no existe
            if (!dirCreated) {
                Log.e("Persistencia", "No se pudo crear el directorio: ${storageDir.absolutePath}")
                return
            }
        }

        // Definir la ruta completa del archivo dentro de la carpeta
        file = File(storageDir, fileName)

        if (!file.exists()) {
            try {
                file.createNewFile()
                FileWriter(file, true).use { writer ->
                    writer.write("Vehiculo,placa,entrada,salida,duracion,duracion_servicio,dia,salto_semaforo\n")
                }
                Log.d("Persistencia", "Ruta del archivo: ${file.absolutePath}")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun guardarDatos(linea: String) {
        try {
            FileWriter(file, true).use { writer ->
                writer.write("$linea\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}