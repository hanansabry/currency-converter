package com.test.currencyconverter.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConvertResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "rates") val rates: Map<String, Double>?,
    @Json(name = "error") val error: ErrorProperty?
) : Parcelable