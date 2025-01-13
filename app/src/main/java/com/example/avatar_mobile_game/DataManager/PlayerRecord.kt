package com.example.avatar_mobile_game.DataManager

import com.google.android.gms.maps.model.LatLng

data class PlayerRecord(
    val name: String,
    val score: Int,
    val location: LatLng
)
