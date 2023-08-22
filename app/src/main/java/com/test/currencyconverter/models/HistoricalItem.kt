package com.test.currencyconverter.models

data class HistoricalItem(
    val time: Long,
    val fromSymbol: String,
    val fromValue: Double,
    val toSymbol: String,
    val toValue: Double
)