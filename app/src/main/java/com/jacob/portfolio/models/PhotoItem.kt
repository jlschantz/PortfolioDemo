package com.jacob.portfolio.models

import androidx.room.ColumnInfo

data class PhotoItem (
    @ColumnInfo(name = "id")
    var id : Int? = null,

    @ColumnInfo(name = "title")
    var title : String? = null,

    @ColumnInfo(name = "url")
    var url : String? = null,

    @ColumnInfo(name = "thumbnailUrl")
    var thumbnailUrl : String? = null
)