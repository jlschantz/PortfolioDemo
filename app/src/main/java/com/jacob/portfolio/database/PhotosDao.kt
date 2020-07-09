package com.jacob.portfolio.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jacob.portfolio.models.Photo
import com.jacob.portfolio.models.PhotoItem

@Dao
interface PhotosDao {
    @Query("SELECT * FROM photos")
    fun getAllItems(): LiveData<List<Photo>>

    @Query("SELECT * FROM photos WHERE id LIKE :id")
    fun getItemByID(id: Int): LiveData<Photo>

    @Query("SELECT * FROM photos WHERE albumId LIKE :id")
    fun getAllItemsByAlbumID(id: Int): LiveData<List<PhotoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(photo: Photo)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(photos: List<Photo>)

    @Delete
    fun deleteItem(photo: Photo)

    @Update
    fun updateItem(photo: Photo)

    @Query("DELETE FROM photos")
    fun clearTable()

    @Transaction
    fun updateTable(photos : List<Photo>) {
        clearTable()
        insertItems(photos)
    }
}