package com.example.avatar_mobile_game

import com.example.avatar_mobile_game.utilities.Constants

class GameManager {

    private var playerPosition = 1
    private var lives = Constants.LIVES
    private val playerMatrix = IntArray(3) { 0 } //for logical
    private val fireMatrix = Array(5) { IntArray(3) { 0 } } //for logical

    private var randomCol: Int = -1 // Initialize with a default value

    init {
        playerMatrix[playerPosition] = 1
    }

    fun movePlayer(direction: Int) {

        if (playerPosition + direction >= 0 && playerPosition + direction < Constants.COLS) {
            playerMatrix[playerPosition] = 0 // no image
            playerPosition += direction
            playerMatrix[playerPosition] = 1 // player image
        }
    }

    fun spawnFire() {

        randomCol = (0 until Constants.COLS).random()
        fireMatrix[0][randomCol] = 2 // fire image - position on the top
    }

    fun moveFireDown() {

        for (row in fireMatrix.size - 1 downTo 1) {
            for (col in fireMatrix[row].indices) {
                if (fireMatrix[row - 1][col] == 2) { // check for fire image in row above
                    fireMatrix[row][col] = 2
                    fireMatrix[row - 1][col] = 0 //no image
                }
            }
        }
    }

    fun checkCollision(): Boolean {
        return playerMatrix[randomCol] == 1 && fireMatrix[fireMatrix.size - 1][randomCol] == 2
    }

    fun handleCollision() {


    }

    fun getPlayerMatrix(): IntArray = playerMatrix

    fun getFireMatrix(): Array<IntArray> = fireMatrix

    fun getLives(): Int = lives


}


