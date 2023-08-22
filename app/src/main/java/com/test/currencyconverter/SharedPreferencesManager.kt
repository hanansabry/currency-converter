package com.test.currencyconverter

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.currencyconverter.models.HistoricalItem
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class SharedPreferencesManager(activity: Context) {

    private var sharedPref: SharedPreferences
    private var editor: SharedPreferences.Editor
    private var gson = Gson()


    init {
        sharedPref = activity.getSharedPreferences(
            activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        editor = sharedPref.edit()
    }

    fun clearAll() {
        editor.clear()
    }

    fun saveConversion(fromValue: Double, fromSymbol: String?, toSymbol: String?, toValue: Double) {
        val time = System.currentTimeMillis()
        val historicalItem = HistoricalItem(time, fromSymbol!!, fromValue, toSymbol!!, toValue)
        val json = gson.toJson(historicalItem)
        sharedPref.edit().putString(time.toString(), json).apply()
        editor.apply()
    }

    fun getHistoryByDate(timeInMillis: Long) : List<HistoricalItem> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -3)

        val history = ArrayList<HistoricalItem>()
        for ((key, value) in sharedPref.all) {
            val time: Long = key.toLong();
            if (time > calendar.timeInMillis) {
                val json = value as String
                val historicalItem = gson.fromJson<HistoricalItem>(json, object : TypeToken<HistoricalItem>() {}.type)
                history.add(historicalItem)
            } else {
                println("not withing period")
            }
        }
        return history
    }

}