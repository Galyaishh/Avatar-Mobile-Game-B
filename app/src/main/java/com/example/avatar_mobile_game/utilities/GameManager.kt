package com.example.avatar_mobile_game.utilities

import com.example.avatar_mobile_game.utilities.Constants.ImageState
import kotlin.random.Random

class GameManager(private val livesCount: Int, rows: Int, private val cols: Int) {

    private var playerPosition = cols / 2
    private val playerMatrix = Array(cols) { ImageState.NONE }
    private val gameMatrix = Array(rows) { Array(cols) { ImageState.NONE } }

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
//            checkPlayerInteraction()
//            if (checkCollision())
//                handleCollision()
//            if (checkFoundAppa())
//                handleFoundAppa()
        }
    }

    private fun handlePlayerInteraction() {
        val playerCell = gameMatrix[gameMatrix.size - 1][playerPosition]
        when (playerCell) {
            ImageState.FIRE -> {
                handleCollision()
                gameMatrix[gameMatrix.size - 1][playerPosition] = ImageState.NONE // Remove FIRE
            }
            ImageState.APPA -> {
                handleFoundAppa()
                gameMatrix[gameMatrix.size - 1][playerPosition] = ImageState.NONE // Remove APPA
            }
            else -> {
                // No interaction
            }
        }
    }
//    fun handlePlayerInteraction(): Constants.InteractionResult {
//        val playerCell = gameMatrix[gameMatrix.size - 1][playerPosition]
//        return when (playerCell) {
//            Constants.ImageState.FIRE -> {
//                handleCollision()
//                Constants.InteractionResult.COLLISION
//            }
//            Constants.ImageState.APPA -> {
//                handleFoundAppa()
//                Constants.InteractionResult.APPA_FOUND
//            }
//            else -> Constants.InteractionResult.NONE
//        }
//    }

    private fun checkPlayerInteraction() {
        if (gameMatrix[gameMatrix.size - 1][playerPosition] == ImageState.FIRE)
            handleCollision()
        else if (gameMatrix[gameMatrix.size - 1][playerPosition] == ImageState.APPA)
            handleFoundAppa()
    }

    fun handleFoundAppa() {
        score += Constants.GameLogic.POINTS
//        gameMatrix[gameMatrix.size - 1][playerPosition] = ImageState.NONE
        SignalManager.getInstance().toast("You found an APPA!")
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


    fun checkCollision(): Boolean {
        return gameMatrix[gameMatrix.size - 1][playerPosition] == ImageState.FIRE
    }

    fun checkFoundAppa(): Boolean {
        return gameMatrix[gameMatrix.size - 1][playerPosition] == ImageState.APPA
    }

    fun handleCollision() {
        numberOfCollisions++
        SignalManager.getInstance().vibrate()
        if (!isGameOver)
            SignalManager.getInstance().toast("Fire nation hit you!")
        else
            SignalManager.getInstance().toast("You lose!")
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


