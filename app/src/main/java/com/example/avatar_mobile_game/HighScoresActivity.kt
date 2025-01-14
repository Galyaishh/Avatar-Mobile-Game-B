package com.example.avatar_mobile_game

import PlayerListFragment
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentTransaction
import com.example.avatar_mobile_game.DataManager.PlayerRecord
import com.example.avatar_mobile_game.fragments.MapFragment
import com.example.avatar_mobile_game.interfaces.PlayerCallback

class HighScoresActivity : AppCompatActivity() {

    private lateinit var highscores_BTN_back: AppCompatImageView
    private lateinit var playerListFragment: PlayerListFragment
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscores)
        enableEdgeToEdge()
        findViews()
        setupFragments()
    }

    private fun findViews() {
        highscores_BTN_back = findViewById(R.id.highscores_BTN_back)
        highscores_BTN_back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupFragments() {
        mapFragment = MapFragment()
        playerListFragment = PlayerListFragment()
        playerListFragment.playerClicked = object : PlayerCallback {
            override fun showLocationOnMap(player: PlayerRecord, position: Int) {
                mapFragment.showPlayerLocation(player.location)
            }
        }

        //Add fragments to the layout
        supportFragmentManager.beginTransaction()
            .replace(R.id.highscores_FRAME_list, playerListFragment)
            .replace(R.id.highscores_FRAME_map, mapFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

}
