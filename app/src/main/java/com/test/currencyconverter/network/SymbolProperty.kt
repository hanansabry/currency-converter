package com.test.currencyconverter.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SymbolProperty(
    val symbol: String,
    val country: String) : Parcelable
