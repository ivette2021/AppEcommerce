package com.ivetteVG.ecommerce

import android.text.format.DateFormat
import java.util.Calendar
import java.util.Locale

object Constantes { //clase nos servira para crear diversas funciones

    fun obtenerTiempoDis(): Long {
        return System.currentTimeMillis()//nos va a permitir saber el tiempo actual que se esta ejecutando

    }

    fun obtenerFecha(tiempo: Long):String {
        val calendario = Calendar.getInstance(Locale.ENGLISH)
        calendario.timeInMillis = tiempo

        return DateFormat.format("DD/MM/YYYY", calendario).toString()
    }
}