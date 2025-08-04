package com.example.unimarketusc.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://rude-lemons-guess.loca.lt/unimarket_usc/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val perfilApi: PerfilApi by lazy {
        retrofit.create(PerfilApi::class.java)
    }

    val productoApi: ProductoApi by lazy {
        retrofit.create(ProductoApi::class.java)
    }
}
