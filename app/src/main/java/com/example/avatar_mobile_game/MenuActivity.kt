package com.example.avatar_mobile_game

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
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
    private lateinit var menu_BTN_startGame: MaterialButton
    private lateinit var menu_BTN_topRated: ExtendedFloatingActionButton

    private var gameMode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
//        fullScreenGame()
        enableEdgeToEdge()
        findViews()
        initViews()
        requestLocationPermission()
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

    fun fullScreenGame() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    private fun requestLocationPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constants.RequestCodes.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
}
