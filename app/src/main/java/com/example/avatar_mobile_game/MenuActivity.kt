package com.example.avatar_mobile_game

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.avatar_mobile_game.utilities.Constants
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MenuActivity : AppCompatActivity() {

    private lateinit var menu_BTN_tilt: ExtendedFloatingActionButton
    private lateinit var menu_BTN_control: ExtendedFloatingActionButton
    private lateinit var menu_BTN_startGame: MaterialButton
    private lateinit var menu_BTN_topRated: ExtendedFloatingActionButton

    private var gameMode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        enableEdgeToEdge()

        findViews()
        initViews()
    }

    private fun findViews() {
        menu_BTN_tilt = findViewById(R.id.menu_BTN_tilt)
        menu_BTN_control = findViewById(R.id.menu_BTN_control)
        menu_BTN_startGame = findViewById(R.id.menu_BTN_startGame)
        menu_BTN_topRated = findViewById(R.id.menu_BTN_topRated)
    }

    private fun initViews() {
        menu_BTN_tilt.setOnClickListener {
            gameMode = Constants.GameMode.TILT
        }
        menu_BTN_control.setOnClickListener {
            gameMode = Constants.GameMode.CONTROL
        }
        menu_BTN_startGame.setOnClickListener {
            changeActivity(MainActivity::class.java,Bundle().apply { putString(Constants.BundleKeys.GAME_MODE_KEY, gameMode) })
        }
        menu_BTN_topRated.setOnClickListener {
            changeActivity(RecordsActivity::class.java)
        }

    }

    private fun changeActivity(targetActivity: Class<*>,extras: Bundle? = null) {
        val intent = Intent(this, targetActivity)
        extras?.let {  intent.putExtras(extras)}
        startActivity(intent)
    }
}
