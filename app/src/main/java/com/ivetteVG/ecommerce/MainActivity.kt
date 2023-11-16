package com.ivetteVG.ecommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ivetteVG.ecommerce.Fragments.FragmentChats
import com.ivetteVG.ecommerce.Fragments.FragmentCuenta
import com.ivetteVG.ecommerce.Fragments.FragmentInicio
import com.ivetteVG.ecommerce.Fragments.FragmentMisAnuncios
import com.ivetteVG.ecommerce.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        verfragmentInicio()
        binding.BottomNV.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.Item_Inicio->{
                    verfragmentInicio()
                    true
                }
                R.id.Item_Chats->{
                    verfragmentChats()
                    true
                }
                R.id.Item_Mis_Anuncios->{
                    verfragmentMisAnuncios()
                    true
                }
                R.id.Item_Cuentas->{
                    verfragmentCuenta()
                    true
                }
                else->{
                    false
                }
            }
        }
    }
    private fun verfragmentInicio(){
binding.TituloRL.text = "Inicio"
        val fragment = FragmentInicio()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id,fragment,"FragmentInicio")
        fragmentTransition.commit()
    }
    private fun verfragmentChats(){
        binding.TituloRL.text = "Chat"
        val fragment = FragmentChats()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id,fragment,"FragmentInicio")
        fragmentTransition.commit()
    }
    private fun verfragmentMisAnuncios(){
        binding.TituloRL.text = "Mis anuncios"
        val fragment = FragmentMisAnuncios()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id,fragment,"FragmentMisAnuncios")
        fragmentTransition.commit()
    }
    private fun verfragmentCuenta(){
        binding.TituloRL.text = "Mi cuenta"
        val fragment = FragmentCuenta()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id,fragment,"FragmentCuenta")
        fragmentTransition.commit()
    }

}