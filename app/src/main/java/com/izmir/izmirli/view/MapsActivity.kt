package com.izmir.izmirli.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.izmir.izmirli.R
import com.izmir.izmirli.databinding.ActivityMapsBinding
import com.izmir.izmirli.model.Location
import com.izmir.izmirli.util.showMessage
import com.izmir.izmirli.util.İzmirConstant

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var lat: String = ""
    private var long: String = ""
    private var eczaneName: String = ""
    private var locationList: ArrayList<Location> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lat = intent.getStringExtra(İzmirConstant.LOCATION_LAT).toString()
        long = intent.getStringExtra(İzmirConstant.LOCATION_LONG).toString()
        eczaneName = intent.getStringExtra(İzmirConstant.ECZANE_NAME).toString()
        locationList = intent.getParcelableArrayListExtra<Location>(İzmirConstant.LOCATION_LIST) as ArrayList<Location>


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationList.forEach {
            it.let {
                val sydney = LatLng(it.lat ?: 0.0, it.long ?: 0.0)
                mMap.addMarker(MarkerOptions().position(sydney).title(it.name))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            }
        }
        //mMap.setMinZoomPreference(12f)
    }
}