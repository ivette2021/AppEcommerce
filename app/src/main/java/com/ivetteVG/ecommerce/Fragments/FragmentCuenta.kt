package com.ivetteVG.ecommerce.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.ivetteVG.ecommerce.OpcionesLogin
import com.ivetteVG.ecommerce.R
import com.ivetteVG.ecommerce.databinding.FragmentCuentaBinding


class FragmentCuenta : Fragment() {

private lateinit var binding : FragmentCuentaBinding
private lateinit var firebaseAuth: FirebaseAuth
private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCuentaBinding.inflate(layoutInflater,container,false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.BtnCerrarSesion.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(mContext, OpcionesLogin::class.java))//se creo un contexto porque aqui no se puede utilizar this
            activity?.finishAffinity()
        }
    }

}