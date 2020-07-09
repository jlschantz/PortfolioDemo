package com.jacob.portfolio.models

//Retrofit
import com.google.gson.annotations.SerializedName
//Room
import androidx.room.*

@Entity(tableName = "users")
data class User (
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id : Int? = null,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name : String? = null,

    @ColumnInfo(name = "username")
    @SerializedName("username")
    var username : String? = null,

    @Embedded(prefix = "address_")
    @SerializedName("address")
    var address : Address? = null,

    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    var phone : String? = null,

    @ColumnInfo(name = "website")
    @SerializedName("website")
    var website : String? = null,

    @Embedded(prefix = "company_")
    @SerializedName("company")
    var company : Company? = null
){
    override fun toString(): String {
        return "User(name=$name, username=$username, address=${address.toString()}, phone=$phone, website=$website, company=${company.toString()})"
    }
}