package com.example.simulacion.modelos

import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class DiasDeObservacion(val id: Int, val fecha: LocalDate) {
    DIA_1(1, LocalDate.of(2024, 10, 7)),
    DIA_2(2, LocalDate.of(2024, 10, 10)),
    DIA_3(3, LocalDate.of(2024, 10, 11)),
    DIA_4(4, LocalDate.of(2024, 10, 15)),
    DIA_5(5, LocalDate.of(2024, 10, 17)),
    DIA_6(6, LocalDate.of(2024, 10, 18)),
    DIA_7(7, LocalDate.of(2024, 10, 21)),
    DIA_8(8, LocalDate.of(2024, 10, 22));

    override fun toString(): String {
        return fecha.toString()
    }
}