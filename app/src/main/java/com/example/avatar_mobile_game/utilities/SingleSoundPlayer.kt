package com.example.avatar_mobile_game.utilities

import android.content.Context
import android.media.MediaPlayer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SingleSoundPlayer(context: Context) {
    private val context: Context = context.applicationContext
    private val executer: ExecutorService = Executors.newSingleThreadExecutor()

    fun playSound(resId: Int) {
        executer.execute {
            val mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer.isLooping = false
            mediaPlayer.setVolume(1.0f, 1.0f)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {mp: MediaPlayer? ->
                var mpl = mp
                mpl!!.stop()
                mpl.release()
                mpl = null
            }


        }

    }
}