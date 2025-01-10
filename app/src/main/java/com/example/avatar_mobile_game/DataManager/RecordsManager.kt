package com.example.avatar_mobile_game.DataManager

import android.content.Context
import android.content.SharedPreferences
import com.example.avatar_mobile_game.utilities.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecordsManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("player_records", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveRecords(records: List<PlayerRecord>) {
        val json = gson.toJson(records)
        sharedPreferences.edit()
            .putString(Constants.BundleKeys.PLAYER_RECORDS, json)
            .apply()
    }

    fun getRecords(): List<PlayerRecord> {
        val json = sharedPreferences.getString(Constants.BundleKeys.PLAYER_RECORDS, null)
        return if (json != null) {
            val type = object : TypeToken<List<PlayerRecord>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun addRecord(record: PlayerRecord) {
        val records = getRecords().toMutableList()
        records.add(record)
        records.sortByDescending { it.score } // Sort by score in descending order
        saveRecords(records.take(10)) // Keep only the top 10 records
    }

    fun clearRecords() {
        sharedPreferences.edit()
            .remove(Constants.BundleKeys.PLAYER_RECORDS)
            .apply()
    }

    companion object {
        @Volatile
        private var instance: RecordsManager? = null

        fun init(context: Context): RecordsManager {
            return instance ?: synchronized(this) {
                instance ?: RecordsManager(context).also { instance = it }
            }
        }

        fun getInstance(): RecordsManager {
            return instance ?: throw IllegalStateException(
                "RecordsManager must be initialized by calling init(context) before use."
            )
        }
    }
}
