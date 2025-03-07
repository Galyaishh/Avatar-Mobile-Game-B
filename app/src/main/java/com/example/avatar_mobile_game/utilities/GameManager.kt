package com.example.avatar_mobile_game.utilities

import com.example.avatar_mobile_game.utilities.Constants.ImageState
import kotlin.random.Random
import android.content.Context
import com.example.avatar_mobile_game.R

class GameManager(
    private val context: Context,
    private val livesCount: Int,
    rows: Int,
    private val cols: Int
) {

    private var playerPosition = cols / 2
    private val playerMatrix = Array(cols) { ImageState.NONE }
    private val gameMatrix = Array(rows) { Array(cols) { ImageState.NONE } }

    private val ssp = SingleSoundPlayer(context)

    var score: Int = 0

    var numberOfCollisions: Int = 0
        private set


    val isGameOver: Boolean
        get() = numberOfCollisions == livesCount


    init {
        playerMatrix[playerPosition] = ImageState.PLAYER
    }

    fun movePlayer(direction: Int) {

        if (playerPosition + direction in 0 until cols) {
            playerMatrix[playerPosition] = ImageState.NONE
            playerPosition += direction
            playerMatrix[playerPosition] = ImageState.PLAYER

            handlePlayerInteraction()
        }
    }

    private fun handlePlayerInteraction() {
        val playerCell = gameMatrix[gameMatrix.size - 1][playerPosition]
        when (playerCell) {
            ImageState.FIRE ->
                handleCollision()

            ImageState.APPA ->
                handleFoundAppa()

            else -> { // No interaction
            }
        }
    }

    fun handleCollision() {
        ssp.playSound(R.raw.firesound)
        numberOfCollisions++
        SignalManager.getInstance().vibrate()
        if (!isGameOver)
            SignalManager.getInstance().toast("Fire nation hit you!")
        else
            SignalManager.getInstance().toast("You lose!")
    }

    fun handleFoundAppa() {
        score += Constants.GameLogic.POINTS
        SignalManager.getInstance().toast("You found APPA!")
    }


    fun spawnEntity() {
        val randomCol = Random.nextInt(cols)
        val typeEntity = if (Random.nextInt(100) < Constants.GameLogic.APPA_SPAWN_CHANCE)
            ImageState.APPA else ImageState.FIRE
        gameMatrix[0][randomCol] = typeEntity
    }


    fun moveEntitiesDown() {
        for (row in gameMatrix.size - 1 downTo 1) { // Iterate from bottom to top
            for (col in gameMatrix[row].indices)
                gameMatrix[row][col] = gameMatrix[row - 1][col]     // Move fire image down
        }
        for (col in gameMatrix[0].indices)
            gameMatrix[0][col] = ImageState.NONE // Clear the top row
    }


    fun resetGame() {

        playerMatrix.fill(ImageState.NONE)
        playerPosition = cols / 2
        playerMatrix[playerPosition] = ImageState.PLAYER

        for (row in gameMatrix.indices)
            gameMatrix[row].fill(ImageState.NONE)

        numberOfCollisions = 0
        score = 0
    }


    fun getPlayerMatrix(): Array<ImageState> = playerMatrix

    fun getGameMatrix(): Array<Array<ImageState>> = gameMatrix

}


