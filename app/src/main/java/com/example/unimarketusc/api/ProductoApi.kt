package com.example.unimarketusc.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductoApi {

    @Multipart
    @POST("crear_producto.php")
    fun crearProducto(
        @Part("user_id") userId: RequestBody,
        @Part("titulo") titulo: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): Call<ResponseBody>
}
