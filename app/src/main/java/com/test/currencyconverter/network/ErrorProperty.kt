package com.test.currencyconverter.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorProperty(
    @Json(name = "code") val code: String,
    @Json(name = "type") val type: String,
    @Json(name = "info") val info: String
) : Parcelable
