package com.jacob.portfolio.models
//Retrofit
import com.google.gson.annotations.SerializedName
//Room
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class Album (
    @ColumnInfo(name = "userId")
    @SerializedName("userId")
    var userId : Int = 0,

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id : Int? = null,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title : String? = null
){
    override fun toString(): String {
        return "Album(albumId=$userId, id=$id, title=$title)"
    }
}