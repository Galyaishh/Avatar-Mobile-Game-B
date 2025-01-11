package com.example.avatar_mobile_game

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.avatar_mobile_game.DataManager.PlayerRecord
import com.example.avatar_mobile_game.DataManager.RecordsManager
import com.example.avatar_mobile_game.adapters.PlayerAdapter
import com.example.avatar_mobile_game.databinding.ActivityRecordsBinding
import com.example.avatar_mobile_game.interfaces.PlayerCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class RecordsActivity:AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityRecordsBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var recordsManager: RecordsManager
    private lateinit var adapter: PlayerAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityRecordsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            recordsManager = RecordsManager.getInstance()
            val playerRecords = recordsManager.getRecords().take(10)

            if (playerRecords.isEmpty()) {
                Toast.makeText(this, "No records available.", Toast.LENGTH_SHORT).show()
                return
            }

            adapter = PlayerAdapter(playerRecords)
            adapter.playerCallback = object: PlayerCallback {
                override fun onPlayerClicked(player: PlayerRecord, position: Int) {
                    showPlayerLocation(player.location)
                }
            }
            binding.recordsRVList.layoutManager = LinearLayoutManager(this)
            binding.recordsRVList.adapter = adapter


            val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = false
        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
    }

    private fun showPlayerLocation(location: LatLng?) {
        if(location == null) {
            Toast.makeText(this, "Location unavailable", Toast.LENGTH_SHORT).show()
            googleMap.clear()
            return
        }
        googleMap.clear()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        googleMap.addMarker(MarkerOptions().position(location).title("Player Location"))
    }
}

