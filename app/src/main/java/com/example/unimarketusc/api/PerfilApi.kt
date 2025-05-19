package com.example.unimarketusc.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PerfilApi {

    @Multipart
    @POST("guardar_perfil.php")
    fun guardarPerfil(
        @Part("user_id") userId: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("direccion") direccion: RequestBody,
        @Part("carrera") carrera: RequestBody,
        @Part("contacto") contacto: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): Call<ResponseBody>

    @GET("obtener_perfil.php")
    fun obtenerPerfil(@Query("user_id") userId: Int): Call<PerfilResponse>
}
