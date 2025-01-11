package com.example.avatar_mobile_game.interfaces

import com.example.avatar_mobile_game.DataManager.PlayerRecord

interface PlayerCallback {
    fun onPlayerClicked(player: PlayerRecord, position: Int)
}