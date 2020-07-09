package com.jacob.portfolio.models
//Retrofit
import com.google.gson.annotations.SerializedName
//Room
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo (
    @ColumnInfo(name = "albumId")
    @SerializedName("albumId")
    var albumId : Int = 0,

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id : Int? = null,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title : String? = null,

    @ColumnInfo(name = "url")
    @SerializedName("url")
    var url : String? = null,

    @ColumnInfo(name = "thumbnailUrl")
    @SerializedName("thumbnailUrl")
    var thumbnailUrl : String? = null
){
    override fun toString(): String {
        return "Photo(albumId=$albumId, id=$id, title=$title, url=$url, thumbnailUrl=$thumbnailUrl)"
    }
}