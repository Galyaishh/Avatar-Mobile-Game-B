package com.example.avatar_mobile_game.utilities

import android.app.Application
import com.example.avatar_mobile_game.DataManager.RecordsManager
import com.example.avatar_mobile_game.R

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        RecordsManager.init(this)
        BackgroundMusicPlayer.init(this)
        BackgroundMusicPlayer.getInstance().setResourceID(R.raw.background_music)
        //        ImageLoader.init(this)

    }

    override fun onTerminate() {
        super.onTerminate()
        BackgroundMusicPlayer.getInstance().stopMusic()
    }
}