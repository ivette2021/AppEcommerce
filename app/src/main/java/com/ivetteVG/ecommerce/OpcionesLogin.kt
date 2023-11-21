package com.ivetteVG.ecommerce

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.ivetteVG.ecommerce.Opciones_Login.Login_email
import com.ivetteVG.ecommerce.databinding.ActivityOpcionesLoginBinding

class OpcionesLogin : AppCompatActivity() {

    private lateinit var binding: ActivityOpcionesLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpcionesLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.IngresarEmail.setOnClickListener {
            startActivity(Intent(this@OpcionesLogin, Login_email::class.java))
        }

        binding.IngresarGoogle.setOnClickListener {
            googleLogin()
        }

        val constraintLayout: RelativeLayout = findViewById(R.id.mainLayout)
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

    }

    private fun googleLogin() {
        val googleSignInIntent = mGoogleSignInClient.signInIntent
        googleSignInARL.launch(googleSignInIntent)
    }

    private val googleSignInARL = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->
        if (resultado.resultCode == RESULT_OK) {
            val data = resultado.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)
                autenticationGoogle(cuenta.idToken)
            } catch (e: Exception) {
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun autenticationGoogle(idToken: String?) {
        val credencial = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credencial)
            .addOnSuccessListener { resultadoAuth -> //si la persona esta ya registrada en la base de datos la pasa a main activity
                if (resultadoAuth.additionalUserInfo!!.isNewUser) {
                    llenarInfoBD()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }
    }

    private fun llenarInfoBD() {
        progressDialog.setMessage("Guardando Informacion")

        val tiempo = Constantes.obtenerTiempoDis()
        val emailUsuario =  firebaseAuth.currentUser!!.email
        val uidUsuario = firebaseAuth.uid
        val nombreUsuario = firebaseAuth.currentUser?.displayName//displayName para usa el nombre usuario de su cuenta gmail

        val  hashMap = HashMap<String, Any>()
        hashMap["nombres"]= "${nombreUsuario}"
        hashMap["codigoTelefono"]= ""
        hashMap["telefono"]= ""
        hashMap["urlImagenPerfil"]= ""
        hashMap["proveedor"]= "Google" // los servicios de google
        hashMap["escribiendo"]= "" //chat para comunicarse vendedor con cliente
        hashMap["tiempo"]= tiempo //desde que fecha se registro el usuario en el sistema
        hashMap["online"]= true //estado del ususario
        hashMap["email"]= "${emailUsuario}"
        hashMap["uid"]= "${uidUsuario}"
        hashMap["fecha_nac"]= ""

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uidUsuario!!)
            .setValue(hashMap)//setear
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

    private fun comprobarSesion() {
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }
}