package com.example.avatar_mobile_game

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.avatar_mobile_game.utilities.Constants
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var main_FAB_left: ExtendedFloatingActionButton
    private lateinit var main_FAB_right: ExtendedFloatingActionButton
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_IMG_player: Array<AppCompatImageView> //for UI
    private lateinit var main_IMG_fire: Array<Array<AppCompatImageView>> //for UI

    private lateinit var gameManager: GameManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()
        startGame()
    }

    val handler: Handler = Handler(Looper.getMainLooper())
    private var gameStarted: Boolean = false

    val runnable: Runnable = object : Runnable {
        override fun run() {
            //reschedule
            handler.postDelayed(this, Constants.DELAY)
            //refresh UI

//            if (gameManager.isGameOver)
//                stopGame()
//            else
            gameProgress()
        }
    }


    private fun gameProgress() {
        gameManager.moveFireDown()
        gameManager.spawnFire()
        updateFireUI()

        if (gameManager.checkCollision())
            gameManager.handleCollision()
        updateLivesUI()

        if (gameManager.isGameOver)
            stopGame()
    }


    private fun startGame() {
        if (!gameStarted) {
            handler.postDelayed(runnable, Constants.DELAY)
            gameStarted = true;
        }
    }

    private fun stopGame() {
        if (gameStarted) {
            handler.removeCallbacks(runnable)
            gameStarted = false
        }
    }

    private fun updateFireUI() {

        //update fire image

        var fireMatrix = gameManager.getFireMatrix()
        for (row in fireMatrix.indices) {
            for (col in fireMatrix[row].indices) {
                if (fireMatrix[row][col] == 2)
                    main_IMG_fire[row][col].visibility = View.VISIBLE
                else
                    main_IMG_fire[row][col].visibility = View.INVISIBLE
            }
        }
    }


    private fun findViews() {

        main_FAB_left = findViewById(R.id.main_FAB_left)
        main_FAB_right = findViewById(R.id.main_FAB_right)
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3)
        )

        main_IMG_player = arrayOf(
            findViewById(R.id.main_IMG_aang50),
            findViewById(R.id.main_IMG_aang51),
            findViewById(R.id.main_IMG_aang52)
        )

        main_IMG_fire = arrayOf(
            arrayOf(
                findViewById(R.id.main_MAT_00),
                findViewById(R.id.main_MAT_01),
                findViewById(R.id.main_MAT_02)
            ),
            arrayOf(
                findViewById(R.id.main_MAT_10),
                findViewById(R.id.main_MAT_11),
                findViewById(R.id.main_MAT_12)
            ),
            arrayOf(
                findViewById(R.id.main_MAT_20),
                findViewById(R.id.main_MAT_21),
                findViewById(R.id.main_MAT_22)
            ),
            arrayOf(
                findViewById(R.id.main_MAT_30),
                findViewById(R.id.main_MAT_31),
                findViewById(R.id.main_MAT_32)
            ),
            arrayOf(
                findViewById(R.id.main_MAT_40),
                findViewById(R.id.main_MAT_41),
                findViewById(R.id.main_MAT_42)
            )
        )
    }

    private fun initViews() {
        main_FAB_left.setOnClickListener {
            gameManager.movePlayer(-1)
            updatePlayerUI()
        }
        main_FAB_right.setOnClickListener {
            gameManager.movePlayer(1)
            updatePlayerUI()
        }
    }

    private fun updatePlayerUI() {

        for (col in gameManager.getPlayerMatrix().indices) {
            if (gameManager.getPlayerMatrix()[col] == 1) // player image
                main_IMG_player[col].visibility = View.VISIBLE
            else
                main_IMG_player[col].visibility = View.INVISIBLE
        }
    }

    private fun updateLivesUI() {
        if (gameManager.numberOfCollisions != 0)
            main_IMG_hearts[gameManager.numberOfCollisions - 1].visibility = View.INVISIBLE
    }


}