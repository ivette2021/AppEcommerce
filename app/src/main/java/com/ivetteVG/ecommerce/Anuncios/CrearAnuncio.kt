package com.ivetteVG.ecommerce.Anuncios

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var progressDialog: ProgressDialog


    private var imagenUri: Uri? = null
    private lateinit var imagenSelecArrayList: ArrayList<ModeloImageSeleccionada>
    private lateinit var adaptadorImagenSel: AdaptadorImagenSeleccionada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearAnuncioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        val adaptadorCat = ArrayAdapter(this, R.layout.item_categoria, Constantes.categorias)
        binding.Categoria.setAdapter(adaptadorCat)
        val adaptadorCon = ArrayAdapter(this, R.layout.item_condicion, Constantes.condiciones)
        binding.Condicion.setAdapter(adaptadorCon)

        imagenSelecArrayList = ArrayList()
        cargaImagenes()

        binding.agregarImg.setOnClickListener {
            mostrarOpciones()
        }
    }

    private var marca = ""
    private var categoria = ""
    private var condicion = ""
    private var direccion = ""
    private var precio = ""
    private var titulo = ""
    private var descripcion = ""
    private var latitud = 0.0
    private var longitud = 0.0

    private fun validarDatos() {
marca = binding.EtMarca.text.toString().trim()
        categoria = binding.Categoria.text.toString().trim()
        condicion = binding.Condicion.text.toString().trim()
        direccion = binding.Locacion.text.toString().trim()
        precio = binding.EtPrecio.text.toString().trim()
        titulo = binding.EtTitulo.text.toString().trim()
        descripcion = binding.EtDescripcion.text.toString().trim()
    }

    private fun mostrarOpciones() {
        val popupMenu = PopupMenu(this, binding.agregarImg)

        popupMenu.menu.add(Menu.NONE, 1, 1, "Camara")
        popupMenu.menu.add(Menu.NONE, 2, 2, "Galeria")

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            val itemId = item.itemId
            if (itemId == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    solicitarPermisoCamara.launch(arrayOf(android.Manifest.permission.CAMERA))
                } else {
                    solicitarPermisoCamara.launch(
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }
            } else if (itemId == 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    imagenGaleria()
                } else {
                    solicitarPermisoAlmacenamiento.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            true
        }
    }

    private val solicitarPermisoAlmacenamiento = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { esConcedido ->
        if (esConcedido) {
            imagenGaleria()
        } else {
            Toast.makeText(
                this,
                "El permido de almacenamiento ha sido denegada",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val solicitarPermisoCamara = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        var todosConcedidos = true
        for (esConcedido in resultado.values) {
            todosConcedidos = todosConcedidos && esConcedido
        }
        if (todosConcedidos) {
            imagenCamara()
        } else {
            Toast.makeText(
                this,
                "El permido de la camara o almacenamiento ha sido denegada, o ambas fueron denegadas",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun imagenGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type =
            "image/*"//con esto decimos que cuando se habra la galeria solo tengamos a disposicion imagenes
        resultadoGaleria_ARL.launch(intent)
    }

    private val resultadoGaleria_ARL =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) { //si la imagen due seleccionada correctamente
                val data = resultado.data
                imagenUri = data!!.data

                val tiempo = "${Constantes}"
                val modeloImgSel = ModeloImageSeleccionada(
                    tiempo, imagenUri, null, false
                )
                cargaImagenes()
            } else {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }

    private fun imagenCamara() {
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.Images.Media.TITLE,
            "Titulo_imagen"
        )//sacar imagen con una alta calidad
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Descripcion_Imagen")
        imagenUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri)
        resultadoCamara_ARL.launch(intent)
    }

    private val resultadoCamara_ARL = //gestionar si la foto fue tomada correctamente
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val tiempo = "${Constantes.obtenerTiempoDis()}"
                val modeloImgSel = ModeloImageSeleccionada(
                    tiempo, imagenUri, null, false
                )
                cargaImagenes()
            } else {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }


    private fun cargaImagenes() {

        adaptadorImagenSel = AdaptadorImagenSeleccionada(this, imagenSelecArrayList)
        binding.RVImagenes.adapter = adaptadorImagenSel
    }

}