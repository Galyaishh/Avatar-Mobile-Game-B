package com.example.avatar_mobile_game

import android.app.Application
import com.example.avatar_mobile_game.utilities.SignalManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
    }
}