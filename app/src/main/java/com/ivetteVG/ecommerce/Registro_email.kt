package com.ivetteVG.ecommerce

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ivetteVG.ecommerce.databinding.ActivityRegistroEmailBinding

class Registro_email : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroEmailBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)//esto esperara a que cargue el progress Dialog y se oculte cuando presionemos fuera de el

        binding.BtnRegistrar.setOnClickListener {
            validarInfo()
        }
    }

    private var email = "" //strings para almacenar la info ingresada por el usuario
    private var password = ""
    private var r_password = ""
    private fun validarInfo() {
        email = binding.EtEmail.text.toString()
            .trim()//trim es para que no ocupe los espacios en blanco que deje el usuario
        password = binding.EtPassword.text.toString().trim()
        r_password = binding.EtRPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.EtEmail.error = "Correo invalido"//no contengan el arroba y el .com
            binding.EtEmail.requestFocus()//hace que el puntero se mantenga activo en la vista del et
        } else if (email.isEmpty()) {
            binding.EtEmail.error = "Ingrese correo"
            binding.EtEmail.requestFocus()
        } else if (password.isEmpty()) {
            binding.EtPassword.error = "Ingrese Contraseña"
            binding.EtEmail.requestFocus()
        } else if (r_password.isEmpty()) {
            binding.EtRPassword.error = "Repetir Contraseña"
            binding.EtRPassword.requestFocus()
        } else if (password != r_password) {
            binding.EtRPassword.error = "No coinciden"
            binding.EtRPassword.requestFocus()
        } else {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { //tarea se ha completado con exito
                llenarInfoBD()
            }
            .addOnFailureListener { e ->//algo malo ocurrio, haremos una excepcion
                progressDialog.dismiss()
                Toast.makeText(
                    this, "Nose registro el usuario debido a ${e.message}", Toast.LENGTH_SHORT
                ).show()

            }
    }

    private fun llenarInfoBD() {
        progressDialog.setMessage("Guardando Informacion")

        val tiempo = Constantes.obtenerTiempoDis()
        val emailUsuario =  firebaseAuth.currentUser!!.email
        val uidUsuario = firebaseAuth.uid

        val  hashMap = HashMap<String, Any>()
        hashMap["nombres"]= ""
        hashMap["codigoTelefono"]= ""
        hashMap["telefono"]= ""
        hashMap["urlImagenPerfil"]= ""
        hashMap["proveedor"]= "Email" //o los servicios de google
        hashMap["escribiendo"]= "" //chat para comunicarse vendedor con cliente
        hashMap["tiempo"]= tiempo //desde que fecha se registro el usuario en el sistema
        hashMap["online"]= true //estado del ususario
        hashMap["email"]= "${emailUsuario}"
        hashMap["uid"]= "${uidUsuario}"
        hashMap["fecha_nac"]= ""

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uidUsuario!!)
            .setValue(hashMap)
            .addOnSuccessListener {
progressDialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()//una vez realizada registro de email(activity) desaparesca
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se registro debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }
}