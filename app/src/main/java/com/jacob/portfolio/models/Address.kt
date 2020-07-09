package com.jacob.portfolio.models

//Retrofit
import com.google.gson.annotations.SerializedName
//Room
import androidx.room.ColumnInfo
import androidx.room.Embedded

data class Address (
    @ColumnInfo(name = "street")
    @SerializedName("street")
    var street : String? = null,

    @ColumnInfo(name = "suite")
    @SerializedName("suite")
    var suite : String? = null,

    @ColumnInfo(name = "city")
    @SerializedName("city")
    var city : String? = null,

    @ColumnInfo(name = "zipcode")
    @SerializedName("zipcode")
    var zipCode : String? = null,

    @Embedded(prefix = "geo_")
    @SerializedName("geo")
    var geo: Geo? = null
){
    override fun toString(): String {
        return "Address(street=$street, suite=$suite, city=$city, zipcode=$zipCode, geo=${geo.toString()})"
    }
}