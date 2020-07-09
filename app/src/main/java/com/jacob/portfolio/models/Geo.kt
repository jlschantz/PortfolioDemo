package com.jacob.portfolio.models

//Retrofit
import com.google.gson.annotations.SerializedName
//Room
import androidx.room.ColumnInfo

data class Geo (
    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    var lat : Double = 0.0,

    @ColumnInfo(name = "lng")
    @SerializedName("lng")
    var lng : Double = 0.0
){
    override fun toString(): String {
        return "Geo(lat=$lat, lng=$lng)"
    }
}