package com.jacob.portfolio.models

import androidx.room.ColumnInfo

data class AlbumItem (
    @ColumnInfo(name = "userId")
    var userId : Int = 0,

    @ColumnInfo(name = "id")
    var id : Int? = null,

    @ColumnInfo(name = "title")
    var title : String? = null
)