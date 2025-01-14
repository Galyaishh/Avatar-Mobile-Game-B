package com.example.avatar_mobile_game

import android.app.Application
import com.example.avatar_mobile_game.DataManager.RecordsManager
import com.example.avatar_mobile_game.utilities.BackgroundMusicPlayer
import com.example.avatar_mobile_game.utilities.SignalManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        RecordsManager.init(this)
        BackgroundMusicPlayer.init(this)
        BackgroundMusicPlayer.getInstance().setResourceID(R.raw.background_music)
    }

    override fun onTerminate() {
        super.onTerminate()
        BackgroundMusicPlayer.getInstance().stopMusic()
    }
}