package com.example.avatar_mobile_game.utilities

class Constants {

    object GameLogic {
        const val DELAY_SLOW = 1100L
        const val DELAY_FAST = 700L
        const val POINTS = 10
        const val APPA_SPAWN_CHANCE = 20

    }

    enum class ImageState {
        NONE,
        PLAYER,
        FIRE,
        APPA
    }

    enum class GameMode { TILT, CONTROL }
    enum class GameSpeed { FAST, SLOW }

    object BundleKeys {
        const val SCORE_KEY: String = "SCORE_KEY"
        const val STATUS_KEY: String = "STATUS_KEY"
        const val GAME_MODE_KEY: String = "GAME_MODE_KEY"
        const val GAME_SPEED_KEY: String = "GAME_SPEED_KEY"
        const val PLAYER_RECORDS: String = "PLAYER_RECORDS"
    }

    object RequestCodes {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }


}