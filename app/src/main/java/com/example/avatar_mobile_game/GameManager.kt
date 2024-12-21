package com.example.avatar_mobile_game

import com.example.avatar_mobile_game.utilities.Constants
import com.example.avatar_mobile_game.utilities.SignalManager
import kotlin.random.Random

class GameManager(private val livesCount: Int = 3, private val rows: Int, private val cols: Int) {

    private var playerPosition = 1
    private val playerMatrix = IntArray(cols) { 0 }
    private val fireMatrix = Array(rows) { IntArray(cols) { 0 } }

    var numberOfCollisions: Int = 0
        private set


    val isGameOver: Boolean
        get() = numberOfCollisions == livesCount


    init {
        playerMatrix[playerPosition] = 1
    }

    fun movePlayer(direction: Int) {

        if (playerPosition + direction >= 0 && playerPosition + direction < Constants.COLS) {
            playerMatrix[playerPosition] = 0 // no image
            playerPosition += direction
            playerMatrix[playerPosition] = 1 // player image
            if (checkCollision())
                handleCollision()
        }
    }


    fun spawnFire() {
        val randomCol = Random.nextInt(cols)
        fireMatrix[0][randomCol] = 2 // fire image
    }


    fun moveFireDown() {
        // Move fire images down
        for (row in fireMatrix.size - 1 downTo 1) { // Iterate from bottom to top
            for (col in fireMatrix[row].indices)  // Iterate through each column
                fireMatrix[row][col] = fireMatrix[row - 1][col]     // Move fire image down
        }
        for (col in fireMatrix[0].indices)
            fireMatrix[0][col] = 0 // Clear the top row
    }


    fun checkCollision(): Boolean {
        return playerMatrix[playerPosition] == 1 && fireMatrix[fireMatrix.size - 1][playerPosition] == 2
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

        playerMatrix[playerPosition]=0
        playerPosition = 1
        playerMatrix[playerPosition] = 1
        numberOfCollisions = 0

        for (row in fireMatrix.indices)
            for (col in fireMatrix[row].indices)
                fireMatrix[row][col] = 0


    }


    fun getPlayerMatrix(): IntArray = playerMatrix

    fun getFireMatrix(): Array<IntArray> = fireMatrix

}


