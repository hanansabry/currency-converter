package com.test.currencyconverter.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "http://data.fixer.io/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface FixerApiService {

    @GET("symbols")
    suspend fun getSymbols(@Query("access_key") apiKey: String): SymbolResponse

}

object FixerApi {
    val retrofitService : FixerApiService by lazy { retrofit.create(FixerApiService::class.java) }
}