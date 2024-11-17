package com.example.simulacion.services

import android.annotation.SuppressLint
import android.content.Context
import com.example.simulacion.modelos.DiasDeObservacion
import com.example.simulacion.modelos.FlujoVehicular
import java.time.LocalTime

class ColasService private constructor(private var vehiculos: ArrayList<FlujoVehicular>){
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ColasService? = null

        fun getInstance(): ColasService {
            return INSTANCE ?: synchronized(this) {
                val instance = ColasService(ArrayList())
                instance.persistencia = PersistenciaService()
                INSTANCE = instance
                instance
            }
        }
    }

    private lateinit var dia : DiasDeObservacion
    private lateinit var persistencia : PersistenciaService

    fun agregarVehiculo(vehiculo: FlujoVehicular){
        vehiculos.add(vehiculo)
    }

    fun liberarVehiculo(index: Int){
        val vehiculo = getVehiculo(index)
        vehiculo.liberar(LocalTime.now())
        this.persistencia.guardarDatos(vehiculo.toString())
        vehiculos.remove(vehiculo)
    }

    fun noEspero(index: Int){
        val vehiculo = getVehiculo(index)
        vehiculo.noEspero(LocalTime.now());
        this.persistencia.guardarDatos(vehiculo.toString())
        vehiculos.remove(vehiculo)
    }

    fun atenderVehiculo(index: Int){
        val vehiculo = getVehiculo(index)
        vehiculo.enServicio()
    }

    fun getVehiculos(): ArrayList<FlujoVehicular>{
        return vehiculos
    }

    fun getIndexVehiculo(vehiculo: FlujoVehicular): Int{
        return vehiculos.indexOf(vehiculo)
    }

    private fun getVehiculo(index: Int): FlujoVehicular{
        return vehiculos[index]
    }

    fun setDia(dia: DiasDeObservacion){
        this.dia = dia
        this.persistencia.setDoc(dia.fecha)
    }

    fun getDia(): DiasDeObservacion{
        return this.dia
    }
}