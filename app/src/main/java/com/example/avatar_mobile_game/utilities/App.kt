package com.example.avatar_mobile_game.utilities

import android.app.Application
import com.example.avatar_mobile_game.DataManager.RecordsManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        RecordsManager.init(this)
//        ImageLoader.init(this)

    }
}