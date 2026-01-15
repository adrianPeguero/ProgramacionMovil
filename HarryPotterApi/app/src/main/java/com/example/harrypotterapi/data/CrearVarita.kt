package com.example.harrypotterapi.data

import com.google.gson.annotations.SerializedName

data class CrearVarita (

    val madera: String,
    val nucleo: String,
    @SerializedName("longitud")
    val longitud: Double,
    @SerializedName("rota")
    var rota: Boolean,
    @SerializedName("mago")
    var mago: String
)