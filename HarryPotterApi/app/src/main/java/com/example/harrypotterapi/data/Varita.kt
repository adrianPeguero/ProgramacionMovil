package com.example.harrypotterapi.data

import com.google.gson.annotations.SerializedName

data class Varita(
    @SerializedName("id")
    val id: Int,
    @SerializedName("materiales")
    val materiales: String,
    @SerializedName("longitud")
    val longitud: Double,
    @SerializedName("rota")
    var rota: String, // En Java es String ("No")
    @SerializedName("mago")
    var mago: String

)
