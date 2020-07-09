package com.jacob.portfolio.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jacob.portfolio.models.Album
import com.jacob.portfolio.models.AlbumItem

@Dao
interface AlbumsDao {
    @Query("SELECT * FROM albums")
    fun getAllItems(): LiveData<List<AlbumItem>>

    @Query("SELECT * FROM albums WHERE id LIKE :id")
    fun getItemByID(id: Int): LiveData<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(album: Album)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(albums: List<Album>)

    @Delete
    fun deleteItem(album: Album)

    @Update
    fun updateItem(album: Album)

    @Query("DELETE FROM albums")
    fun clearTable()

    @Transaction
    fun updateTable(albums : List<Album>) {
        clearTable()
        insertItems(albums)
    }
}