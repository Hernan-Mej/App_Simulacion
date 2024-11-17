package com.example.simulacion.modelos

import java.time.LocalTime
import java.time.format.DateTimeFormatter

class FlujoVehicular(
    private val entrada: LocalTime,
    private val placa: String,
    private val dia: DiasDeObservacion,
    private val tipoVehiculo: TipoVehiculo
) {
    private lateinit var salida: LocalTime
    private lateinit var servicio: LocalTime
    private var duracion: Long = 0
    private var seVolo = false
    private var duracion_servicio: Long = 0
    private var esAtendido = false

    fun liberar(salida: LocalTime){
        this.salida = salida
        this.duracion_servicio = this.getDuracion(this.servicio, salida)
        this.duracion = this.getDuracion(this.entrada, salida);
    }

    fun noEspero(salida: LocalTime){
        this.salida = salida

        this.duracion_servicio = this.getDuracion(this.servicio, salida)
        this.duracion = this.getDuracion(this.entrada,salida)
        this.seVolo = true
    }

    fun enServicio(){
        this.servicio = LocalTime.now()
        this.esAtendido = true
    }


    override fun toString(): String {
        return "${tipoVehiculo.id},$placa,${entrada.format(DateTimeFormatter.ofPattern("HH:mm:ss"))},${salida.format(DateTimeFormatter.ofPattern("HH:mm:ss"))},$duracion,$duracion_servicio,${dia.id},${seVolo}";
    }

    fun getTipoVehiculo(): TipoVehiculo {
        return tipoVehiculo
    }

    fun getPlaca(): String {
        return placa
    }

    fun getHoraEntrada(): LocalTime {
        return entrada
    }

    fun isAtendido(): Boolean {
        return esAtendido
    }

    fun getDuracion(entrada: LocalTime, salida: LocalTime): Long{
        val tiempoEntrada = entrada.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        val tiempoSalida = salida.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        val tiempoEntradaSegundos = tiempoEntrada.split(":")
        val tiempoSalidaSegundos = tiempoSalida.split(":")

        val tiempoEntradaSegundosTotal = tiempoEntradaSegundos[0].toLong() * 3600 + tiempoEntradaSegundos[1].toLong() * 60 + tiempoEntradaSegundos[2].toLong()
        val tiempoSalidaSegundosTotal = tiempoSalidaSegundos[0].toLong() * 3600 + tiempoSalidaSegundos[1].toLong() * 60 + tiempoSalidaSegundos[2].toLong()

        val duracion = tiempoSalidaSegundosTotal - tiempoEntradaSegundosTotal

        return duracion;
    }
}