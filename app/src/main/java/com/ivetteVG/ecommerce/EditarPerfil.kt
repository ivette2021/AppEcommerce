package com.ivetteVG.ecommerce

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ivetteVG.ecommerce.databinding.ActivityEditarPerfilBinding

class EditarPerfil : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPerfilBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private var imageUri: Uri?= null //creamos esta variable tipo uri para almacenar y inicializamos como posible nulo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        cargarInfo()

        binding.FABCambiarImg.setOnClickListener {
            select_imagen_de()
        }
    }

    private fun cargarInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val imagen = "${snapshot.child("urlImagenPerfil").value}"
                    val f_nac = "${snapshot.child("fecha_nac").value}"
                    val telefono = "${snapshot.child("telefono").value}"
                    val codTelefono = "${snapshot.child("codigoTelefono").value}"

                    //setear
                    binding.EtNombres.setText(nombres)
                    binding.EtFNac.setText(f_nac)
                    binding.EtTelefono.setText(telefono)

                    try {
                        Glide.with(applicationContext)
                            .load(imagen)
                            .placeholder((R.drawable.img_perfil))
                            .into(binding.imgPerfil)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@EditarPerfil,
                            "${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    try {
                        val codigo = codTelefono.replace("+", "")
                            .toInt()//cambio de codigo ej +51 a simplemente 51
                        binding.selectorCod.setCountryForPhoneCode(codigo)

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@EditarPerfil,
                            "${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun select_imagen_de() {
        val popupMenu = PopupMenu(this, binding.FABCambiarImg)

        popupMenu.menu.add(Menu.NONE, 1, 1, "Camara")
        popupMenu.menu.add(Menu.NONE, 2, 2, "Galeria")

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            val itemId = item.itemId
            if (itemId == 1) {
                //Cámara
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                    concederPermisoCamara.launch(arrayOf(android.Manifest.permission.CAMERA))
                } else {
                    concederPermisoCamara.launch(
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }
            } else if (itemId == 2) {
                //galeria
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                    imagenGaleria() //si el usuario desea abrir galeria de un sdk igual o superior a tiramisu se ejecuta esta funcion
                } else {
                    concederPermisoAlmacenamiento.launch(
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE

                    )
                }

            }
            return@setOnMenuItemClickListener true
        }
    }

    //crearemos un metodo
    private val concederPermisoCamara =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultado ->
            var concedidoTodos = true
            for (seConcede in resultado.values) {
                concedidoTodos = concedidoTodos && seConcede
            }

            if (concedidoTodos) {
                imageCamara()
            } else {
                Toast.makeText(
                    this,
                    "El permiso de la camara o almacenamiento a sido denegado o ambas fueron denegadas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun imageCamara() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Titulo_imagen")//sacar imagen con una alta calidad
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Descripcion_Imagen")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        resultadoCamara_ARL.launch(intent)
    }

    private val resultadoCamara_ARL = //gestionar si la foto fue tomada correctamente
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado ->
            if (resultado.resultCode == Activity.RESULT_OK){
                try {
                    Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.img_perfil)
                        .into(binding.imgPerfil)
                }catch (e:Exception){

                }
            }else{
                Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show()
            }
        }


    private val concederPermisoAlmacenamiento =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { esConcedido ->
            if (esConcedido) {
                imagenGaleria()
            } else {
                Toast.makeText(
                    this,
                    "El permiso de almacenamiento a sido denegado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun imagenGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"//con esto decimos que cuando se habra la galeria solo tengamos a disposicion imagenes
        resultadoGaleria_ARL.launch(intent)
    }

    private  val resultadoGaleria_ARL =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado->
            if (resultado.resultCode ==Activity.RESULT_OK){ //si la imagen due seleccionada correctamente
                val data = resultado.data
                imageUri = data!!.data
                //setear el imageview
                try {
                    Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.img_perfil)
                        .into(binding.imgPerfil)
                }catch (e:Exception){

                }
            }else{
                Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show()
            }
        }
}