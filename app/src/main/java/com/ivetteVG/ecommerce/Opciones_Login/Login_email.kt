package com.ivetteVG.ecommerce.Opciones_Login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ivetteVG.ecommerce.MainActivity
import com.ivetteVG.ecommerce.R
import com.ivetteVG.ecommerce.Registro_email
import com.ivetteVG.ecommerce.databinding.ActivityLoginEmailBinding

class Login_email : AppCompatActivity() {


    private lateinit var binding: ActivityLoginEmailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.BtnIngresar.setOnClickListener {
            validarInfo()
        }

        binding.TxtRegistrarme.setOnClickListener {
            startActivity(Intent(this@Login_email, Registro_email::class.java))
        }
    }

    private var email = ""
    private var password = ""
    private fun validarInfo() {
        email = binding.EtEmail.text.toString().trim()
        password = binding.EtPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.EtEmail.error = "Email Inválido"
            binding.EtEmail.requestFocus()
        } else if (email.isEmpty()) {
            binding.EtEmail.error = "Ingrese Email"
            binding.EtEmail.requestFocus()
        } else if (password.isEmpty()) {
            binding.EtPassword.error = "Ingrese Email"
            binding.EtPassword.requestFocus()
        } else {
            loginUsuario()
        }
    }

    private fun loginUsuario() {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
                Toast.makeText(this, "Bienvenido(a)", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se pudo iniciar sesion debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }

    }
}