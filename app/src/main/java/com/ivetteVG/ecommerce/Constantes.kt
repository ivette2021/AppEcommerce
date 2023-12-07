package com.ivetteVG.ecommerce

import android.text.format.DateFormat
import java.util.Calendar
import java.util.Locale

object Constantes { //clase nos servira para crear diversas funciones

    const val anuncio_disponible = "Disponible"
    const val  anuncio_vendido = "Vendido"

    val categorias = arrayOf(
        "Mobiles",
        "Ordenadores/Laptops",
        "Electronica y Electrodomesticos",
        "Vehiculos",
        "Consolas y videojuegos",
        "Belleza y cuidado personal",
        "Libros",
        "Deportes"
    )

    val condiciones = arrayOf(
        "Nuevo",
        "Usado",
        "Renovado",
        "Casi Nuevo",
        "Para Reparacion",
    )


    fun obtenerTiempoDis(): Long {
        return System.currentTimeMillis()//nos va a permitir saber el tiempo actual que se esta ejecutando

    }

    fun obtenerFecha(tiempo: Long):String {
        val calendario = Calendar.getInstance(Locale.ENGLISH)
        calendario.timeInMillis = tiempo

        return DateFormat.format("dd/MM/yyyy", calendario).toString()
    }
}