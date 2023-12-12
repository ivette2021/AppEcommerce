package com.ivetteVG.ecommerce

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView.LayoutManager.Properties
import com.google.firebase.auth.FirebaseAuth
import com.ivetteVG.ecommerce.Anuncios.CrearAnuncio
import com.ivetteVG.ecommerce.Fragments.FragmentChats
import com.ivetteVG.ecommerce.Fragments.FragmentCuenta
import com.ivetteVG.ecommerce.Fragments.FragmentInicio
import com.ivetteVG.ecommerce.Fragments.FragmentMisAnuncios
import com.ivetteVG.ecommerce.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var firebaseApiKey: String
    private lateinit var googleMapsApiKey: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readKeysFromFile()
        setApiKeysInManifest()

        firebaseAuth = FirebaseAuth.getInstance()
        comporbarSesion()
        verfragmentInicio()
        binding.BottomNV.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Item_Inicio -> {
                    verfragmentInicio()
                    true
                }

                R.id.Item_Chats -> {
                    verfragmentChats()
                    true
                }

                R.id.Item_Mis_Anuncios -> {
                    verfragmentMisAnuncios()
                    true
                }

                R.id.Item_Cuentas -> {
                    verfragmentCuenta()
                    true
                }

                else -> {
                    false
                }
            }
        }
        binding.FAB.setOnClickListener {
            startActivity(Intent(this, CrearAnuncio::class.java))
        }
    }

    private fun comporbarSesion() {
        if (firebaseAuth.currentUser == null) {
            startActivity(Intent(this, OpcionesLogin::class.java))
            finishAffinity()
        }


    }

    private fun verfragmentInicio() {
        binding.TituloRL.text = "Inicio"
        val fragment = FragmentInicio()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentInicio")
        fragmentTransition.commit()
    }

    private fun verfragmentChats() {
        binding.TituloRL.text = "Chat"
        val fragment = FragmentChats()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentInicio")
        fragmentTransition.commit()
    }

    private fun verfragmentMisAnuncios() {
        binding.TituloRL.text = "Mis anuncios"
        val fragment = FragmentMisAnuncios()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentMisAnuncios")
        fragmentTransition.commit()
    }

    private fun verfragmentCuenta() {
        binding.TituloRL.text = "Mi cuenta"
        val fragment = FragmentCuenta()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentCuenta")
        fragmentTransition.commit()
    }
    private fun readKeysFromFile() {
        try {
            val inputStream: InputStream = resources.openRawResource(R.raw.keys)
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val parts = line?.split("=")
                if (parts?.size == 2) {
                    when (parts[0].trim()) {
                        "fireBaseApi" -> firebaseApiKey = parts[1].trim()
                        "googleMapsApi" -> googleMapsApiKey = parts[1].trim()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun setApiKeysInManifest() {
        val applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

        // Configura la clave de Firebase en el manifest
        applicationInfo.metaData.putString("com.google.firebase.api_key", firebaseApiKey)

        // Configura la clave de Google Maps en el manifest
        applicationInfo.metaData.putString("com.google.android.geo.API_KEY", googleMapsApiKey)
    }
}