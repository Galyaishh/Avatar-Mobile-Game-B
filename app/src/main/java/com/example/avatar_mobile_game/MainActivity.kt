package com.example.avatar_mobile_game

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.avatar_mobile_game.DataManager.PlayerRecord
import com.example.avatar_mobile_game.DataManager.RecordsManager
import com.example.avatar_mobile_game.databinding.ActivityScoreBinding
import com.example.avatar_mobile_game.interfaces.TiltCallback
import com.example.avatar_mobile_game.utilities.Constants
import com.example.avatar_mobile_game.utilities.GameManager
import com.example.avatar_mobile_game.utilities.TiltDetector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.example.avatar_mobile_game.HighScoresActivity


class MainActivity : AppCompatActivity() {


    private lateinit var main_FAB_left: ExtendedFloatingActionButton
    private lateinit var main_FAB_right: ExtendedFloatingActionButton
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_IMG_player: Array<AppCompatImageView>
    private lateinit var main_IMG_fire: Array<Array<AppCompatImageView>>
    private lateinit var main_LBL_score: AppCompatTextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var gameManager: GameManager
    private var gameMode: String? = null
    private var gameSpeed: String? = null
    private var gameDelay: Long = 0
    private var gameStarted = false

    private val handler = Handler(Looper.getMainLooper())
    private val gameRunnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, gameDelay)
            gameProgress()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViews()
        initGameSettings()
        setupGameManager()
        initViews()
    }

    private fun initGameSettings() {
        gameMode = intent.getStringExtra(Constants.BundleKeys.GAME_MODE_KEY)
        gameSpeed = intent.getStringExtra(Constants.BundleKeys.GAME_SPEED_KEY)
        gameDelay = if (gameSpeed == Constants.GameSpeed.FAST.toString()) {
            Constants.GameLogic.DELAY_FAST
        } else {
            Constants.GameLogic.DELAY_SLOW
        }
    }

    private fun setupGameManager() {
        gameManager = GameManager(main_IMG_hearts.size, main_IMG_fire.size, main_IMG_fire[0].size)
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
        if (gameMode == Constants.GameMode.CONTROL.toString()) {
            setupControlMode()
        } else {
            main_FAB_left.visibility = View.GONE
            main_FAB_right.visibility = View.GONE
        }
        main_LBL_score.text = "${gameManager.score}"
    }

    private fun setupControlMode() {
        main_FAB_left.setOnClickListener {
            gameManager.movePlayer(-1)
            updatePlayerUI()
        }
        main_FAB_right.setOnClickListener {
            gameManager.movePlayer(1)
            updatePlayerUI()
        }
    }

    private fun gameProgress() {
        if (!gameManager.isGameOver) {
            gameManager.moveFireDown()
            gameManager.spawnFire()
            updateFireUI()
            if (gameManager.checkCollision()) {
                gameManager.handleCollision()
                updateLivesUI()
            }
            gameManager.score += Constants.GameLogic.POINTS
            updateScoreUI()
        } else {
            endGame()
        }
    }

    private fun endGame() {
        stopGame()
        showGameOverDialog(gameManager.score)
    }

    private fun startGame() {
        if (!gameStarted) {
            handler.postDelayed(gameRunnable, gameDelay)
            gameStarted = true
        }
    }

    private fun stopGame() {
        if (gameStarted) {
            handler.removeCallbacks(gameRunnable)
            gameStarted = false
        }
    }

    private fun showGameOverDialog(score: Int) {
        val dialog = Dialog(this)
        val binding = ActivityScoreBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        binding.root.background = ContextCompat.getDrawable(this, R.drawable.dialog_background)

        binding.scoreLBLScore.text = "Score: $score"
        binding.scoreBTNSubmit.setOnClickListener {
            val playerName = binding.scoreEDTName.text.toString()
            if (playerName.isNotEmpty()) {
                savePlayerRecord(playerName, score)
                dialog.dismiss()
                navigateToHighScores()
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun savePlayerRecord(playerName: String, score: Int) {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val playerLocation = location?.let { LatLng(it.latitude, it.longitude) }
                    ?: LatLng(0.0, 0.0)
                addRecord(playerName, score, playerLocation)
            }
        } else {
            addRecord(playerName, score, LatLng(0.0, 0.0))
        }
    }

    private fun addRecord(playerName: String, score: Int, location: LatLng) {
        val recordsManager = RecordsManager.getInstance()
        recordsManager.addRecord(PlayerRecord(playerName, score, location))
    }

    private fun navigateToHighScores() {
        val intent = Intent(this, HighScoresActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateScoreUI() {
        main_LBL_score.text = "${gameManager.score}"
    }

    private fun updateFireUI() {
        gameManager.getFireMatrix().forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, cell ->
                main_IMG_fire[rowIndex][colIndex].apply {
                    setImageResource(if (cell == Constants.ImageState.FIRE) R.drawable.fire else 0)
                }
            }
        }
    }

    private fun updatePlayerUI() {
        gameManager.getPlayerMatrix().forEachIndexed { index, state ->
            main_IMG_player[index].apply {
                setImageResource(if (state == Constants.ImageState.PLAYER) R.drawable.aang1 else 0)
            }
        }
    }

    private fun updateLivesUI() {
        main_IMG_hearts.forEachIndexed { index, heart ->
            heart.visibility = if (index < gameManager.numberOfCollisions) View.INVISIBLE else View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if (!gameStarted) startGame()
    }

    override fun onPause() {
        super.onPause()
        stopGame()
    }

//    private lateinit var main_FAB_left: ExtendedFloatingActionButton
//    private lateinit var main_FAB_right: ExtendedFloatingActionButton
//    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
//    private lateinit var main_IMG_player: Array<AppCompatImageView> //for UI
//    private lateinit var main_IMG_fire: Array<Array<AppCompatImageView>> //for UI
//    private lateinit var main_LBL_score: AppCompatTextView
//    private var tiltDetector: TiltDetector? = null
//    private lateinit var gameManager: GameManager
//    private var gameMode: String? = null
//    private var gameSpeed: String? = null
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private var gameDelay: Long = 0
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        findViews()
//        gameMode = intent.getStringExtra(Constants.BundleKeys.GAME_MODE_KEY)
//        gameSpeed = intent.getStringExtra(Constants.BundleKeys.GAME_SPEED_KEY)
//        if (gameSpeed == Constants.GameSpeed.FAST.toString())
//            gameDelay = Constants.GameLogic.DELAY_FAST
//        else
//            gameDelay = Constants.GameLogic.DELAY_SLOW
//        gameManager = GameManager(main_IMG_hearts.size, main_IMG_fire.size, main_IMG_fire[0].size)
//        initViews()
//    }
//
//    private fun initTiltDetector() {
//        tiltDetector = TiltDetector(
//            context = this,
//            tiltCallback = object : TiltCallback {
//                override fun tiltRight() {
//                    gameManager.movePlayer(1)
//                    updatePlayerUI()
//                }
//
//                override fun tiltLeft() {
//                    gameManager.movePlayer(-1)
//                    updatePlayerUI()
//                }
//
//                override fun tiltUp() {
//                }
//
//                override fun tiltDown() {
//                }
//            }
//        )
//    }
//
//    val handler: Handler = Handler(Looper.getMainLooper())
//    private var gameStarted: Boolean = false
//
//    val runnable: Runnable = object : Runnable {
//        override fun run() {
//            handler.postDelayed(this, gameDelay)
//            gameProgress()
//        }
//    }
//
//
//    private fun gameProgress() {
//        if (!gameManager.isGameOver) {
//            gameManager.moveFireDown()
//            gameManager.spawnFire()
//            updateFireUI()
//            if (gameManager.checkCollision())
//                gameManager.handleCollision()
//            updateLivesUI()
//            gameManager.score += Constants.GameLogic.POINTS
//            updateScoreUI()
//
//        } else
//            loseGame()
//    }
//
//    private fun updateScoreUI() {
//        main_LBL_score.text = "${gameManager.score}"
//    }
//
//    private fun loseGame() {
//        stopGame()
//        showGameOverDialog(gameManager.score)
////        gameManager.resetGame()
////        updateFireUI()
////        updatePlayerUI()
////        updateLivesUI()
////        startGame()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (!gameStarted && !gameManager.isGameOver) {
//            if (gameMode == Constants.GameMode.TILT.toString())
//                tiltDetector?.start()
//            startGame()
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stopGame()
//        if (gameMode == Constants.GameMode.TILT.toString())
//            tiltDetector?.stop()
//    }
//
//
//    private fun startGame() {
//        if (!gameStarted) {
//            handler.postDelayed(runnable, gameDelay)
//            gameStarted = true
//
//        }
//    }
//
//    private fun stopGame() {
//        if (gameStarted) {
//            handler.removeCallbacks(runnable)
//            gameStarted = false
//        }
//    }
//
//
//    private fun updateFireUI() {
//        val fireMatrix = gameManager.getFireMatrix()
//        for (row in fireMatrix.indices) {
//            for (col in fireMatrix[row].indices) {
//                if (fireMatrix[row][col] == Constants.ImageState.FIRE)
//                    main_IMG_fire[row][col].setImageResource(R.drawable.fire)
//                else
//                    main_IMG_fire[row][col].setImageDrawable(null)
//            }
//        }
//    }
//
//    private fun updatePlayerUI() {
//
//        for (col in gameManager.getPlayerMatrix().indices) {
//            if (gameManager.getPlayerMatrix()[col] == Constants.ImageState.PLAYER) // player image
//                main_IMG_player[col].setImageResource(R.drawable.aang1)
//            else
//                main_IMG_player[col].setImageDrawable(null)
//        }
//    }
//
//    private fun updateLivesUI() {
//
//        main_IMG_hearts.forEachIndexed { index, heart ->
//            heart.visibility =
//                if (index < gameManager.numberOfCollisions) View.INVISIBLE else View.VISIBLE
//        }
//    }
//
//
//    private fun findViews() {
//
//        main_LBL_score = findViewById(R.id.main_LBL_score)
//        main_FAB_left = findViewById(R.id.main_FAB_left)
//        main_FAB_right = findViewById(R.id.main_FAB_right)
//        main_IMG_hearts = arrayOf(
//            findViewById(R.id.main_IMG_heart1),
//            findViewById(R.id.main_IMG_heart2),
//            findViewById(R.id.main_IMG_heart3)
//        )
//
//        main_IMG_player = arrayOf(
//            findViewById(R.id.main_IMG_aang50),
//            findViewById(R.id.main_IMG_aang51),
//            findViewById(R.id.main_IMG_aang52),
//            findViewById(R.id.main_IMG_aang53),
//            findViewById(R.id.main_IMG_aang54)
//        )
//
//        main_IMG_fire = arrayOf(
//            arrayOf(
//                findViewById(R.id.main_MAT_00),
//                findViewById(R.id.main_MAT_01),
//                findViewById(R.id.main_MAT_02),
//                findViewById(R.id.main_MAT_03),
//                findViewById(R.id.main_MAT_04)
//
//            ),
//            arrayOf(
//                findViewById(R.id.main_MAT_10),
//                findViewById(R.id.main_MAT_11),
//                findViewById(R.id.main_MAT_12),
//                findViewById(R.id.main_MAT_13),
//                findViewById(R.id.main_MAT_14)
//
//            ),
//            arrayOf(
//                findViewById(R.id.main_MAT_20),
//                findViewById(R.id.main_MAT_21),
//                findViewById(R.id.main_MAT_22),
//                findViewById(R.id.main_MAT_23),
//                findViewById(R.id.main_MAT_24)
//
//            ),
//            arrayOf(
//                findViewById(R.id.main_MAT_30),
//                findViewById(R.id.main_MAT_31),
//                findViewById(R.id.main_MAT_32),
//                findViewById(R.id.main_MAT_33),
//                findViewById(R.id.main_MAT_34)
//
//            ),
//            arrayOf(
//                findViewById(R.id.main_MAT_40),
//                findViewById(R.id.main_MAT_41),
//                findViewById(R.id.main_MAT_42),
//                findViewById(R.id.main_MAT_43),
//                findViewById(R.id.main_MAT_44)
//            ),
//            arrayOf(
//                findViewById(R.id.main_MAT_50),
//                findViewById(R.id.main_MAT_51),
//                findViewById(R.id.main_MAT_52),
//                findViewById(R.id.main_MAT_53),
//                findViewById(R.id.main_MAT_54)
//            ),
//            arrayOf(
//                findViewById(R.id.main_MAT_60),
//                findViewById(R.id.main_MAT_61),
//                findViewById(R.id.main_MAT_62),
//                findViewById(R.id.main_MAT_63),
//                findViewById(R.id.main_MAT_64)
//            )
//        )
//    }
//
//    private fun initViews() {
//        if (gameMode == Constants.GameMode.CONTROL.toString()) {
//            main_FAB_left.setOnClickListener {
//                gameManager.movePlayer(-1)
//                updatePlayerUI()
//            }
//            main_FAB_right.setOnClickListener {
//                gameManager.movePlayer(1)
//                updatePlayerUI()
//            }
//        } else {
//            initTiltDetector()
//            tiltDetector?.start()
//            main_FAB_left.visibility = View.GONE
//            main_FAB_right.visibility = View.GONE
//        }
//        main_LBL_score.text = "${gameManager.score}"
//    }
//
//
//    private fun changeActivity() {
//        val intent = Intent(this, HighScoresActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    private fun showGameOverDialog(score: Int) {
//        val dialog = Dialog(this)
//        val binding = ActivityScoreBinding.inflate(layoutInflater)
//        dialog.setContentView(binding.root)
//
//        // Set dialog properties
//        dialog.setCancelable(false) // Prevent closing the dialog accidentally
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // Transparent window background
//        binding.root.background = ContextCompat.getDrawable(this, R.drawable.dialog_background) // Set the custom drawable as background
//
//        // Set score and input behavior
//        binding.scoreLBLScore.text = "Score: $score"
//        binding.scoreBTNSubmit.setOnClickListener {
//            val playerName = binding.scoreEDTName.text.toString()
//            if (playerName.isNotEmpty()) {
//                savePlayerRecord(playerName, score)
//                dialog.dismiss() // Close dialog
//                changeActivity()
//            } else {
//                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // Show dialog
//        dialog.show()
//    }
//
//
////    private fun showGameOverDialog(score: Int) {
////        val dialog = Dialog(this)
////        val binding = ActivityScoreBinding.inflate(layoutInflater)
////        dialog.setContentView(binding.root)
////
////        // Set dialog properties
////        dialog.setCancelable(false) // Prevent closing the dialog accidentally
////        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
////
////        // Set score and input behavior
////        binding.scoreLBLScore.text = "Score: $score"
////        binding.scoreBTNSubmit.setOnClickListener {
////            val playerName = binding.scoreEDTName.text.toString()
////            if (playerName.isNotEmpty()) {
////                savePlayerRecord(playerName, score)
////                dialog.dismiss() // Close dialog
////                changeActivity()
////            } else {
////                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
////            }
////        }
////
////        // Show dialog
////        dialog.show()
////    }
//
//    private fun savePlayerRecord(playerName: String, score: Int) {
//        val recordsManager = RecordsManager.getInstance()
//        val playerLocation = getPlayerLocation()
//        recordsManager.addRecord(PlayerRecord(playerName, score, playerLocation))
//    }
//
//    private fun getPlayerLocation(): LatLng {
//        return if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            LatLng(0.0, 0.0)
//        } else {
//            var location = LatLng(0.0, 0.0)
//            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//                if (loc != null) {
//                    location = LatLng(loc.latitude, loc.longitude)
//                }
//            }
//            location
//        }
//    }

}