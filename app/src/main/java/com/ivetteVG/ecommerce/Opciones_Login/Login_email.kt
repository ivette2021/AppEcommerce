package com.ivetteVG.ecommerce.Opciones_Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ivetteVG.ecommerce.R
import com.ivetteVG.ecommerce.Registro_email
import com.ivetteVG.ecommerce.databinding.ActivityLoginEmailBinding

class Login_email : AppCompatActivity() {
    private lateinit var binding :ActivityLoginEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.TxtRegistrarme.setOnClickListener {
            startActivity(Intent(this@Login_email,Registro_email::class.java))
        }
    }
}