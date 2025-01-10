package com.example.avatar_mobile_game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.avatar_mobile_game.interfaces.TiltCallback
import com.example.avatar_mobile_game.utilities.Constants
import com.example.avatar_mobile_game.utilities.GameManager
import com.example.avatar_mobile_game.utilities.TiltDetector
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var main_FAB_left: ExtendedFloatingActionButton
    private lateinit var main_FAB_right: ExtendedFloatingActionButton
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_IMG_player: Array<AppCompatImageView> //for UI
    private lateinit var main_IMG_fire: Array<Array<AppCompatImageView>> //for UI
    private lateinit var main_LBL_score: AppCompatTextView
    private var tiltDetector: TiltDetector? = null
    private lateinit var gameManager: GameManager
    private var gameMode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViews()
        gameMode = intent.getStringExtra(Constants.BundleKeys.GAME_MODE_KEY)
        gameManager = GameManager(main_IMG_hearts.size, main_IMG_fire.size, main_IMG_fire[0].size)
        initViews()
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            context = this,
            tiltCallback = object : TiltCallback {
                override fun tiltRight() {
                    gameManager.movePlayer(1)
                    updatePlayerUI()
                }

                override fun tiltLeft() {
                    gameManager.movePlayer(-1)
                    updatePlayerUI()
                }

                override fun tiltUp() {
                }

                override fun tiltDown() {
                }
            }
        )
    }

    val handler: Handler = Handler(Looper.getMainLooper())
    private var gameStarted: Boolean = false

    val runnable: Runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, Constants.GameLogic.DELAY)
            gameProgress()
        }
    }


    private fun gameProgress() {
        if (!gameManager.isGameOver) {
            gameManager.moveFireDown()
            gameManager.spawnFire()
            updateFireUI()
            if (gameManager.checkCollision())
                gameManager.handleCollision()
            updateLivesUI()
            gameManager.score += Constants.GameLogic.POINTS
            updateScoreUI()

        } else
            loseGame()
    }

    private fun updateScoreUI() {
        main_LBL_score.text = "${gameManager.score}"
    }

    private fun loseGame() {
        stopGame()
        changeActivity("Score:\n", gameManager.score)
        gameManager.resetGame()
        updateFireUI()
        updatePlayerUI()
        updateLivesUI()
        startGame()
    }

    override fun onResume() {
        super.onResume()
        if (!gameStarted && !gameManager.isGameOver) {
            if (gameMode == Constants.GameMode.TILT)
                tiltDetector?.start()
            startGame()
        }
    }

    override fun onPause() {
        super.onPause()
        stopGame()
        if (gameMode == Constants.GameMode.TILT)
            tiltDetector?.stop()
    }


    private fun startGame() {
        if (!gameStarted) {
            handler.postDelayed(runnable, Constants.GameLogic.DELAY)
            gameStarted = true

        }
    }

    private fun stopGame() {
        if (gameStarted) {
            handler.removeCallbacks(runnable)
            gameStarted = false
        }
    }


    private fun updateFireUI() {
        val fireMatrix = gameManager.getFireMatrix()
        for (row in fireMatrix.indices) {
            for (col in fireMatrix[row].indices) {
                if (fireMatrix[row][col] == Constants.ImageState.FIRE)
                    main_IMG_fire[row][col].setImageResource(R.drawable.fire)
                else
                    main_IMG_fire[row][col].setImageDrawable(null)
            }
        }
    }

    private fun updatePlayerUI() {

        for (col in gameManager.getPlayerMatrix().indices) {
            if (gameManager.getPlayerMatrix()[col] == Constants.ImageState.PLAYER) // player image
                main_IMG_player[col].setImageResource(R.drawable.aang1)
            else
                main_IMG_player[col].setImageDrawable(null)
        }
    }

    private fun updateLivesUI() {

        main_IMG_hearts.forEachIndexed { index, heart ->
            heart.visibility =
                if (index < gameManager.numberOfCollisions) View.INVISIBLE else View.VISIBLE
        }
    }


    private fun findViews() {

        main_LBL_score = findViewById(R.id.main_LBL_score)
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
            findViewById(R.id.main_IMG_aang52),
            findViewById(R.id.main_IMG_aang53),
            findViewById(R.id.main_IMG_aang54)
        )

        main_IMG_fire = arrayOf(
            arrayOf(
                findViewById(R.id.main_MAT_00),
                findViewById(R.id.main_MAT_01),
                findViewById(R.id.main_MAT_02),
                findViewById(R.id.main_MAT_03),
                findViewById(R.id.main_MAT_04)

            ),
            arrayOf(
                findViewById(R.id.main_MAT_10),
                findViewById(R.id.main_MAT_11),
                findViewById(R.id.main_MAT_12),
                findViewById(R.id.main_MAT_13),
                findViewById(R.id.main_MAT_14)

            ),
            arrayOf(
                findViewById(R.id.main_MAT_20),
                findViewById(R.id.main_MAT_21),
                findViewById(R.id.main_MAT_22),
                findViewById(R.id.main_MAT_23),
                findViewById(R.id.main_MAT_24)

            ),
            arrayOf(
                findViewById(R.id.main_MAT_30),
                findViewById(R.id.main_MAT_31),
                findViewById(R.id.main_MAT_32),
                findViewById(R.id.main_MAT_33),
                findViewById(R.id.main_MAT_34)

            ),
            arrayOf(
                findViewById(R.id.main_MAT_40),
                findViewById(R.id.main_MAT_41),
                findViewById(R.id.main_MAT_42),
                findViewById(R.id.main_MAT_43),
                findViewById(R.id.main_MAT_44)
            ),
            arrayOf(
                findViewById(R.id.main_MAT_50),
                findViewById(R.id.main_MAT_51),
                findViewById(R.id.main_MAT_52),
                findViewById(R.id.main_MAT_53),
                findViewById(R.id.main_MAT_54)
            ),
            arrayOf(
                findViewById(R.id.main_MAT_60),
                findViewById(R.id.main_MAT_61),
                findViewById(R.id.main_MAT_62),
                findViewById(R.id.main_MAT_63),
                findViewById(R.id.main_MAT_64)
            )
        )
    }

    private fun initViews() {
        if (gameMode == Constants.GameMode.CONTROL) {
            main_FAB_left.setOnClickListener {
                gameManager.movePlayer(-1)
                updatePlayerUI()
            }
            main_FAB_right.setOnClickListener {
                gameManager.movePlayer(1)
                updatePlayerUI()
            }
        } else {
            initTiltDetector()
            tiltDetector?.start()
            main_FAB_left.visibility = View.GONE
            main_FAB_right.visibility = View.GONE
        }
        main_LBL_score.text = "${gameManager.score}"
    }


    private fun changeActivity(message: String, score: Int) {
        val intent = Intent(this, ScoreActivity::class.java)
        var bundle = Bundle()
        bundle.putInt(Constants.BundleKeys.SCORE_KEY, score)
        bundle.putString(Constants.BundleKeys.STATUS_KEY, message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


}