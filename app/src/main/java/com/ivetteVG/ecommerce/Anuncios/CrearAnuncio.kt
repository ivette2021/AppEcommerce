package com.ivetteVG.ecommerce.Anuncios

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.ivetteVG.ecommerce.Adaptadores.AdaptadorImagenSeleccionada
import com.ivetteVG.ecommerce.Constantes
import com.ivetteVG.ecommerce.Modelo.ModeloImageSeleccionada
import com.ivetteVG.ecommerce.R
import com.ivetteVG.ecommerce.databinding.ActivityCrearAnuncioBinding

class CrearAnuncio : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityCrearAnuncioBinding
    private lateinit var progressDialog : ProgressDialog


    private var imagenUri : Uri?=null
    private lateinit var imagenSelecArrayList: ArrayList<ModeloImageSeleccionada>
    private lateinit var adaptadorImagenSel : AdaptadorImagenSeleccionada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearAnuncioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        val adaptadorCat = ArrayAdapter(this, R.layout.item_categoria,Constantes.categorias)
        binding.Categoria.setAdapter(adaptadorCat)
        val adaptadorCon = ArrayAdapter(this,R.layout.item_condicion, Constantes.condiciones)
        binding.Condicion.setAdapter(adaptadorCon)
}}