package com.ivetteVG.ecommerce

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.ivetteVG.ecommerce.databinding.ActivitySeleccionarUbicacionBinding

class SeleccionarUbicacion : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivitySeleccionarUbicacionBinding
    private companion object{
        private const val  DEFAULT_ZOOM= 15
    }

    private var mMap: GoogleMap?=null

    private var mPlaceClient : PlacesClient?=null
    private var mFusedLocationProviderClient : FusedLocationProviderClient?=null

    //variables para obtener la informacion geografica
    private var mLastKnowLocation: Location?=null
    private var selectedLatitude : Double?=null
    private var selectedLongitude : Double?=null
    private var direccion = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarUbicacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listoLl.visibility = View.GONE

        val mapFragment = supportFragmentManager.findFragmentById(R.id.MapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        Places.initialize(this,getString(R.raw.keys))
        mPlaceClient = Places.createClient(this)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val autoCompleteSupportMapFragment = supportFragmentManager.findFragmentById(R.id.autocompletar_fragment)
        as AutocompleteSupportFragment

        val placeList = arrayOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG)

        autoCompleteSupportMapFragment.setPlaceFields(listOf(*placeList))


        autoCompleteSupportMapFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                TODO("Not yet implemented")
            }

            override fun onPlaceSelected(p0: Place) {
                TODO("Not yet implemented")
            }

        } )
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }
}
