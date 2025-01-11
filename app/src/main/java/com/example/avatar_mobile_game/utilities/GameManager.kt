package com.example.avatar_mobile_game.utilities

import com.example.avatar_mobile_game.utilities.Constants.ImageState
import kotlin.random.Random

class GameManager(private val livesCount: Int, rows: Int, private val cols: Int) {

    private var playerPosition = cols/2
    private val playerMatrix = Array(cols) { ImageState.NONE}
    private val fireMatrix = Array(rows) { Array(cols) { ImageState.NONE } }

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
            if (checkCollision())
                handleCollision()
        }
    }


    fun spawnFire() {
        val randomCol = Random.nextInt(cols)
        fireMatrix[0][randomCol] = ImageState.FIRE
    }


    fun moveFireDown() {
        for (row in fireMatrix.size - 1 downTo 1) { // Iterate from bottom to top
            for (col in fireMatrix[row].indices)
                fireMatrix[row][col] = fireMatrix[row - 1][col]     // Move fire image down
        }
        for (col in fireMatrix[0].indices)
            fireMatrix[0][col] = ImageState.NONE // Clear the top row
    }


    fun checkCollision(): Boolean {
        return fireMatrix[fireMatrix.size - 1][playerPosition] == ImageState.FIRE
    }

    fun handleCollision() {
        numberOfCollisions++
        SignalManager.getInstance().vibrate()
        if(!isGameOver)
            SignalManager.getInstance().toast("Fire nation hit you!")
        else
            SignalManager.getInstance().toast("You lose!")
    }

    fun resetGame(){

        playerMatrix.fill(ImageState.NONE)
        playerPosition = cols/2
        playerMatrix[playerPosition] = ImageState.PLAYER

        for (row in fireMatrix.indices)
            fireMatrix[row].fill(ImageState.NONE)

        numberOfCollisions = 0
    }


    fun getPlayerMatrix(): Array<ImageState> = playerMatrix

    fun getFireMatrix(): Array<Array<ImageState>> = fireMatrix

}


