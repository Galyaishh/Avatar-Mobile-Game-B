package com.example.avatar_mobile_game

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.avatar_mobile_game.utilities.Constants
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MenuActivity : AppCompatActivity() {

    private lateinit var menu_BTN_tilt: ExtendedFloatingActionButton
    private lateinit var menu_BTN_control: ExtendedFloatingActionButton
    private lateinit var menu_BTN_fast: ExtendedFloatingActionButton
    private lateinit var menu_BTN_slow: ExtendedFloatingActionButton
    private lateinit var menu_BTN_startGame: MaterialButton
    private lateinit var menu_BTN_topRated: ExtendedFloatingActionButton

    private var gameMode: Constants.GameMode? = null
    private var gameSpeed: Constants.GameSpeed? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        enableEdgeToEdge()
        findViews()
        initViews()
        requestLocationPermission()
    }

    private fun findViews() {
        menu_BTN_tilt = findViewById(R.id.menu_BTN_tilt)
        menu_BTN_control = findViewById(R.id.menu_BTN_control)
        menu_BTN_fast = findViewById(R.id.menu_BTN_fast)
        menu_BTN_slow = findViewById(R.id.menu_BTN_slow)
        menu_BTN_startGame = findViewById(R.id.menu_BTN_startGame)
        menu_BTN_topRated = findViewById(R.id.menu_BTN_topRated)
    }

    private fun initViews() {
        menu_BTN_tilt.setOnClickListener { selectGameMode(Constants.GameMode.TILT, menu_BTN_tilt) }
        menu_BTN_control.setOnClickListener { selectGameMode(Constants.GameMode.CONTROL, menu_BTN_control) }
        menu_BTN_fast.setOnClickListener { selectGameSpeed(Constants.GameSpeed.FAST, menu_BTN_fast) }
        menu_BTN_slow.setOnClickListener { selectGameSpeed(Constants.GameSpeed.SLOW, menu_BTN_slow) }

        menu_BTN_startGame.setOnClickListener {
            if (gameMode == null || gameSpeed == null) {
                Toast.makeText(this, "Please select game mode and speed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            changeActivity(
                MainActivity::class.java,
                Bundle().apply {
                    putString(Constants.BundleKeys.GAME_MODE_KEY, gameMode.toString())
                    putString(Constants.BundleKeys.GAME_SPEED_KEY, gameSpeed.toString())
                }
            )
        }

        menu_BTN_topRated.setOnClickListener {
            changeActivity(RecordsActivity::class.java)
        }
    }

    private fun selectGameMode(mode: Constants.GameMode, button: ExtendedFloatingActionButton) {
        if (gameMode == mode) {
            Toast.makeText(this, "${mode.name} is already selected", Toast.LENGTH_SHORT).show()
            return
        }
        gameMode = mode
        updateButtonSelection(arrayOf(menu_BTN_tilt, menu_BTN_control), button)
    }

    private fun selectGameSpeed(speed: Constants.GameSpeed, button: ExtendedFloatingActionButton) {
        if (gameSpeed == speed) {
            Toast.makeText(this, "${speed.name} is already selected", Toast.LENGTH_SHORT).show()
            return
        }
        gameSpeed = speed
        updateButtonSelection(arrayOf(menu_BTN_fast, menu_BTN_slow), button)
    }

    private fun updateButtonSelection(buttons: Array<ExtendedFloatingActionButton>, selectedButton: ExtendedFloatingActionButton) {
        buttons.forEach { it.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray)) }
        selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
    }

    private fun changeActivity(targetActivity: Class<*>, extras: Bundle? = null) {
        val intent = Intent(this, targetActivity)
        extras?.let { intent.putExtras(extras) }
        startActivity(intent)
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constants.RequestCodes.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
}
