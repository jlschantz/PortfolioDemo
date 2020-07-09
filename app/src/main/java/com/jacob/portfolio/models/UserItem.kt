package com.jacob.portfolio.models

import androidx.room.ColumnInfo

data class UserItem (
    @ColumnInfo(name = "id")
    var id : Int? = null,

    @ColumnInfo(name = "name")
    var name : String? = null
)