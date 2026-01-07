package com.example.harrypotterapi.data


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("api/varita")
    suspend fun getVaritas(): Response<List<Varita>>

    @PUT("api/varita/romper/{id}")
    suspend fun romperVarita(@Path("id") idVarita: Long): Response<Varita>
}