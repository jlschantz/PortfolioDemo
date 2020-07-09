package com.jacob.portfolio.models

//Retrofit
import com.google.gson.annotations.SerializedName
//Room
import androidx.room.ColumnInfo

data class Company (
    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name : String? = null,

    @ColumnInfo(name = "catchPhrase")
    @SerializedName("catchPhrase")
    var catchPhrase : String? = null,

    @ColumnInfo(name = "bs")
    @SerializedName("bs")
    var bs : String? = null
){
    override fun toString(): String {
        return "Company(name=$name, catchPhrase=$catchPhrase, bs=$bs)"
    }
}