package com.jacob.portfolio.activities

import com.jacob.portfolio.models.AlbumItem
import com.jacob.portfolio.models.UserItem

interface MainInterface {
    fun goToPhotosForAlbum(albumItem: AlbumItem)
    fun goToPhotosForUser(userItem: UserItem)
}