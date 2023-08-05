package com.test.currencyconverter.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class SymbolResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "symbols") val symbols: Map<String, String>?,
    @Json(name = "error") val error: ErrorProperty?
) : Parcelable
