package com.example.avatar_mobile_game.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.avatar_mobile_game.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_LAY_map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
            googleMap.uiSettings.isZoomControlsEnabled = true
        }
        return view
    }

    fun showPlayerLocation(location: LatLng?) {
        if (location == null) {
            Toast.makeText(context, "Player location is unavailable", Toast.LENGTH_SHORT).show()
            googleMap.clear()
            return
        }
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(location).title("Player Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

//    private lateinit var googleMap: GoogleMap
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_map, container, false)
//
//        val mapFragment =
//            childFragmentManager.findFragmentById(R.id.map_LAY_map) as? SupportMapFragment
//        mapFragment?.getMapAsync { map ->
//            googleMap = map
//            googleMap.uiSettings.isZoomControlsEnabled = true
//        }
//
//        return view
//    }
//
//    fun onMapReady(map: GoogleMap) {
//        googleMap = map
//        googleMap.uiSettings.isZoomControlsEnabled = true
//        googleMap.uiSettings.isScrollGesturesEnabled = true
//        googleMap.uiSettings.isTiltGesturesEnabled = false
//        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
//    }
//
//    fun showPlayerLocation(location: LatLng?) {
//
//        if(location == null) {
//            Toast.makeText(this.context, "Location is null", Toast.LENGTH_SHORT).show()
//            googleMap.clear()
//            return
//        }
//        googleMap.clear()
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
//        googleMap.addMarker(MarkerOptions().position(location).title("Player Location"))


//        if (::googleMap.isInitialized && location != null) {
//            googleMap.clear()
//            googleMap.addMarker(MarkerOptions().position(location).title("Player Location"))
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
//        }
}
