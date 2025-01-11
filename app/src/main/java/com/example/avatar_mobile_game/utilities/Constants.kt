package com.example.avatar_mobile_game.utilities

class Constants {

    object GameLogic {
        const val DELAY = 1100L
        const val POINTS = 10

    }

    object GameMode{
        const val TILT = "tilt"
        const val CONTROL = "control"
    }


    enum class ImageState {
        NONE,
        PLAYER,
        FIRE,
        COIN
    }

    object BundleKeys {
        const val SCORE_KEY: String = "SCORE_KEY"
        const val STATUS_KEY: String = "STATUS_KEY"
        const val GAME_MODE_KEY: String = "GAME_MODE_KEY"
        const val PLAYER_RECORDS: String = "PLAYER_RECORDS"
    }

    object RequestCodes {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }


}